import { CommonModule } from '@angular/common';
import { Component, computed, effect, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { ButtonDirective } from 'primeng/button';
import { AutoCompleteCompleteEvent, AutoCompleteModule } from 'primeng/autocomplete';
import { Checkbox } from 'primeng/checkbox';
import { InputText } from 'primeng/inputtext';
import { SelectButton } from 'primeng/selectbutton';
import { debounceTime, distinctUntilChanged } from 'rxjs';
import { SectionCardComponent } from '../../../shared/ui/section-card/section-card.component';
import { Customer } from '../../../core/models';
import { OfferWizardStateService } from '../state/offer-wizard-state.service';

@Component({
  selector: 'app-customer-step-page',
  imports: [CommonModule, ReactiveFormsModule, SectionCardComponent, InputText, Checkbox, ButtonDirective, AutoCompleteModule, SelectButton],
  templateUrl: './customer-step-page.component.html',
  styleUrl: './customer-step-page.component.scss'
})
export class CustomerStepPageComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly wizardState = inject(OfferWizardStateService);
  private readonly postalDictionary: Record<string, { city: string; streets: string[] }> = {
    '03-143': {
      city: 'Warszawa',
      streets: ['Modlińska', 'Porajów', 'Światowida']
    },
    '30-418': {
      city: 'Kraków',
      streets: ['Zakopiańska', 'Brożka', 'Wadowicka']
    },
    '80-122': {
      city: 'Gdańsk',
      streets: ['Kartuska', 'Nowolipie', 'Siedlicka']
    },
    '02-677': {
      city: 'Warszawa',
      streets: ['Cybernetyki', 'Postępu', 'Konstruktorska']
    }
  };

  protected readonly allPostalCodes = Object.keys(this.postalDictionary);
  protected filteredPostalCodes = [...this.allPostalCodes];
  protected filteredStreetOptions: string[] = [];
  protected readonly paymentMethodOptions = [
    { label: 'Inkaso', value: 'INKASO' as const },
    { label: 'Przelew', value: 'PRZELEW' as const }
  ];

  protected readonly customerForm = this.formBuilder.group({
    coverageStartDate: this.formBuilder.control(''),
    coverageEndDate: this.formBuilder.control(''),
    paymentMethod: this.formBuilder.control<'INKASO' | 'PRZELEW'>('PRZELEW'),
    firstName: this.formBuilder.control(''),
    lastName: this.formBuilder.control(''),
    pesel: this.formBuilder.control(''),
    birthDate: this.formBuilder.control(''),
    email: this.formBuilder.control(''),
    phoneNumber: this.formBuilder.control(''),
    street: this.formBuilder.control(''),
    buildingNumber: this.formBuilder.control(''),
    postalCode: this.formBuilder.control(''),
    city: this.formBuilder.control(''),
    marketingConsentEmail: this.formBuilder.control(false),
    marketingConsentSms: this.formBuilder.control(false),
    marketingConsentPhone: this.formBuilder.control(false)
  });

  protected readonly customerKindLabel = computed(() => {
    const customer = this.wizardState.draftOffer()?.customer;

    if (!customer) {
      return '';
    }

    if (customer.kind === 'NATURAL_PERSON') {
      return 'Klient indywidualny';
    }

    if (customer.kind === 'SOLE_PROPRIETOR') {
      return 'Jednoosobowa działalność';
    }

    return 'Osoba prawna';
  });

  constructor() {
    effect(() => {
      const offer = this.wizardState.draftOffer();
      const customer = offer?.customer;

      if (!customer) {
        return;
      }

      const identity = customer.identity;
      const controls = this.customerForm.controls;
      const preserveIfDirty = (controlName: keyof typeof controls, nextValue: string | boolean): string | boolean =>
        controls[controlName].dirty ? ((controls[controlName].value as string | boolean | null) ?? nextValue) : nextValue;

      this.customerForm.patchValue(
        {
          coverageStartDate: preserveIfDirty('coverageStartDate', offer?.contractData?.coverageStartDate ?? this.todayIso()) as string,
          coverageEndDate: preserveIfDirty(
            'coverageEndDate',
            offer?.contractData?.coverageEndDate ?? this.plusOneYear(offer?.contractData?.coverageStartDate ?? this.todayIso())
          ) as string,
          paymentMethod: preserveIfDirty('paymentMethod', offer?.contractData?.paymentMethod ?? 'PRZELEW') as 'INKASO' | 'PRZELEW',
          firstName: preserveIfDirty('firstName', identity.type !== 'LEGAL_ENTITY' ? identity.personName.firstName : '') as string,
          lastName: preserveIfDirty('lastName', identity.type !== 'LEGAL_ENTITY' ? identity.personName.lastName : '') as string,
          pesel: preserveIfDirty(
            'pesel',
            identity.type === 'NATURAL_PERSON' ? identity.pesel : identity.type === 'SOLE_PROPRIETOR' ? (identity.pesel ?? '') : ''
          ) as string,
          birthDate: preserveIfDirty('birthDate', identity.type === 'NATURAL_PERSON' ? identity.birthDate : '') as string,
          email: preserveIfDirty('email', customer.contact?.email ?? '') as string,
          phoneNumber: preserveIfDirty('phoneNumber', customer.contact?.phoneNumber ?? '') as string,
          street: preserveIfDirty('street', customer.residenceAddress?.street ?? '') as string,
          buildingNumber: preserveIfDirty('buildingNumber', customer.residenceAddress?.buildingNumber ?? '') as string,
          postalCode: preserveIfDirty('postalCode', customer.residenceAddress?.postalCode ?? '') as string,
          city: preserveIfDirty('city', customer.residenceAddress?.city ?? '') as string,
          marketingConsentEmail: preserveIfDirty('marketingConsentEmail', offer?.contractData?.marketingConsents?.email ?? false) as boolean,
          marketingConsentSms: preserveIfDirty('marketingConsentSms', offer?.contractData?.marketingConsents?.sms ?? false) as boolean,
          marketingConsentPhone: preserveIfDirty('marketingConsentPhone', offer?.contractData?.marketingConsents?.phone ?? false) as boolean
        },
        { emitEvent: false }
      );

      this.syncStreetSuggestions();
    });

    this.customerForm.controls.coverageStartDate.valueChanges.pipe(distinctUntilChanged()).subscribe((startDate) => {
      if (!startDate) {
        return;
      }

      this.customerForm.patchValue(
        {
          coverageEndDate: this.plusOneYear(startDate)
        },
        { emitEvent: false }
      );
    });

    this.customerForm.controls.postalCode.valueChanges.pipe(distinctUntilChanged()).subscribe((postalCode) => {
      const normalized = this.normalizePostalCode(postalCode ?? '');

      if (normalized !== (postalCode ?? '')) {
        this.customerForm.patchValue({ postalCode: normalized }, { emitEvent: false });
      }

      this.filteredPostalCodes = this.filterOptions(this.allPostalCodes, normalized);
      this.syncStreetSuggestions();

      const dictionaryEntry = this.postalDictionary[normalized];

      if (!dictionaryEntry) {
        return;
      }

      this.customerForm.patchValue(
        {
          city: dictionaryEntry.city,
          street: this.customerForm.controls.street.value || dictionaryEntry.streets[0]
        },
        { emitEvent: false }
      );
    });

    this.customerForm.controls.street.valueChanges.pipe(distinctUntilChanged()).subscribe(() => {
      this.syncStreetSuggestions();
    });

    this.customerForm.valueChanges.pipe(debounceTime(200)).subscribe(() => {
      this.persistCustomerState();
    });
  }

  protected filterPostalCodes(event: AutoCompleteCompleteEvent): void {
    this.filteredPostalCodes = this.filterOptions(this.allPostalCodes, event.query ?? '');
  }

  protected filterStreets(event: AutoCompleteCompleteEvent): void {
    this.filteredStreetOptions = this.filterOptions(this.streetOptionsSource(), event.query ?? '');
  }

  protected addAttachmentPlaceholder(): void {
    // Placeholder action for future attachment upload integration.
    console.log('[CustomerStep] Add attachment placeholder clicked');
  }

  protected saveCustomer(): void {
    this.persistCustomerState();
  }

  private persistCustomerState(): void {
    if (this.wizardState.readonlyMode()) {
      return;
    }

    const currentOffer = this.wizardState.draftOffer();
    const formValue = this.customerForm.getRawValue();

    if (!currentOffer) {
      return;
    }

    const identity = currentOffer.customer.identity;
    let updatedIdentity = identity;

    if (identity.type === 'NATURAL_PERSON') {
      updatedIdentity = {
        ...identity,
        personName: {
          firstName: formValue.firstName ?? '',
          lastName: formValue.lastName ?? ''
        },
        pesel: formValue.pesel ?? '',
        birthDate: formValue.birthDate ?? ''
      };
    } else if (identity.type === 'SOLE_PROPRIETOR') {
      updatedIdentity = {
        ...identity,
        personName: {
          firstName: formValue.firstName ?? '',
          lastName: formValue.lastName ?? ''
        },
        pesel: formValue.pesel ?? ''
      };
    }

    const updatedCustomer: Customer = {
      ...currentOffer.customer,
      identity: updatedIdentity,
      contact: {
        email: formValue.email || undefined,
        phoneNumber: formValue.phoneNumber || undefined
      },
      residenceAddress: {
        street: formValue.street || '',
        buildingNumber: formValue.buildingNumber || '',
        postalCode: formValue.postalCode || '',
        city: formValue.city || '',
        countryCode: currentOffer.customer.residenceAddress?.countryCode ?? 'PL'
      },
      isVatPayer: currentOffer.customer.isVatPayer ?? false,
      isForeignClient: currentOffer.customer.isForeignClient ?? false
    };

    this.wizardState.updateCustomer(updatedCustomer);
    this.wizardState.updateContractData({
      productCode: currentOffer.contractData?.productCode ?? 'DEALER',
      paymentMethod: formValue.paymentMethod ?? 'PRZELEW',
      coverageStartDate: formValue.coverageStartDate || undefined,
      coverageEndDate: formValue.coverageEndDate || undefined,
      correspondenceAddress: {
        street: formValue.street || '',
        buildingNumber: formValue.buildingNumber || '',
        postalCode: formValue.postalCode || '',
        city: formValue.city || '',
        countryCode: currentOffer.customer.residenceAddress?.countryCode ?? 'PL'
      },
      marketingConsents: {
        email: !!formValue.marketingConsentEmail,
        sms: !!formValue.marketingConsentSms,
        phone: !!formValue.marketingConsentPhone
      },
      attachments: currentOffer.contractData?.attachments ?? []
    });
  }

  private syncStreetSuggestions(): void {
    this.filteredStreetOptions = this.streetOptionsSource();
  }

  private normalizePostalCode(rawPostalCode: string): string {
    const digits = rawPostalCode.replace(/[^\d]/g, '').slice(0, 5);

    if (digits.length <= 2) {
      return digits;
    }

    return `${digits.slice(0, 2)}-${digits.slice(2)}`;
  }

  private filterOptions(options: string[], query: string): string[] {
    const normalizedQuery = query.trim().toLocaleLowerCase('pl');

    if (!normalizedQuery) {
      return [...options];
    }

    return options.filter((option) => option.toLocaleLowerCase('pl').includes(normalizedQuery));
  }

  private streetOptionsSource(): string[] {
    const postalCode = this.customerForm.controls.postalCode.value ?? '';
    const streets = this.postalDictionary[postalCode]?.streets ?? this.allPostalCodes.flatMap((code) => this.postalDictionary[code].streets);
    return Array.from(new Set(streets));
  }

  private todayIso(): string {
    return new Date().toISOString().slice(0, 10);
  }

  private plusOneYear(startDate: string): string {
    const date = new Date(startDate);

    if (Number.isNaN(date.getTime())) {
      return '';
    }

    date.setFullYear(date.getFullYear() + 1);
    return date.toISOString().slice(0, 10);
  }
}
