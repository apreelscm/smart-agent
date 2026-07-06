import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { WizytaComponent } from './wizyta.component';

describe('WizytaComponent', () => {
  let fixture: ComponentFixture<WizytaComponent>;
  let component: WizytaComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WizytaComponent],
      providers: [provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(WizytaComponent);
    component = fixture.componentInstance;
  });

  function renderVisit(id: string): HTMLElement {
    fixture.componentRef.setInput('id', id);
    fixture.detectChanges();

    return fixture.nativeElement as HTMLElement;
  }

  it('renders the map trigger and opens a popup with an OpenStreetMap iframe for visits with coordinates', () => {
    const nativeElement = renderVisit('w1');
    const facilityBlock = nativeElement.querySelector('[data-testid="visit-facility-block"]');
    const mapTrigger = nativeElement.querySelector(
      '[data-testid="visit-map-trigger"]',
    ) as HTMLButtonElement;

    expect(facilityBlock).not.toBeNull();
    expect(facilityBlock?.textContent).toContain('Centrum Medyczne Świat Zdrowia – Warszawa Wola');
    expect(facilityBlock?.textContent).toContain('ul. Kasprzaka 25, 01-224 Warszawa');
    expect(mapTrigger).not.toBeNull();

    mapTrigger.click();
    fixture.detectChanges();

    const dialog = nativeElement.querySelector('[data-testid="visit-map-dialog"]');
    const iframe = nativeElement.querySelector(
      '[data-testid="visit-map-iframe"]',
    ) as HTMLIFrameElement;
    const decodedSrc = decodeURIComponent(iframe.getAttribute('src') ?? '');

    expect(component.isMapOpen()).toBeTrue();
    expect(dialog).not.toBeNull();
    expect(iframe).not.toBeNull();
    expect(decodedSrc).toContain('https://www.openstreetmap.org/export/embed.html');
    expect(decodedSrc).toContain('marker=52.230700,20.958300');
    expect(decodedSrc).toContain('bbox=20.948300,52.224700,20.968300,52.236700');

    const closeButton = dialog?.querySelector('button[aria-label="Zamknij mapę"]') as HTMLButtonElement;

    closeButton.click();
    fixture.detectChanges();

    expect(component.isMapOpen()).toBeFalse();
    expect(nativeElement.querySelector('[data-testid="visit-map-dialog"]')).toBeNull();
  });

  it('hides the facility block and map action entirely for visits without coordinates', () => {
    const nativeElement = renderVisit('w2');

    expect(component.hasCoordinates()).toBeFalse();
    expect(component.mapUrl()).toBeNull();
    expect(nativeElement.querySelector('[data-testid="visit-facility-block"]')).toBeNull();
    expect(nativeElement.querySelector('[data-testid="visit-map-trigger"]')).toBeNull();
    expect(nativeElement.textContent).not.toContain('Telemedycyna Świat Zdrowia');
    expect(nativeElement.textContent).not.toContain('Wideokonsultacja online');
  });
});
