package org.thi.sps.routes.invocieRoutes.invoiceToRecord;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class InvoiceToRecordRoute extends RouteBuilder {
  @Inject
  @ConfigProperty(name = "redpanda.url")
  String redpandaUrl;


  public static String TOPIC_NAME = "invoiceToRecord";
  @Override
  public void configure() throws Exception {
    getContext().setTracing(true);
    System.out.println(redpandaUrl);
    from("kafka:" + TOPIC_NAME + "?brokers=" + redpandaUrl)
        .log("Message received from Kafka: ${body}")
        .removeHeaders("CamelHttp*")
        .removeHeaders("kafka*")
        .setHeader("Content-Type", constant("*/*"))
        .setHeader(Exchange.HTTP_METHOD, constant("POST"))
        .log("Sending message to booking service with body: ${body}")
        .log("Sending message to booking service with headers: ${headers}");

  }

}
