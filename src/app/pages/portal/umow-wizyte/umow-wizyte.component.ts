import { Component, computed, signal } from '@angular/core';
import { PortalLayoutComponent } from '../../../shared/components/portal-layout/portal-layout.component';

interface SlotRow {
  time: string;
  /** Indeks dnia (0–6) z wolnym terminem, lub null gdy brak. */
  freeDay: number | null;
}

interface Facility {
  name: string;
  address: string;
  doctor: string;
  hasSlots: boolean;
  rows: SlotRow[];
}

const CONSULTATION_TYPES = [
  'Opieka specjalistyczna – Ginekolog',
  'Opieka specjalistyczna – Kardiolog',
  'Opieka specjalistyczna – Dermatolog',
  'Lekarz POZ – Internista',
];

@Component({
  selector: 'app-umow-wizyte',
  standalone: true,
  imports: [PortalLayoutComponent],
  templateUrl: './umow-wizyte.component.html',
  styleUrl: './umow-wizyte.component.scss',
})
export class UmowWizyteComponent {
  readonly consultationTypes = CONSULTATION_TYPES;

  readonly step = signal<1 | 2 | 3>(1);

  readonly patient = signal('');
  readonly consultation = signal('');
  readonly city = signal('');
  readonly dateRange = signal('06.07.2026 – 12.07.2026');

  readonly bookedFacility = signal('');
  readonly bookedSlot = signal('');

  readonly weekDays = ['PON 06.07', 'WT 07.07', 'ŚR 08.07', 'CZW 09.07', 'PT 10.07', 'SOB 11.07', 'ND 12.07'];

  readonly facilities: Facility[] = [
    {
      name: 'Centrum Medyczne Świat Zdrowia – Warszawa Centrum',
      address: 'ul. Zielna 12, 00-108 Warszawa',
      doctor: '',
      hasSlots: false,
      rows: [],
    },
    {
      name: 'Centrum Medyczne Świat Zdrowia – Warszawa Wola',
      address: 'ul. Kasprzaka 25, 01-224 Warszawa',
      doctor: 'lek. med. Anna Zielińska',
      hasSlots: true,
      rows: [
        { time: 'przed 06:00', freeDay: null },
        { time: '06:00 – 08:00', freeDay: null },
        { time: '08:00 – 10:00', freeDay: 1 },
        { time: '10:00 – 12:00', freeDay: 3 },
        { time: '12:00 – 14:00', freeDay: 4 },
      ],
    },
  ];

  readonly canProceed = computed(
    () => this.patient().length > 0 && this.consultation().length > 0 && this.city().length > 0,
  );

  onPatientChange(value: string): void {
    this.patient.set(value);
  }

  onConsultationChange(value: string): void {
    this.consultation.set(value);
  }

  clearConsultation(): void {
    this.consultation.set('');
  }

  onCityChange(value: string): void {
    this.city.set(value);
  }

  goToStep2(): void {
    if (this.canProceed()) {
      this.step.set(2);
      window.scrollTo({ top: 0 });
    }
  }

  backToStep1(): void {
    this.step.set(1);
  }

  selectSlot(facility: Facility, dayIndex: number): void {
    this.bookedFacility.set(facility.name);
    this.bookedSlot.set(this.weekDays[dayIndex]);
    this.step.set(3);
    window.scrollTo({ top: 0 });
  }

  restart(): void {
    this.step.set(1);
    this.patient.set('');
    this.consultation.set('');
    this.city.set('');
    window.scrollTo({ top: 0 });
  }
}
