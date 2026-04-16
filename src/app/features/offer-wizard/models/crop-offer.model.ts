export type CropSoilClassCode = 'I' | 'II' | 'III' | 'IV' | 'V' | 'VI';

export type CropParcel = {
  id: string;
  voivodeship: string;
  county: string;
  locality: string;
  precinctNumber: string;
  fieldNumber: string;
  cadastralPlotNumber: string;
  parcelAreaHa: number;
  cropAreaHa: number;
  soilClassCode: CropSoilClassCode | string;
};

export type CropMaster = {
  id: string;
  species: string;
  yieldPerHa: number;
  pricePerUnit: number;
  totalAreaHa: number;
  dominantSoilClassCode: CropSoilClassCode | string;
  parcels: CropParcel[];
};

export type CropClaimsHistory = {
  previousYear: number;
  twoYearsAgo: number;
  threeYearsAgo: number;
};

export type CropCoverCode =
  | 'HAIL'
  | 'HEAVY_RAIN_HURRICANE'
  | 'SPRING_FROST'
  | 'FLOOD'
  | 'WATER_STAGNATION'
  | 'FIRE';

export type CropCoverSelection = Record<CropCoverCode, boolean>;

export type CropVariantId = 'BASIC' | 'RECOMMENDED' | 'FULL';

export type CropParcelVariantConfig = {
  parcelId: string;
  selectedCovers: CropCoverSelection;
  deductiblePercent: 5 | 10 | 20;
};

export type CropVariantConfig = {
  variantId: CropVariantId;
  parcelConfigurations: CropParcelVariantConfig[];
};
