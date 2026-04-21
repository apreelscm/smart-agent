import { Injectable, computed, inject, signal } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { Customer, Offer, OfferStatus, OfferVariant, Policy, PolicyLineCode, Vehicle } from '../../../core/models';
import { PaymentPlan } from '../../../core/models/payment/payment-plan.model';
import { OfferProduct, OffersRepository } from '../../../core/repositories/offers.repository';
import { SalesFlowRuntimeRepository } from '../../../core/repositories/sales-flow-runtime.repository';
import { CropMaster, CropVariantConfig, CropVariantId } from '../models/crop-offer.model';

type OfferProductCode = 'DEALER' | 'OC' | 'SHORT_TERM_OC' | 'BORDER_OC' | 'GREEN_CARD';

type CropClaimHistory = {
  previousYear: number;
  twoYearsAgo: number;
  threeYearsAgo: number;
};

type CropPersistedPayload = {
  crops?: CropMaster[];
  variantConfigs?: CropVariantConfig[];
  selectedVariantId?: CropVariantId;
  discountAmount?: number;
  selectedPaymentFrequency?: PaymentPlan['frequency'];
  transportMainPlanEnabled?: boolean;
  tariffClients?: Array<Record<string, unknown>>;
  claimHistory?: CropClaimHistory;
};

type CropOffer = Offer & {
  cropData?: CropPersistedPayload;
};

@Injectable({
  providedIn: 'root'
})
export class OfferWizardStateService {
  private readonly offersRepository = inject(OffersRepository);
  private readonly salesFlowRuntimeRepository = inject(SalesFlowRuntimeRepository);

  private readonly draftOfferState = signal<Offer | undefined>(undefined);
  private readonly modeState = signal<'new' | 'continue'>('new');
  private readonly sourceOfferIdState = signal<string | null>(null);
  private readonly discountAmountState = signal<number>(0);
  private readonly readonlyModeState = signal<boolean>(false);
  private readonly cropDraftState = signal<CropMaster[]>([]);
  private readonly cropVariantConfigsState = signal<CropVariantConfig[]>([]);
  private readonly cropSelectedVariantIdState = signal<CropVariantId>('RECOMMENDED');
  private readonly cropDiscountAmountState = signal<number>(0);
  private readonly cropSelectedPaymentFrequencyState = signal<PaymentPlan['frequency']>('ANNUAL');
  private readonly cropTransportMainPlanEnabledState = signal<boolean>(false);

  readonly draftOffer = computed(() => this.draftOfferState());
  readonly mode = computed(() => this.modeState());
  readonly sourceOfferId = computed(() => this.sourceOfferIdState());
  readonly discountAmount = computed(() => this.discountAmountState());
  readonly readonlyMode = computed(() => this.readonlyModeState());
  readonly cropDraft = computed(() => this.cropDraftState());
  readonly cropVariantConfigs = computed(() => this.cropVariantConfigsState());
  readonly cropSelectedVariantId = computed(() => this.cropSelectedVariantIdState());
  readonly cropDiscountAmount = computed(() => this.cropDiscountAmountState());
  readonly cropSelectedPaymentFrequency = computed(() => this.cropSelectedPaymentFrequencyState());
  readonly cropTransportMainPlanEnabled = computed(() => this.cropTransportMainPlanEnabledState());
  readonly selectedVariant = computed<OfferVariant | undefined>(() =>
    this.draftOffer()?.variants.find((variant) => variant.id === this.draftOffer()?.selectedVariantId)
  );

  async initializeNewDraft(product: OfferProduct = 'MOTOR'): Promise<void> {
    const templateOffer = await firstValueFrom(this.offersRepository.getTemplateOffer(product));

    if (!templateOffer) {
      return;
    }

    this.modeState.set('new');
    this.sourceOfferIdState.set(null);
    this.discountAmountState.set(0);
    const emptyOffer = this.createEmptyOffer(templateOffer, product);
    const normalizedOffer = product === 'MOTOR' ? this.ensureAddonLines(emptyOffer) : emptyOffer;
    this.draftOfferState.set(this.withSelectedVariantFlag(normalizedOffer));

    if (product === 'CROP') {
      this.cropDraftState.set([]);
      this.cropVariantConfigsState.set([]);
      this.cropSelectedVariantIdState.set('RECOMMENDED');
      this.cropDiscountAmountState.set(0);
      this.cropSelectedPaymentFrequencyState.set('ANNUAL');
      this.cropTransportMainPlanEnabledState.set(false);
      this.syncCropOfferState();
    }
  }

