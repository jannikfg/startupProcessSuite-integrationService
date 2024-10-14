package org.thi.sps.routes.invocieRoutes.dataForInvoiceChanges;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class AllProductsRoute extends RouteBuilder {

  @Inject
  @ConfigProperty(name = "productservice.url")
  String productServiceUrl;

  @Override
  public void configure() throws Exception {

    from("jetty:http://0.0.0.0:8087/api/v1/products")
        .log("Received a GET request on /api/v1/products")
        .removeHeaders("CamelHttp*")
        .setHeader(Exchange.HTTP_METHOD, constant("GET"))
        .to( productServiceUrl + "/api/v1/products")  // Externe API aufrufen
        .log("Response received: ${body}");
  }
}
