package org.thi.sps.routes.invocieRoutes.dataForInvoiceChanges;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class ClientByIdRoute extends RouteBuilder {

  @Inject
  @ConfigProperty(name = "clientservice.url")
  String clientServiceUrl;

  @Override
  public void configure() throws Exception {

    from("jetty:http://0.0.0.0:8087/api/v1/clients/*")
        .log("Received a GET request on /api/v1/clients with raw path: ${header.CamelHttpPath}")
        .setHeader("id", simple("${header.CamelHttpPath.split('/')[4]}"))
        .log("Extracted client ID: ${header.id}")
        .removeHeaders("CamelHttp*")
        .setHeader(Exchange.HTTP_METHOD, constant("GET"))
        .setHeader(Exchange.HTTP_URI, simple(clientServiceUrl + "/api/v1/clients/${header.id}"))
        .toD("${header." + Exchange.HTTP_URI + "}")
        .log("Response received for client ID ${header.id}: ${body}");
  }
}
