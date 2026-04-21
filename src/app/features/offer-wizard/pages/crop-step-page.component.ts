import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ButtonDirective } from 'primeng/button';
import { Checkbox } from 'primeng/checkbox';
import { InputText } from 'primeng/inputtext';
import { Select } from 'primeng/select';
import { SelectButton } from 'primeng/selectbutton';
import { Offer } from '../../../core/models';
import { PresentAmountPipe } from '../../../shared/pipes/present-amount.pipe';
import { CurrencyAmountInputComponent } from '../../../shared/ui/currency-amount-input/currency-amount-input.component';
import { SectionCardComponent } from '../../../shared/ui/section-card/section-card.component';
import { CropMaster, CropParcel } from '../models/crop-offer.model';
import { OfferWizardStateService } from '../state/offer-wizard-state.service';

type YesNoOption = {
  label: string;
  value: boolean;
};

type TariffClientRole = 'POLICY_HOLDER' | 'INSURED';

type TariffClient = {
  id: string;
  role: TariffClientRole;
  customerType: string;
  citizenshipCountryCode: string;
  firstName: string;
  lastName: string;
  pesel: string;
  birthDate: string;
  postalCode: string;
  previousLastName?: string;
  isVip: boolean;
};

type SoilClassOption = {
  code: string;
  label: string;
  factor: number;
};

type CropOfferPayload = {
  cropData?: {
    tariffClients?: TariffClient[];
    claimHistory?: {
      previousYear: number;
      twoYearsAgo: number;
      threeYearsAgo: number;
    };
  };
};

