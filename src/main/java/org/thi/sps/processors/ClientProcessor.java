package org.thi.sps.processors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.thi.sps.CanonicalDataModel.Client;

public class ClientProcessor implements Processor {

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void process(Exchange exchange) throws Exception {
    String body = exchange.getIn().getBody(String.class);
    System.out.println("Body: " + body);

    JsonNode jsonNode = objectMapper.readTree(body);
    System.out.println("JsonNode" + jsonNode);

    Long id = jsonNode.get("id").asLong();
    String firstName = jsonNode.get("firstName").asText();
    String lastName = jsonNode.get("lastName").asText();
    String company = jsonNode.get("company").asText();
    String email = jsonNode.get("email").asText();
    boolean digitalContact = jsonNode.get("digitalContact").asBoolean();
    String phone = jsonNode.get("phone").asText();
    String address = jsonNode.get("address").asText();
    String city = jsonNode.get("city").asText();
    String plz = jsonNode.get("plz").asText();

    Client client = new Client();
    client.setId(id);
    client.setFirstName(firstName);
    client.setLastName(lastName);
    client.setCompany(company);
    client.setEmail(email);
    client.setDigitalContact(digitalContact);
    client.setPhone(phone);
    client.setAddress(address);
    client.setCity(city);
    client.setPlz(plz);

    String clientJson = objectMapper.writeValueAsString(client);

    exchange.getIn().setBody(clientJson);
  }
}
