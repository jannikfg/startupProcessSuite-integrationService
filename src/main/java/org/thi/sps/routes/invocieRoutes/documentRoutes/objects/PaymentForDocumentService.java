package org.thi.sps.routes.invocieRoutes.documentRoutes.objects;

import java.time.LocalDate;
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
public class PaymentForDocumentService {

  private Long id;
  private String invoiceId;
  private LocalDate paymentDate;
  private double amount;
  private String method;
  private String reference;

}
