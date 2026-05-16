export type ProductType = 'car' | 'home' | 'health' | 'other';

export interface PolicyRenewal {
  id: string;
  productType: ProductType;
  policyNumber: string;
  insuredName: string;
  daysToEnd: number;
  identifier: string;
  hasRenewAction: boolean;
}

export interface PolicyRenewalFilter {
  product: string;
  policyNumber: string;
  client: string;
  regNumber: string;
}
