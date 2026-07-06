FROM amazoncorretto:21
COPY target/*.jar market.jar
ENTRYPOINT ["java", "-jar", "/market.jar"]