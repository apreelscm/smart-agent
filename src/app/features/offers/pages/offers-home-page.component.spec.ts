import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { provideNoopAnimations } from '@angular/platform-browser/animations';
import { Observable, Subject, of } from 'rxjs';
import { ExchangeRateQuote, Offer, ReferenceData } from '../../../core/models';
import { ExchangeRatesRepository } from '../../../core/repositories/exchange-rates.repository';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { ReferenceDataRepository } from '../../../core/repositories/reference-data.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { OffersHomePageComponent } from './offers-home-page.component';

class OffersRepositoryStub {
  constructor(private readonly offers: Offer[]) {}

  getOffers(): Observable<Offer[]> {
    return of(this.offers);
  }
}

class ReferenceDataRepositoryStub {
  getReferenceData(): Observable<ReferenceData> {
    return of({
      offerStatuses: [],
      policyLines: [],
      salesChannels: [],
      vehicleUsages: [],
      vehicleFinancing: []
    } as ReferenceData);
  }
}

class SalesFlowRuntimeRepositoryStub {
  readonly runtimeOffers = signal<Offer[]>([]).asReadonly();

  saveDraftOffer(offer: Offer): Offer {
    return offer;
  }

  promoteOfferToPolicy(): void {}
}

class ExchangeRatesRepositoryStub {
  readonly getExchangeRate = jasmine.createSpy('getExchangeRate').and.callFake((code: 'EUR' | 'USD') => {
    const response$ = new Subject<ExchangeRateQuote>();

    this.responses.set(code, response$);
    return response$.asObservable();
  });

  private readonly responses = new Map<'EUR' | 'USD', Subject<ExchangeRateQuote>>();

  emitSuccess(code: 'EUR' | 'USD', quote: ExchangeRateQuote): void {
    const response$ = this.responses.get(code);

    if (!response$) {
      throw new Error(`Missing exchange-rate subject for ${code}.`);
    }

    response$.next(quote);
    response$.complete();
  }

  emitError(code: 'EUR' | 'USD', error: unknown = new Error('NBP unavailable')): void {
    const response$ = this.responses.get(code);

    if (!response$) {
      throw new Error(`Missing exchange-rate subject for ${code}.`);
    }

    response$.error(error);
  }
}

