import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EmptyComponent } from './empty.component';

describe('EmptyComponent', () => {
  let fixture: ComponentFixture<EmptyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmptyComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(EmptyComponent);
    fixture.detectChanges();
  });

  it('renders the sparse start view without the hero banner', () => {
    const nativeElement = fixture.nativeElement as HTMLElement;

    expect(nativeElement.querySelector('[data-testid="empty-start-view"]')).not.toBeNull();
    expect(nativeElement.querySelector('.hero-wrap')).toBeNull();
    expect(nativeElement.querySelector('.empty-stage')).not.toBeNull();
  });

  it('does not render the previous placeholder copy on the start screen', () => {
    const textContent = (fixture.nativeElement as HTMLElement).textContent ?? '';

    expect(textContent).not.toContain('/empty');
    expect(textContent).not.toContain('Pusta strona');
    expect(textContent).not.toContain('Logowanie zakończyło się powodzeniem.');
    expect(textContent).not.toContain('Użytkownik został zalogowany.');
    expect(textContent).not.toContain('To jest docelowa strona po zalogowaniu w tym zadaniu.');
  });
});
