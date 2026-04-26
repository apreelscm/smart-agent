import { LOCALE_ID, signal } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import localePl from '@angular/common/locales/pl';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideNoopAnimations } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';
import { providePrimeNG } from 'primeng/config';
import {
  ExchangeRateQuote,
  Offer,
  ReferenceData
} from '../../../core/models';
import { ExchangeRatesRepository } from '../../../core/repositories/exchange-rates.repository';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { ReferenceDataRepository } from '../../../core/repositories/reference-data.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { OffersHomePageComponent } from './offers-home-page.component';

describe('OffersHomePageComponent', () => {
  let fixture: ComponentFixture<OffersHomePageComponent>;
  let component: OffersHomePageComponent;
  let mockOffers: Offer[];
  let exchangeRatesRepository: jasmine.SpyObj<ExchangeRatesRepository>;

  const eurQuote: ExchangeRateQuote = {
    table: 'A',
    currency: 'euro',
    code: 'EUR',
    effectiveDate: '2026-04-17',
    mid: 4.2941
  };

  const usdQuote: ExchangeRateQuote = {
    table: 'A',
    currency: 'dolar amerykański',
    code: 'USD',
    effectiveDate: '2026-04-17',
    mid: 3.85
  };

  const referenceData: ReferenceData = {
    offerStatuses: [
      { code: 'DRAFT', label: 'Draft' },
      { code: 'CALCULATION', label: 'Kalkulacja' },
      { code: 'ISSUED', label: 'Oferta wystawiona' }
    ],
    policyLines: [],
    salesChannels: [],
    vehicleUsages: [],
    vehicleFinancing: []
  };

  beforeAll(() => {
    registerLocaleData(localePl);
  });

  beforeEach(async () => {
    mockOffers = [
      createOffer({
        id: 'offer-1',
        offerNumber: 'OFR/1',
        selectedVariantId: 'variant-1',
        selectedPaymentPlan: {
          frequency: 'ANNUAL',
          totalPremium: {
            amount: 1200,
            currency: 'PLN'
          },
          installments: []
        } as Offer['selectedPaymentPlan']
      }),
      createOffer({
        id: 'offer-2',
        offerNumber: 'OFR/2',
        selectedVariantId: 'variant-2',
        selectedPaymentPlan: undefined,
        variants: [
          {
            id: 'variant-2',
            name: 'Ekonomiczny',
            rank: 1,
            totalPremium: {
              amount: 0,
              currency: 'PLN'
            },
            policyLines: []
          } as Offer['variants'][number]
        ]
      })
    ];

    exchangeRatesRepository = jasmine.createSpyObj<ExchangeRatesRepository>('ExchangeRatesRepository', ['getCurrentRate']);

    await TestBed.configureTestingModule({
      imports: [OffersHomePageComponent],
      providers: [
        provideRouter([]),
        provideNoopAnimations(),
        providePrimeNG({}),
        {
          provide: LOCALE_ID,
          useValue: 'pl-PL'
        },
        {
          provide: OffersRepository,
          useValue: {
            getOffers: () => of(mockOffers)
          }
        },
        {
          provide: ReferenceDataRepository,
          useValue: {
            getReferenceData: () => of(referenceData)
          }
        },
        {
          provide: SalesFlowRuntimeRepository,
          useValue: {
            runtimeOffers: signal<Offer[]>([]),
            saveDraftOffer: jasmine.createSpy('saveDraftOffer'),
            promoteOfferToPolicy: jasmine.createSpy('promoteOfferToPolicy')
          }
        },
        {
          provide: ExchangeRatesRepository,
          useValue: exchangeRatesRepository
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(OffersHomePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should render PLN premiums by default on initial load', () => {
    expect((component as any).activePresentationCurrency()).toBe('PLN');
    expect(getPremiumTexts()).toHaveSize(2);
    expect(getPremiumTexts()[0]).toContain('1 200');
    expect(getPremiumTexts()[0]).toContain('zł');
    expect(getPremiumTexts()[1]).toContain('0');
    expect(getPremiumTexts()[1]).toContain('zł');
    expect(fixture.nativeElement.querySelector('.results-meta__rate')).toBeNull();
  });

  it('should convert visible offer premiums to EUR without mutating the source offers', () => {
    exchangeRatesRepository.getCurrentRate.and.returnValue(of(eurQuote));

    (component as any).searchTerm.set('OFR/2');
    fixture.detectChanges();

    (component as any).onPresentationCurrencyChange('EUR');
    fixture.detectChanges();

    expect(exchangeRatesRepository.getCurrentRate).toHaveBeenCalledOnceWith('EUR');
    expect((component as any).activePresentationCurrency()).toBe('EUR');
    expect(getPremiumTexts()).toEqual([jasmine.stringMatching(/0/), jasmine.anything()] as unknown as string[]);
    expect(getPremiumTexts().length).toBe(1);
    expect(getPremiumTexts()[0]).toContain('0');
    expect(getPremiumTexts()[0]).toContain('€');
    expect(getText('.results-meta__rate')).toContain('1 EUR = 4,2941 PLN');
    expect(getText('.results-meta__rate')).toContain('17.04.2026');
    expect(mockOffers[0].selectedPaymentPlan?.totalPremium.amount).toBe(1200);
    expect(mockOffers[1].variants[0].totalPremium.amount).toBe(0);
  });

  it('should convert premiums to USD after a successful rate lookup', () => {
    exchangeRatesRepository.getCurrentRate.and.returnValue(of(usdQuote));

    (component as any).onPresentationCurrencyChange('USD');
    fixture.detectChanges();

    expect(exchangeRatesRepository.getCurrentRate).toHaveBeenCalledOnceWith('USD');
    expect((component as any).activePresentationCurrency()).toBe('USD');
    expect(getPremiumTexts()[0]).toContain('312');
    expect(getPremiumTexts()[0]).toContain('$');
    expect(getPremiumTexts()[1]).toContain('0');
    expect(getPremiumTexts()[1]).toContain('$');
    expect(getText('.results-meta__rate')).toContain('1 USD = 3,8500 PLN');
  });

  it('should restore PLN premiums without calling the rate service when switching back to PLN', () => {
    exchangeRatesRepository.getCurrentRate.and.returnValue(of(eurQuote));

    (component as any).onPresentationCurrencyChange('EUR');
    fixture.detectChanges();

    exchangeRatesRepository.getCurrentRate.calls.reset();

    (component as any).onPresentationCurrencyChange('PLN');
    fixture.detectChanges();

    expect(exchangeRatesRepository.getCurrentRate).not.toHaveBeenCalled();
    expect((component as any).activePresentationCurrency()).toBe('PLN');
    expect((component as any).activeExchangeRate()).toBeNull();
    expect(getPremiumTexts()[0]).toContain('1 200');
    expect(getPremiumTexts()[0]).toContain('zł');
    expect(fixture.nativeElement.querySelector('.results-meta__rate')).toBeNull();
  });

  it('should keep the previous currency and premiums when a rate lookup fails', () => {
    exchangeRatesRepository.getCurrentRate.and.returnValues(
      of(eurQuote),
      throwError(() => new Error('NBP unavailable'))
    );

    (component as any).onPresentationCurrencyChange('EUR');
    fixture.detectChanges();

    const previousPremiums = [...getPremiumTexts()];

    (component as any).onPresentationCurrencyChange('USD');
    fixture.detectChanges();

    expect(exchangeRatesRepository.getCurrentRate).toHaveBeenCalledTimes(2);
    expect((component as any).activePresentationCurrency()).toBe('EUR');
    expect(getPremiumTexts()).toEqual(previousPremiums);
    expect(getText('.results-meta__error')).toContain('Nie udało się pobrać kursu USD');
    expect(getText('.results-meta__rate')).toContain('1 EUR = 4,2941 PLN');
  });

  function getPremiumTexts(): string[] {
    return Array.from(fixture.nativeElement.querySelectorAll('.premium-box strong')).map((element) =>
      normalizeText((element as HTMLElement).textContent)
    );
  }

  function getText(selector: string): string {
    return normalizeText((fixture.nativeElement.querySelector(selector) as HTMLElement | null)?.textContent ?? '');
  }

  function normalizeText(value: string): string {
    return value.replace(/\s+/g, ' ').trim();
  }

  function createOffer(overrides: Partial<Offer> = {}): Offer {
    return {
      id: 'offer-base',
      product: 'MOTOR',
      offerNumber: 'OFR/BASE',
      status: 'ISSUED',
      createdAt: '2026-04-10T08:00:00Z',
      updatedAt: '2026-04-11T10:00:00Z',
      validTo: '2026-04-30T00:00:00Z',
      salesChannel: 'AGENCY' as never,
      agent: {
        salesChannel: 'Agent',
        fullName: 'Jan Kowalski'
      } as Offer['agent'],
      customer: {
        identity: {
          type: 'NATURAL_PERSON',
          personName: {
            firstName: 'Anna',
            lastName: 'Nowak'
          }
        }
      } as Offer['customer'],
      vehicle: {
        make: 'Toyota',
        model: 'Corolla',
        productionYear: 2022,
        engine: {
          fuelType: 'Benzyna'
        },
        registration: {
          registrationNumber: 'WX 12345'
        }
      } as Offer['vehicle'],
      insuredObject: {} as Offer['insuredObject'],
      variants: [
        {
          id: 'variant-1',
          name: 'Standard',
          rank: 1,
          totalPremium: {
            amount: 1200,
            currency: 'PLN'
          },
          policyLines: []
        } as Offer['variants'][number]
      ],
      ...overrides
    };
  }
});
