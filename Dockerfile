FROM openjdk:17
WORKDIR /opt/app
COPY target/*.jar poster.jar
CMD ["java","-jar","poster.jar"]