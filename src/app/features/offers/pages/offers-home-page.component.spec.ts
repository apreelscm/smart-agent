import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { of, throwError } from 'rxjs';
import { Offer, ReferenceData } from '../../../core/models';
import { ExchangeRatesRepository } from '../../../core/repositories/exchange-rates.repository';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { ReferenceDataRepository } from '../../../core/repositories/reference-data.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { OffersHomePageComponent } from './offers-home-page.component';

describe('OffersHomePageComponent', () => {
  let fixture: ComponentFixture<OffersHomePageComponent>;
  let offersRepository: jasmine.SpyObj<OffersRepository>;
  let exchangeRatesRepository: jasmine.SpyObj<ExchangeRatesRepository>;
  let referenceDataRepository: jasmine.SpyObj<ReferenceDataRepository>;

  const runtimeOffers = signal<Offer[]>([]);
  const salesFlowRuntimeRepositoryMock = {
    runtimeOffers: runtimeOffers.asReadonly(),
    saveDraftOffer: jasmine.createSpy('saveDraftOffer'),
    promoteOfferToPolicy: jasmine.createSpy('promoteOfferToPolicy')
  };

  const referenceData: ReferenceData = {
    offerStatuses: [
      { code: 'ISSUED', label: 'Oferta wystawiona' },
      { code: 'DRAFT', label: 'Draft' }
    ],
    policyLines: [],
    salesChannels: [],
    vehicleUsages: [],
    vehicleFinancing: []
  };

  beforeEach(async () => {
    offersRepository = jasmine.createSpyObj<OffersRepository>('OffersRepository', ['getOffers']);
    exchangeRatesRepository = jasmine.createSpyObj<ExchangeRatesRepository>('ExchangeRatesRepository', ['getExchangeRates']);
    referenceDataRepository = jasmine.createSpyObj<ReferenceDataRepository>('ReferenceDataRepository', ['getReferenceData']);

    offersRepository.getOffers.and.returnValue(of([buildOffer()]));
    referenceDataRepository.getReferenceData.and.returnValue(of(referenceData));

    await TestBed.configureTestingModule({
      imports: [OffersHomePageComponent, NoopAnimationsModule],
      providers: [
        provideRouter([]),
        { provide: OffersRepository, useValue: offersRepository },
        { provide: ExchangeRatesRepository, useValue: exchangeRatesRepository },
        { provide: ReferenceDataRepository, useValue: referenceDataRepository },
        { provide: SalesFlowRuntimeRepository, useValue: salesFlowRuntimeRepositoryMock }
      ]
    }).compileComponents();
  });

  it('should start in PLN on every page load', () => {
    exchangeRatesRepository.getExchangeRates.and.returnValue(
      of({
        effectiveDate: '2026-04-17',
        EUR: 4,
        USD: 3.5
      })
    );

    fixture = TestBed.createComponent(OffersHomePageComponent);
    fixture.detectChanges();

    const pageText = normalizeText(fixture.nativeElement.textContent ?? '');

    expect(pageText).toContain('1 200 zł');
    expect(pageText).toContain('100 zł');
    expect(pageText).not.toContain('Kurs NBP:');

    const plnButton = getCurrencyButton('PLN');
    const eurButton = getCurrencyButton('EUR');

    expect(plnButton.getAttribute('aria-pressed')).toBe('true');
    expect(eurButton.disabled).toBeFalse();
  });

  it('should convert row premium and summary tile to EUR and show rate caption', () => {
    exchangeRatesRepository.getExchangeRates.and.returnValue(
      of({
        effectiveDate: '2026-04-17',
        EUR: 4,
        USD: 3.5
      })
    );

    fixture = TestBed.createComponent(OffersHomePageComponent);
    fixture.detectChanges();

    getCurrencyButton('EUR').click();
    fixture.detectChanges();

    const pageText = normalizeText(fixture.nativeElement.textContent ?? '');

    expect(pageText).toContain('300,00 €');
    expect(pageText).toContain('25,00 €');
    expect(pageText).toContain('Kurs NBP: 1 EUR = 4,0000 PLN · 17.04.2026');
  });

  it('should keep PLN active, disable EUR and USD, and show message when FX loading fails', () => {
    exchangeRatesRepository.getExchangeRates.and.returnValue(throwError(() => new Error('NBP unavailable')));

    fixture = TestBed.createComponent(OffersHomePageComponent);
    fixture.detectChanges();

    const eurButton = getCurrencyButton('EUR');
    const usdButton = getCurrencyButton('USD');
    const plnButton = getCurrencyButton('PLN');
    const pageText = normalizeText(fixture.nativeElement.textContent ?? '');

    expect(plnButton.getAttribute('aria-pressed')).toBe('true');
    expect(eurButton.disabled).toBeTrue();
    expect(usdButton.disabled).toBeTrue();
    expect(pageText).toContain('Kursy EUR i USD są obecnie niedostępne. Ceny pozostają w PLN.');
    expect(pageText).toContain('1 200 zł');
    expect(pageText).toContain('100 zł');
  });

  function getCurrencyButton(currency: 'PLN' | 'EUR' | 'USD'): HTMLButtonElement {
    return fixture.nativeElement.querySelector(`[data-currency="${currency}"]`) as HTMLButtonElement;
  }
});

function normalizeText(value: string): string {
  return value.replace(/\s+/g, ' ').trim();
}

function buildOffer(): Offer {
  return {
    id: 'offer-1',
    product: 'MOTOR',
    offerNumber: 'OFR/2026/0001',
    status: 'ISSUED',
    createdAt: '2026-04-10T10:00:00Z',
    updatedAt: '2026-04-10T10:00:00Z',
    validTo: '2026-05-10T10:00:00Z',
    salesChannel: 'AGENT' as Offer['salesChannel'],
    agent: {
      id: 'agent-1',
      fullName: 'Jan Agent',
      salesChannel: 'AGENT'
    } as Offer['agent'],
    customer: {
      id: 'customer-1',
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
      productionYear: 2023,
      engine: {
        fuelType: 'BENZYNA'
      },
      registration: {
        registrationNumber: 'WX 1234A'
      }
    } as Offer['vehicle'],
    insuredObject: {} as Offer['insuredObject'],
    variants: [
      {
        id: 'variant-1',
        name: 'Komfort',
        rank: 1,
        totalPremium: {
          amount: 1200,
          currency: 'PLN'
        },
        policyLines: []
      }
    ],
    selectedVariantId: 'variant-1'
  };
}
