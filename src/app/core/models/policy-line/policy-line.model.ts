import { Money } from '../common/money.model';
import { Cover } from '../cover/cover.model';
import { PolicyLineCode } from './policy-line-code.model';

export interface PolicyLine {
  id: string;
  code: PolicyLineCode;
  label: string;
  included: boolean;
  isRecommended?: boolean;
  basePremium?: Money;
  premium: Money;
  covers: Cover[];
}
