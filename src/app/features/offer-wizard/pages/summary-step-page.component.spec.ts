import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SummaryStepPageComponent } from './summary-step-page.component';
import { OfferWizardStateService } from '../state/offer-wizard-state.service';

describe('SummaryStepPageComponent', () => {
  let fixture: ComponentFixture<SummaryStepPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SummaryStepPageComponent],
      providers: [
        {
          provide: OfferWizardStateService,
          useValue: {
            draftOffer: () => ({
              id: 'offer-usd',
              offerNumber: 'OF/USD/1',
              status: 'CALCULATION',
              customer: {
                contact: {
                  phoneNumber: '+48 600 000 002',
                  email: 'john@example.pl'
                }
              },
              vehicle: {
                make: 'Ford',
                model: 'Mustang',
                registration: {
                  registrationNumber: 'GD12345'
                },
                productionYear: 2024,
                vin: 'VIN3'
              },
              contractData: {
                coverageStartDate: '2026-06-10',
                coverageEndDate: '2027-06-10',
                correspondenceAddress: {
                  street: 'Polna',
                  buildingNumber: '3',
                  postalCode: '00-003',
                  city: 'Gdańsk',
                  countryCode: 'PL'
                }
              }
            }),
            selectedVariant: () => ({
              id: 'variant-3',
              name: 'Premium',
              rank: 1,
              totalPremium: {
                amount: 1500,
                currency: 'USD'
              },
              policyLines: [
                {
                  id: 'line-oc',
                  code: 'OC',
                  label: 'OC',
                  premium: {
                    amount: 600,
                    currency: 'USD'
                  },
                  covers: [],
                  included: true
                },
                {
                  id: 'line-ac',
                  code: 'AC',
                  label: 'AC',
                  premium: {
                    amount: 900,
                    currency: 'USD'
                  },
                  covers: [],
                  included: true
                }
              ]
            }),
            discountAmount: () => 100
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(SummaryStepPageComponent);
    fixture.detectChanges();
  });

  it('renders summary premium in selected offer currency', () => {
    const text = fixture.nativeElement.textContent;
    expect(text).toContain('$1,400');
  });

  it('renders policy line premiums in their own currency', () => {
    const text = fixture.nativeElement.textContent;
    expect(text).toContain('$600');
    expect(text).toContain('$900');
  });
});