  async initializeFromOffer(offerId: string): Promise<void> {
    const runtimeOffer = this.salesFlowRuntimeRepository.runtimeOffers().find((item) => item.id === offerId);
    const offer = runtimeOffer ?? (await firstValueFrom(this.offersRepository.getOfferById(offerId)));

    if (!offer) {
      return;
    }

    this.modeState.set('continue');
    this.sourceOfferIdState.set(offerId);
    this.discountAmountState.set(0);
    const product = offer.product ?? 'MOTOR';
    const normalizedOffer = product === 'MOTOR' ? this.ensureAddonLines(structuredClone(offer)) : structuredClone(offer);
    this.draftOfferState.set(this.withSelectedVariantFlag(normalizedOffer));
    this.restoreCropStateFromOffer(offer);
  }

  async initializeCopyFromOffer(offerId: string): Promise<void> {
    const runtimeOffer = this.salesFlowRuntimeRepository.runtimeOffers().find((item) => item.id === offerId);
    const offer = runtimeOffer ?? (await firstValueFrom(this.offersRepository.getOfferById(offerId)));

    if (!offer) {
      return;
    }

    const source = structuredClone(offer);
    const product = source.product ?? 'MOTOR';

    this.modeState.set('new');
    this.sourceOfferIdState.set(null);
    this.discountAmountState.set(0);
    this.draftOfferState.set(
      this.withSelectedVariantFlag(
        product === 'MOTOR'
          ? this.ensureAddonLines({
              ...source,
              id: 'draft-new-offer',
              offerNumber: 'NOWA / kopia',
              status: 'DRAFT',
              createdAt: new Date().toISOString(),
              updatedAt: new Date().toISOString(),
              validTo: undefined,
              renewalContext: undefined
            })
          : {
              ...source,
              id: 'draft-new-offer',
              offerNumber: 'NOWA / kopia',
              status: 'DRAFT',
              createdAt: new Date().toISOString(),
              updatedAt: new Date().toISOString(),
              validTo: undefined,
              renewalContext: undefined
            }
      )
    );
    this.restoreCropStateFromOffer(source);
  }

  setCropDraft(crops: CropMaster[]): void {
    this.cropDraftState.set(structuredClone(crops));
    this.syncCropOfferState();
  }

  setCropVariantConfigs(configs: CropVariantConfig[]): void {
    this.cropVariantConfigsState.set(structuredClone(configs));
    this.syncCropOfferState();
  }

  setCropSelectedVariantId(variantId: CropVariantId): void {
    this.cropSelectedVariantIdState.set(variantId);
    this.syncCropOfferState();
  }

  setCropDiscountAmount(amount: number): void {
    this.cropDiscountAmountState.set(Math.max(0, Math.round(amount)));
    this.syncCropOfferState();
  }

  setCropSelectedPaymentFrequency(frequency: PaymentPlan['frequency']): void {
    this.cropSelectedPaymentFrequencyState.set(frequency);
    this.syncCropOfferState();
  }

  setCropTransportMainPlanEnabled(enabled: boolean): void {
    this.cropTransportMainPlanEnabledState.set(enabled);
    this.syncCropOfferState();
  }

  setCropAuxiliaryData(patch: Partial<Pick<CropPersistedPayload, 'tariffClients' | 'claimHistory'>>): void {
    if (this.readonlyModeState()) {
      return;
    }

    const offer = this.draftOfferState();

    if (!offer || (offer.product ?? 'MOTOR') !== 'CROP') {
      return;
    }

    const cropOffer = offer as CropOffer;
    const currentPayload = cropOffer.cropData ?? {};

    this.draftOfferState.set({
      ...cropOffer,
      cropData: {
        ...currentPayload,
        ...structuredClone(patch)
      }
    });
  }

  updateVehicle(vehicle: Vehicle): void {
    this.patchOffer((offer) => ({
      ...offer,
      vehicle,
      insuredObject: {
        ...offer.insuredObject,
        label: `${vehicle.make} ${vehicle.model} ${vehicle.registration?.registrationNumber ?? ''}`.trim()
      }
    }));
  }

  updateCustomer(customer: Customer): void {
    this.patchOffer((offer) => ({
      ...offer,
      customer
    }));
  }

