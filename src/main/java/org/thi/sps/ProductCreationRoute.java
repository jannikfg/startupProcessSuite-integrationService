package org.thi.sps;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class ProductCreationRoute extends RouteBuilder {

  @Inject
  @ConfigProperty(name = "redpanda.url")
  String redpandaUrl;

  @Inject
  @ConfigProperty(name = "productservice.url")
  String productServiceUrl;

  public static String TOPIC_NAME = "products";

  @Override
  public void configure() throws Exception {
    System.out.println(redpandaUrl);
    System.out.println(productServiceUrl);
    RouteDefinition routeDefinition =
        from("kafka:" + TOPIC_NAME + "?brokers=" + redpandaUrl)
            .log("Message received from Kafka: ${body}")
            .setHeader("Content-Type", constant("application/json"))
            .to(productServiceUrl + "/api/v1/products");
  }

}
