export interface ReferenceDataEntry {
  code: string;
  label: string;
}

export interface ReferenceData {
  offerStatuses: ReferenceDataEntry[];
  policyLines: ReferenceDataEntry[];
  salesChannels: ReferenceDataEntry[];
  vehicleUsages: ReferenceDataEntry[];
  vehicleFinancing: ReferenceDataEntry[];
}
