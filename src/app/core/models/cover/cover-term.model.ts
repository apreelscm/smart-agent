export type CoverTermType =
  | 'SUM_INSURED'
  | 'LIMIT'
  | 'DEDUCTIBLE'
  | 'WAITING_PERIOD'
  | 'PERCENTAGE'
  | 'TEXT';

export type CoverTermUnit = 'PLN' | 'EUR' | 'DAY' | 'PERCENT' | 'KM';

export interface CoverTerm {
  id: string;
  type: CoverTermType;
  code: string;
  label: string;
  value: string | number;
  editable?: boolean;
  options?: Array<string | number>;
  unit?: CoverTermUnit;
}
