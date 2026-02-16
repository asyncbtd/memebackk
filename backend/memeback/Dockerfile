FROM eclipse-temurin:17-alpine-3.22 AS build
WORKDIR /build

COPY .mvn .mvn
COPY mvnw mvnw

RUN chmod +x mvnw

COPY pom.xml pom.xml
COPY src/ src/

RUN ls -la .mvn/wrapper

RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw package

FROM eclipse-temurin:17-alpine-3.22
WORKDIR /opt/image-gallery

COPY --from=build /build/target/image-gallery.jar ./image-gallery.jar

ENTRYPOINT ["java", "-jar", "image-gallery.jar"]
