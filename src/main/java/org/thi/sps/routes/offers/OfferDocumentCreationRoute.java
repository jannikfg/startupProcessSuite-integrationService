package org.thi.sps.routes.offers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class OfferDocumentCreationRoute extends RouteBuilder {

  @Inject
  @ConfigProperty(name = "documentservice.url")
  String documentServiceUrl;

  @Override
  public void configure() throws Exception {
    from("jetty:http://0.0.0.0:8087/api/v1/documents/offer/create")
        .log("Received a POST request on /api/v1/offers/create")
        .removeHeaders("CamelHttp*")
        .setHeader(Exchange.HTTP_METHOD, constant("POST"))
        .to(documentServiceUrl + "/api/v1/documents/offer/create")
        .log("Response received: ${body}");
  }
}
