FROM openjdk:8-jre-alpine
COPY target/refactoring-0.0.1-SNAPSHOT-executable.jar /refactoring.jar
CMD ["/usr/bin/java", "-jar", "/refactoring.jar"]