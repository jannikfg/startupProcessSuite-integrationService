package org.thi.sps.routes.products;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.thi.sps.routes.generic.GenericSpiffworkflowRouteBuilder;

@ApplicationScoped
public class ProductLoadingRoute extends GenericSpiffworkflowRouteBuilder {

  @Inject
  @ConfigProperty(name = "redpanda.url")
  String redpandaUrl;

  @Inject
  @ConfigProperty(name = "productservice.url")
  String productServiceUrl;

  public static String TOPIC_NAME = "ProductLoadingQueue";
  public static String SPIFFWORKFLOW_MESSAGE_NAME = "ProductListMessage";

  @Override
  public void configure() throws Exception {

    from("kafka:" + TOPIC_NAME + "?brokers=" + redpandaUrl)
        .log("Message received from Kafka: ${body}")
        .removeHeaders("CamelKafka*")
        .setHeader("Content-Type", constant("application/json"))
        .setHeader(Exchange.HTTP_METHOD, constant("GET"))
        .toD(productServiceUrl + "/api/v1/products")
        .log("Response from Product Service: ${body}")
        .process(sendToSpiffworkflow(SPIFFWORKFLOW_MESSAGE_NAME))
        .onException(Exception.class)
        .log("Error occurred: ${exception.message}");
  }
}
