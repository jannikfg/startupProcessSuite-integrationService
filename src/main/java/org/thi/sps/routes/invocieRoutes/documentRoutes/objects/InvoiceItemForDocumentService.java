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
public class InvoiceItemForDocumentService {
  private Long id;
  private String name;
  private String description;
  private String category;
  private double netPrice;
  private double quantity;
  private String unit;
  private String taxRate;
  private double discount; // x % Discount
  private String netTotal;
  private String taxTotal;
  private String total;
}
