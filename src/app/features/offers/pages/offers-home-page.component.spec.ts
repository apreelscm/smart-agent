import { signal } from '@angular/core'
import { TestBed } from '@angular/core/testing'
import { provideRouter } from '@angular/router'
import { NoopAnimationsModule } from '@angular/platform-browser/animations'
import { of } from 'rxjs'
import { Offer } from '../../../core/models'
import { OffersRepository } from '../../../core/repositories/offers.repository'
import { ReferenceDataRepository } from '../../../core/repositories/reference-data.repository'
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository'
import { OffersHomePageComponent } from './offers-home-page.component'

describe('OffersHomePageComponent', () => {
  const offersRepositoryStub = {
    getOffers: jasmine.createSpy('getOffers').and.returnValue(of([createOffer()]))
  }

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
  }

  const salesFlowRuntimeRepositoryStub = {
    runtimeOffers: signal<Offer[]>([]),
    saveDraftOffer: jasmine.createSpy('saveDraftOffer'),
    promoteOfferToPolicy: jasmine.createSpy('promoteOfferToPolicy')
  }

  beforeEach(async () => {
    jasmine.clock().install()
    jasmine.clock().mockDate(new Date('2025-05-10T10:15:00.000Z'))

    await TestBed.configureTestingModule({
      imports: [OffersHomePageComponent, NoopAnimationsModule],
      providers: [
        provideRouter([]),
        { provide: OffersRepository, useValue: offersRepositoryStub },
        { provide: ReferenceDataRepository, useValue: referenceDataRepositoryStub },
        { provide: SalesFlowRuntimeRepository, useValue: salesFlowRuntimeRepositoryStub }
      ]
    }).compileComponents()
  })

  afterEach(() => {
    jasmine.clock().uninstall()
  })

  it('renders the protection period label and expected value', async () => {
    const fixture = TestBed.createComponent(OffersHomePageComponent)

    fixture.detectChanges()
    await fixture.whenStable()
    fixture.detectChanges()

    const compiled = fixture.nativeElement as HTMLElement
    const coverageLabel = compiled.querySelector('.offer-row__meta-item--coverage .offer-row__meta-label')?.textContent?.trim()
    const coverageValue = compiled.querySelector('.offer-row__coverage-value')?.textContent?.replace(/\s+/g, ' ').trim()

    expect(coverageLabel).toBe('Okres ochrony')
    expect(coverageValue).toBe('2025/05/10 00:00 - 2026/05/09 23:59')
  })

  it('renders the protection period metadata before the update metadata', async () => {
    const fixture = TestBed.createComponent(OffersHomePageComponent)

    fixture.detectChanges()
    await fixture.whenStable()
    fixture.detectChanges()

    const compiled = fixture.nativeElement as HTMLElement
    const metaLabels = Array.from(compiled.querySelectorAll('.offer-row__meta-grid > div .offer-row__meta-label'))
      .map((element) => element.textContent?.trim())
      .filter((label): label is string => Boolean(label))

    const coverageIndex = metaLabels.indexOf('Okres ochrony')
    const updateIndex = metaLabels.indexOf('Aktualizacja')

    expect(coverageIndex).toBeGreaterThan(-1)
    expect(updateIndex).toBeGreaterThan(-1)
    expect(coverageIndex).toBeLessThan(updateIndex)
  })
})

function createOffer(): Offer {
  return {
    id: 'offer-1',
    product: 'MOTOR',
    offerNumber: 'OFR/2025/0001',
    status: 'ISSUED',
    createdAt: '2025-05-10T08:00:00.000Z',
    updatedAt: '2025-05-10T09:30:00.000Z',
    validTo: '2025-05-31T21:59:59.000Z',
    salesChannel: 'AGENCY',
    agent: {
      salesChannel: 'Agent własny',
      fullName: 'Jan Kowalski'
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
        fuelType: 'Benzyna'
      },
      registration: {
        registrationNumber: 'WA12345'
      }
    },
    insuredObject: {},
    variants: [
      {
        id: 'variant-1',
        name: 'Komfort',
        totalPremium: {
          amount: 2400
        }
      }
    ],
    selectedVariantId: 'variant-1'
  } as Offer
}
