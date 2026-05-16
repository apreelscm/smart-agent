import { Component, inject, signal, computed, effect } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { KwitariuszService, POLICY_STATUS_WARNINGS } from '../../../core/services/kwitariusz.service';
import { Installment, KwitariuszType, MockPolicy } from '../../../core/models/kwitariusz.model';

@Component({
  selector: 'app-new-kwitariusz',
  standalone: true,
  imports: [RouterLink, FormsModule, MatIconModule, MatButtonModule],
  templateUrl: './new-kwitariusz.html',
  styleUrl: './new-kwitariusz.scss',
})
export class NewKwitariuszComponent {
  private readonly service = inject(KwitariuszService);
  private readonly router = inject(Router);

  // Step 1 — type
  readonly selectedType = signal<KwitariuszType | null>(null);

  // Step 2 — policy search
  readonly policySeries = signal('');
  readonly policyNumber = signal('');
  readonly searchDone = signal(false);
  readonly foundPolicy = signal<MockPolicy | null>(null);
  readonly policyNotFound = signal(false);

  // Step 3 — payer data
  readonly payerName   = signal('');
  readonly payerPeselNip = signal('');
  readonly payerClientType = signal('osoba-fizyczna');
  readonly payerAddress = signal('');
  readonly payerEmail  = signal('');
  readonly payerPhone  = signal('');

  readonly clientTypes = [
    { value:'osoba-fizyczna', label:'Osoba fizyczna' },
    { value:'firma',          label:'Firma'          },
    { value:'rolnik',         label:'Rolnik'         },
    { value:'inny',           label:'Inny'           },
  ];

  // Step 4 — recalculation (only for 'rekalkulacja-odsetki')
  readonly rekalkReason      = signal('');
  readonly rekalkDate        = signal('');
  readonly rekalkAmountBefore = signal(0);
  readonly rekalkAmountAfter  = signal(0);
  readonly rekalkDiff = computed(() =>
    +(this.rekalkAmountAfter() - this.rekalkAmountBefore()).toFixed(2)
  );

  readonly rekalkReasons = [
    'Zmiana zakresu ubezpieczenia',
    'Zmiana sumy ubezpieczenia',
    'Zmiana danych pojazdu',
    'Zmiana użytkownika pojazdu',
    'Korekta błędu w polisie',
    'Zmiana okresu ubezpieczenia',
    'Inna przyczyna',
  ];

  // Step 4 — financial data
  readonly finBaseAmount     = signal(0);
  readonly finInterest       = signal(0);
  readonly finCurrency       = signal('PLN');
  readonly finPaymentDeadline = signal('');
  readonly finInterestDate   = signal('');
  readonly finInterestRate   = signal(7);
  readonly finDaysOverdue    = signal(0);

  readonly finTotal = computed(() => +(this.finBaseAmount() + this.finInterest()).toFixed(2));

  readonly currencies = ['PLN', 'EUR', 'USD', 'GBP', 'CHF'];
  readonly today = new Date().toISOString().slice(0, 10);

  // Step 5 — interest calculator
  readonly baseAmount = signal(0);
  readonly daysOverdue = signal(0);
  readonly interestRate = signal(7);
  readonly calculatedInterest = signal(0);
  readonly interestCalculated = signal(false);

  // Derived
  readonly warning = computed(() => {
    const p = this.foundPolicy();
    if (!p) return null;
    return POLICY_STATUS_WARNINGS[p.status] ?? null;
  });

  readonly isBlocked = computed(() => this.warning()?.type === 'error');

  readonly showInstallmentPlan = computed(() =>
    this.selectedType() === 'rata-odsetki' &&
    !!this.foundPolicy()?.installmentPlan?.length
  );

  readonly installmentPlan = computed<Installment[]>(() =>
    this.foundPolicy()?.installmentPlan ?? []
  );

  readonly selectedInstallmentNumber = signal<number | null>(null);

  selectInstallment(inst: Installment): void {
    if (inst.paid) return;

    this.selectedInstallmentNumber.set(inst.number);

    // Auto-fill calculator
    this.baseAmount.set(inst.amount);
    this.finBaseAmount.set(inst.amount);

    // Calculate days overdue from due date to today
    const due  = new Date(inst.dueDate);
    const now  = new Date(this.today);
    const diff = Math.max(0, Math.floor((now.getTime() - due.getTime()) / 86_400_000));
    this.daysOverdue.set(diff);
    this.finDaysOverdue.set(diff);
    this.finInterestDate.set(inst.dueDate);
    this.finPaymentDeadline.set(inst.dueDate);
  }

  readonly canSubmit = computed(() =>
    !!this.selectedType() &&
    this.searchDone() &&
    !this.isBlocked() &&
    this.baseAmount() > 0
  );

  readonly totalAmount = computed(() =>
    +(this.baseAmount() + this.calculatedInterest()).toFixed(2)
  );

  constructor() {
    // Przepisuje wartość bezwzględną dopłaty/zwrotu do kwoty bazowej
    effect(() => {
      if (this.selectedType() !== 'rekalkulacja-odsetki') return;
      const diff = Math.abs(this.rekalkDiff());
      if (diff > 0) {
        this.finBaseAmount.set(diff);
        this.baseAmount.set(diff);
      }
    });

    // Domyślna data rekalkulacji = dzisiaj
    effect(() => {
      if (this.selectedType() === 'rekalkulacja-odsetki' && !this.rekalkDate()) {
        this.rekalkDate.set(this.today);
      }
    }, { allowSignalWrites: true });
  }

  selectType(t: KwitariuszType): void {
    this.selectedType.set(t);
  }

  searchPolicy(): void {
    const found = this.service.searchPolicy(
      this.policySeries(),
      this.policyNumber()
    );
    this.foundPolicy.set(found);
    this.policyNotFound.set(!found);
    this.searchDone.set(true);
    if (found) {
      this.baseAmount.set(found.baseAmount);
      this.finBaseAmount.set(found.baseAmount);
      this.payerName.set(found.insuredName);
      this.rekalkAmountBefore.set(found.baseAmount);
    }
  }

  calcInterest(): void {
    const interest = this.daysOverdue() <= 0
      ? 0
      : +(this.baseAmount() * this.daysOverdue() * (this.interestRate() / 100) / 365).toFixed(2);
    this.calculatedInterest.set(interest);
    this.interestCalculated.set(true);
  }

  submit(): void {
    if (!this.canSubmit()) return;
    const policy = this.foundPolicy();
    this.service.addKwitariusz({
      number: `KW/2025/${String(Date.now()).slice(-3)}`,
      type: this.selectedType()!,
      policyNumber: policy
        ? `${this.policySeries()}/${this.policyNumber()}`
        : `${this.policySeries()}/${this.policyNumber()}`,
      insuredName: policy?.insuredName ?? 'NIEZNANY',
      issueDate: new Date().toISOString().slice(0, 10),
      baseAmount: this.baseAmount(),
      interest: this.calculatedInterest(),
      status: 'wystawiony',
      source: 'SITU',
    });
    this.router.navigate(['/rozliczenia/kwitariusze']);
  }

  formatAmount(n: number): string {
    return n.toFixed(2).replace('.', ',') + ' zł';
  }
}
