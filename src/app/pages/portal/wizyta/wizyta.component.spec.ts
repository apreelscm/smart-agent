import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { WizytaComponent } from './wizyta.component';

describe('WizytaComponent', () => {
  let fixture: ComponentFixture<WizytaComponent>;
  let httpTestingController: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WizytaComponent],
      providers: [provideRouter([]), provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    httpTestingController = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(WizytaComponent);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('shows the map action for a stationary visit with an address', () => {
    setVisitId('w1');

    expect(getByTestId('visit-map-open')).not.toBeNull();
  });

  it('hides the map action for telemedicine visits', () => {
    setVisitId('w2');

    expect(getByTestId('visit-map-open')).toBeNull();
  });

  it('opens the modal, requests geocoding for the visit address, and renders the map', () => {
    setVisitId('w1');

    clickByTestId('visit-map-open');
    fixture.detectChanges();

    expect(getByTestId('visit-map-modal')).not.toBeNull();
    expect(getByTestId('visit-map-loading')).not.toBeNull();

    const request = httpTestingController.expectOne(
      (req) => req.method === 'GET' && req.url === 'https://nominatim.openstreetmap.org/search',
    );

    expect(request.request.params.get('q')).toBe('ul. Kasprzaka 25, 01-224 Warszawa');
    expect(request.request.params.get('format')).toBe('jsonv2');
    expect(request.request.params.get('limit')).toBe('1');

    request.flush([{ lat: '52.228000', lon: '20.989000' }]);
    fixture.detectChanges();

    const nativeElement = fixture.nativeElement as HTMLElement;
    const iframe = nativeElement.querySelector('iframe');

    expect(getByTestId('visit-map-loading')).toBeNull();
    expect(getByTestId('visit-map-frame-container')).not.toBeNull();
    expect(getByTestId('visit-map-error')).toBeNull();
    expect(nativeElement.textContent).toContain('Centrum Medyczne Świat Zdrowia – Warszawa Wola');
    expect(nativeElement.textContent).toContain('ul. Kasprzaka 25, 01-224 Warszawa');
    expect(iframe?.getAttribute('src')).toContain('https://www.openstreetmap.org/export/embed.html');
    expect(iframe?.getAttribute('src')).toContain('marker=52.228000%2C20.989000');
  });

  it('closes the modal without leaving the visit details view', () => {
    setVisitId('w1');

    clickByTestId('visit-map-open');
    fixture.detectChanges();

    const request = httpTestingController.expectOne(
      (req) => req.method === 'GET' && req.url === 'https://nominatim.openstreetmap.org/search',
    );

    request.flush([{ lat: '52.228000', lon: '20.989000' }]);
    fixture.detectChanges();

    clickByTestId('visit-map-close');
    fixture.detectChanges();

    expect(getByTestId('visit-map-modal')).toBeNull();
    expect((fixture.nativeElement as HTMLElement).textContent).toContain('Szczegóły wizyty');
  });

  it('shows a user-friendly error state when geocoding returns no result', () => {
    setVisitId('w1');

    clickByTestId('visit-map-open');
    fixture.detectChanges();

    const request = httpTestingController.expectOne(
      (req) => req.method === 'GET' && req.url === 'https://nominatim.openstreetmap.org/search',
    );

    request.flush([]);
    fixture.detectChanges();

    expect(getByTestId('visit-map-frame-container')).toBeNull();
    expect(getByTestId('visit-map-error')).not.toBeNull();
    expect((getByTestId('visit-map-error')?.textContent ?? '').toLowerCase()).toContain(
      'nie udało się pokazać lokalizacji',
    );
  });

  function setVisitId(id: string): void {
    fixture.componentRef.setInput('id', id);
    fixture.detectChanges();
  }

  function clickByTestId(testId: string): void {
    const element = getByTestId(testId);

    expect(element).withContext(`Missing element with data-testid="${testId}"`).not.toBeNull();
    (element as HTMLElement).click();
  }

  function getByTestId(testId: string): HTMLElement | null {
    return (fixture.nativeElement as HTMLElement).querySelector(`[data-testid="${testId}"]`);
  }
});
