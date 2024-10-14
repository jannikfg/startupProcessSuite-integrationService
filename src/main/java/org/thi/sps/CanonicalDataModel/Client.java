package org.thi.sps.CanonicalDataModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Client {

  private Long id;
  private String firstName;
  private String lastName;
  private String company;
  private String email;
  private boolean digitalContact;
  private String phone;
  private String address;
  private String city;
  private String plz;

}
