import { CommonModule } from '@angular/common';
import { Component, OnDestroy, inject, signal } from '@angular/core';
import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import { FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AutoCompleteCompleteEvent, AutoCompleteModule } from 'primeng/autocomplete';
import { ButtonDirective } from 'primeng/button';
import { Checkbox } from 'primeng/checkbox';
import { InputText } from 'primeng/inputtext';
import { SelectButton } from 'primeng/selectbutton';
import { Select } from 'primeng/select';
import { debounceTime, distinctUntilChanged, filter } from 'rxjs';
import { Offer, VehicleSpecialUsage } from '../../../core/models';
import { SectionCardComponent } from '../../../shared/ui/section-card/section-card.component';
import { OfferWizardStateService } from '../state/offer-wizard-state.service';

type TariffClientRole = 'POLICY_HOLDER' | 'INSURED';
type OfferProductCode = 'DEALER' | 'OC' | 'SHORT_TERM_OC' | 'BORDER_OC' | 'GREEN_CARD';

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
  claims: {
    last12Months: number;
    last36Months: number;
    last5Years: number;
  };
};

@Component({
  selector: 'app-vehicle-step-page',
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SectionCardComponent,
    InputText,
    Select,
    Checkbox,
    SelectButton,
    ButtonDirective,
    AutoCompleteModule
  ],
  templateUrl: './vehicle-step-page.component.html',
  styleUrl: './vehicle-step-page.component.scss'
})
export class VehicleStepPageComponent implements OnDestroy {
  private readonly formBuilder = inject(FormBuilder);
  private readonly wizardState = inject(OfferWizardStateService);
  private readonly vehicleCatalog: Record<string, Record<string, string[]>> = {
    Skoda: {
      Octavia: ['Selection 1.5 TSI', 'Style 1.5 TSI DSG', 'Sportline 2.0 TDI DSG'],
      Superb: ['Essence 1.5 mHEV', 'Selection 2.0 TDI DSG'],
      Kamiq: ['Essence 1.0 TSI', 'Monte Carlo 1.5 TSI DSG']
    },
    Toyota: {
      Corolla: ['Comfort 1.8 Hybrid', 'Style 2.0 Hybrid', 'GR Sport 2.0 Hybrid'],
      'Corolla Cross': ['Comfort 2.0 Hybrid', 'Style 2.0 Hybrid', 'Executive 2.0 Hybrid'],
      RAV4: ['Comfort 2.5 Hybrid', 'Selection 2.5 Hybrid AWD']
    },
    Volkswagen: {
      Golf: ['Life 1.5 eTSI', 'Style 1.5 eTSI DSG', 'R-Line 2.0 TDI DSG'],
      Tiguan: ['Life 1.5 eTSI', 'Elegance 2.0 TDI 4Motion'],
      Passat: ['Business 1.5 eTSI', 'Elegance 2.0 TDI DSG']
    },
    Kia: {
      Sportage: ['M 1.6 T-GDI', 'L 1.6 HEV', 'GT-Line 1.6 HEV AWD'],
      Ceed: ['M 1.5 T-GDI', 'L 1.5 T-GDI DCT'],
      XCeed: ['Business Line 1.5 T-GDI', 'GT-Line 1.6 MHEV']
    }
  };

  protected readonly usageOptions = [
    { code: 'PRIVATE', label: 'Prywatnie' },
    { code: 'BUSINESS', label: 'Firmowo' },
    { code: 'MIXED', label: 'Mieszanie' },
    { code: 'TAXI', label: 'Taxi' },
    { code: 'DELIVERY', label: 'Dostawczy' }
  ];

  protected readonly financingOptions = [
    { code: 'OWNED', label: 'Własność' },
    { code: 'CREDIT', label: 'Kredyt' },
    { code: 'LEASING', label: 'Leasing' }
  ];

  protected readonly fuelOptions = [
    { code: 'PETROL', label: 'Benzyna' },
    { code: 'DIESEL', label: 'Diesel' },
    { code: 'HYBRID', label: 'Hybryda' },
    { code: 'ELECTRIC', label: 'Elektryczny' },
    { code: 'LPG', label: 'LPG' }
  ];

  protected readonly productOptions: Array<{ code: OfferProductCode; label: string }> = [
    { code: 'DEALER', label: 'Ubezpieczenie OC / AC' },
    { code: 'OC', label: 'Ubezpieczenie OC' },
    { code: 'SHORT_TERM_OC', label: 'OC krótkoterminowe' },
    { code: 'BORDER_OC', label: 'OC graniczne' },
    { code: 'GREEN_CARD', label: 'Zielona Karta' }
  ];

