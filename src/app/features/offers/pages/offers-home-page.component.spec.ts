import { CommonModule } from '@angular/common';
import { NO_ERRORS_SCHEMA, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { Offer } from '../../../core/models';
import { CurrencyRatesRepository, CurrencyRatesTable } from '../../../core/repositories/currency-rates.repository';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { ReferenceDataRepository } from '../../../core/repositories/reference-data.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { OffersHomePageComponent } from './offers-home-page.component';

type SetupOptions = {
  offers?: Offer[];
  runtimeOffers?: Offer[];
  rates$?: ReturnType<typeof of<CurrencyRatesTable>>;
};

describe('OffersHomePageComponent', () => {
  it('should render the currency selector in a dedicated row below the filters', async () => {
    const { nativeElement } = await setup();

    const toolbar = nativeElement.querySelector('.toolbar') as HTMLElement;

    expect(toolbar.children[1]?.classList.contains('toolbar__currency-row')).toBeTrue();
    expect(nativeElement.querySelectorAll('.toolbar__currency-row p-select').length).toBe(1);
  });

  it('should expose the clear filters button label as "Wyczyść"', async () => {
    const { nativeElement } = await setup();

    const clearFiltersButton = nativeElement.querySelector('.clear-filters-button');

    expect(clearFiltersButton).not.toBeNull();
    expect(clearFiltersButton?.getAttribute('label')).toBe('Wyczyść');
  });

  it('should keep the clear filters button disabled on initial render', async () => {
    const { nativeElement } = await setup();

    const clearFiltersButton = nativeElement.querySelector('.clear-filters-button') as HTMLButtonElement | null;

    expect(clearFiltersButton).not.toBeNull();
    expect(clearFiltersButton?.disabled).toBeTrue();
  });

  it('should convert visible premiums and dialog premium for EUR and USD', async () => {
    const baseOffer = buildOffer({
      id: 'offer-1',
      offerNumber: 'OFR/001',
      createdAt: '2026-04-10T10:00:00.000Z',
      updatedAt: '2026-04-10T10:00:00.000Z',
      selectedPaymentPlan: {
        totalPremium: {
          amount: 2400,
          currency: 'PLN'
        }
      } as never
    });
    const runtimeOffer = buildOffer({
      id: 'runtime-offer-1',
      offerNumber: 'OFR/RUNTIME/001',
      createdAt: '2026-04-11T10:00:00.000Z',
      updatedAt: '2026-04-11T10:00:00.000Z',
      selectedPaymentPlan: {
        totalPremium: {
          amount: 1200,
          currency: 'PLN'
        }
      } as never
    });

    const { component, fixture, nativeElement } = await setup({
      offers: [baseOffer],
      runtimeOffers: [runtimeOffer]
    });

    component['selectedPresentationCurrency'].set('EUR');
    component['pendingTransition'].set({
      offer: baseOffer,
      transition: {
        actionLabel: 'Akceptuj',
        title: 'Akceptacja oferty',
        description: 'Czy potwierdzasz akceptację tej oferty?',
        targetStatus: 'ACCEPTED'
      }
    });
    component['transitionDialogVisible'].set(true);
    fixture.detectChanges();

    expect(readPremiumValues(nativeElement)).toEqual(['300,00 €', '600,00 €']);
    expect(component['summaryTiles']()[2].value).toBe('37,50 €');

    const dialogPremium = Array.from(nativeElement.querySelectorAll('.transition-dialog__summary strong'))
      .at(3)
      ?.textContent;

    expect(normalizeText(dialogPremium)).toBe('600,00 €');

    component['selectedPresentationCurrency'].set('USD');
    fixture.detectChanges();

    expect(readPremiumValues(nativeElement)).toEqual(['240,00 USD', '480,00 USD']);
    expect(component['summaryTiles']()[2].value).toBe('30,00 USD');

    const dialogPremiumInUsd = Array.from(nativeElement.querySelectorAll('.transition-dialog__summary strong'))
      .at(3)
      ?.textContent;

    expect(normalizeText(dialogPremiumInUsd)).toBe('480,00 USD');
  });

  it('should keep fractional average precision visible in PLN and after conversion', async () => {
    const firstOffer = buildOffer({
      id: 'offer-fractional-1',
      offerNumber: 'OFR/FRACTIONAL/001',
      createdAt: '2026-04-10T10:00:00.000Z',
      updatedAt: '2026-04-10T10:00:00.000Z',
      selectedPaymentPlan: {
        totalPremium: {
          amount: 6000,
          currency: 'PLN'
        }
      } as never
    });
    const secondOffer = buildOffer({
      id: 'offer-fractional-2',
      offerNumber: 'OFR/FRACTIONAL/002',
      createdAt: '2026-04-11T10:00:00.000Z',
      updatedAt: '2026-04-11T10:00:00.000Z',
      selectedPaymentPlan: {
        totalPremium: {
          amount: 6342,
          currency: 'PLN'
        }
      } as never
    });

    const { component, fixture } = await setup({
      offers: [firstOffer, secondOffer]
    });

    expect(component['summaryTiles']()[2].value).toBe('514,25 zł');

    component['selectedPresentationCurrency'].set('EUR');
    fixture.detectChanges();

    expect(component['summaryTiles']()[2].value).toBe('128,56 €');

    component['selectedPresentationCurrency'].set('USD');
    fixture.detectChanges();

    expect(component['summaryTiles']()[2].value).toBe('102,85 USD');
  });

  it('should fall back to the first variant premium when selected payment plan is missing', async () => {
    const variantFallbackOffer = buildOffer({
      id: 'offer-variant-fallback',
      offerNumber: 'OFR/002',
      selectedPaymentPlan: undefined,
      variants: [
        {
          id: 'variant-1',
          name: 'Komfort',
          totalPremium: {
            amount: 1200,
            currency: 'PLN'
          }
        } as never
      ]
    });

    const { component, fixture, nativeElement } = await setup({
      offers: [variantFallbackOffer]
    });

    component['selectedPresentationCurrency'].set('EUR');
    fixture.detectChanges();

    expect(normalizeText(nativeElement.querySelector('.premium-box strong')?.textContent)).toBe('300,00 €');
    expect(component['summaryTiles']()[2].value).toBe('25,00 €');
  });

  it('should keep PLN presentation and show a non-blocking error when rates fail', async () => {
    const offer = buildOffer({
      id: 'offer-failure',
      offerNumber: 'OFR/003',
      selectedPaymentPlan: {
        totalPremium: {
          amount: 2400,
          currency: 'PLN'
        }
      } as never
    });

    const { component, fixture, nativeElement } = await setup({
      offers: [offer],
      rates$: throwError(() => new Error('NBP unavailable')) as never
    });

    component['selectedPresentationCurrency'].set('EUR');
    fixture.detectChanges();

    expect(normalizeText(nativeElement.querySelector('.premium-box strong')?.textContent)).toBe('2400 zł');
    expect(normalizeText(nativeElement.querySelector('.toolbar__currency-message--error')?.textContent)).toContain(
      'Wyświetlamy składki w PLN'
    );
    expect(component['filteredOffers']().length).toBe(1);
  });
});

async function setup(options: SetupOptions = {}) {
  TestBed.resetTestingModule();

  const router = jasmine.createSpyObj<Router>('Router', ['navigate']);
  const offers = options.offers ?? [buildOffer()];
  const runtimeOffers = options.runtimeOffers ?? [];
  const rates$ =
    options.rates$ ??
    of<CurrencyRatesTable>({
      tableType: 'A',
      sourceTableNo: '074/A/NBP/2026',
      publicationDate: '2026-04-17',
      rates: {
        EUR: 4,
        USD: 5
      }
    });

  TestBed.configureTestingModule({
    imports: [OffersHomePageComponent],
    providers: [
      {
        provide: OffersRepository,
        useValue: {
          getOffers: () => of(offers)
        }
      },
      {
        provide: ReferenceDataRepository,
        useValue: {
          getReferenceData: () =>
            of({
              offerStatuses: [
                { code: 'ALL', label: 'Wszystkie statusy' },
                { code: 'ISSUED', label: 'Oferta wystawiona' },
                { code: 'DRAFT', label: 'Draft' }
              ],
              policyLines: [],
              salesChannels: [],
              vehicleUsages: [],
              vehicleFinancing: []
            })
        }
      },
      {
        provide: SalesFlowRuntimeRepository,
        useValue: {
          runtimeOffers: signal(runtimeOffers),
          saveDraftOffer: jasmine.createSpy('saveDraftOffer'),
          promoteOfferToPolicy: jasmine.createSpy('promoteOfferToPolicy')
        }
      },
      {
        provide: CurrencyRatesRepository,
        useValue: {
          getRates: () => rates$
        }
      },
      {
        provide: Router,
        useValue: router
      }
    ],
    schemas: [NO_ERRORS_SCHEMA]
  });

  TestBed.overrideComponent(OffersHomePageComponent, {
    set: {
      imports: [CommonModule, FormsModule]
    }
  });

  await TestBed.compileComponents();

  const fixture = TestBed.createComponent(OffersHomePageComponent);
  fixture.detectChanges();

  return {
    fixture,
    component: fixture.componentInstance,
    nativeElement: fixture.nativeElement as HTMLElement,
    router
  };
}

function buildOffer(overrides: Partial<Offer> = {}): Offer {
  const baseOffer = {
    id: 'offer-1001',
    product: 'MOTOR',
    offerNumber: 'OFR/BASE/001',
    status: 'ISSUED',
    createdAt: '2026-04-10T10:00:00.000Z',
    updatedAt: '2026-04-10T10:00:00.000Z',
    validTo: '2026-05-10T00:00:00.000Z',
    salesChannel: 'AGENCY',
    agent: {
      salesChannel: 'Agent',
      fullName: 'Jan Agent'
    },
    customer: {
      identity: {
        type: 'NATURAL_PERSON',
        personName: {
          firstName: 'Jan',
          lastName: 'Kowalski'
        }
      },
      residenceAddress: {
        city: 'Warszawa'
      }
    },
    vehicle: {
      make: 'Toyota',
      model: 'Corolla',
      productionYear: 2022,
      engine: {
        fuelType: 'BENZYNA'
      },
      registration: {
        registrationNumber: 'WX 12345'
      }
    },
    insuredObject: {},
    variants: [
      {
        id: 'variant-1',
        name: 'Komfort',
        totalPremium: {
          amount: 1800,
          currency: 'PLN'
        }
      }
    ],
    selectedVariantId: 'variant-1',
    selectedPaymentPlan: {
      totalPremium: {
        amount: 2400,
        currency: 'PLN'
      }
    }
  } as Offer;

  return {
    ...baseOffer,
    ...overrides,
    agent: overrides.agent ?? baseOffer.agent,
    customer: overrides.customer ?? baseOffer.customer,
    vehicle: overrides.vehicle ?? baseOffer.vehicle,
    insuredObject: overrides.insuredObject ?? baseOffer.insuredObject,
    variants: overrides.variants ?? baseOffer.variants,
    selectedPaymentPlan:
      Object.prototype.hasOwnProperty.call(overrides, 'selectedPaymentPlan') ? overrides.selectedPaymentPlan : baseOffer.selectedPaymentPlan
  };
}

function readPremiumValues(nativeElement: HTMLElement): string[] {
  return Array.from(nativeElement.querySelectorAll('.premium-box strong')).map((element) => normalizeText(element.textContent));
}

function normalizeText(value: string | null | undefined): string {
  return (value ?? '').replace(/\s+/g, ' ').trim();
}
