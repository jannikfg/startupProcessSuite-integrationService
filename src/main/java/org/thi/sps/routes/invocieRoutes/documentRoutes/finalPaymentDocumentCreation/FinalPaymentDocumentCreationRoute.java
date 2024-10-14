package org.thi.sps.routes.invocieRoutes.documentRoutes.finalPaymentDocumentCreation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.camel.Exchange;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.thi.sps.CanonicalDataModel.Document;
import org.thi.sps.processors.DocumentProcessor;
import org.thi.sps.routes.generic.GenericSpiffworkflowRouteBuilder;
import org.thi.sps.routes.invocieRoutes.documentRoutes.invoiceDocumentCreation.InvoiceCreationRequest;
import org.thi.sps.routes.invocieRoutes.documentRoutes.objects.ClientForDocumentService;
import org.thi.sps.routes.invocieRoutes.documentRoutes.objects.InvoiceForDocumentService;
import org.thi.sps.routes.invocieRoutes.documentRoutes.objects.InvoiceItemForDocumentService;

@ApplicationScoped
public class FinalPaymentDocumentCreationRoute extends GenericSpiffworkflowRouteBuilder {

  @Inject
  @ConfigProperty(name = "redpanda.url")
  String redpandaUrl;

  @Inject
  @ConfigProperty(name = "documentservice.url")
  String documentServiceUrl;

  @Inject
  @ConfigProperty(name = "clientservice.url")
  String clientServiceUrl;

  public static String TOPIC_NAME = "finalPaymentDocumentToCreate";
  public static String SPIFFWORKFLOW_MESSAGE_NAME = "DokumentEingang";

