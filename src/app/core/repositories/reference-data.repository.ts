import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ReferenceData } from '../models/reference-data/reference-data.model';

@Injectable({
  providedIn: 'root'
})
export class ReferenceDataRepository {
  private readonly httpClient = inject(HttpClient);
  private readonly referenceDataUrl = '/mock/reference-data.json';

  getReferenceData(): Observable<ReferenceData> {
    return this.httpClient.get<ReferenceData>(this.referenceDataUrl);
  }
}
