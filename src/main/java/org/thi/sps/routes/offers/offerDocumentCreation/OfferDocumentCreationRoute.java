package org.thi.sps.routes.offers.offerDocumentCreation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.thi.sps.routes.generic.GenericSpiffworkflowRouteBuilder;

@ApplicationScoped
public class OfferDocumentCreationRoute extends GenericSpiffworkflowRouteBuilder {

  @Inject
  @ConfigProperty(name = "redpanda.url")
  String redpandaUrl;

  @Inject
  @ConfigProperty(name = "documentservice.url")
  String documentServiceUrl;

  public static String TOPIC_NAME = "OfferDocumentCreationQueue";
  public static String SPIFFWORKFLOW_MESSAGE_NAME = "OfferDocumentCreationMessage";

  @Override
  public void configure() throws Exception {

    from("kafka:" + TOPIC_NAME + "?brokers=" + redpandaUrl)
        .log("Message received from Kafka: ${body}")
        .setHeader("Content-Type", constant("application/json"))
        .setHeader(Exchange.HTTP_METHOD, constant("POST"))
        .toD(documentServiceUrl + "/api/v1/documents/offer/create")
        .log("Response received: ${body}")
        .process(sendToSpiffworkflow(SPIFFWORKFLOW_MESSAGE_NAME))
        .onException(Exception.class)
        .log("Error occurred: ${exception.message}");
  }
}
