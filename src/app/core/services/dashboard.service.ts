import { Injectable, signal } from '@angular/core';
import { DashboardMessage, KpiCard, QuickAction } from '../models/dashboard.model';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  readonly quickActions = signal<QuickAction[]>([
    { id: 'auto',      label: 'Pakiet Auto+',      icon: 'directions_car', locked: false },
    { id: 'mieszkanie',label: 'Pakiet Mieszkanie+', icon: 'home',           locked: false },
    { id: 'zdrowie',   label: 'Zdrowie',            icon: 'monitor_heart',  locked: true  },
    { id: 'osobowe',   label: 'Osobowe',            icon: 'umbrella',       locked: true  },
  ]);

  readonly kpiCards = signal<KpiCard[]>([
    {
      id: 'zdjecia',
      title: 'ZDJĘCIA DO POLIS',
      count: 29,
      description: 'polis wymaga dodania zdjęć',
      subTextPrefix: 'Uzupełnij',
      subTextSuffix: ' braki, żeby zamknąć sprzedaż i otrzymać prowizję.',
      actionLabel: 'Uzupełnij polisy',
      actionRoute: '/zdjecia-do-polisy',
    },
    {
      id: 'mobilne',
      title: 'POLISY MOBILNE',
      count: 18,
      description: 'polis czeka na akcję klienta',
      subTextSuffix: 'Przypomnij o akceptacji, zanim stracą ważność.',
      actionLabel: 'Zobacz polisy',
      actionRoute: '/polisy-do-wznowienia',
    },
    {
      id: 'wnioski',
      title: 'WNIOSKI DO AKCEPTACJI',
      emptyText: 'Jesteś na bieżąco!',
      actionLabel: 'Zobacz wnioski',
      actionRoute: '/wnioski',
    },
  ]);

  readonly messages = signal<DashboardMessage[]>([]);
}
