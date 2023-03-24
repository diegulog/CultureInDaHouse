### BUILD image
FROM maven:3-jdk-11 as builder
# create app folder for sources
RUN mkdir -p /build
WORKDIR /build
COPY pom.xml /build
#Download all required dependencies into one layer
RUN mvn -B dependency:resolve dependency:resolve-plugins
#Copy source code
COPY src /build/src
# Build application skip test
RUN mvn package -Dmaven.test.skip


FROM openjdk:11-slim as runtime
EXPOSE 18081
#Set app home folder
ENV APP_HOME /app
ENV JAVA_OPTS=""

WORKDIR $APP_HOME
#Copy executable jar file from the builder image
COPY --from=builder /build/target/*.jar app.jar

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar app.jar" ]
