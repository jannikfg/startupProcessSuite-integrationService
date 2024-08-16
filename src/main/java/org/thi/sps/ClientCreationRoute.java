package org.thi.sps;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class ClientCreationRoute extends RouteBuilder {

  @Inject
  @ConfigProperty(name = "redpanda.url")
  String redpandaUrl;

  @Inject
  @ConfigProperty(name = "clientservice.url")
  String clientServiceUrl;

  public static String TOPIC_NAME = "clients";

  @Override
  public void configure() throws Exception {
    System.out.println(redpandaUrl);
    System.out.println(clientServiceUrl);
    RouteDefinition routeDefinition =
    from("kafka:" + TOPIC_NAME + "?brokers=" + redpandaUrl)
        .log("Message received from Kafka: ${body}")
        .setHeader("Content-Type", constant("application/json"))
        .to(clientServiceUrl + "/api/v1/clients");
  }
}
