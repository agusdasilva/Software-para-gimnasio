import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../../core/auth/auth.service';
import { MercadoPagoService } from '../../../../core/services/mercadopago.service';

@Component({
  selector: 'app-planes-membresia',
  templateUrl: './planes.component.html',
  styleUrls: ['./planes.component.css']
})
export class PlanesComponent {

  loadingPlan: string | null = null;
  error = '';

  constructor(
    private router: Router,
    private authService: AuthService,
    private mercadoPagoService: MercadoPagoService
  ) {}

  choosePlan(plan: string): void {
    this.error = '';

    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login'], { queryParams: { plan } });
      return;
    }

    this.loadingPlan = plan;
    this.mercadoPagoService.createPreference(plan).subscribe({
      next: (res) => {
        window.location.href = res.initPoint;
      },
      error: (err) => {
        this.loadingPlan = null;
        const apiMessage = err?.error?.message;
        this.error = apiMessage || 'No pudimos iniciar el pago. Intenta nuevamente.';
      }
    });
  }

  isLoading(plan: string): boolean {
    return this.loadingPlan === plan;
  }
}
