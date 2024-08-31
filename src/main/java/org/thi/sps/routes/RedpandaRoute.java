package org.thi.sps.routes;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.apache.camel.model.RouteDefinition;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.thi.sps.routes.generic.GenericSpiffworkflowRouteBuilder;

@ApplicationScoped
public class RedpandaRoute extends GenericSpiffworkflowRouteBuilder {

  @Inject
  @ConfigProperty(name = "redpanda.url")
  String redpandaUrl;

  public static String MESSAGE_NAME = "demo";
  public static String TOPIC_NAME = "demo";

  @Override
  public void configure() throws Exception {
    System.out.println(redpandaUrl);
    RouteDefinition routeDefinition =
    from("kafka:" + TOPIC_NAME + "?brokers=" + redpandaUrl)
        .log("Message received from Kafka: ${body}")
        .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
        .process(sendToSpiffworkflow(MESSAGE_NAME));
  }
}
