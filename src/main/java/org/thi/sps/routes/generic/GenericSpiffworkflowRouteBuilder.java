package org.thi.sps.routes.generic;

import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.Response;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RouteDefinition;
import org.eclipse.microprofile.config.inject.ConfigProperty;

public abstract class GenericSpiffworkflowRouteBuilder extends RouteBuilder {
  @Inject
  @ConfigProperty(name = "oidc.token.endpoint")
  String tokenEndpoint;

  @Inject
  @ConfigProperty(name = "oidc.client-id")
  String clientId;

  @Inject
  @ConfigProperty(name = "oidc.credentials.secret")
  String clientSecret;

  @Inject
  @ConfigProperty(name = "oidc.user.name")
  String user;

  @Inject
  @ConfigProperty(name = "oidc.user.password")
  String userPassword;

  @Inject
  @ConfigProperty(name = "spiffworkflow.backend.url")
  String spiffworkflowBackendUrl;

  public Processor sendToSpiffworkflow(String message) {
    return exchange -> {
      String accessToken = getAccessToken();
      String url = spiffworkflowBackendUrl + "/v1.0/messages/" + message;

      Client client = ClientBuilder.newClient();
      WebTarget target = client.target(url);

      Response response = target.request()
          .header("Authorization", "Bearer " + accessToken)
          .post(Entity.json(exchange.getIn().getBody()));

      exchange.getIn().setBody(response.readEntity(String.class));
      response.close();
    };
  }

  private String getAccessToken() {
    Client client = ClientBuilder.newClient();
    WebTarget target = client.target(tokenEndpoint);

    Form form = new Form();
    form.param("grant_type", "password");
    form.param("client_id", clientId);
    form.param("client_secret", clientSecret);
    form.param("username", user);
    form.param("password", userPassword);

    Response response = target.request().post(Entity.form(form));
    String token = response.readEntity(JsonObject.class).getString("access_token");
    response.close();

    return token;
  }
}