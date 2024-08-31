package org.thi.sps.routes;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
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
    getContext().setTracing(true);
    System.out.println(redpandaUrl);
    System.out.println(productServiceUrl);
    from("kafka:" + TOPIC_NAME + "?brokers=" + redpandaUrl)
        .log("Message received from Kafka: ${body}")
        .removeHeaders("CamelHttp*")
        .removeHeaders("kafka*")
        .setHeader("Content-Type", constant("*/*"))
        .setHeader(Exchange.HTTP_METHOD, constant("POST"))
        .log("Sending message to product service with body: ${body}")
        .log("Sending message to product service with headers: ${headers}")
        .to(productServiceUrl + "/api/v1/products")
        .log("HTTP Response Code: ${header.CamelHttpResponseCode}")
        .log("Response from product service: ${body}");
  }

}
