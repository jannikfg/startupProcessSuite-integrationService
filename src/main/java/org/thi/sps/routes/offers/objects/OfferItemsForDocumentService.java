package org.thi.sps.routes.offers.objects;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class OfferItemsForDocumentService {
  private Long id;
  private String name;
  private String description;
  private String category;

  private String quantity;
  private String unit;

  private String netPrice;
  private String discount;
  private String netTotal;

  private String taxRate;
  private String taxTotal;

  private String total;
}
