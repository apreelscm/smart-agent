import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Offer } from '../models';

@Injectable({
  providedIn: 'root'
})
export class OffersRepository {
  private readonly httpClient = inject(HttpClient);
  private readonly offersUrl = '/mock/offers.json';

  getOffers(): Observable<Offer[]> {
    return this.httpClient.get<Offer[]>(this.offersUrl);
  }

  getOfferById(offerId: string): Observable<Offer | undefined> {
    return this.getOffers().pipe(map((offers) => offers.find((offer) => offer.id === offerId)));
  }

  getTemplateOffer(): Observable<Offer | undefined> {
    return this.getOfferById('offer-1001');
  }
}
