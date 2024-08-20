package org.thi.sps.routes;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class AllClientsRoute extends RouteBuilder {

  @Inject
  @ConfigProperty(name = "clientservice.url")
  String clientServiceUrl;

  @Override
  public void configure() throws Exception {

    from("jetty:http://0.0.0.0:8087/api/v1/clients")
        .log("Received a GET request on /api/v1/clients")
        .removeHeaders("CamelHttp*")
        .setHeader(Exchange.HTTP_METHOD, constant("GET"))
        .to( clientServiceUrl + "/api/v1/clients")  // Externe API aufrufen
        .log("Response received: ${body}");
  }
}
