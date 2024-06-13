FROM openjdk:22
COPY target/pro.fit-0.0.1-SNAPSHOT.jar pro.fit-0.0.1.jar
ENTRYPOINT ["java", "-jar", "/pro.fit-0.0.1.jar"]