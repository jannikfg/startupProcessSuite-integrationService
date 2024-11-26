package org.thi.sps.routes.offers.objects;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class OfferForDocumentService {
  private Long id;
  private String description;
  private List<OfferItemsForDocumentService> items;
  private String clientId;
  private LocalDate offerDate;
  private LocalDate validUntil;

  private String netTotal;
  private String taxTotal;
  private String total;
}
