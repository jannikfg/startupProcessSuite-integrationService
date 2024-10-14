package org.thi.sps.CanonicalDataModel;

import java.time.LocalDate;
import java.util.List;
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
public class Invoice {

  private String id;
  private String description;
  private LocalDate createdDate;
  private String clientId;
  private LocalDate dateOfDelivery;
  private String noticeOfTaxExemption;
  private String noticeOfRetentionObligation;
  private String netTotal;
  private String taxTotal;
  private String total;
  private String totalOutstanding;
  private boolean isPaid;

}
