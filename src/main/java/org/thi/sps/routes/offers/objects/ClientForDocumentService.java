package org.thi.sps.routes.offers.objects;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
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
