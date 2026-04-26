import { signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { provideNoopAnimations } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { Offer } from '../../../core/models';
import { OffersRepository } from '../../../core/repositories/offers.repository';
import { ReferenceDataRepository } from '../../../core/repositories/reference-data.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { OffersHomePageComponent } from './offers-home-page.component';

describe('OffersHomePageComponent', () => {
  const today = new Date(2025, 4, 6, 12, 0, 0);
  const expectedProtectionPeriod = '2025-05-06 – 2026-05-06';

  const offersRepositoryStub = {
    getOffers: jasmine.createSpy('getOffers').and.returnValue(
      of([
        {
          id: 'offer-1',
          product: 'MOTOR',
          offerNumber: 'OFR/001',
          status: 'ISSUED',
          createdAt: '2025-05-01T08:30:00.000Z',
          updatedAt: '2025-05-02T09:45:00.000Z',
          validTo: '2025-05-15T00:00:00.000Z',
          salesChannel: 'AGENCY',
          agent: {
            fullName: 'Jan Kowalski',
            salesChannel: 'AGENCY'
          },
          customer: {
            identity: {
              type: 'NATURAL_PERSON',
              personName: {
                firstName: 'Anna',
                lastName: 'Nowak'
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
                amount: 1200,
                currency: 'PLN'
              }
            }
          ],
          selectedVariantId: 'variant-1',
          contractData: {
            coverageStartDate: '2030-01-01T00:00:00.000Z',
            coverageEndDate: '2031-01-01T23:59:00.000Z'
          }
        },
        {
          id: 'offer-2',
          product: 'CROP',
          offerNumber: 'OFR/002',
          status: 'CALCULATION',
          createdAt: '2025-05-03T10:00:00.000Z',
          updatedAt: '2025-05-04T11:15:00.000Z',
          validTo: '2025-05-20T00:00:00.000Z',
          salesChannel: 'AGENCY',
          agent: {
            fullName: 'Jan Kowalski',
            salesChannel: 'AGENCY'
          },
          customer: {
            identity: {
              type: 'NATURAL_PERSON',
              personName: {
                firstName: 'Piotr',
                lastName: 'Zieliński'
              }
            },
            residenceAddress: {
              city: 'Lublin'
            }
          },
          vehicle: {
            make: '',
            model: '',
            productionYear: 2024,
            engine: {
              fuelType: 'DIESEL'
            },
            registration: {
              registrationNumber: ''
            }
          },
          insuredObject: {},
          variants: [
            {
              id: 'variant-2',
              name: 'Rolnik',
              totalPremium: {
                amount: 1800,
                currency: 'PLN'
              }
            }
          ],
          selectedVariantId: 'variant-2',
          contractData: {
            coverageStartDate: '2040-03-15T00:00:00.000Z',
            coverageEndDate: '2041-03-15T23:59:00.000Z'
          },
          cropData: {
            crops: [
              {
                parcels: [{ id: 'parcel-1' }, { id: 'parcel-2' }]
              }
            ]
          }
        }
      ] as Offer[])
    )
  };

  const referenceDataRepositoryStub = {
    getReferenceData: jasmine.createSpy('getReferenceData').and.returnValue(
      of({
        offerStatuses: [],
        policyLines: [],
        salesChannels: [],
        vehicleUsages: [],
        vehicleFinancing: []
      })
    )
  };

  const salesFlowRuntimeRepositoryStub = {
    runtimeOffers: signal<Offer[]>([]).asReadonly(),
    saveDraftOffer: jasmine.createSpy('saveDraftOffer'),
    promoteOfferToPolicy: jasmine.createSpy('promoteOfferToPolicy')
  };

  beforeEach(async () => {
    jasmine.clock().install();
    jasmine.clock().mockDate(today);

    await TestBed.configureTestingModule({
      imports: [OffersHomePageComponent],
      providers: [
        provideRouter([]),
        provideNoopAnimations(),
        { provide: OffersRepository, useValue: offersRepositoryStub },
        { provide: ReferenceDataRepository, useValue: referenceDataRepositoryStub },
        { provide: SalesFlowRuntimeRepository, useValue: salesFlowRuntimeRepositoryStub }
      ]
    }).compileComponents();
  });

  afterEach(() => {
    jasmine.clock().uninstall();
  });

  it('renders the Okres ochrony field for every offer row using the current day', () => {
    const fixture = TestBed.createComponent(OffersHomePageComponent);
    fixture.detectChanges();

    const element = fixture.nativeElement as HTMLElement;
    const rows = Array.from(element.querySelectorAll('.offer-row'));

    expect(rows.length).toBe(2);

    rows.forEach((row) => {
      const protectionPeriodCell = Array.from(row.querySelectorAll('.offer-row__meta-grid > div')).find(
        (cell) => cell.querySelector('.offer-row__meta-label')?.textContent?.trim() === 'Okres ochrony'
      );

      expect(protectionPeriodCell).toBeDefined();
      expect(protectionPeriodCell?.querySelector('strong')?.textContent?.trim()).toBe(expectedProtectionPeriod);
    });

    expect(element.textContent).not.toContain('2030-01-01');
    expect(element.textContent).not.toContain('2040-03-15');
  });
});
