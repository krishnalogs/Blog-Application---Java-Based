FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY . /app
EXPOSE 8080
CMD ["java","-jar","target/blogApplication-1.0.0.jar"]
