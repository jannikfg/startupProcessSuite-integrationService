package org.thi.sps;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class RouteForProductMasterData extends RouteBuilder {

  @Inject
  @ConfigProperty(name = "productservice.url")
  String productServiceUrl;

  @Override
  public void configure() throws Exception {

    from("jetty:http://0.0.0.0:8087/api/v1/products/masterdata")
        .log("Received a GET request on /api/v1/products/masterdata")
        .removeHeaders("CamelHttp*")
        .setHeader(Exchange.HTTP_METHOD, constant("GET"))
        .to(productServiceUrl + "/api/v1/products/productcategories")  // Externe API aufrufen
        .log("Response received: ${body}");
        /*.process(exchange -> {
          // Optionale Verarbeitung des Antwortkörpers
          String responseBody = exchange.getIn().getBody(String.class);
          exchange.getMessage().setBody(responseBody);
        });*/
  }
}
