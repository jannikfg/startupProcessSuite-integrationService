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
public class Document {

  private String documentId;
  private String invoiceId;
  private String linkToDocument;
  private String documentType;

}
