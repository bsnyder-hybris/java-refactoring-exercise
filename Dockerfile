FROM openjdk:8-jre-alpine
COPY target/refactoring-0.0.1-SNAPSHOT.jar /refactoring.jar
CMD ["/usr/bin/java", "-jar", "/refactoring.jar"]