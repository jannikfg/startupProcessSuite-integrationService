# startup-process-suite

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/startup-process-suite-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- Camel Minio ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/minio.html)): Store and retrieve objects from Minio Storage Service using Minio SDK
- Camel Core ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/core.html)): Camel core functionality and basic Camel languages: Constant, ExchangeProperty, Header, Ref, Simple and Tokenize
- Camel Jackson ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/jackson.html)): Marshal POJOs to JSON and back using Jackson
- Camel AWS 2 S3 Storage Service ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/aws2-s3.html)): Store and retrieve objects from AWS S3 Storage Service
- Camel Bean ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/bean.html)): Invoke methods of Java beans
- Camel REST OpenApi ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/rest-openapi.html)): To call REST services using OpenAPI specification as contract
- Camel HTTP ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/http.html)): Send requests to external HTTP servers using Apache HTTP Client 5.x
- Camel JSON Path ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/jsonpath.html)): Evaluate a JSONPath expression against a JSON message body
- Camel Log ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/log.html)): Prints data form the routed message (such as body and headers) to the logger
- Camel OpenAPI Java ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/openapi-java.html)): Expose OpenAPI resources defined in Camel REST DSL
- Camel Kafka ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/kafka.html)): Sent and receive messages to/from an Apache Kafka broker
- Camel JacksonXML ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/jacksonxml.html)): Unmarshal an XML payloads to POJOs and back using XMLMapper extension of Jackson
- SmallRye OpenAPI ([guide](https://quarkus.io/guides/openapi-swaggerui)): Document your REST APIs with OpenAPI - comes with Swagger UI
- Camel Timer ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/timer.html)): Generate messages in specified intervals using java.util.Timer
