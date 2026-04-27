import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideNoopAnimations } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';
import { Offer } from '../../../core/models';
import { ExchangeRate, ExchangeRatesRepository } from '../../../core/repositories/exchange-rates.repository';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { ReferenceDataRepository } from '../../../core/repositories/reference-data.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { OffersHomePageComponent } from './offers-home-page.component';

describe('OffersHomePageComponent', () => {
  let fixture: ComponentFixture<OffersHomePageComponent>;
  let component: OffersHomePageComponent;
  let exchangeRatesRepository: jasmine.SpyObj<ExchangeRatesRepository>;
  let isClockInstalled = false;

  const eurRate: ExchangeRate = {
    tableType: 'A',
    currencyCode: 'EUR',
    currencyName: 'euro',
    publicationDate: '2026-04-17',
    midRate: 4,
    sourceTableNo: '074/A/NBP/2026'
  };

  const usdRate: ExchangeRate = {
    tableType: 'A',
    currencyCode: 'USD',
    currencyName: 'dolar amerykański',
    publicationDate: '2026-04-18',
    midRate: 5,
    sourceTableNo: '075/A/NBP/2026'
  };

  const referenceData = {
    offerStatuses: [{ code: 'ISSUED', label: 'Oferta wystawiona' }],
    policyLines: [],
    salesChannels: [],
    vehicleUsages: [],
    vehicleFinancing: []
  };

  const mockOffers: Offer[] = [
    createOffer({
      id: 'offer-1',
      offerNumber: 'OF/1/2026',
      selectedPaymentPlan: {
        frequency: 'ANNUAL',
        totalPremium: { amount: 1200, currency: 'PLN' },
        installments: []
      }
    }),
    createOffer({
      id: 'offer-2',
      offerNumber: 'OF/2/2026',
      selectedPaymentPlan: undefined,
      variants: [
        {
          id: 'variant-2',
          name: 'Plus',
          rank: 1,
          totalPremium: { amount: 2400, currency: 'PLN' },
          policyLines: []
        }
      ],
      selectedVariantId: 'variant-2'
    })
  ];

  beforeEach(async () => {
    exchangeRatesRepository = jasmine.createSpyObj<ExchangeRatesRepository>('ExchangeRatesRepository', ['getCurrentRate']);

    await TestBed.configureTestingModule({
      imports: [OffersHomePageComponent],
      providers: [
        provideNoopAnimations(),
        provideRouter([]),
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
  });

  afterEach(() => {
    if (isClockInstalled) {
      jasmine.clock().uninstall();
      isClockInstalled = false;
    }

    fixture?.destroy();
  });

  it('should render offers with default PLN presentation', () => {
    createComponent();

    const nativeElement = fixture.nativeElement as HTMLElement;
    const premiumText = nativeElement.querySelector('.premium-box strong')?.textContent ?? '';

    expect((component as any).presentationCurrency()).toBe('PLN');
    expect(premiumText).toContain('zł');
    expect(nativeElement.querySelector('.presentation-meta__rate')).toBeNull();
  });

  it('should render protection period for every offer with mocked system date', () => {
    mockSystemDate(2025, 4, 10);
    createComponent();

    const nativeElement = fixture.nativeElement as HTMLElement;
    const labels = Array.from(nativeElement.querySelectorAll('.offer-row__protection-period .offer-row__meta-label')).map((element) =>
      element.textContent?.trim()
    );
    const values = getProtectionPeriodValues(nativeElement);

    expect(labels).toEqual(['Okres ochrony', 'Okres ochrony']);
    expect(values).toEqual(['2025/05/10 - 2026/05/10', '2025/05/10 - 2026/05/10']);
  });

  it('should render the same protection period value for all visible offers', () => {
    mockSystemDate(2025, 4, 10);
    createComponent();

    const protectionPeriodValues = getProtectionPeriodValues(fixture.nativeElement as HTMLElement);

    expect(protectionPeriodValues.length).toBe(mockOffers.length);
    expect(new Set(protectionPeriodValues).size).toBe(1);
    expect(protectionPeriodValues[0]).toBe('2025/05/10 - 2026/05/10');
  });

  it('should update protection period after recreating the component on another day', () => {
    mockSystemDate(2025, 4, 10);
    createComponent();

    const firstRenderValues = getProtectionPeriodValues(fixture.nativeElement as HTMLElement);
    fixture.destroy();

    jasmine.clock().mockDate(new Date(2025, 4, 11, 12, 0, 0));
    createComponent();

    const secondRenderValues = getProtectionPeriodValues(fixture.nativeElement as HTMLElement);

    expect(firstRenderValues[0]).toBe('2025/05/10 - 2026/05/10');
    expect(secondRenderValues[0]).toBe('2025/05/11 - 2026/05/11');
  });

  it('should convert visible premiums after selecting EUR and display applied rate', () => {
    createComponent();
    exchangeRatesRepository.getCurrentRate.and.returnValue(of(eurRate));

    (component as any).changePresentationCurrency('EUR');
    fixture.detectChanges();

    const nativeElement = fixture.nativeElement as HTMLElement;
    const rateBanner = nativeElement.querySelector('.presentation-meta__rate')?.textContent ?? '';

    expect(exchangeRatesRepository.getCurrentRate).toHaveBeenCalledWith('EUR');
    expect((component as any).presentationCurrency()).toBe('EUR');
    expect((component as any).getPresentedPremium(mockOffers[0])).toBe(300);
    expect((component as any).getPresentedPremium(mockOffers[1])).toBe(600);
    expect(rateBanner).toContain('1 EUR = 4,00 PLN');
    expect(rateBanner).toContain('17.04.2026');
  });

  it('should convert visible premiums after selecting USD', () => {
    createComponent();
    exchangeRatesRepository.getCurrentRate.and.returnValue(of(usdRate));

    (component as any).changePresentationCurrency('USD');
    fixture.detectChanges();

    expect(exchangeRatesRepository.getCurrentRate).toHaveBeenCalledWith('USD');
    expect((component as any).presentationCurrency()).toBe('USD');
    expect((component as any).getPresentedPremium(mockOffers[0])).toBe(240);
    expect((component as any).getPresentedPremium(mockOffers[1])).toBe(480);
  });

  it('should keep previous presentation and show error when next rate request fails', () => {
    createComponent();
    exchangeRatesRepository.getCurrentRate.and.returnValues(
      of(eurRate),
      throwError(() => new Error('NBP unavailable'))
    );

    (component as any).changePresentationCurrency('EUR');
    fixture.detectChanges();

    (component as any).changePresentationCurrency('USD');
    fixture.detectChanges();

    const nativeElement = fixture.nativeElement as HTMLElement;
    const alertText = nativeElement.querySelector('[role="alert"]')?.textContent ?? '';

    expect((component as any).presentationCurrency()).toBe('EUR');
    expect((component as any).selectedPresentationTargetCurrency()).toBe('EUR');
    expect((component as any).getPresentedPremium(mockOffers[0])).toBe(300);
    expect(alertText).toContain('Nie udało się pobrać kursu USD');
  });

  function createComponent(): void {
    fixture = TestBed.createComponent(OffersHomePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }

  function mockSystemDate(year: number, monthIndex: number, day: number): void {
    jasmine.clock().install();
    jasmine.clock().mockDate(new Date(year, monthIndex, day, 12, 0, 0));
    isClockInstalled = true;
  }

  function getProtectionPeriodValues(nativeElement: HTMLElement): string[] {
    return Array.from(nativeElement.querySelectorAll('.offer-row__protection-period-value')).map(
      (element) => element.textContent?.trim() ?? ''
    );
  }

  function createOffer(overrides: Partial<Offer> = {}): Offer {
    return {
      id: 'offer-base',
      product: 'MOTOR',
      offerNumber: 'OF/BASE/2026',
      status: 'ISSUED',
      createdAt: '2026-04-10T08:00:00Z',
      updatedAt: '2026-04-10T08:00:00Z',
      validTo: '2026-04-30T00:00:00Z',
      salesChannel: 'AGENT' as never,
      agent: {
        salesChannel: 'AGENT',
        fullName: 'Jan Agent'
      } as never,
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
      } as never,
      vehicle: {
        make: 'Toyota',
        model: 'Corolla',
        productionYear: 2022,
        engine: {
          fuelType: 'PETROL'
        },
        registration: {
          registrationNumber: 'WX 1234A'
        }
      } as never,
      insuredObject: {} as never,
      variants: [
        {
          id: 'variant-1',
          name: 'Standard',
          rank: 1,
          totalPremium: { amount: 1200, currency: 'PLN' },
          policyLines: []
        }
      ],
      selectedVariantId: 'variant-1',
      selectedPaymentPlan: {
        frequency: 'ANNUAL',
        totalPremium: { amount: 1200, currency: 'PLN' },
        installments: []
      },
      ...overrides
    };
  }
});
