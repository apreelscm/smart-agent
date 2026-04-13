export type VehicleUsage = 'PRIVATE' | 'BUSINESS' | 'MIXED' | 'TAXI' | 'DELIVERY';
export type VehicleFinancing = 'OWNED' | 'CREDIT' | 'LEASING';
export type VehicleSpecialUsage = 'RENTAL' | 'TAXI' | 'DRIVING_SCHOOL' | 'DEALER_VEHICLE';

export interface VehicleEngine {
  fuelType: 'PETROL' | 'DIESEL' | 'HYBRID' | 'ELECTRIC' | 'LPG';
  displacementCc?: number;
  powerKw?: number;
  powerHp?: number;
}

export interface VehicleRegistration {
  registrationNumber?: string;
  firstRegistrationDate?: string;
  countryCode?: string;
}

export interface Vehicle {
  id: string;
  make: string;
  model: string;
  version?: string;
  productionYear: number;
  vin: string;
  bodyType?: string;
  usage: VehicleUsage;
  financing: VehicleFinancing;
  specialUsages?: VehicleSpecialUsage[];
  grossVehicleWeightKg?: number;
  seats?: number;
  annualMileageKm?: number;
  marketValue?: number;
  registration?: VehicleRegistration;
  engine: VehicleEngine;
}
