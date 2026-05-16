import { Component, inject, signal, computed, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { KwitariuszService } from '../../../core/services/kwitariusz.service';
import { Kwitariusz, KwitariuszStatus, KwitariuszType } from '../../../core/models/kwitariusz.model';

@Component({
  selector: 'app-kwitariusz-edit',
  standalone: true,
  imports: [RouterLink, FormsModule, MatIconModule],
  templateUrl: './kwitariusz-edit.html',
  styleUrl: './kwitariusz-edit.scss',
})
export class KwitariuszEditComponent implements OnInit {
  private readonly route  = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly service = inject(KwitariuszService);

  readonly id = signal('');
  readonly notFound = signal(false);

  // Podstawowe
  readonly type         = signal<KwitariuszType>('rata-odsetki');
  readonly policyNumber = signal('');
  readonly insuredName  = signal('');
  readonly issueDate    = signal('');
  readonly baseAmount   = signal(0);
  readonly interest     = signal(0);
  readonly status       = signal<KwitariuszStatus>('wystawiony');

  // Dane płatnika
  readonly payerName       = signal('');
  readonly payerPeselNip   = signal('');
  readonly payerClientType = signal('osoba-fizyczna');
  readonly payerAddress    = signal('');
  readonly payerEmail      = signal('');
  readonly payerPhone      = signal('');

  // Dane finansowe
  readonly finCurrency          = signal('PLN');
  readonly finPaymentDeadline   = signal('');
  readonly finInterestDate      = signal('');
  readonly finInterestRate      = signal(7);
  readonly finDaysOverdue       = signal(0);

  // Rekalkulacja
  readonly rekalkReason         = signal('');
  readonly rekalkDate           = signal('');
  readonly rekalkAmountBefore   = signal(0);
  readonly rekalkAmountAfter    = signal(0);

  readonly total = computed(() => +(this.baseAmount() + this.interest()).toFixed(2));
  readonly rekalkDiff = computed(() => +(this.rekalkAmountAfter() - this.rekalkAmountBefore()).toFixed(2));

  readonly statuses: { value: KwitariuszStatus; label: string }[] = [
    { value:'wystawiony',  label:'Wystawiony'  },
    { value:'oczekujacy',  label:'Oczekujący'  },
    { value:'oplacony',    label:'Opłacony'    },
    { value:'anulowany',   label:'Anulowany'   },
  ];

  readonly clientTypes = [
    { value:'osoba-fizyczna', label:'Osoba fizyczna' },
    { value:'firma',          label:'Firma'          },
    { value:'rolnik',         label:'Rolnik'         },
    { value:'inny',           label:'Inny'           },
  ];

  readonly rekalkReasons = [
    'Zmiana zakresu ubezpieczenia', 'Zmiana sumy ubezpieczenia',
    'Zmiana danych pojazdu', 'Zmiana użytkownika pojazdu',
    'Korekta błędu w polisie', 'Zmiana okresu ubezpieczenia', 'Inna przyczyna',
  ];

  readonly currencies = ['PLN', 'EUR', 'USD', 'GBP', 'CHF'];

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id') ?? '';
    this.id.set(id);
    const k = this.service.getById(id);
    if (!k) { this.notFound.set(true); return; }

    this.type.set(k.type);
    this.policyNumber.set(k.policyNumber);
    this.insuredName.set(k.insuredName);
    this.issueDate.set(k.issueDate);
    this.baseAmount.set(k.baseAmount);
    this.interest.set(k.interest);
    this.status.set(k.status);

    this.payerName.set(k.payerName ?? '');
    this.payerPeselNip.set(k.payerPeselNip ?? '');
    this.payerClientType.set(k.payerClientType ?? 'osoba-fizyczna');
    this.payerAddress.set(k.payerAddress ?? '');
    this.payerEmail.set(k.payerEmail ?? '');
    this.payerPhone.set(k.payerPhone ?? '');

    this.finCurrency.set(k.finCurrency ?? 'PLN');
    this.finPaymentDeadline.set(k.finPaymentDeadline ?? '');
    this.finInterestDate.set(k.finInterestDate ?? '');
    this.finInterestRate.set(k.finInterestRate ?? 7);
    this.finDaysOverdue.set(k.finDaysOverdue ?? 0);

    this.rekalkReason.set(k.rekalkReason ?? '');
    this.rekalkDate.set(k.rekalkDate ?? '');
    this.rekalkAmountBefore.set(k.rekalkAmountBefore ?? 0);
    this.rekalkAmountAfter.set(k.rekalkAmountAfter ?? 0);
  }

  save(): void {
    const changes: Partial<Omit<Kwitariusz, 'id'>> = {
      type: this.type(), policyNumber: this.policyNumber(),
      insuredName: this.insuredName(), issueDate: this.issueDate(),
      baseAmount: this.baseAmount(), interest: this.interest(), status: this.status(),
      payerName: this.payerName(), payerPeselNip: this.payerPeselNip(),
      payerClientType: this.payerClientType(), payerAddress: this.payerAddress(),
      payerEmail: this.payerEmail(), payerPhone: this.payerPhone(),
      finCurrency: this.finCurrency(), finPaymentDeadline: this.finPaymentDeadline(),
      finInterestDate: this.finInterestDate(), finInterestRate: this.finInterestRate(),
      finDaysOverdue: this.finDaysOverdue(),
      rekalkReason: this.rekalkReason(), rekalkDate: this.rekalkDate(),
      rekalkAmountBefore: this.rekalkAmountBefore(), rekalkAmountAfter: this.rekalkAmountAfter(),
    };
    this.service.updateKwitariusz(this.id(), changes);
    this.router.navigate(['/rozliczenia/kwitariusze', this.id()]);
  }

  formatAmount(n: number): string {
    return n.toFixed(2).replace('.', ',') + ' zł';
  }
}
