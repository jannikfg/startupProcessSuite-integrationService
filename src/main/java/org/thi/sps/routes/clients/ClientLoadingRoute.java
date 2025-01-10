package org.thi.sps.routes.clients;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.thi.sps.routes.generic.GenericSpiffworkflowRouteBuilder;

@ApplicationScoped
public class ClientLoadingRoute extends GenericSpiffworkflowRouteBuilder {

  @Inject
  @ConfigProperty(name = "clientservice.url")
  String clientServiceUrl;

  @Inject
  @ConfigProperty(name = "redpanda.url")
  String redpandaUrl;

  public static String TOPIC_NAME = "ClientLoadingQueue";
  public static String SPIFFWORKFLOW_MESSAGE_NAME = "ClientListMessage";

  @Override
  public void configure() throws Exception {

    from("kafka:" + TOPIC_NAME + "?brokers=" + redpandaUrl)
        .log("Message received from Kafka: ${body}")
        .removeHeaders("CamelKafka*")
        .setHeader("Content-Type", constant("application/json"))
        .setHeader(Exchange.HTTP_METHOD, constant("GET"))
        .toD( clientServiceUrl + "/api/v1/clients")  // Externe API aufrufen
        .log("Response received: ${body}")
        .process(sendToSpiffworkflow(SPIFFWORKFLOW_MESSAGE_NAME))
        .onException(Exception.class)
        .log("Error occurred: ${exception.message}");
  }
}
