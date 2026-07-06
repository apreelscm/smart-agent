import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
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

    fixture = TestBed.createComponent(WizytaComponent);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('shows the map action for stationary visits and hides it for telemedicine visits', () => {
    setVisitId('w1');

    expect(getMapActionButton()).not.toBeNull();

    setVisitId('w2');

    expect(getMapActionButton()).toBeNull();
  });

  it('renders the map modal lazily only after the user clicks the map action', () => {
    setVisitId('w1');

    expect(getMapModal()).toBeNull();
    expect(getMapFrame()).toBeNull();

    getMapActionButton()?.click();
    fixture.detectChanges();

    expect(getMapModal()).not.toBeNull();
    expect(getMapFrame()).not.toBeNull();
    expect(getMapFrame()?.getAttribute('src')).toContain('https://www.openstreetmap.org/export/embed.html?');
  });

  it('closes the map modal without leaving the visit details view', () => {
    setVisitId('w1');

    getMapActionButton()?.click();
    fixture.detectChanges();

    expect(getMapModal()).not.toBeNull();

    getCloseButton()?.click();
    fixture.detectChanges();

    expect(getMapModal()).toBeNull();
    expect((fixture.nativeElement as HTMLElement).textContent).toContain('Informacje o wizycie');
    expect((fixture.nativeElement as HTMLElement).querySelector('.hero-title')?.textContent).toContain(
      'Opieka specjalistyczna – Ginekolog',
    );
  });

  function setVisitId(id: string): void {
    fixture.componentRef.setInput('id', id);
    fixture.detectChanges();
  }

  function getMapActionButton(): HTMLButtonElement | null {
    return (fixture.nativeElement as HTMLElement).querySelector('[data-testid="visit-map-action"]');
  }

  function getMapModal(): HTMLElement | null {
    return (fixture.nativeElement as HTMLElement).querySelector('[data-testid="visit-map-modal"]');
  }

  function getMapFrame(): HTMLIFrameElement | null {
    return (fixture.nativeElement as HTMLElement).querySelector('[data-testid="visit-map-frame"]');
  }

  function getCloseButton(): HTMLButtonElement | null {
    return (fixture.nativeElement as HTMLElement).querySelector('[data-testid="visit-map-close"]');
  }
});