  @Override
  public void configure() throws Exception {
    getContext().setTracing(true);

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    JacksonDataFormat clientJsonDataFormat = new JacksonDataFormat(ClientForDocumentService.class);
    clientJsonDataFormat.setObjectMapper(objectMapper);

    JacksonDataFormat invoiceRequestJsonDataFormat = new JacksonDataFormat(
        InvoiceCreationRequest.class);
    invoiceRequestJsonDataFormat.setObjectMapper(objectMapper);

    from("kafka:" + TOPIC_NAME + "?brokers=" + redpandaUrl)
        .log("Message received from Kafka: ${body}")
        .removeHeaders("CamelHttp*")
        .removeHeaders("kafka*")
        .unmarshal().json()
        .log("Message unmarshalled to JSON: ${body}")

        .setProperty("invoiceData", simple("${body[invoice]}"))
        .setProperty("clientId", simple("${body[invoice][clientId]}"))

        .setBody(simple("${exchangeProperty.clientId}"))
        .setHeader(Exchange.HTTP_METHOD, constant("GET"))
        .toD(clientServiceUrl + "/api/v1/clients/${body}")

        .unmarshal(clientJsonDataFormat)
        .process(exchange -> {
          Map<String, Object> invoiceData = exchange.getProperty("invoiceData", Map.class);
          if (invoiceData == null) {
            throw new RuntimeException("Invoice data not found in exchange.");
          }

          InvoiceForDocumentService invoiceForDoc = new InvoiceForDocumentService();
          invoiceForDoc.setId((String) invoiceData.get("id"));
          invoiceForDoc.setDescription((String) invoiceData.get("description"));
          invoiceForDoc.setCreatedDate(LocalDate.parse((String) invoiceData.get("createdDate")));
          invoiceForDoc.setClientId(invoiceData.get("clientId").toString());
          invoiceForDoc.setDateOfDelivery(
              LocalDate.parse((String) invoiceData.get("dateOfDelivery")));
          invoiceForDoc.setNoticeOfTaxExemption((String) invoiceData.get("noticeOfTaxExemption"));
          invoiceForDoc.setNoticeOfRetentionObligation(
              (String) invoiceData.get("noticeOfRetentionObligation"));

          invoiceForDoc.setNetTotal(convertToString(invoiceData.get("netTotal")));
          invoiceForDoc.setTaxTotal(convertToString(invoiceData.get("taxTotal")));
          invoiceForDoc.setTotal(convertToString(invoiceData.get("total")));
          invoiceForDoc.setTotalOutstanding(convertToString(invoiceData.get("totalOutstanding")));
          invoiceForDoc.setPaid((Boolean) invoiceData.get("paid"));

          List<Map<String, Object>> invoiceItemsData = (List<Map<String, Object>>) invoiceData.get(
              "invoiceItems");
          log.info("invoiceItemsData: " + invoiceItemsData);
          if (invoiceItemsData != null) {
            List<InvoiceItemForDocumentService> invoiceItems = new ArrayList<>();
            for (Map<String, Object> itemData : invoiceItemsData) {
              InvoiceItemForDocumentService invoiceItem = new InvoiceItemForDocumentService();
              invoiceItem.setId(Long.parseLong(itemData.get("id").toString()));
              invoiceItem.setName((String) itemData.get("name"));
              invoiceItem.setDescription((String) itemData.get("description"));
              invoiceItem.setCategory((String) itemData.get("category"));

              invoiceItem.setNetPrice(convertToDouble(itemData.get("netPrice")));
              invoiceItem.setQuantity(convertToDouble(itemData.get("quantity")));
              invoiceItem.setUnit((String) itemData.get("unit"));
              invoiceItem.setTaxRate(itemData.get("taxRate").toString());
              invoiceItem.setDiscount(convertToDouble(itemData.get("discount")));

              invoiceItem.setNetTotal(convertToString(itemData.get("netTotal")));
              invoiceItem.setTaxTotal(convertToString(itemData.get("taxTotal")));
              invoiceItem.setTotal(convertToString(itemData.get("total")));

              invoiceItems.add(invoiceItem);
            }
            invoiceForDoc.setInvoiceItems(invoiceItems);
          }

          InvoiceClosingCreationRequest invoiceClosingCreationRequest = new InvoiceClosingCreationRequest();
          invoiceClosingCreationRequest.setInvoice(invoiceForDoc);

          ClientForDocumentService client = exchange.getIn()
              .getBody(ClientForDocumentService.class);
          invoiceClosingCreationRequest.setClient(client);

          exchange.getIn().setBody(invoiceClosingCreationRequest);
        })
        .marshal(invoiceRequestJsonDataFormat)
        .setHeader(Exchange.HTTP_METHOD, constant("POST"))
        .setHeader("Content-Type", constant("application/json"))
        .toD(documentServiceUrl + "/api/v1/documents/invoice/invoice-closing/create")
        .log("Response received from document service: ${body}")
        .process(exchange -> {

          String responseBody = exchange.getIn().getBody(String.class);
          ObjectMapper objectMapperForDocument = new ObjectMapper();
          JsonNode responseJson = objectMapperForDocument.readTree(responseBody);

          String documentLink = responseJson.get("documentUrl").asText();
          String invoiceId = responseJson.get("invoiceId").asText();
          String documentId = responseJson.get("documentId").asText();
          String documentType = responseJson.get("documentType").asText();

          Document document = new Document();
          document.setDocumentId(documentId);
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

  // Methode zur Konvertierung von Werten in Double
  private double convertToDouble(Object value) {
    if (value == null) {
      return 0.0; // Default-Wert, wenn der Wert null ist
    }
    if (value instanceof Number) {
      return ((Number) value).doubleValue(); // Für alle numerischen Typen
    }
    try {
      return Double.parseDouble(value.toString()); // Fallback, um String zu konvertieren
    } catch (NumberFormatException e) {
      throw new RuntimeException("Cannot convert value to double: " + value);
    }
  }

  private String convertToString(Object value) {
    if (value == null) {
      return "0.00";
    }
    if (value instanceof Double || value instanceof Integer || value instanceof Long) {
      return String.format("%.2f",
          ((Number) value).doubleValue());
    }
    return value.toString();
  }
}
