package org.thi.sps.processors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.thi.sps.CanonicalDataModel.Document;

public class DocumentProcessor implements Processor {

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void process(Exchange exchange) throws Exception {
    String body = exchange.getIn().getBody(String.class);

    JsonNode jsonNode = objectMapper.readTree(body);

    String documentId = jsonNode.get("documentId").asText();
    String invoiceId = jsonNode.get("invoiceId").asText();
    String linkToDocument = jsonNode.get("linkToDocument").asText();
    String documentType = jsonNode.get("documentType").asText();

    Document document = new Document();
    document.setDocumentId(documentId);
    document.setInvoiceId(invoiceId);
    document.setLinkToDocument(linkToDocument);
    document.setDocumentType(documentType);

    exchange.getIn().setBody(document);
  }

}