@Component({
  selector: 'app-crop-step-page',
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SectionCardComponent,
    SelectButton,
    InputText,
    Select,
    Checkbox,
    ButtonDirective,
    PresentAmountPipe,
    CurrencyAmountInputComponent
  ],
  templateUrl: './crop-step-page.component.html',
  styleUrl: './crop-step-page.component.scss'
})
export class CropStepPageComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly wizardState = inject(OfferWizardStateService);

  protected readonly currentYear = new Date().getFullYear();
  protected readonly expandedCrops = signal<Record<string, boolean>>({ 'crop-1': true });
  protected readonly expandedParcels = signal<Record<string, boolean>>({});

  protected readonly yesNoSelectOptions: YesNoOption[] = [
    { label: 'Tak', value: true },
    { label: 'Nie', value: false }
  ];

  protected readonly customerTypeOptions = [
    { code: 'NATURAL_PERSON', label: 'Osoba fizyczna' },
    { code: 'SOLE_PROPRIETOR', label: 'Jednoosobowa działalność' }
  ];

  protected readonly citizenshipOptions = [
    { code: 'PL', label: 'Polska' },
    { code: 'DE', label: 'Niemcy' },
    { code: 'CZ', label: 'Czechy' },
    { code: 'SK', label: 'Słowacja' }
  ];

  protected readonly cropOptions = [
    { code: 'PSZENICA', label: 'Pszenica' },
    { code: 'KUKURYDZA', label: 'Kukurydza' },
    { code: 'RZEPAK', label: 'Rzepak' },
    { code: 'BURAK_CUKROWY', label: 'Burak cukrowy' }
  ];
  protected readonly soilClassOptions: SoilClassOption[] = [
    { code: 'I', label: 'Klasa I', factor: 10 },
    { code: 'II', label: 'Klasa II', factor: 9 },
    { code: 'III', label: 'Klasa III', factor: 8 },
    { code: 'IV', label: 'Klasa IV', factor: 7 },
    { code: 'V', label: 'Klasa V', factor: 6 },
    { code: 'VI', label: 'Klasa VI', factor: 5 }
  ];
  protected readonly voivodeshipOptions = [
    { code: 'MAZOWIECKIE', label: 'Mazowieckie' },
    { code: 'WIELKOPOLSKIE', label: 'Wielkopolskie' },
    { code: 'LUBELSKIE', label: 'Lubelskie' },
    { code: 'PODLASKIE', label: 'Podlaskie' },
    { code: 'KUJAWSKO_POMORSKIE', label: 'Kujawsko-pomorskie' }
  ];

  protected readonly tariffClients = signal<TariffClient[]>([]);
  protected readonly editingClientId = signal<string | null>(null);
  protected readonly clientRoleControl = this.formBuilder.control<TariffClientRole>('POLICY_HOLDER', { nonNullable: true });
  protected roleOptions = [
    {
      label: 'Ubezpieczający',
      value: 'POLICY_HOLDER',
      disabled: false
    },
    {
      label: 'Ubezpieczony',
      value: 'INSURED',
      disabled: false
    }
  ];

  protected readonly tariffClientForm = this.formBuilder.group({
    customerType: this.formBuilder.control('NATURAL_PERSON'),
    citizenshipCountryCode: this.formBuilder.control('PL'),
    customerFirstName: this.formBuilder.control(''),
    customerLastName: this.formBuilder.control(''),
    customerPesel: this.formBuilder.control(''),
    customerBirthDate: this.formBuilder.control(''),
    customerPostalCode: this.formBuilder.control(''),
    previousLastName: this.formBuilder.control(''),
    isVip: this.formBuilder.control(false)
  });
  protected readonly claimHistoryForm = this.formBuilder.group({
    previousYear: this.formBuilder.control(0, { nonNullable: true }),
    twoYearsAgo: this.formBuilder.control(0, { nonNullable: true }),
    threeYearsAgo: this.formBuilder.control(0, { nonNullable: true })
  });

  protected analysisNeeded = true;
  protected readonly crops = signal<CropMaster[]>([]);

  constructor() {
    const draftCrops = this.wizardState.cropDraft();
    this.crops.set(draftCrops.length > 0 ? structuredClone(draftCrops) : this.createInitialCrops());
    this.wizardState.setCropDraft(this.crops());
    this.initializeTariffClientsFromOffer();
    this.initializeClaimHistoryFromOffer();

    this.claimHistoryForm.valueChanges.pipe(takeUntilDestroyed()).subscribe((value) => {
      this.wizardState.setCropAuxiliaryData({
        claimHistory: {
          previousYear: Number(value.previousYear ?? 0),
          twoYearsAgo: Number(value.twoYearsAgo ?? 0),
          threeYearsAgo: Number(value.threeYearsAgo ?? 0)
        }
      });
    });
  }

  protected addCrop(): void {
    const index = this.crops().length + 1;
    const newCrop = this.createDefaultCrop(index);
    this.crops.update((items) => [...items, newCrop]);
    this.expandedCrops.update((state) => ({
      ...state,
      [newCrop.id]: false
    }));
    this.syncCropsDraft();
  }

  protected removeCrop(cropId: string): void {
    const crop = this.crops().find((item) => item.id === cropId);

    this.crops.update((items) => items.filter((item) => item.id !== cropId));

    if (!crop) {
      return;
    }

    this.expandedParcels.update((state) => {
      const next = { ...state };
      crop.parcels.forEach((parcel) => delete next[parcel.id]);
      return next;
    });
    this.expandedCrops.update((state) => {
      if (!(cropId in state)) {
        return state;
      }
      const next = { ...state };
      delete next[cropId];
      return next;
    });
    this.syncCropsDraft();
  }

  protected updateCrop(cropId: string, field: keyof Omit<CropMaster, 'id' | 'parcels'>, value: string | number): void {
    this.crops.update((items) =>
      items.map((crop) => {
        if (crop.id !== cropId) {
          return crop;
        }

        if (field === 'yieldPerHa' || field === 'pricePerUnit' || field === 'totalAreaHa') {
          const parsed = typeof value === 'number' ? value : Number(value);
          return { ...crop, [field]: Number.isNaN(parsed) ? crop[field] : parsed } as CropMaster;
        }

        return { ...crop, [field]: String(value) } as CropMaster;
      })
    );
    this.syncCropsDraft();
  }

  protected addParcel(cropId: string): void {
    this.crops.update((items) =>
      items.map((crop) => {
        if (crop.id !== cropId) {
          return crop;
        }

        const parcel = this.createDefaultParcel(crop.parcels.length + 1);
        const parcels = [...crop.parcels, parcel];
        return {
          ...crop,
          parcels,
          totalAreaHa: this.sumCropArea(parcels)
        };
      })
    );
    this.syncCropsDraft();
  }

  protected removeParcel(cropId: string, parcelId: string): void {
    this.crops.update((items) =>
      items.map((crop) => {
        if (crop.id !== cropId) {
          return crop;
        }

        const parcels = crop.parcels.filter((parcel) => parcel.id !== parcelId);
        return {
          ...crop,
          parcels,
          totalAreaHa: this.sumCropArea(parcels)
        };
      })
    );
    this.expandedParcels.update((state) => {
      if (!(parcelId in state)) {
        return state;
      }
      const next = { ...state };
      delete next[parcelId];
      return next;
    });
    this.syncCropsDraft();
  }

  protected updateParcel(cropId: string, parcelId: string, field: keyof Omit<CropParcel, 'id'>, value: string | number): void {
    this.crops.update((items) =>
      items.map((crop) => {
        if (crop.id !== cropId) {
          return crop;
        }

        const parcels = crop.parcels.map((parcel) => {
          if (parcel.id !== parcelId) {
            return parcel;
          }

          if (field === 'parcelAreaHa' || field === 'cropAreaHa') {
            const parsed = typeof value === 'number' ? value : Number(value);
            return { ...parcel, [field]: Number.isNaN(parsed) ? parcel[field] : parsed } as CropParcel;
          }

          return { ...parcel, [field]: String(value) } as CropParcel;
        });

        return {
          ...crop,
          parcels,
          totalAreaHa: this.sumCropArea(parcels)
        };
      })
    );
    this.syncCropsDraft();
  }

  protected addOrUpdateTariffClient(): void {
    const formValue = this.tariffClientForm.getRawValue();
    const client: TariffClient = {
      id: this.editingClientId() ?? crypto.randomUUID(),
      role: this.clientRoleControl.getRawValue(),
      customerType: formValue.customerType || 'NATURAL_PERSON',
      citizenshipCountryCode: formValue.citizenshipCountryCode || 'PL',
      firstName: formValue.customerFirstName || '',
      lastName: formValue.customerLastName || '',
      pesel: formValue.customerPesel || '',
      birthDate: formValue.customerBirthDate || '',
      postalCode: formValue.customerPostalCode || '',
      previousLastName: formValue.previousLastName || '',
      isVip: !!formValue.isVip
    };

    if (!client.firstName && !client.lastName && !client.pesel) {
      return;
    }

    const updatedClients = this.tariffClients()
      .filter((entry) => entry.id !== client.id)
      .map((entry) =>
        client.role === 'POLICY_HOLDER' && entry.role === 'POLICY_HOLDER'
          ? { ...entry, role: 'INSURED' as TariffClientRole }
          : entry
      );

    this.tariffClients.set([...updatedClients, client]);
    this.persistTariffClients();
    this.editingClientId.set(null);
    this.clientRoleControl.setValue(this.tariffClients().some((entry) => entry.role === 'POLICY_HOLDER') ? 'INSURED' : 'POLICY_HOLDER', {
      emitEvent: false
    });
    this.updateRoleOptions();
    this.resetClientEntryForm();
  }

  protected editTariffClient(clientId: string): void {
    const client = this.tariffClients().find((entry) => entry.id === clientId);

    if (!client) {
      return;
    }

    this.editingClientId.set(client.id);
    this.clientRoleControl.setValue(client.role, { emitEvent: false });
    this.tariffClientForm.patchValue(
      {
        customerType: client.customerType,
        citizenshipCountryCode: client.citizenshipCountryCode,
        customerFirstName: client.firstName,
        customerLastName: client.lastName,
        customerPesel: client.pesel,
        customerBirthDate: client.birthDate,
        customerPostalCode: client.postalCode,
        previousLastName: client.previousLastName ?? '',
        isVip: client.isVip
      },
      { emitEvent: false }
    );
    this.updateRoleOptions();
  }

  protected removeTariffClient(clientId: string): void {
    this.tariffClients.set(this.tariffClients().filter((entry) => entry.id !== clientId));
    this.persistTariffClients();

    if (this.editingClientId() === clientId) {
      this.editingClientId.set(null);
      this.resetClientEntryForm();
    }

    if (!this.tariffClients().some((entry) => entry.role === 'POLICY_HOLDER')) {
      this.clientRoleControl.setValue('POLICY_HOLDER', { emitEvent: false });
    }

    this.updateRoleOptions();
  }

  protected getRoleLabel(role: TariffClientRole): string {
    return role === 'POLICY_HOLDER' ? 'Ubezpieczający' : 'Ubezpieczony';
  }

  protected cropLabel(code: string): string {
    return this.cropOptions.find((option) => option.code === code)?.label ?? code;
  }

  protected soilClassLabel(code: string): string {
    return this.soilClassOptions.find((option) => option.code === code)?.label ?? code;
  }

  protected dominantSoilLabel(crop: CropMaster): string {
    return this.soilClassLabel(crop.dominantSoilClassCode);
  }

  protected insuranceSumForParcel(parcel: CropParcel): number {
    const factor = this.soilClassOptions.find((option) => option.code === parcel.soilClassCode)?.factor ?? 5;
    const area = Math.max(0, parcel.cropAreaHa || 0);
    return Math.round((345 * factor * area) / 10);
  }

  protected cropAreaFromParcels(crop: CropMaster): number {
    return this.sumCropArea(crop.parcels);
  }

  protected isParcelExpanded(parcelId: string): boolean {
    return this.expandedParcels()[parcelId] === true;
  }

  protected toggleParcel(parcelId: string): void {
    this.expandedParcels.update((state) => ({
      ...state,
      [parcelId]: !state[parcelId]
    }));
  }

  protected isCropExpanded(cropId: string): boolean {
    return this.expandedCrops()[cropId] === true;
  }

  protected toggleCrop(cropId: string): void {
    this.expandedCrops.update((state) => ({
      ...state,
      [cropId]: !state[cropId]
    }));
  }

  private resetClientEntryForm(): void {
    this.tariffClientForm.patchValue(
      {
        customerType: 'NATURAL_PERSON',
        citizenshipCountryCode: 'PL',
        customerFirstName: '',
        customerLastName: '',
        customerPesel: '',
        customerBirthDate: '',
        customerPostalCode: '',
        previousLastName: '',
        isVip: false
      },
      { emitEvent: false }
    );
    this.clientRoleControl.setValue(this.tariffClients().some((entry) => entry.role === 'POLICY_HOLDER') ? 'INSURED' : 'POLICY_HOLDER', {
      emitEvent: false
    });
    this.updateRoleOptions();
  }

  private initializeTariffClientsFromOffer(): void {
    const offer = this.wizardState.draftOffer();

    if (!offer) {
      this.updateRoleOptions();
      return;
    }

    const persistedTariffClients = (offer as Offer & CropOfferPayload).cropData?.tariffClients;

    if (persistedTariffClients?.length) {
      this.tariffClients.set(structuredClone(persistedTariffClients));
      this.clientRoleControl.setValue('INSURED', { emitEvent: false });
      this.updateRoleOptions();
      return;
    }

    const identity = offer.customer.identity;
    const isPerson = identity.type === 'NATURAL_PERSON';
    const firstName = isPerson ? identity.personName.firstName : '';
    const lastName = isPerson ? identity.personName.lastName : identity.companyName;
    const pesel = isPerson ? identity.pesel : identity.nip;

    if (!firstName && !lastName && !pesel) {
      this.updateRoleOptions();
      return;
    }

    this.tariffClients.set([
      {
        id: `prefill-${offer.customer.id}`,
        role: 'POLICY_HOLDER',
        customerType: offer.customer.identity.type === 'SOLE_PROPRIETOR' ? 'SOLE_PROPRIETOR' : 'NATURAL_PERSON',
        citizenshipCountryCode: 'PL',
        firstName,
        lastName,
        pesel: pesel ?? '',
        birthDate: isPerson ? identity.birthDate : '',
        postalCode: offer.customer.residenceAddress?.postalCode ?? '',
        previousLastName: '',
        isVip: false
      }
    ]);
    this.persistTariffClients();
    this.clientRoleControl.setValue('INSURED', { emitEvent: false });
    this.updateRoleOptions();
  }

  private initializeClaimHistoryFromOffer(): void {
    const offer = this.wizardState.draftOffer();
    const claimHistory = (offer as Offer & CropOfferPayload | undefined)?.cropData?.claimHistory;

    if (!claimHistory) {
      return;
    }

    this.claimHistoryForm.patchValue(
      {
        previousYear: claimHistory.previousYear ?? 0,
        twoYearsAgo: claimHistory.twoYearsAgo ?? 0,
        threeYearsAgo: claimHistory.threeYearsAgo ?? 0
      },
      { emitEvent: false }
    );
  }

  private persistTariffClients(): void {
    this.wizardState.setCropAuxiliaryData({
      tariffClients: structuredClone(this.tariffClients())
    });
  }

  private hasExistingPolicyHolderBesidesEditedClient(): boolean {
    const editingClientId = this.editingClientId();
    return this.tariffClients().some((entry) => entry.role === 'POLICY_HOLDER' && entry.id !== editingClientId);
  }

  private updateRoleOptions(): void {
    this.roleOptions = [
      {
        label: 'Ubezpieczający',
        value: 'POLICY_HOLDER',
        disabled: this.hasExistingPolicyHolderBesidesEditedClient()
      },
      {
        label: 'Ubezpieczony',
        value: 'INSURED',
        disabled: false
      }
    ];
  }

  private syncCropsDraft(): void {
    this.wizardState.setCropDraft(this.crops());
  }

  private createInitialCrops(): CropMaster[] {
    return [
      {
        id: 'crop-1',
        species: 'PSZENICA',
        yieldPerHa: 6.2,
        pricePerUnit: 910,
        totalAreaHa: 14.6,
        dominantSoilClassCode: 'III',
        parcels: [
          {
            id: 'parcel-1',
            voivodeship: 'MAZOWIECKIE',
            county: 'Płocki',
            locality: 'Słupno',
            precinctNumber: '0012',
            fieldNumber: 'Pole A-17',
            cadastralPlotNumber: '14/2',
            parcelAreaHa: 8.1,
            cropAreaHa: 7.4,
            soilClassCode: 'III'
          },
          {
            id: 'parcel-2',
            voivodeship: 'MAZOWIECKIE',
            county: 'Płocki',
            locality: 'Słupno',
            precinctNumber: '0013',
            fieldNumber: 'Pole B-08',
            cadastralPlotNumber: '22/7',
            parcelAreaHa: 7.2,
            cropAreaHa: 7.2,
            soilClassCode: 'IV'
          }
        ]
      }
    ];
  }

  private createDefaultCrop(index: number): CropMaster {
    return {
      id: `crop-${Date.now()}-${index}`,
      species: 'PSZENICA',
      yieldPerHa: 5,
      pricePerUnit: 850,
      totalAreaHa: 1,
      dominantSoilClassCode: 'IV',
      parcels: [this.createDefaultParcel(index)]
    };
  }

  private createDefaultParcel(index: number): CropParcel {
    return {
      id: `parcel-${Date.now()}-${index}`,
      voivodeship: 'MAZOWIECKIE',
      county: '',
      locality: '',
      precinctNumber: '',
      fieldNumber: `Pole ${index}`,
      cadastralPlotNumber: '',
      parcelAreaHa: 1,
      cropAreaHa: 1,
      soilClassCode: 'IV'
    };
  }

  private sumCropArea(parcels: CropParcel[]): number {
    return Number(
      parcels
        .reduce((sum, parcel) => sum + Math.max(0, parcel.cropAreaHa || 0), 0)
        .toFixed(2)
    );
  }
}
