package org.thi.sps.routes.invocieRoutes.loadDataForSending;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.thi.sps.CanonicalDataModel.Document;
import org.thi.sps.routes.generic.GenericSpiffworkflowRouteBuilder;

@ApplicationScoped
public class LoadDocumentForSendingRoute extends GenericSpiffworkflowRouteBuilder {

  @Inject
  @ConfigProperty(name = "redpanda.url")
  String redpandaUrl;

  @Inject
  @ConfigProperty(name = "documentservice.url")
  String documentServiceUrl;

  public static String TOPIC_NAME = "documentToGetForSending";
  public static String SPIFFWORKFLOW_MESSAGE_NAME = "DokumentenEingangFuerVersand";

  @Override
  public void configure() throws Exception {
    getContext().setTracing(true);
    System.out.println(redpandaUrl);
    System.out.println(documentServiceUrl);
    from("kafka:" + TOPIC_NAME + "?brokers=" + redpandaUrl)
        .log("Message received from Kafka to load document: ${body}")
        .delay(5000)
        .removeHeaders("CamelHttp*")
        .removeHeaders("kafka*")
        .setHeader("Content-Type", constant("*/*"))
        .setHeader(Exchange.HTTP_METHOD, constant("GET"))
        .log("Logging the Rest URL " + documentServiceUrl + "/api/v1/documents/" + "${body}")
        .process(exchange -> {
          String body = exchange.getIn().getBody(String.class);
          body = body.replace("\"", ""); // Entferne Anführungszeichen
          exchange.getIn().setBody(body);
        })
        .toD(documentServiceUrl + "/api/v1/documents/${body}")
        .log("Response received from document service: ${body}")
        .process(exchange -> {

          String responseBody = exchange.getIn().getBody(String.class);
          ObjectMapper objectMapperForDocument = new ObjectMapper();
          JsonNode responseJson = objectMapperForDocument.readTree(responseBody);

          String documentLink = responseJson.get("url").asText();
          String invoiceId = responseJson.get("id").asText();
          String documentType = "document";

          Document document = new Document();
          document.setDocumentId(invoiceId);
          document.setInvoiceId(invoiceId);
          document.setLinkToDocument(documentLink);
          document.setDocumentType(documentType);

          String jsonResponse = objectMapperForDocument.writeValueAsString(document);

          exchange.getIn().setBody(jsonResponse);
        })
        .log("Response to Send To PDA: ${body}")
        .process(sendToSpiffworkflow(SPIFFWORKFLOW_MESSAGE_NAME))
        .onException(Exception.class)
        .log("Error occurred: ${exception.message}")
        .handled(true);
  }


}