describe('OffersHomePageComponent', () => {
  let fixture: ComponentFixture<OffersHomePageComponent>;
  let component: OffersHomePageComponent;
  let exchangeRatesRepository: ExchangeRatesRepositoryStub;
  let offers: Offer[];

  beforeEach(async () => {
    offers = [
      createOffer({
        id: 'offer-1',
        offerNumber: 'OFR/001',
        selectedPaymentPlan: {
          frequency: 'ANNUAL',
          totalPremium: { amount: 1200, currency: 'PLN' },
          installments: []
        } as Offer['selectedPaymentPlan']
      }),
      createOffer({
        id: 'offer-2',
        offerNumber: 'OFR/002',
        selectedPaymentPlan: undefined,
        variants: [
          {
            id: 'variant-2',
            name: 'Rozszerzony',
            rank: 1,
            totalPremium: { amount: 2400, currency: 'PLN' },
            policyLines: []
          } as Offer['variants'][number]
        ]
      })
    ];

    exchangeRatesRepository = new ExchangeRatesRepositoryStub();

    await TestBed.configureTestingModule({
      imports: [OffersHomePageComponent],
      providers: [
        provideRouter([]),
        provideNoopAnimations(),
        { provide: OffersRepository, useValue: new OffersRepositoryStub(offers) },
        { provide: ReferenceDataRepository, useClass: ReferenceDataRepositoryStub },
        { provide: SalesFlowRuntimeRepository, useClass: SalesFlowRuntimeRepositoryStub },
        { provide: ExchangeRatesRepository, useValue: exchangeRatesRepository }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(OffersHomePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should update rendered offer premiums, summary tile and dialog premium after selecting EUR', async () => {
    expect(readTexts(fixture, '.premium-box strong')[0]).toContain('zł');

    (component as any).selectDisplayCurrency('EUR');
    expect(exchangeRatesRepository.getExchangeRate).toHaveBeenCalledWith('EUR');

    exchangeRatesRepository.emitSuccess('EUR', {
      code: 'EUR',
      mid: 4,
      effectiveDate: '2026-04-17'
    });

    fixture.detectChanges();
    await fixture.whenStable();

    const renderedPremiums = readTexts(fixture, '.premium-box strong');
    expect(renderedPremiums[0]).toContain('300');
    expect(renderedPremiums[0]).toContain('€');
    expect(renderedPremiums[1]).toContain('600');
    expect(renderedPremiums[1]).toContain('€');

    const summaryValues = readTexts(fixture, '.stat-tile__value');
    expect(summaryValues[2]).toContain('37,50');
    expect(summaryValues[2]).toContain('€');

    (component as any).openTransitionDialog(offers[0], {
      actionLabel: 'Akceptuj',
      title: 'Akceptacja oferty',
      description: 'Czy potwierdzasz akceptację tej oferty?',
      targetStatus: 'ACCEPTED'
    });

    fixture.detectChanges();
    await fixture.whenStable();

    const dialogValues = readTexts(fixture, '.transition-dialog__summary strong');
    expect(dialogValues[3]).toContain('300');
    expect(dialogValues[3]).toContain('€');

    const currencyStatus = readTexts(fixture, '.results-meta__currency-status')[0];
    expect(currencyStatus).toContain('EUR');
    expect(currencyStatus).toContain('17.04.2026');
  });

  it('should keep the last valid active currency and show feedback when the next rate load fails', async () => {
    (component as any).selectDisplayCurrency('EUR');
    exchangeRatesRepository.emitSuccess('EUR', {
      code: 'EUR',
      mid: 4,
      effectiveDate: '2026-04-17'
    });

    fixture.detectChanges();
    await fixture.whenStable();

    (component as any).selectDisplayCurrency('USD');
    expect(exchangeRatesRepository.getExchangeRate).toHaveBeenCalledWith('USD');

    exchangeRatesRepository.emitError('USD');

    fixture.detectChanges();
    await fixture.whenStable();

    expect((component as any).selectedDisplayCurrency()).toBe('EUR');

    const renderedPremiums = readTexts(fixture, '.premium-box strong');
    expect(renderedPremiums[0]).toContain('300');
    expect(renderedPremiums[0]).toContain('€');
    expect(renderedPremiums[0]).not.toContain('$');

    const currencyStatus = readTexts(fixture, '.results-meta__currency-status')[0];
    expect(currencyStatus).toContain('Nie udało się pobrać kursu NBP dla USD');
    expect(currencyStatus).toContain('Pozostawiono EUR');

    const usdOption = (component as any).displayCurrencyOptions().find((option: { code: string; disabled?: boolean }) => option.code === 'USD');
    expect(usdOption?.disabled).toBeTrue();
  });
});

function readTexts(fixture: ComponentFixture<OffersHomePageComponent>, selector: string): string[] {
  return Array.from(fixture.nativeElement.querySelectorAll(selector)).map((element) => element.textContent?.trim() ?? '');
}

function createOffer(overrides: Partial<Offer> = {}): Offer {
  return {
    id: 'offer-1',
    product: 'MOTOR',
    offerNumber: 'OFR/BASE',
    status: 'ISSUED',
    createdAt: '2026-04-10T10:00:00.000Z',
    updatedAt: '2026-04-11T10:00:00.000Z',
    validTo: '2026-04-20T10:00:00.000Z',
    salesChannel: 'AGENCY' as Offer['salesChannel'],
    agent: {
      fullName: 'Anna Agent',
      salesChannel: 'Sieć własna'
    } as Offer['agent'],
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
    } as Offer['customer'],
    vehicle: {
      make: 'Toyota',
      model: 'Corolla',
      productionYear: 2020,
      engine: {
        fuelType: 'BENZYNA'
      },
      registration: {
        registrationNumber: 'WI12345'
      }
    } as Offer['vehicle'],
    insuredObject: {} as Offer['insuredObject'],
    variants: [
      {
        id: 'variant-1',
        name: 'Podstawowy',
        rank: 1,
        totalPremium: { amount: 999, currency: 'PLN' },
        policyLines: []
      } as Offer['variants'][number]
    ],
    selectedVariantId: 'variant-1',
    selectedPaymentPlan: {
      frequency: 'ANNUAL',
      totalPremium: { amount: 1200, currency: 'PLN' },
      installments: []
    } as Offer['selectedPaymentPlan'],
    ...overrides
  };
}
