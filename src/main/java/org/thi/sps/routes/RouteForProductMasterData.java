package org.thi.sps.routes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class RouteForProductMasterData extends RouteBuilder {

  @Inject
  @ConfigProperty(name = "productservice.url")
  String productServiceUrl;

  @Override
  public void configure() {


    from("jetty:http://0.0.0.0:8087/api/v1/products/masterdata")
        .log("Received a GET request on /api/v1/products/masterdata")
        .removeHeaders("CamelHttp*")
        .setHeader(Exchange.HTTP_METHOD, constant("GET"))
        .to("direct:fetchUnits")
        .to("direct:fetchProductCategories")
        .to("direct:fetchTaxRates")
        .end()
        .process(exchange -> {
          String productCategoriesJson = exchange.getProperty("productCategories", String.class);
          String taxRatesJson = exchange.getProperty("taxRates", String.class);
          String unitsJson = exchange.getProperty("units", String.class);

          String combinedJsonResponse = String.format(
              "{\"productCategories\":%s,\"taxRates\":%s,\"units\":%s}",
              removeWrapper(productCategoriesJson),
              removeWrapper(taxRatesJson),
              removeWrapper(unitsJson)
          );


          exchange.getMessage().setBody(combinedJsonResponse);
          System.out.println("Response: " + combinedJsonResponse);
        });

    from("direct:fetchProductCategories")
        .setHeader("CamelHttpMethod", constant("GET"))
        .to(productServiceUrl + "/api/v1/products/productcategories")
        .log("Response from product categories: ${body}")
        .process(exchange -> {
          exchange.setProperty("productCategories", exchange.getIn().getBody(String.class));
        });

    from("direct:fetchTaxRates")
        .setHeader("CamelHttpMethod", constant("GET"))
        .to(productServiceUrl + "/api/v1/products/taxrates")
        .log("Response from tax rates: ${body}")
        .process(exchange -> {
          exchange.setProperty("taxRates", exchange.getIn().getBody(String.class));
        });

    from("direct:fetchUnits")
        .setHeader("CamelHttpMethod", constant("GET"))
        .to(productServiceUrl + "/api/v1/products/units")
        .log("Response from units: ${body}")
        .process(exchange -> {
          exchange.setProperty("units", exchange.getIn().getBody(String.class));
        });
  }

  private String removeWrapper(String json) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();

      Map<String, Object> map = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});

      if (!map.isEmpty()) {
        return objectMapper.writeValueAsString(map.values().iterator().next());
      } else {
        return "{}";
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "{}";
    }
  }
}
