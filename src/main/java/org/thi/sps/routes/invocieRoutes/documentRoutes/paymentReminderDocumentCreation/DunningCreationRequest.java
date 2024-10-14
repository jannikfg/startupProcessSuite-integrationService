package org.thi.sps.routes.invocieRoutes.documentRoutes.paymentReminderDocumentCreation;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.thi.sps.routes.invocieRoutes.documentRoutes.objects.ClientForDocumentService;
import org.thi.sps.routes.invocieRoutes.documentRoutes.objects.InvoiceForDocumentService;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class DunningCreationRequest {

  private InvoiceForDocumentService invoice;
  private ClientForDocumentService client;
  private String dunningId;
  private String reminderLevel;
  private LocalDate reminderDate;
  private int dueInDays;

}
