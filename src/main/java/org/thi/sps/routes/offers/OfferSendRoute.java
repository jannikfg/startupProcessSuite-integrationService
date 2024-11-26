package org.thi.sps.routes.offers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.thi.sps.routes.generic.GenericSpiffworkflowRouteBuilder;

@ApplicationScoped
public class OfferSendRoute extends GenericSpiffworkflowRouteBuilder {

    @Inject
    @ConfigProperty(name = "redpanda.url")
    String redpandaUrl;

    public static String TOPIC_NAME = "offer-send";

    @Override
    public void configure() throws Exception {
      from("kafka:" + TOPIC_NAME + "?brokers=" + redpandaUrl)
          .log("Message received from Kafka: ${body}")
          .setHeader(Exchange.HTTP_METHOD, constant("POST"))
          .log("Sending offer to client: ${body}");
    }
}
