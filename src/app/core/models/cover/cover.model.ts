import { Money } from '../common/money.model';
import { CoverTerm } from './cover-term.model';

export interface Cover {
  id: string;
  code: string;
  label: string;
  enabled: boolean;
  selectable?: boolean;
  premiumDelta?: Money;
  description?: string;
  terms: CoverTerm[];
}
