package org.thi.sps.routes.offers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.thi.sps.routes.generic.GenericSpiffworkflowRouteBuilder;

@ApplicationScoped
public class DocumentSendingRoute extends GenericSpiffworkflowRouteBuilder {

    @Inject
    @ConfigProperty(name = "redpanda.url")
    String redpandaUrl;

    public static String TOPIC_NAME = "DocumentSendingQueue";

    @Override
    public void configure() throws Exception {
      from("kafka:" + TOPIC_NAME + "?brokers=" + redpandaUrl)
          .log("Message received from Kafka: ${body}")
          .unmarshal().json()
          .choice()
          .when(simple("${body[route]} == 'mailRoute'"))
          .to("direct:mailRoute")
          .otherwise()
          .to("direct:otherSystemRoute");

      from("direct:mailRoute")
            .log("Sending offer to client via mail.");

      from("direct:otherSystemRoute")
            .log("Sending offer to client via other system.");

    }
}