  updateContractData(contractData: NonNullable<Offer['contractData']>): void {
    this.patchOffer((offer) => ({
      ...offer,
      contractData
    }));
  }

  updateSelectedVariant(variantId: string): void {
    this.patchOffer((offer) => {
      const selectedVariant = offer.variants.find((variant) => variant.id === variantId);

      return {
        ...offer,
        selectedVariantId: variantId,
        variants: offer.variants.map((variant) => ({
          ...variant,
          selected: variant.id === variantId
        })),
        selectedPaymentPlan: selectedVariant?.paymentPlans?.[0] ?? offer.selectedPaymentPlan
      };
    });
  }

  updateSelectedPaymentPlan(frequency: PaymentPlan['frequency']): void {
    this.patchOffer((offer) => {
      const selectedVariant = offer.variants.find((variant) => variant.id === offer.selectedVariantId);
      const selectedPaymentPlan = selectedVariant?.paymentPlans?.find((plan) => plan.frequency === frequency);

      if (!selectedPaymentPlan) {
        return offer;
      }

      return {
        ...offer,
        selectedPaymentPlan
      };
    });
  }

  updateProductCode(productCode: OfferProductCode): void {
    this.patchOffer((offer) => {
      const variants = offer.variants.map((variant) =>
        this.recalculateVariant({
          ...variant,
          policyLines: variant.policyLines.map((line) => {
            if (line.code === 'OC') {
              return {
                ...line,
                covers: line.covers.map((cover) => ({ ...cover, enabled: true }))
              };
            }

            if (line.code === 'AC') {
              const acEnabled = productCode === 'DEALER';

              return {
                ...line,
                covers: line.covers.map((cover) => ({ ...cover, enabled: acEnabled }))
              };
            }

            if (line.code === 'GREEN_CARD' && productCode === 'GREEN_CARD') {
              return {
                ...line,
                covers: line.covers.map((cover) => ({ ...cover, enabled: true }))
              };
            }

            return line;
          })
        })
      );

      const selectedVariant = variants.find((variant) => variant.id === offer.selectedVariantId);
      const currentSelectedFrequency = offer.selectedPaymentPlan?.frequency;
      const selectedPaymentPlan =
        selectedVariant?.paymentPlans?.find((plan) => plan.frequency === currentSelectedFrequency) ??
        selectedVariant?.paymentPlans?.[0] ??
        offer.selectedPaymentPlan;

      return {
        ...offer,
        variants,
        selectedPaymentPlan,
        contractData: {
          ...offer.contractData,
          productCode
        }
      };
    });
  }

  updateOfferStatus(status: OfferStatus): void {
    this.patchOffer((offer) => ({
      ...offer,
      status,
      updatedAt: new Date().toISOString()
    }));
  }

  setDiscountAmount(amount: number): void {
    if (this.readonlyModeState()) {
      return;
    }

    this.discountAmountState.set(Math.max(0, Math.round(amount)));
  }

  setReadonlyMode(readonlyMode: boolean): void {
    this.readonlyModeState.set(readonlyMode);
  }

  ensureDefaultVariantSelection(): void {
    this.patchOffer((offer) => {
      if (offer.selectedVariantId || offer.variants.length === 0) {
        return offer;
      }

      const defaultVariant = [...offer.variants].sort((left, right) => left.rank - right.rank)[0];

      return {
        ...offer,
        selectedVariantId: defaultVariant.id,
        variants: offer.variants.map((variant) => ({
          ...variant,
          selected: variant.id === defaultVariant.id
        })),
        selectedPaymentPlan: defaultVariant.paymentPlans?.[0] ?? offer.selectedPaymentPlan
      };
    });
  }

  setCoverSelection(variantId: string, lineCode: PolicyLineCode, coverCode: string, enabled: boolean): void {
    this.patchOffer((offer) => {
      const variants = offer.variants.map((variant) => {
        if (variant.id !== variantId) {
          return variant;
        }

        const policyLines = variant.policyLines.map((line) => {
          if (line.code !== lineCode) {
            return line;
          }

          const covers = line.covers.map((cover) => {
            if (cover.code !== coverCode || cover.selectable === false) {
              return cover;
            }

            return {
              ...cover,
              enabled
            };
          });

          return this.recalculateLine({
            ...line,
            covers
          });
        });

        return this.recalculateVariant({
          ...variant,
          policyLines
        });
      });

      const selectedVariant = variants.find((variant) => variant.id === offer.selectedVariantId);

      return {
        ...offer,
        variants,
        selectedPaymentPlan: selectedVariant?.paymentPlans?.[0] ?? offer.selectedPaymentPlan
      };
    });
  }

