# Build Stage
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# Runtime Stage
FROM eclipse-temurin:21-jre-jammy

# Non-root User anlegen — Sicherheit: kein root-Prozess im Container und install wget for healthcheck
RUN groupadd -r appgroup  \
    && useradd -r -g appgroup -m -d /home/garmindb appuser  \
    && apt-get update  \
    && apt-get install -y --no-install-recommends wget python3 python3-venv git ca-certificates \
    && rm -rf /var/lib/apt/lists/*

ARG GARMINDB_REF=v3.6.7
RUN git clone --depth 1 --branch ${GARMINDB_REF} https://github.com/tcgoetz/GarminDB.git /opt/garmindb-src \
    && python3 -m venv /opt/garmindb-venv \
    && /opt/garmindb-venv/bin/pip install --no-cache-dir -r /opt/garmindb-src/requirements.txt \
    && /opt/garmindb-venv/bin/pip install --no-cache-dir /opt/garmindb-src \
    && /opt/garmindb-venv/bin/pip install --no-cache-dir --upgrade pip "setuptools==84.0.0" "msgpack==1.2.2"

WORKDIR /home/garmindb

ENV HOME=/home/garmindb

RUN mkdir -p /home/garmindb/HealthData/DBs && chown -R appuser:appgroup /home/garmindb/HealthData

COPY --from=builder /target/trainer-*.jar /app/trainer.jar

RUN chown appuser:appgroup /app/trainer.jar

USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "/app/trainer.jar"]

