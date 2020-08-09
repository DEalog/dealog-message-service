# DEalog Message Service (DMS)

![Java CI with Maven](https://github.com/DEalog/dealog-common/workflows/Java%20CI%20with%20Maven/badge.svg)

The DEalog Message Service is the delivering part of the DEalog platform, a platform
for official communication in cases of emergency.

For more information please see [the DEalog website](https://dealog.info).

## Development

The following section describes the development setup with all needed
prerequisites needed to get the application up and running locally.

### Requirements

- [Maven](https://maven.apache.org/) for building the project
- [OpenJDK >= 11.0.x](https://openjdk.java.net/) to develop and deploy the application
- [Docker](https://docker.io) for running the code
- Editor for coding

## Running the application

### ... in dev mode

- Start an Apache Kafka

You can use DEalog Kafka Development Cluster e.g. the (https://github.com/DEalog/dealog-kafka-dev-cluster)
- Start Postgres
```
. docker run --name "postgis" -p 25432:5432 -d -t kartoza/postgis
```

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```
### ... and packaging

The application can be packaged using `./mvnw package`.
It produces the `dealog-message-service-[VERSION]-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/dealog-message-service-[VERSION]-runner.jar`.

### ... creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/dealog-message-service-[VERSION]`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.

### Collaboration

If you want to collaborate please follow this guide:

- Fork the repository
- Implement the feature, change or bugfix
- Add tests where it makes sense
- Update the [CHANGELOG.md](CHANGELOG.md)
- Create a pull request against the original repository

## Used technologies

The DEalog Message service uses the following (main) technologies, frameworks and libraries:

- [Quarkus](https://quarkus.io/)
- [APACHE KAFKA](https://kafka.apache.org)
- [Postgres](https://www.postgresql.org)
- [Project Lombok](https://projectlombok.org/)

