export type VehicleType = 'osobowy' | 'motocykl' | 'przyczepa' | 'dostawczy';
export type FuelType = 'benzyna' | 'diesel' | 'elektryczny' | 'gaz';
export type LicenseYears = 'more3' | '1to3' | 'less1';
export type VehiclePurpose = 'prywatny' | 'firmowy';
export type PaymentMethod = 'przelew' | 'karta' | 'applepay' | 'googlepay' | 'odroczona';

export interface Address {
  zipCode: string;
  city: string;
  street: string;
  houseNumber: string;
  flatNumber: string;
}

export interface Person {
  firstName: string;
  lastName: string;
  pesel: string;
  email: string;
  phone: string;
  address: Address;
}

export interface AcDetails {
  theftScope: 'total' | 'total_partial';
  damageScope: 'total' | 'total_partial';
  claimSettlement: 'kosztorys' | 'siec' | 'aso';
  partsProtection: 'all' | 'exploitable';
  deductible: 0 | 500 | 1000 | 3000;
  fixedValue: boolean;
}

export interface AssistanceDetails {
  territory: 'polska' | 'europa';
  level: 'podstawowy' | 'standardowy' | 'optymalny' | 'maksymalny';
}

export interface NnwDetails {
  sumInsured: number;
  mediplan: 'podstawowy' | 'rozszerzony';
}

export interface SzybyDetails {
  partsType: 'alternatywne' | 'oryginalne';
  noDeductibleFirst: boolean;
  deductibleNext: boolean;
  sumInsured: number;
}

export interface PolicyDraft {
  personalInfo: {
    firstName: string;
    lastName: string;
    dateOfBirth: string;
  };
  vehicle: {
    plateNumber: string;
    type: VehicleType;
    year: number;
    make: string;
    model: string;
    fuelType: FuelType;
    engineCC: number;
    engineKM: number;
    version: string;
    versionValue: number;
    additionalEquipmentValue: number;
    totalValue: number;
  };
  usage: {
    purpose: VehiclePurpose;
    dailyKm: number;
    licenseYears: LicenseYears;
    zipCode: string;
  };
  coverages: {
    oc: boolean;
    ac: boolean;
    acDetails: AcDetails;
    assistance: boolean;
    assistanceDetails: AssistanceDetails;
    nnwKip: boolean;
    nnwDetails: NnwDetails;
    szyby: boolean;
    szybyDetails: SzybyDetails;
    bagaz: boolean;
    opony: boolean;
    promoCode: string;
    totalPremium: number;
  };
  policyholder: Person;
  insured: Person;
  coOwner: Person | null;
  payment: {
    installments: 1 | 2 | 3 | 4;
    method: PaymentMethod;
  };
  consents: {
    owu: boolean;
    kid: boolean;
    marketing: boolean;
  };
}

export function emptyAddress(): Address {
  return { zipCode: '', city: '', street: '', houseNumber: '', flatNumber: '' };
}

export function emptyPerson(): Person {
  return { firstName: '', lastName: '', pesel: '', email: '', phone: '', address: emptyAddress() };
}

export function emptyDraft(): PolicyDraft {
  return {
    personalInfo: { firstName: '', lastName: '', dateOfBirth: '' },
    vehicle: {
      plateNumber: '', type: 'osobowy', year: 0, make: '', model: '',
      fuelType: 'benzyna', engineCC: 0, engineKM: 0, version: '',
      versionValue: 0, additionalEquipmentValue: 0, totalValue: 0,
    },
    usage: { purpose: 'prywatny', dailyKm: 40, licenseYears: 'more3', zipCode: '' },
    coverages: {
      oc: true, ac: false,
      acDetails: { theftScope: 'total', damageScope: 'total', claimSettlement: 'kosztorys', partsProtection: 'all', deductible: 0, fixedValue: true },
      assistance: false,
      assistanceDetails: { territory: 'polska', level: 'podstawowy' },
      nnwKip: false,
      nnwDetails: { sumInsured: 50000, mediplan: 'podstawowy' },
      szyby: false,
      szybyDetails: { partsType: 'alternatywne', noDeductibleFirst: true, deductibleNext: true, sumInsured: 10000 },
      bagaz: false, opony: false, promoCode: '', totalPremium: 289,
    },
    policyholder: emptyPerson(),
    insured: emptyPerson(),
    coOwner: null,
    payment: { installments: 1, method: 'przelew' },
    consents: { owu: false, kid: false, marketing: false },
  };
}

export function seedDraft(): PolicyDraft {
  const person: Person = {
    firstName: 'Anna', lastName: 'Kowalska',
    pesel: '82030312345', email: 'anna.kowalska@example.pl', phone: '+48 123 456 789',
    address: { zipCode: '00-713', city: 'Warszawa', street: 'ul. Lubomirska', houseNumber: '4', flatNumber: '32' },
  };
  return {
    personalInfo: { firstName: 'Anna', lastName: 'Kowalska', dateOfBirth: '1982-03-03' },
    vehicle: {
      plateNumber: 'WI 12345',
      type: 'osobowy',
      year: 2020,
      make: 'Volkswagen',
      model: 'T-ROC 17-21',
      fuelType: 'benzyna',
      engineCC: 1498,
      engineKM: 150,
      version: 'T-ROC 1.5 TSI ACT Premium',
      versionValue: 135790,
      additionalEquipmentValue: 5000,
      totalValue: 140790,
    },
    usage: { purpose: 'prywatny', dailyKm: 40, licenseYears: 'more3', zipCode: '00-713' },
    coverages: {
      oc: true,
      ac: false,
      acDetails: { theftScope: 'total_partial', damageScope: 'total_partial', claimSettlement: 'kosztorys', partsProtection: 'all', deductible: 0, fixedValue: true },
      assistance: false,
      assistanceDetails: { territory: 'polska', level: 'optymalny' },
      nnwKip: false,
      nnwDetails: { sumInsured: 100000, mediplan: 'podstawowy' },
      szyby: false,
      szybyDetails: { partsType: 'alternatywne', noDeductibleFirst: true, deductibleNext: true, sumInsured: 10000 },
      bagaz: false,
      opony: false,
      promoCode: '',
      totalPremium: 289,
    },
    policyholder: { ...person },
    insured: { ...person },
    coOwner: null,
    payment: { installments: 1, method: 'przelew' },
    consents: { owu: true, kid: true, marketing: false },
  };
}
