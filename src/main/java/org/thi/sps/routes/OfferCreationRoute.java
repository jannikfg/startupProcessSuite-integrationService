package org.thi.sps.routes;

import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

public class OfferCreationRoute extends RouteBuilder {

  @Inject
  @ConfigProperty(name = "offerservice.url")
  String offerServiceUrl;

  @Override
  public void configure() throws Exception {
    from("jetty:http://0.0.0.0:8087/api/v1/offers/create")
        .log("Received a POST request on /api/v1/offers/create")
        .removeHeaders("CamelHttp*")
        .setHeader(Exchange.HTTP_METHOD, constant("POST"))
        .to(offerServiceUrl + "/api/v1/offers/create")
        .log("Response received: ${body}");
  }
}
