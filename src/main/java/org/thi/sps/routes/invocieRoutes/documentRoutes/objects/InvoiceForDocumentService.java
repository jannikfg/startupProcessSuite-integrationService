package org.thi.sps.routes.invocieRoutes.documentRoutes.objects;

import java.time.LocalDate;
import java.util.List;
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
public class InvoiceForDocumentService {

  private String id;
  private String description;
  private LocalDate createdDate;
  private List<InvoiceItemForDocumentService> invoiceItems;
  private String clientId;
  private LocalDate dateOfDelivery;
  private String noticeOfTaxExemption;
  private String noticeOfRetentionObligation;
  private String netTotal;
  private String taxTotal;
  private String total;
  private String totalOutstanding;
  private boolean isPaid;
  private List<PaymentForDocumentService> payments;
  private List<ReminderForDocumentService> reminders;

}