  protected readonly yesNoOptions = [
    { code: 'YES', label: 'Tak' },
    { code: 'NO', label: 'Nie' }
  ];
  protected readonly yesNoSelectOptions = [
    { label: 'Tak', value: 'YES' },
    { label: 'Nie', value: 'NO' }
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
  protected readonly firstNameOptions = [
    'Anna',
    'Katarzyna',
    'Maria',
    'Agnieszka',
    'Magdalena',
    'Ewa',
    'Barbara',
    'Joanna',
    'Aleksandra',
    'Monika',
    'Michał',
    'Tomasz',
    'Piotr',
    'Paweł',
    'Marcin',
    'Jakub',
    'Krzysztof',
    'Andrzej',
    'Jan',
    'Adam'
  ];
  protected readonly lastNameOptions = [
    'Nowak',
    'Kowalska',
    'Wiśniewski',
    'Wójcik',
    'Kowalczyk',
    'Kamińska',
    'Lewandowski',
    'Zielińska',
    'Szymański',
    'Woźniak',
    'Dąbrowska',
    'Kozłowski',
    'Jankowska',
    'Mazur',
    'Wojciechowski',
    'Kwiatkowska',
    'Krawczyk',
    'Kaczmarek',
    'Piotrowska',
    'Grabowska'
  ];
  protected readonly makeOptions = Object.keys(this.vehicleCatalog);
  protected filteredMakeOptions = [...this.makeOptions];
  protected filteredModelOptions: string[] = [];
  protected filteredVersionOptions: string[] = [];
  protected filteredFirstNameOptions = [...this.firstNameOptions];
  protected filteredLastNameOptions = [...this.lastNameOptions];
  protected filteredPreviousLastNameOptions = [...this.lastNameOptions];
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
  protected readonly quickPrefillHints = [
    'KR7A921 + PESEL 90021212345',
    'WA8K331 + REGON 389221445',
    'GD8V250 + PESEL 85040477881'
  ];
  private readonly quickPrefillByRegistration: Record<
    string,
    {
      customer: {
        firstName: string;
        lastName: string;
        pesel: string;
        birthDate: string;
        email?: string;
        phoneNumber?: string;
        street: string;
        buildingNumber: string;
        postalCode: string;
        city: string;
      };
      vehicle: {
        registrationNumber: string;
        vin: string;
        make: string;
        model: string;
        version: string;
        productionYear: number;
        firstRegistrationDate: string;
        usage: 'PRIVATE' | 'BUSINESS' | 'MIXED' | 'TAXI' | 'DELIVERY';
        financing: 'OWNED' | 'CREDIT' | 'LEASING';
        specialUsages?: VehicleSpecialUsage[];
        grossVehicleWeightKg?: number;
        marketValue: number;
        annualMileageKm: number;
        fuelType: 'PETROL' | 'DIESEL' | 'HYBRID' | 'ELECTRIC' | 'LPG';
        displacementCc?: number;
        powerHp?: number;
      };
    }
  > = {
    KR7A921: {
      customer: {
        firstName: 'Michał',
        lastName: 'Wysocki',
        pesel: '90021212345',
        birthDate: '1990-02-12',
        email: 'm.wysocki@example.pl',
        phoneNumber: '+48 602 443 110',
        street: 'Zakopiańska',
        buildingNumber: '88A',
        postalCode: '30-418',
        city: 'Kraków'
      },
      vehicle: {
        registrationNumber: 'KR7A921',
        vin: 'TMBJR7NX4RY012345',
        make: 'Skoda',
        model: 'Octavia',
        version: 'Style 1.5 TSI DSG',
        productionYear: 2024,
        firstRegistrationDate: '2024-06-18',
        usage: 'PRIVATE',
        financing: 'OWNED',
        specialUsages: [],
        grossVehicleWeightKg: 1910,
        marketValue: 138000,
        annualMileageKm: 18000,
        fuelType: 'PETROL',
        displacementCc: 1498,
        powerHp: 150
      }
    },
    WA8K331: {
      customer: {
        firstName: 'Katarzyna',
        lastName: 'Różańska',
        pesel: '',
        birthDate: '',
        email: 'biuro@rozanska-detailing.pl',
        phoneNumber: '+48 509 220 009',
        street: 'Cybernetyki',
        buildingNumber: '4B',
        postalCode: '02-677',
        city: 'Warszawa'
      },
      vehicle: {
        registrationNumber: 'WA8K331',
        vin: 'JTNABAAE50J098765',
        make: 'Toyota',
        model: 'Corolla Cross',
        version: 'Comfort 2.0 Hybrid',
        productionYear: 2025,
        firstRegistrationDate: '2025-12-14',
        usage: 'BUSINESS',
        financing: 'LEASING',
        specialUsages: ['RENTAL'],
        grossVehicleWeightKg: 2045,
        marketValue: 154900,
        annualMileageKm: 26000,
        fuelType: 'HYBRID',
        displacementCc: 1987,
        powerHp: 197
      }
    }
  };
  private readonly quickPrefillByIdentity: Record<
    string,
    {
      customer: {
        firstName: string;
        lastName: string;
        pesel: string;
        birthDate: string;
        email?: string;
        phoneNumber?: string;
        street: string;
        buildingNumber: string;
        postalCode: string;
        city: string;
      };
      vehicle: {
        registrationNumber: string;
        vin: string;
        make: string;
        model: string;
        version: string;
        productionYear: number;
        firstRegistrationDate: string;
        usage: 'PRIVATE' | 'BUSINESS' | 'MIXED' | 'TAXI' | 'DELIVERY';
        financing: 'OWNED' | 'CREDIT' | 'LEASING';
        specialUsages?: VehicleSpecialUsage[];
        grossVehicleWeightKg?: number;
        marketValue: number;
        annualMileageKm: number;
        fuelType: 'PETROL' | 'DIESEL' | 'HYBRID' | 'ELECTRIC' | 'LPG';
        displacementCc?: number;
        powerHp?: number;
      };
    }
  > = {
    '90021212345': this.quickPrefillByRegistration['KR7A921'],
    '389221445': this.quickPrefillByRegistration['WA8K331'],
    '85040477881': {
      customer: {
        firstName: 'Tomasz',
        lastName: 'Lis',
        pesel: '85040477881',
        birthDate: '1985-04-04',
        email: '',
        phoneNumber: '+48 502 900 144',
        street: 'Kartuska',
        buildingNumber: '214',
        postalCode: '80-122',
        city: 'Gdańsk'
      },
      vehicle: {
        registrationNumber: 'GD8V250',
        vin: 'WVWZZZCD2PW012111',
        make: 'Volkswagen',
        model: 'Golf',
        version: 'Life 1.5 eTSI',
        productionYear: 2023,
        firstRegistrationDate: '2023-11-03',
        usage: 'PRIVATE',
        financing: 'CREDIT',
        specialUsages: [],
        grossVehicleWeightKg: 1840,
        marketValue: 112500,
        annualMileageKm: 12000,
        fuelType: 'PETROL',
        displacementCc: 1498,
        powerHp: 150
      }
    }
  };

  protected readonly vehicleForm = this.formBuilder.group({
    productCode: this.formBuilder.control('DEALER'),
    analysisNeeded: this.formBuilder.control('YES'),
    quickPesel: this.formBuilder.control(''),
    quickRegistrationNumber: this.formBuilder.control(''),
    isPolicyHolderClient: this.formBuilder.control('YES'),
    customerType: this.formBuilder.control('NATURAL_PERSON'),
    citizenshipCountryCode: this.formBuilder.control('PL'),
    customerFirstName: this.formBuilder.control(''),
    customerLastName: this.formBuilder.control(''),
    customerPesel: this.formBuilder.control(''),
    customerBirthDate: this.formBuilder.control(''),
    customerPostalCode: this.formBuilder.control(''),
    previousLastName: this.formBuilder.control(''),
    isVip: this.formBuilder.control(false),
    isBusinessCustomer: this.formBuilder.control(false),
    ufgConsent: this.formBuilder.control(false),
    registrationNumber: this.formBuilder.control(''),
    vin: this.formBuilder.control(''),
    make: this.formBuilder.control(''),
    model: this.formBuilder.control(''),
    version: this.formBuilder.control(''),
    productionYear: this.formBuilder.control<number | null>(null),
    firstRegistrationDate: this.formBuilder.control(''),
    usage: this.formBuilder.control('PRIVATE'),
    financing: this.formBuilder.control('OWNED'),
    specialUsageRental: this.formBuilder.control(false),
    specialUsageTaxi: this.formBuilder.control(false),
    specialUsageDrivingSchool: this.formBuilder.control(false),
    specialUsageDealerVehicle: this.formBuilder.control(false),
    grossVehicleWeightKg: this.formBuilder.control<number | null>(null),
    marketValue: this.formBuilder.control<number | null>(null),
    annualMileageKm: this.formBuilder.control<number | null>(null),
    fuelType: this.formBuilder.control('PETROL'),
    displacementCc: this.formBuilder.control<number | null>(null),
    powerHp: this.formBuilder.control<number | null>(null)
  });

  protected get modelOptions(): string[] {
    const make = this.vehicleForm.controls.make.value?.trim() ?? '';
    return make && this.vehicleCatalog[make] ? Object.keys(this.vehicleCatalog[make]) : [];
  }

  protected get versionOptions(): string[] {
    const make = this.vehicleForm.controls.make.value?.trim() ?? '';
    const model = this.vehicleForm.controls.model.value?.trim() ?? '';
    return make && model && this.vehicleCatalog[make]?.[model] ? this.vehicleCatalog[make][model] : [];
  }

  constructor() {
    toObservable(this.wizardState.draftOffer)
      .pipe(
        filter((offer): offer is Offer => !!offer),
        distinctUntilChanged(),
        takeUntilDestroyed()
      )
      .subscribe((offer) => this.hydrateFormFromOffer(offer));

    this.vehicleForm.controls.make.valueChanges
      .pipe(distinctUntilChanged(), takeUntilDestroyed())
      .subscribe((make) => {
        const model = this.vehicleForm.controls.model.value?.trim() ?? '';
        const modelExists = !!make && !!model && this.vehicleCatalog[make]?.[model];

        this.syncAutocompleteSuggestions();

        if (!modelExists) {
          this.vehicleForm.patchValue(
            {
              model: '',
              version: ''
            },
            { emitEvent: false }
          );
        }
      });

    this.vehicleForm.controls.model.valueChanges
      .pipe(distinctUntilChanged(), takeUntilDestroyed())
      .subscribe((model) => {
        const make = this.vehicleForm.controls.make.value?.trim() ?? '';
        const version = this.vehicleForm.controls.version.value?.trim() ?? '';
        const versionExists = !!make && !!model && !!version && this.vehicleCatalog[make]?.[model]?.includes(version);

        this.syncAutocompleteSuggestions();

        if (!versionExists) {
          this.vehicleForm.patchValue(
            {
              version: ''
            },
            { emitEvent: false }
          );
        }
      });

    this.vehicleForm.valueChanges
      .pipe(debounceTime(200), takeUntilDestroyed())
      .subscribe(() => {
        this.persistVehicleState();
      });
  }

  private hydrateFormFromOffer(offer: Offer): void {
    const quickPeselControl = this.vehicleForm.controls.quickPesel;
    const quickRegistrationControl = this.vehicleForm.controls.quickRegistrationNumber;
    const quickPeselFromOffer = offer.customer.identity.type === 'NATURAL_PERSON' ? offer.customer.identity.pesel : '';
    const quickRegistrationFromOffer = offer.vehicle.registration?.registrationNumber ?? '';
    const quickPeselValue = quickPeselControl.dirty ? (quickPeselControl.value ?? '') : quickPeselFromOffer;
    const quickRegistrationValue = quickRegistrationControl.dirty ? (quickRegistrationControl.value ?? '') : quickRegistrationFromOffer;

    this.vehicleForm.patchValue(
      {
        productCode: (offer.contractData?.productCode ?? 'DEALER') as OfferProductCode,
        analysisNeeded: 'YES',
        quickPesel: quickPeselValue,
        quickRegistrationNumber: quickRegistrationValue,
        isPolicyHolderClient: 'YES',
        customerType: offer.customer.kind,
        citizenshipCountryCode: offer.customer.identity.type === 'NATURAL_PERSON'
          ? (offer.customer.identity.citizenshipCountryCode ?? 'PL')
          : 'PL',
        customerFirstName: offer.customer.identity.type !== 'LEGAL_ENTITY' ? offer.customer.identity.personName.firstName : '',
        customerLastName: offer.customer.identity.type !== 'LEGAL_ENTITY' ? offer.customer.identity.personName.lastName : '',
        customerPesel:
          offer.customer.identity.type === 'NATURAL_PERSON'
            ? offer.customer.identity.pesel
            : offer.customer.identity.type === 'SOLE_PROPRIETOR'
              ? (offer.customer.identity.pesel ?? '')
              : '',
        customerBirthDate: offer.customer.identity.type === 'NATURAL_PERSON' ? offer.customer.identity.birthDate : '',
        customerPostalCode: offer.customer.residenceAddress?.postalCode ?? '',
        previousLastName: '',
        isVip: false,
        isBusinessCustomer: offer.customer.kind === 'SOLE_PROPRIETOR',
        ufgConsent: false,
        registrationNumber: offer.vehicle.registration?.registrationNumber ?? '',
        vin: offer.vehicle.vin,
        make: offer.vehicle.make,
        model: offer.vehicle.model,
        version: offer.vehicle.version ?? '',
        productionYear: offer.vehicle.productionYear,
        firstRegistrationDate: offer.vehicle.registration?.firstRegistrationDate ?? '',
        usage: offer.vehicle.usage,
        financing: offer.vehicle.financing,
        specialUsageRental: (offer.vehicle.specialUsages ?? []).includes('RENTAL'),
        specialUsageTaxi: (offer.vehicle.specialUsages ?? []).includes('TAXI'),
        specialUsageDrivingSchool: (offer.vehicle.specialUsages ?? []).includes('DRIVING_SCHOOL'),
        specialUsageDealerVehicle: (offer.vehicle.specialUsages ?? []).includes('DEALER_VEHICLE'),
        grossVehicleWeightKg: offer.vehicle.grossVehicleWeightKg ?? null,
        marketValue: offer.vehicle.marketValue ?? null,
        annualMileageKm: offer.vehicle.annualMileageKm ?? null,
        fuelType: offer.vehicle.engine.fuelType,
        displacementCc: offer.vehicle.engine.displacementCc ?? null,
        powerHp: offer.vehicle.engine.powerHp ?? null
      },
      { emitEvent: false }
    );

    this.syncAutocompleteSuggestions();

    if (
      this.tariffClients().length === 0 &&
      offer.customer.identity.type === 'NATURAL_PERSON' &&
      (offer.customer.identity.personName.firstName || offer.customer.identity.personName.lastName || offer.customer.identity.pesel)
    ) {
      const initialClaims = offer.id === 'offer-1003'
        ? { last12Months: 0, last36Months: 1, last5Years: 2 }
        : this.createZeroClaims();

      this.tariffClients.set([
        {
          id: crypto.randomUUID(),
          role: 'POLICY_HOLDER',
          customerType: offer.customer.kind,
          citizenshipCountryCode: offer.customer.identity.citizenshipCountryCode ?? 'PL',
          firstName: offer.customer.identity.personName.firstName,
          lastName: offer.customer.identity.personName.lastName,
          pesel: offer.customer.identity.pesel,
          birthDate: offer.customer.identity.birthDate,
          postalCode: offer.customer.residenceAddress?.postalCode ?? '',
          previousLastName: '',
          isVip: false,
          claims: initialClaims
        }
      ]);
    }

    this.updateRoleOptions();
  }

  protected filterMakes(event: AutoCompleteCompleteEvent): void {
    this.filteredMakeOptions = this.filterOptions(this.makeOptions, event.query);
  }

  protected filterModels(event: AutoCompleteCompleteEvent): void {
    this.filteredModelOptions = this.filterOptions(this.modelOptions, event.query);
  }

  protected filterVersions(event: AutoCompleteCompleteEvent): void {
    this.filteredVersionOptions = this.filterOptions(this.versionOptions, event.query);
  }

  protected filterFirstNames(event: AutoCompleteCompleteEvent): void {
    this.filteredFirstNameOptions = this.filterOptions(this.firstNameOptions, event.query);
  }

  protected filterLastNames(event: AutoCompleteCompleteEvent): void {
    this.filteredLastNameOptions = this.filterOptions(this.lastNameOptions, event.query);
  }

  protected filterPreviousLastNames(event: AutoCompleteCompleteEvent): void {
    this.filteredPreviousLastNameOptions = this.filterOptions(this.lastNameOptions, event.query);
  }

  protected saveVehicle(): void {
    this.persistVehicleState();
  }

  ngOnDestroy(): void {
    this.persistVehicleState();
  }

  private persistVehicleState(): void {
    if (this.wizardState.readonlyMode()) {
      return;
    }

    const currentOffer = this.wizardState.draftOffer();
    const formValue = this.vehicleForm.getRawValue();

    if (!currentOffer) {
      return;
    }

    const policyHolder = this.tariffClients().find((client) => client.role === 'POLICY_HOLDER');
    const firstName = formValue.customerFirstName || policyHolder?.firstName || '';
    const lastName = formValue.customerLastName || policyHolder?.lastName || '';
    const pesel = formValue.customerPesel || policyHolder?.pesel || '';
    const birthDate = formValue.customerBirthDate || policyHolder?.birthDate || '';
    const postalCode = formValue.customerPostalCode || policyHolder?.postalCode || '';

    if (currentOffer.customer.identity.type === 'NATURAL_PERSON') {
      this.wizardState.updateCustomer({
        ...currentOffer.customer,
        identity: {
          ...currentOffer.customer.identity,
          personName: {
            firstName,
            lastName
          },
          pesel,
          birthDate,
          citizenshipCountryCode: formValue.citizenshipCountryCode || 'PL'
        },
        residenceAddress: {
          street: currentOffer.customer.residenceAddress?.street ?? '',
          buildingNumber: currentOffer.customer.residenceAddress?.buildingNumber ?? '',
          postalCode,
          city: currentOffer.customer.residenceAddress?.city ?? '',
          countryCode: currentOffer.customer.residenceAddress?.countryCode ?? 'PL'
        },
        isVatPayer: !!formValue.isBusinessCustomer,
        isForeignClient: formValue.citizenshipCountryCode !== 'PL'
      });
    }

    this.wizardState.updateVehicle({
      ...currentOffer.vehicle,
      make: formValue.make || currentOffer.vehicle.make,
      model: formValue.model || currentOffer.vehicle.model,
      version: formValue.version || undefined,
      productionYear: formValue.productionYear || currentOffer.vehicle.productionYear,
      vin: formValue.vin || currentOffer.vehicle.vin,
      usage: (formValue.usage as typeof currentOffer.vehicle.usage) || currentOffer.vehicle.usage,
      financing: (formValue.financing as typeof currentOffer.vehicle.financing) || currentOffer.vehicle.financing,
      specialUsages: this.collectSpecialUsages(formValue),
      grossVehicleWeightKg: formValue.grossVehicleWeightKg || undefined,
      annualMileageKm: formValue.annualMileageKm || undefined,
      marketValue: formValue.marketValue || undefined,
      registration: {
        ...currentOffer.vehicle.registration,
        registrationNumber: formValue.registrationNumber || undefined,
        firstRegistrationDate: formValue.firstRegistrationDate || undefined
      },
      engine: {
        ...currentOffer.vehicle.engine,
        fuelType: (formValue.fuelType as typeof currentOffer.vehicle.engine.fuelType) || currentOffer.vehicle.engine.fuelType,
        displacementCc: formValue.displacementCc || undefined,
        powerHp: formValue.powerHp || undefined
      }
    });

    this.wizardState.updateProductCode((formValue.productCode as OfferProductCode) || 'DEALER');
  }

  protected applyQuickInput(): void {
    const currentOffer = this.wizardState.draftOffer();
    const registrationNumber = this.vehicleForm.controls.quickRegistrationNumber.value?.trim().toUpperCase() ?? '';
    const identity = this.vehicleForm.controls.quickPesel.value?.trim() ?? '';
    const prefill = this.quickPrefillByRegistration[registrationNumber] ?? this.quickPrefillByIdentity[identity];

    if (!currentOffer || !prefill || currentOffer.customer.identity.type !== 'NATURAL_PERSON') {
      return;
    }

    this.wizardState.applyQuickPrefill({
      customer: {
        ...currentOffer.customer,
        identity: {
          ...currentOffer.customer.identity,
          personName: {
            firstName: prefill.customer.firstName,
            lastName: prefill.customer.lastName
          },
          pesel: prefill.customer.pesel,
          birthDate: prefill.customer.birthDate
        },
        contact: {
          email: prefill.customer.email,
          phoneNumber: prefill.customer.phoneNumber
        },
        residenceAddress: {
          street: prefill.customer.street,
          buildingNumber: prefill.customer.buildingNumber,
          postalCode: prefill.customer.postalCode,
          city: prefill.customer.city,
          countryCode: 'PL'
        }
      },
      vehicle: {
        ...currentOffer.vehicle,
        make: prefill.vehicle.make,
        model: prefill.vehicle.model,
        version: prefill.vehicle.version,
        vin: prefill.vehicle.vin,
        productionYear: prefill.vehicle.productionYear,
        usage: prefill.vehicle.usage,
        financing: prefill.vehicle.financing,
        specialUsages: prefill.vehicle.specialUsages ?? [],
        grossVehicleWeightKg: prefill.vehicle.grossVehicleWeightKg,
        annualMileageKm: prefill.vehicle.annualMileageKm,
        marketValue: prefill.vehicle.marketValue,
        registration: {
          registrationNumber: prefill.vehicle.registrationNumber,
          firstRegistrationDate: prefill.vehicle.firstRegistrationDate,
          countryCode: 'PL'
        },
        engine: {
          ...currentOffer.vehicle.engine,
          fuelType: prefill.vehicle.fuelType,
          displacementCc: prefill.vehicle.displacementCc,
          powerHp: prefill.vehicle.powerHp
        }
      }
    });

    this.tariffClients.set([
      {
        id: crypto.randomUUID(),
        role: 'POLICY_HOLDER',
        customerType: 'NATURAL_PERSON',
        citizenshipCountryCode: 'PL',
        firstName: prefill.customer.firstName,
        lastName: prefill.customer.lastName,
        pesel: prefill.customer.pesel,
        birthDate: prefill.customer.birthDate,
        postalCode: prefill.customer.postalCode,
        previousLastName: '',
        isVip: false,
        claims: this.createZeroClaims()
      }
    ]);
    this.clientRoleControl.setValue('POLICY_HOLDER', { emitEvent: false });
    this.editingClientId.set(null);
    this.updateRoleOptions();
  }

  protected addOrUpdateTariffClient(): void {
    const formValue = this.vehicleForm.getRawValue();
    const existingClient = this.tariffClients().find((entry) => entry.id === this.editingClientId());
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
      isVip: !!formValue.isVip,
      claims: existingClient?.claims ?? this.createZeroClaims()
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
    this.vehicleForm.patchValue(
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

  protected getClaimRows(client: TariffClient): Array<{ periodLabel: string; claimsCount: number }> {
    return [
      { periodLabel: '12 miesięcy', claimsCount: client.claims.last12Months },
      { periodLabel: '36 miesięcy', claimsCount: client.claims.last36Months },
      { periodLabel: '5 lat', claimsCount: client.claims.last5Years }
    ];
  }

  protected getFallbackClaimRows(): Array<{ periodLabel: string; claimsCount: number }> {
    return [
      { periodLabel: '12 miesięcy', claimsCount: 0 },
      { periodLabel: '36 miesięcy', claimsCount: 0 },
      { periodLabel: '5 lat', claimsCount: 0 }
    ];
  }

  private resetClientEntryForm(): void {
    this.vehicleForm.patchValue(
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

  private syncAutocompleteSuggestions(): void {
    this.filteredMakeOptions = [...this.makeOptions];
    this.filteredModelOptions = [...this.modelOptions];
    this.filteredVersionOptions = [...this.versionOptions];
    this.filteredFirstNameOptions = [...this.firstNameOptions];
    this.filteredLastNameOptions = [...this.lastNameOptions];
    this.filteredPreviousLastNameOptions = [...this.lastNameOptions];
  }

  private filterOptions(options: string[], query: string | undefined): string[] {
    const normalizedQuery = (query ?? '').trim().toLocaleLowerCase('pl');

    if (!normalizedQuery) {
      return [...options];
    }

    return options.filter((option) => option.toLocaleLowerCase('pl').includes(normalizedQuery));
  }

  private collectSpecialUsages(formValue: ReturnType<typeof this.vehicleForm.getRawValue>): VehicleSpecialUsage[] {
    const specialUsages: VehicleSpecialUsage[] = [];

    if (formValue.specialUsageRental) {
      specialUsages.push('RENTAL');
    }
    if (formValue.specialUsageTaxi) {
      specialUsages.push('TAXI');
    }
    if (formValue.specialUsageDrivingSchool) {
      specialUsages.push('DRIVING_SCHOOL');
    }
    if (formValue.specialUsageDealerVehicle) {
      specialUsages.push('DEALER_VEHICLE');
    }

    return specialUsages;
  }

  private createZeroClaims(): TariffClient['claims'] {
    return {
      last12Months: 0,
      last36Months: 0,
      last5Years: 0
    };
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
}
