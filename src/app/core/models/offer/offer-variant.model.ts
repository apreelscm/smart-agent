import { Money } from '../common/money.model';
import { PaymentPlan } from '../payment/payment-plan.model';
import { PolicyLine } from '../policy-line/policy-line.model';

export interface OfferVariant {
  id: string;
  name: string;
  rank: number;
  badge?: string;
  recommended?: boolean;
  selected?: boolean;
  totalPremium: Money;
  monthlyPremium?: Money;
  paymentPlans?: PaymentPlan[];
  policyLines: PolicyLine[];
}
