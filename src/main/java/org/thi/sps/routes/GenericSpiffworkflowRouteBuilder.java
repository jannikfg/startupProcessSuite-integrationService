package org.thi.sps.routes;

import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.Response;
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

  protected void sendToSpiffworkflow(ProcessorDefinition<RouteDefinition> processorDefinition, String message){
  processorDefinition
      .log("Sending message to Spiffworkflow: ${body}")
      .process(exchange -> {
        String accessToken = getAccessToken();
        exchange.getIn().setHeader("Authorization", "Bearer " + accessToken);
      })
      .to(spiffworkflowBackendUrl + "/v1.0/messages/" + message);
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
