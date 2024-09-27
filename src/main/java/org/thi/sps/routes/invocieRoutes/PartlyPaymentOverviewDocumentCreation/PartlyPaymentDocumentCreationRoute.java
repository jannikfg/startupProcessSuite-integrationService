package org.thi.sps.routes.invocieRoutes.PartlyPaymentOverviewDocumentCreation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.thi.sps.routes.generic.GenericSpiffworkflowRouteBuilder;

@ApplicationScoped
public class PartlyPaymentDocumentCreationRoute extends GenericSpiffworkflowRouteBuilder {

  @Inject
  @ConfigProperty(name = "redpanda.url")
  String redpandaUrl;

  @Inject
  @ConfigProperty(name = "documentservice.url")
  String documentServiceUrl;

  public static String TOPIC_NAME = "partlyPaymentOverviewDocumentToCreate";
  public static String SPIFFWORKFLOW_MESSAGE_NAME = "TeilzahlungsUebersichtsDokumentEingang";
  @Override
  public void configure() throws Exception {

    getContext().setTracing(true);
    System.out.println(redpandaUrl);
    System.out.println(documentServiceUrl);
    from("kafka:" + TOPIC_NAME + "?brokers=" + redpandaUrl)
        .log("Message received from Kafka: ${body}")
        .removeHeaders("CamelHttp*")
        .removeHeaders("kafka*")
        .setHeader("Content-Type", constant("application/json"))
        .setHeader("CamelHttpMethod", constant("POST"))
        .toD(documentServiceUrl + "/api/v1/documents/invoice/partial-payment-overview/create")
        .log("Response received: ${body}")
        .process(sendToSpiffworkflow(SPIFFWORKFLOW_MESSAGE_NAME))
        .onException(Exception.class)
        .log("Error occurred: ${exception.message}");
  }
}