  updateCoverTermValue(
    variantId: string,
    lineCode: PolicyLineCode,
    coverCode: string,
    termId: string,
    value: string | number
  ): void {
    this.patchOffer((offer) => ({
      ...offer,
      variants: offer.variants.map((variant) => {
        if (variant.id !== variantId) {
          return variant;
        }

        return {
          ...variant,
          policyLines: variant.policyLines.map((line) => {
            if (line.code !== lineCode) {
              return line;
            }

            return {
              ...line,
              covers: line.covers.map((cover) => {
                if (cover.code !== coverCode) {
                  return cover;
                }

                return {
                  ...cover,
                  terms: cover.terms.map((term) => (term.id === termId ? { ...term, value } : term))
                };
              })
            };
          })
        };
      })
    }));
  }

  applyQuickPrefill(prefill: { customer: Customer; vehicle: Vehicle }): void {
    this.patchOffer((offer) => ({
      ...offer,
      customer: prefill.customer,
      vehicle: prefill.vehicle,
      insuredObject: {
        ...offer.insuredObject,
        label: `${prefill.vehicle.make} ${prefill.vehicle.model} ${prefill.vehicle.registration?.registrationNumber ?? ''}`.trim()
      }
    }));
  }

  applyPolicyPrefill(policy: Policy, mode: 'RENEW' | 'COPY' = 'COPY'): void {
    this.patchOffer((offer) => {
      const [firstName, ...lastNameParts] = policy.customerName.trim().split(/\s+/);
      const lastName = lastNameParts.join(' ');

      return {
        ...offer,
        renewalContext:
          mode === 'RENEW'
            ? {
                sourcePolicyId: policy.id,
                sourcePolicyNumber: policy.policyNumber,
                mode
              }
            : undefined,
        customer: {
          ...offer.customer,
          identity:
            offer.customer.identity.type === 'NATURAL_PERSON'
              ? {
                  ...offer.customer.identity,
                  personName: {
                    firstName: firstName || offer.customer.identity.personName.firstName,
                    lastName: lastName || offer.customer.identity.personName.lastName
                  }
                }
              : offer.customer.identity,
          residenceAddress: policy.residenceAddress
            ? {
                ...policy.residenceAddress
              }
            : offer.customer.residenceAddress
        },
        vehicle: {
          ...offer.vehicle,
          make: policy.vehicleLabel.split(' ')[0] || offer.vehicle.make,
          model: policy.vehicleLabel.split(' ').slice(1).join(' ') || offer.vehicle.model,
          registration: {
            ...offer.vehicle.registration,
            registrationNumber: policy.registrationNumber || offer.vehicle.registration?.registrationNumber || ''
          }
        },
        insuredObject: {
          ...offer.insuredObject,
          label: policy.vehicleLabel || offer.insuredObject.label
        },
        contractData: {
          coverageStartDate: policy.conclusionDate?.slice(0, 10),
          coverageEndDate: policy.coverageEndDate?.slice(0, 10),
          correspondenceAddress: policy.residenceAddress
            ? {
                ...policy.residenceAddress
              }
            : offer.contractData?.correspondenceAddress,
          marketingConsents: policy.marketingConsents ?? offer.contractData?.marketingConsents,
          attachments: offer.contractData?.attachments ?? []
        }
      };
    });
  }

  private patchOffer(projector: (offer: Offer) => Offer): void {
    if (this.readonlyModeState()) {
      return;
    }

    const offer = this.draftOfferState();

    if (!offer) {
      return;
    }

    this.draftOfferState.set(projector(offer));
  }

  private syncCropOfferState(): void {
    if (this.readonlyModeState()) {
      return;
    }

    const offer = this.draftOfferState();

    if (!offer || (offer.product ?? 'MOTOR') !== 'CROP') {
      return;
    }

    const cropOffer = offer as CropOffer;
    const currentPayload = cropOffer.cropData ?? {};

    this.draftOfferState.set({
      ...cropOffer,
      cropData: {
        ...currentPayload,
        crops: structuredClone(this.cropDraftState()),
        variantConfigs: structuredClone(this.cropVariantConfigsState()),
        selectedVariantId: this.cropSelectedVariantIdState(),
        discountAmount: this.cropDiscountAmountState(),
        selectedPaymentFrequency: this.cropSelectedPaymentFrequencyState(),
        transportMainPlanEnabled: this.cropTransportMainPlanEnabledState()
      }
    });
  }

