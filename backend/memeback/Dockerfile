FROM eclipse-temurin:25-alpine-3.22 AS build
WORKDIR /build

COPY .mvn .mvn
COPY mvnw mvnw
RUN sed -i 's/\r$//' ./mvnw && chmod +x ./mvnw

COPY pom.xml pom.xml
COPY src/ src/

RUN ./mvnw package

FROM eclipse-temurin:25-alpine-3.22
WORKDIR /opt/image-gallery

COPY --from=build /build/target/image-gallery.jar ./image-gallery.jar

ENTRYPOINT ["java", "-jar", "image-gallery.jar"]
