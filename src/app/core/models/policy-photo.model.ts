export type PolicyPhotoStatus = 'do-blisko' | 'w-werifikacja';

export interface PolicyPhoto {
  id: string;
  productType: string;
  productColor: string;
  policyNumber: string;
  status: PolicyPhotoStatus;
  branchNumber: string;
  insuredName: string;
  expiryDate: string;
}

export interface PolicyPhotoFilter {
  days30: boolean;
  product: string;
  status: string;
}
