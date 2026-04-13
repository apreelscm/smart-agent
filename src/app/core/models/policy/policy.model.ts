export type PolicyPaymentStatus = 'PAID' | 'TO_PAY';

export type PolicyStatus = 'ACTIVE' | 'RENEWAL' | 'CANCELED';

type PolicyAddress = {
  street: string;
  buildingNumber: string;
  apartmentNumber?: string;
  postalCode: string;
  city: string;
  countryCode: string;
};

export type Policy = {
  id: string;
  sourceOfferId?: string;
  policyNumber: string;
  customerName: string;
  vehicleLabel: string;
  registrationNumber: string;
  productName: string;
  status: PolicyStatus;
  paymentStatus: PolicyPaymentStatus;
  annualPremium: number;
  monthlyPremium: number;
  issueDate: string;
  conclusionDate: string;
  coverageEndDate: string;
  dueDate?: string;
  renewalDate?: string;
  updatedAt: string;
  agentName: string;
  residenceAddress?: PolicyAddress;
  marketingConsents?: {
    email: boolean;
    sms: boolean;
    phone: boolean;
  };
};
