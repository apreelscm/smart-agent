import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { WizardCardComponent } from '../../../../shared/components/wizard-card/wizard-card.component';
import { PolicyDraftService } from '../../../../core/services/policy-draft.service';

@Component({
  selector: 'app-step-03-registration',
  standalone: true,
  imports: [WizardCardComponent, FormsModule],
  templateUrl: './step-03-registration.component.html',
})
export class Step03RegistrationComponent {
  private draft = inject(PolicyDraftService);
  private router = inject(Router);

  plateNumber = this.draft.draft().vehicle.plateNumber;

  skip() { this.router.navigate(['/kalkulator/typ-pojazdu']); }

  next() {
    this.draft.patchVehicle({ plateNumber: this.plateNumber });
    this.router.navigate(['/kalkulator/typ-pojazdu']);
  }
}
