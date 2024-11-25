package org.thi.sps.routes.invocieRoutes.accountNumbersToGetDeposits;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.thi.sps.routes.generic.GenericSpiffworkflowRouteBuilder;

@ApplicationScoped
public class AccountNumbersToGetDepositsRoute extends GenericSpiffworkflowRouteBuilder {
  @Inject
  @ConfigProperty(name = "redpanda.url")
  String redpandaUrl;


  public static String TOPIC_NAME = "accountNumbersToGetDeposits";
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
        .log("Getting all deposist for incoming account numbers: ${body}");
    //TODO: Hier muss die Anbindung an die Banken implementiert werden
    //Nicht im Rahmen dieser Arbeit fortgeführt

  }

}
