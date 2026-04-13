import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Policy } from '../models';

@Injectable({
  providedIn: 'root'
})
export class PoliciesRepository {
  private readonly httpClient = inject(HttpClient);
  private readonly policiesUrl = '/mock/policies.json';

  getPolicies(): Observable<Policy[]> {
    return this.httpClient.get<Policy[]>(this.policiesUrl);
  }

  getPolicyById(policyId: string): Observable<Policy | undefined> {
    return this.getPolicies().pipe(map((policies) => policies.find((policy) => policy.id === policyId)));
  }
}
