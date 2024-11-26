package org.thi.sps.routes.offers.offerDocumentCreation;

import lombok.*;
import org.thi.sps.routes.offers.objects.ClientForDocumentService;
import org.thi.sps.routes.offers.objects.OfferForDocumentService;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class OfferCreationRequest {
  ClientForDocumentService client;
  OfferForDocumentService offer;
}
