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
public class ReminderForDocumentService {

  private String id;
  private String reminderLevel;
  private LocalDate reminderDate;
  private int dueInDays;
  private double totalOutstanding;

}
