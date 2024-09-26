package org.thi.sps.routes.dataForInvoiceChanges;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.thi.sps.routes.generic.GenericSpiffworkflowRouteBuilder;

@ApplicationScoped
public class LoadDocumentRoute extends GenericSpiffworkflowRouteBuilder {

  @Inject
  @ConfigProperty(name = "redpanda.url")
  String redpandaUrl;

  @Inject
  @ConfigProperty(name = "documentservice.url")
  String documentServiceUrl;

  public static String TOPIC_NAME = "documentToGet";
  public static String SPIFFWORKFLOW_MESSAGE_NAME = "DokumentEingang";

  @Override
  public void configure() throws Exception {
    getContext().setTracing(true);
    System.out.println(redpandaUrl);
    System.out.println(documentServiceUrl);
    from("kafka:" + TOPIC_NAME + "?brokers=" + redpandaUrl)
        .log("Message received from Kafka: ${body}")
        .removeHeaders("CamelHttp*")
        .removeHeaders("kafka*")
        .setHeader("Content-Type", constant("*/*"))
        .setHeader(Exchange.HTTP_METHOD, constant("GET"))
        .toD(documentServiceUrl + "/api/v1/documents/" + "${body}")
        .log("HTTP Response Code: ${header.CamelHttpResponseCode}")
        .log("Response from product service: ${body}")
        .process(sendToSpiffworkflow(SPIFFWORKFLOW_MESSAGE_NAME))
        .onException(Exception.class)
        .log("Error occurred: ${exception.message}");
  }

}
