package org.thi.sps.routes.clients;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.thi.sps.routes.generic.GenericSpiffworkflowRouteBuilder;

@ApplicationScoped
public class ClientCreationRoute extends GenericSpiffworkflowRouteBuilder {

  @Inject
  @ConfigProperty(name = "redpanda.url")
  String redpandaUrl;

  @Inject
  @ConfigProperty(name = "clientservice.url")
  String clientServiceUrl;

  public static String TOPIC_NAME = "clients";
  public static String SPIFFWORKFLOW_MESSAGE_NAME = "GespeicherterKundeNachricht";

  @Override
  public void configure() throws Exception {
    from("kafka:" + TOPIC_NAME + "?brokers=" + redpandaUrl)
        .log("Message received from Kafka: ${body}")
        .removeHeaders("CamelHttp*")
        .removeHeaders("kafka*")
        .removeHeaders("CamelKafka*")
        .setHeader("Content-Type", constant("application/json"))
        .toD(clientServiceUrl + "/api/v1/clients")
        .log("Response from Client Service: ${body}")
        .process(sendToSpiffworkflow(SPIFFWORKFLOW_MESSAGE_NAME))
        .onException(Exception.class)
        .log("Error occurred: ${exception.message}");
  }
}
