import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Offer } from '../models';

export type OfferProduct = 'MOTOR' | 'CROP';

@Injectable({
  providedIn: 'root'
})
export class OffersRepository {
  private readonly httpClient = inject(HttpClient);
  private readonly offersUrl = '/mock/offers.json';
  private readonly motorTemplateOfferId = 'offer-1001';
  private readonly cropTemplateUrl = '/mock/offers-crop-template.json';

  getOffers(): Observable<Offer[]> {
    return this.httpClient.get<Offer[]>(this.offersUrl);
  }

  getOfferById(offerId: string): Observable<Offer | undefined> {
    return this.getOffers().pipe(map((offers) => offers.find((offer) => offer.id === offerId)));
  }

  getTemplateOffer(product: OfferProduct = 'MOTOR'): Observable<Offer | undefined> {
    if (product === 'CROP') {
      return this.httpClient.get<Offer>(this.cropTemplateUrl).pipe(map((offer) => offer ?? undefined));
    }

    return this.getOfferById(this.motorTemplateOfferId);
  }
}