  private createEmptyOffer(templateOffer: Offer, product: OfferProduct): Offer {
    const clonedTemplate = structuredClone(templateOffer);

    return {
      ...clonedTemplate,
      product,
      id: 'draft-new-offer',
      offerNumber: 'NOWA / robocza',
      status: 'DRAFT',
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      validTo: undefined,
      selectedVariantId: undefined,
      selectedPaymentPlan: undefined,
      notes: [],
      customer: {
        ...clonedTemplate.customer,
        identity:
          clonedTemplate.customer.identity.type === 'NATURAL_PERSON'
            ? {
                ...clonedTemplate.customer.identity,
                personName: {
                  firstName: '',
                  lastName: ''
                },
                pesel: '',
                birthDate: ''
              }
            : clonedTemplate.customer.identity,
        contact: {
          email: '',
          phoneNumber: ''
        },
        residenceAddress: {
          street: '',
          buildingNumber: '',
          postalCode: '',
          city: '',
          countryCode: 'PL'
        },
        isVatPayer: false,
        isForeignClient: false
      },
      vehicle: {
        ...clonedTemplate.vehicle,
        make: '',
        model: '',
        version: '',
        vin: '',
        productionYear: new Date().getFullYear(),
        specialUsages: [],
        grossVehicleWeightKg: undefined,
        annualMileageKm: undefined,
        marketValue: undefined,
        registration: {
          registrationNumber: '',
          firstRegistrationDate: '',
          countryCode: 'PL'
        },
        engine: {
          ...clonedTemplate.vehicle.engine,
          fuelType: 'PETROL',
          displacementCc: undefined,
          powerHp: undefined
        }
      },
      insuredObject: {
        ...clonedTemplate.insuredObject,
        label: product === 'CROP' ? 'Nowa uprawa' : 'Nowy pojazd',
        vehicleId: 'vehicle-new',
        ownerCustomerId: clonedTemplate.customer.id,
        primaryDriverCustomerId: clonedTemplate.customer.id
      },
      contractData: {
        productCode: 'DEALER',
        paymentMethod: 'PRZELEW',
        coverageStartDate: undefined,
        coverageEndDate: undefined,
        correspondenceAddress: undefined,
        marketingConsents: {
          email: false,
          sms: false,
          phone: false
        },
        attachments: []
      }
    };
  }

  private restoreCropStateFromOffer(offer: Offer): void {
    if ((offer.product ?? 'MOTOR') !== 'CROP') {
      this.cropDraftState.set([]);
      this.cropVariantConfigsState.set([]);
      this.cropSelectedVariantIdState.set('RECOMMENDED');
      this.cropDiscountAmountState.set(0);
      this.cropSelectedPaymentFrequencyState.set('ANNUAL');
      this.cropTransportMainPlanEnabledState.set(false);
      return;
    }

    const payload = (offer as CropOffer).cropData;

    this.cropDraftState.set(payload?.crops ? structuredClone(payload.crops) : []);
    this.cropVariantConfigsState.set(payload?.variantConfigs ? structuredClone(payload.variantConfigs) : []);
    this.cropSelectedVariantIdState.set(payload?.selectedVariantId ?? 'RECOMMENDED');
    this.cropDiscountAmountState.set(Math.max(0, Math.round(payload?.discountAmount ?? 0)));
    this.cropSelectedPaymentFrequencyState.set(payload?.selectedPaymentFrequency ?? 'ANNUAL');
    this.cropTransportMainPlanEnabledState.set(payload?.transportMainPlanEnabled === true);
  }

  private withSelectedVariantFlag(offer: Offer): Offer {
    const selectedVariantId = offer.selectedVariantId ?? offer.variants.find((variant) => variant.selected)?.id;

    return {
      ...offer,
      selectedVariantId,
      variants: offer.variants.map((variant) => ({
        ...variant,
        selected: variant.id === selectedVariantId
      }))
    };
  }

