export type InsuredObjectType = 'VEHICLE';

export interface InsuredObject {
  id: string;
  type: InsuredObjectType;
  label: string;
  vehicleId: string;
  ownerCustomerId: string;
  primaryDriverCustomerId?: string;
}
