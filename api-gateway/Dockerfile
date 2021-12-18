FROM openjdk:15-alpine
RUN addgroup -S spring && adduser -S spring -G spring
VOLUME /tmp
EXPOSE 8761
ARG DEPENDENCY=target
ADD ${DEPENDENCY}/*.jar api-gateway.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/api-gateway.jar"]