  private ensureAddonLines(offer: Offer): Offer {
    const withLine = (
      variant: OfferVariant,
      lineCode: PolicyLineCode,
      label: string,
      coverCode: string,
      premiumDeltaAmount: number
    ): OfferVariant => {
      const existingLine = variant.policyLines.find((line) => line.code === lineCode);

      if (existingLine) {
        const hasCover = existingLine.covers.some((cover) => cover.code === coverCode);

        if (hasCover) {
          return variant;
        }

        return {
          ...variant,
          policyLines: variant.policyLines.map((line) =>
            line.code !== lineCode
              ? line
              : {
                  ...line,
                  covers: [
                    ...line.covers,
                    {
                      id: `${line.id}-cover-${coverCode.toLowerCase()}`,
                      code: coverCode,
                      label,
                      description: label,
                      enabled: false,
                      selectable: true,
                      premiumDelta: {
                        amount: premiumDeltaAmount,
                        currency: 'PLN'
                      },
                      terms: []
                    }
                  ]
                }
          )
        };
      }

      return {
        ...variant,
        policyLines: [
          ...variant.policyLines,
          {
            id: `${variant.id}-line-${lineCode.toLowerCase()}`,
            code: lineCode,
            label,
            included: true,
            premium: {
              amount: 0,
              currency: 'PLN'
            },
            basePremium: {
              amount: 0,
              currency: 'PLN'
            },
            covers: [
              {
                id: `${variant.id}-cover-${coverCode.toLowerCase()}`,
                code: coverCode,
                label,
                description: label,
                enabled: false,
                selectable: true,
                premiumDelta: {
                  amount: premiumDeltaAmount,
                  currency: 'PLN'
                },
                terms: []
              }
            ]
          }
        ]
      };
    };

    return {
      ...offer,
      variants: offer.variants.map((variant) => {
        const withGreenCard = withLine(variant, 'GREEN_CARD', 'Zielona karta', 'GREEN_CARD', 25);
        const withTyres = withLine(withGreenCard, 'TYRE_ASSISTANCE', 'Opony', 'TYRE_ASSISTANCE', 118);
        return this.recalculateVariant(withTyres);
      })
    };
  }

  private recalculateVariant(variant: OfferVariant): OfferVariant {
    const recalculatedPolicyLines = variant.policyLines.map((line) => this.recalculateLine(line));
    const totalPremiumAmount = recalculatedPolicyLines.reduce((sum, line) => sum + line.premium.amount, 0);
    const totalPremium = {
      ...variant.totalPremium,
      amount: totalPremiumAmount
    };

    return {
      ...variant,
      totalPremium,
      monthlyPremium: variant.monthlyPremium
        ? {
            ...variant.monthlyPremium,
            amount: Number((totalPremiumAmount / 12).toFixed(2))
          }
        : variant.monthlyPremium,
      paymentPlans: variant.paymentPlans?.map((plan) => ({
        ...plan,
        totalPremium: {
          ...plan.totalPremium,
          amount: totalPremiumAmount
        },
        installments: this.recalculateInstallments(totalPremiumAmount, plan.installments.length, plan.installments.map((installment) => installment.dueDate))
      })),
      policyLines: recalculatedPolicyLines
    };
  }

  private recalculateLine<
    T extends {
      basePremium?: { amount: number };
      premium: { amount: number; currency: 'PLN' };
      covers: Array<{ enabled: boolean; selectable?: boolean; premiumDelta?: { amount: number } }>;
    }
  >(line: T): T {
    const enabledSelectableDelta = line.covers.reduce((sum, cover) => {
      if (!cover.enabled) {
        return sum;
      }

      return sum + (cover.premiumDelta?.amount ?? 0);
    }, 0);

    const inferredBasePremium = line.premium.amount - enabledSelectableDelta;
    const basePremiumAmount = line.basePremium?.amount ?? inferredBasePremium;

    return {
      ...line,
      premium: {
        ...line.premium,
        amount: basePremiumAmount + enabledSelectableDelta
      }
    };
  }

  private recalculateInstallments(totalPremiumAmount: number, count: number, dueDates: Array<string | undefined>) {
    if (count <= 1) {
      return [
        {
          sequence: 1,
          dueDate: dueDates[0] ?? new Date().toISOString().slice(0, 10),
          amount: {
            amount: totalPremiumAmount,
            currency: 'PLN' as const
          }
        }
      ];
    }

    const base = Math.floor(totalPremiumAmount / count);
    let remainder = totalPremiumAmount - base * count;

    return dueDates.map((dueDate, index) => ({
      sequence: index + 1,
      dueDate,
      amount: {
        amount: base + (remainder-- > 0 ? 1 : 0),
        currency: 'PLN' as const
      }
    }));
  }
}
