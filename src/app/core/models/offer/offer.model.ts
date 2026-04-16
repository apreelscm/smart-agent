import { Customer } from '../customer/customer.model';
import { InsuredObject } from '../insured-object/insured-object.model';
import { PaymentPlan } from '../payment/payment-plan.model';
import { Vehicle } from '../vehicle/vehicle.model';
import { Address } from '../common/address.model';
import { AgentRef } from './agent-ref.model';
import { OfferStatus } from './offer-status.model';
import { OfferVariant } from './offer-variant.model';
import { SalesChannel } from './sales-channel.model';

export type OfferProduct = 'MOTOR' | 'CROP';

export interface Offer {
  id: string;
  product?: OfferProduct;
  offerNumber: string;
  status: OfferStatus;
  createdAt: string;
  updatedAt: string;
  validTo?: string;
  salesChannel: SalesChannel;
  agent: AgentRef;
  customer: Customer;
  vehicle: Vehicle;
  insuredObject: InsuredObject;
  variants: OfferVariant[];
  selectedVariantId?: string;
  selectedPaymentPlan?: PaymentPlan;
  renewalContext?: {
    sourcePolicyId: string;
    sourcePolicyNumber: string;
    mode: 'RENEW' | 'COPY';
  };
  contractData?: {
    productCode?: 'DEALER' | 'OC' | 'SHORT_TERM_OC' | 'BORDER_OC' | 'GREEN_CARD';
    paymentMethod?: 'INKASO' | 'PRZELEW';
    coverageStartDate?: string;
    coverageEndDate?: string;
    correspondenceAddress?: Address;
    marketingConsents?: {
      email: boolean;
      sms: boolean;
      phone: boolean;
    };
    attachments?: Array<{
      id: string;
      name: string;
      type: string;
    }>;
  };
  notes?: string[];
}
