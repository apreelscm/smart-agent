export type VisitState = 'planned' | 'cancelled';
export type VisitStatusKind = 'upcoming' | 'done' | 'cancelled';

export interface VisitCoordinates {
  lat: number;
  lon: number;
}

export interface Visit {
  id: string;
  service: string;
  type: string;
  doctor: string;
  facility: string;
  address: string;
  date: string;
  time: string;
  duration: string;
  patient: string;
  status: string;
  statusKind: VisitStatusKind;
  group: VisitState;
  price: string;
  prepare: string[];
  coordinates?: VisitCoordinates;
}

/** Dane demonstracyjne wizyt (makieta — bez danych rzeczywistych). */
export const VISITS: Visit[] = [
  {
    id: 'w1',
    service: 'Opieka specjalistyczna – Ginekolog',
    type: 'Wizyta stacjonarna',
    doctor: 'lek. med. Anna Zielińska',
    facility: 'Centrum Medyczne Świat Zdrowia – Warszawa Wola',
    address: 'ul. Kasprzaka 25, 01-224 Warszawa',
    date: '09.07.2026',
    time: '10:30',
    duration: '20 min',
    patient: 'Jan Kowalski',
    status: 'Umówiona',
    statusKind: 'upcoming',
    group: 'planned',
    price: 'W cenie pakietu',
    prepare: [
      'Zabierz dokument tożsamości.',
      'Przyjdź 10 minut przed wizytą do recepcji.',
      'Weź wyniki wcześniejszych badań, jeśli je posiadasz.',
    ],
    coordinates: {
      lat: 52.23028,
      lon: 20.96313,
    },
  },
  {
    id: 'w2',
    service: 'Konsultacja telemedyczna – Internista',
    type: 'Konsultacja telemedyczna',
    doctor: 'lek. med. Piotr Nowak',
    facility: 'Telemedycyna Świat Zdrowia',
    address: 'Wideokonsultacja online',
    date: '14.07.2026',
    time: '08:15',
    duration: '15 min',
    patient: 'Anna Kowalska',
    status: 'Umówiona',
    statusKind: 'upcoming',
    group: 'planned',
    price: 'W cenie pakietu',
    prepare: [
      'Zadbaj o stabilne połączenie internetowe.',
      'Przygotuj listę przyjmowanych leków.',
      'Link do konsultacji otrzymasz na 15 minut przed wizytą.',
    ],
  },
  {
    id: 'w3',
    service: 'Badanie – Morfologia krwi',
    type: 'Badanie diagnostyczne',
    doctor: 'Punkt pobrań',
    facility: 'Centrum Medyczne Świat Zdrowia – Warszawa Centrum',
    address: 'ul. Zielna 12, 00-108 Warszawa',
    date: '28.06.2026',
    time: '07:40',
    duration: '10 min',
    patient: 'Jan Kowalski',
    status: 'Zrealizowana',
    statusKind: 'done',
    group: 'planned',
    price: 'W cenie pakietu',
    prepare: [
      'Badanie wykonaj na czczo (min. 8 godzin bez posiłku).',
      'Wypij szklankę wody przed pobraniem.',
    ],
    coordinates: {
      lat: 52.23642,
      lon: 21.00451,
    },
  },
  {
    id: 'w4',
    service: 'Opieka specjalistyczna – Dermatolog',
    type: 'Wizyta stacjonarna',
    doctor: 'lek. med. Maria Wójcik',
    facility: 'Centrum Medyczne Świat Zdrowia – Warszawa Wola',
    address: 'ul. Kasprzaka 25, 01-224 Warszawa',
    date: '21.06.2026',
    time: '15:00',
    duration: '20 min',
    patient: 'Anna Kowalska',
    status: 'Odwołana',
    statusKind: 'cancelled',
    group: 'cancelled',
    price: 'W cenie pakietu',
    prepare: [],
    coordinates: {
      lat: 52.23028,
      lon: 20.96313,
    },
  },
];

export function findVisit(id: string): Visit | undefined {
  return VISITS.find((visit) => visit.id === id);
}
