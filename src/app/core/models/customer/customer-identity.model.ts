import { PersonName } from '../common/person-name.model';

export interface NaturalPersonIdentity {
  type: 'NATURAL_PERSON';
  personName: PersonName;
  pesel: string;
  birthDate: string;
  citizenshipCountryCode?: string;
}

export interface SoleProprietorIdentity {
  type: 'SOLE_PROPRIETOR';
  personName: PersonName;
  pesel?: string;
  companyName: string;
  nip: string;
  regon?: string;
}

export interface LegalEntityIdentity {
  type: 'LEGAL_ENTITY';
  companyName: string;
  nip: string;
  regon?: string;
  krs?: string;
}

export type CustomerIdentity =
  | NaturalPersonIdentity
  | SoleProprietorIdentity
  | LegalEntityIdentity;
