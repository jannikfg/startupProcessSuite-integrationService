package org.thi.sps.routes.invocieRoutes.documentRoutes.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ClientForDocumentService {

  private Long id;
  private String firstName;
  private String lastName;
  private String company;
  private String email;
  private String digitalContact;
  private String phone;
  private String address;
  private String city;
  private String plz;

}
