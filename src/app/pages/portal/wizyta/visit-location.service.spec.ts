import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Visit } from '../visits.data';
import { VisitLocationService } from './visit-location.service';

describe('VisitLocationService', () => {
  let service: VisitLocationService;
  let httpTestingController: HttpTestingController;

  const geocodingEndpoint = 'https://nominatim.openstreetmap.org/search';

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(VisitLocationService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('prefers coordinates already available on the visit', () => {
    const visit = createVisit({
      coordinates: {
        lat: 52.23028,
        lon: 20.96313,
      },
    });

    let receivedLocation: ReturnType<typeof captureResolvedLocation>['value'];

    service.resolveLocation(visit).subscribe((location) => {
      receivedLocation = location;
    });

    httpTestingController.expectNone((request) => request.url === geocodingEndpoint);

    expect(receivedLocation).toEqual({
      coordinates: {
        lat: 52.23028,
        lon: 20.96313,
      },
      embedUrl: jasmine.any(String),
    });
    expect(receivedLocation?.embedUrl).toContain('https://www.openstreetmap.org/export/embed.html?');
  });

  it('falls back to geocoding the visit address when coordinates are missing', () => {
    const visit = createVisit({
      coordinates: undefined,
      address: 'ul. Zielna 12, 00-108 Warszawa',
    });

    let receivedLocation: ReturnType<typeof captureResolvedLocation>['value'];

    service.resolveLocation(visit).subscribe((location) => {
      receivedLocation = location;
    });

    const request = httpTestingController.expectOne((pendingRequest) => pendingRequest.url === geocodingEndpoint);

    expect(request.request.method).toBe('GET');
    expect(request.request.params.get('format')).toBe('jsonv2');
    expect(request.request.params.get('limit')).toBe('1');
    expect(request.request.params.get('q')).toBe('ul. Zielna 12, 00-108 Warszawa');

    request.flush([
      {
        lat: '52.23642',
        lon: '21.00451',
      },
    ]);

    expect(receivedLocation).toEqual({
      coordinates: {
        lat: 52.23642,
        lon: 21.00451,
      },
      embedUrl: jasmine.any(String),
    });
    expect(receivedLocation?.embedUrl).toContain('marker=52.23642%2C21.00451');
  });

  it('returns null when geocoding finds no result', () => {
    const visit = createVisit({
      coordinates: undefined,
      address: 'ul. Nieznana 999, 00-000 Warszawa',
    });

    let receivedLocation: ReturnType<typeof captureResolvedLocation>['value'];

    service.resolveLocation(visit).subscribe((location) => {
      receivedLocation = location;
    });

    const request = httpTestingController.expectOne((pendingRequest) => pendingRequest.url === geocodingEndpoint);

    request.flush([]);

    expect(receivedLocation).toBeNull();
  });

  it('reuses cached geocoding results for the same address', () => {
    const visit = createVisit({
      coordinates: undefined,
      address: 'ul. Kasprzaka 25, 01-224 Warszawa',
    });

    let firstLocation: ReturnType<typeof captureResolvedLocation>['value'];
    let secondLocation: ReturnType<typeof captureResolvedLocation>['value'];

    service.resolveLocation(visit).subscribe((location) => {
      firstLocation = location;
    });

    const request = httpTestingController.expectOne((pendingRequest) => pendingRequest.url === geocodingEndpoint);

    request.flush([
      {
        lat: '52.23028',
        lon: '20.96313',
      },
    ]);

    service.resolveLocation(visit).subscribe((location) => {
      secondLocation = location;
    });

    httpTestingController.expectNone((pendingRequest) => pendingRequest.url === geocodingEndpoint);

    expect(firstLocation).toEqual(secondLocation);
    expect(secondLocation?.coordinates).toEqual({
      lat: 52.23028,
      lon: 20.96313,
    });
  });

  it('builds an OpenStreetMap embed URL with bbox, marker and layer parameters', () => {
    const url = service.buildEmbedUrl({
      lat: 52.23028,
      lon: 20.96313,
    });

    const parsedUrl = new URL(url);

    expect(`${parsedUrl.origin}${parsedUrl.pathname}`).toBe('https://www.openstreetmap.org/export/embed.html');
    expect(parsedUrl.searchParams.get('layer')).toBe('mapnik');
    expect(parsedUrl.searchParams.get('marker')).toBe('52.23028,20.96313');
    expect(parsedUrl.searchParams.get('bbox')).toBe('20.958130,52.225280,20.968130,52.235280');
  });
});

function createVisit(overrides: Partial<Visit> = {}): Visit {
  return {
    id: 'w-test',
    service: 'Wizyta testowa',
    type: 'Wizyta stacjonarna',
    doctor: 'lek. med. Testowy',
    facility: 'Placówka testowa',
    address: 'ul. Testowa 1, 00-001 Warszawa',
    date: '01.01.2026',
    time: '10:00',
    duration: '20 min',
    patient: 'Jan Testowy',
    status: 'Umówiona',
    statusKind: 'upcoming',
    group: 'planned',
    price: 'W cenie pakietu',
    prepare: [],
    ...overrides,
  };
}

function captureResolvedLocation() {
  return {
    value: null as {
      coordinates: {
        lat: number;
        lon: number;
      };
      embedUrl: string;
    } | null,
  };
}
