import { Injectable, signal, computed } from '@angular/core';
import { PolicyDraft, emptyDraft } from '../models/policy-draft.model';

@Injectable({ providedIn: 'root' })
export class PolicyDraftService {
  private _draft = signal<PolicyDraft>(emptyDraft());

  readonly draft = this._draft.asReadonly();

  readonly vehicle = computed(() => this._draft().vehicle);
  readonly coverages = computed(() => this._draft().coverages);
  readonly policyholder = computed(() => this._draft().policyholder);

  patch(partial: Partial<PolicyDraft>): void {
    this._draft.update(d => ({ ...d, ...partial }));
  }

  patchVehicle(partial: Partial<PolicyDraft['vehicle']>): void {
    this._draft.update(d => ({ ...d, vehicle: { ...d.vehicle, ...partial } }));
  }

  patchUsage(partial: Partial<PolicyDraft['usage']>): void {
    this._draft.update(d => ({ ...d, usage: { ...d.usage, ...partial } }));
  }

  patchCoverages(partial: Partial<PolicyDraft['coverages']>): void {
    this._draft.update(d => ({ ...d, coverages: { ...d.coverages, ...partial } }));
  }

  patchPolicyholder(partial: Partial<PolicyDraft['policyholder']>): void {
    this._draft.update(d => ({ ...d, policyholder: { ...d.policyholder, ...partial } }));
  }

  patchInsured(partial: Partial<PolicyDraft['insured']>): void {
    this._draft.update(d => ({ ...d, insured: { ...d.insured, ...partial } }));
  }

  patchPersonalInfo(partial: Partial<PolicyDraft['personalInfo']>): void {
    this._draft.update(d => ({ ...d, personalInfo: { ...d.personalInfo, ...partial } }));
  }

  reset(): void {
    this._draft.set(emptyDraft());
  }
}
