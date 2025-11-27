import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MembresiaService, MembresiaResponse } from '../../../../core/services/membresia.service';
import { AuthService } from '../../../../core/auth/auth.service';
import { PagoService } from '../../../../core/services/pago.service';

@Component({
  selector: 'app-planes-membresia',
  templateUrl: './planes.component.html',
  styleUrls: ['./planes.component.css']
})
export class PlanesComponent implements OnInit {

  membresia: MembresiaResponse | null = null;
  loading = false;
  error = '';
  success = '';

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private membresiaService: MembresiaService,
    public authService: AuthService,
    private pagoService: PagoService
  ) {}

  ngOnInit(): void {
    this.checkPaymentReturn();
    if (!this.authService.isAuthenticated()) {
      return;
    }
    this.loading = true;
    this.membresiaService.getMembresiaActual().subscribe({
      next: (m) => {
        this.membresia = m;
        this.loading = false;
        this.error = '';
      },
      error: () => {
        this.loading = false;
        this.membresia = null;
        // Silenciamos si no tiene membresía activa; mostramos solo si es otro error
        this.error = '';
      }
    });
  }

  choosePlan(plan: string): void {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login'], { queryParams: { plan, returnUrl: '/membresias' } });
      return;
    }

    this.loading = true;
    this.error = '';
    this.pagoService.crearPreferencia(plan).subscribe({
      next: (pref) => {
        this.loading = false;
        window.location.href = pref.initPoint;
      },
      error: () => {
        this.loading = false;
        this.error = 'No se pudo iniciar el pago. Verifica tu sesión o inténtalo más tarde.';
      }
    });
  }

  diasRestantes(): number | null {
    if (!this.membresia?.fechaFin) return null;
    const fin = new Date(this.membresia.fechaFin).getTime();
    const ahora = Date.now();
    const diff = fin - ahora;
    return diff > 0 ? Math.ceil(diff / (1000 * 60 * 60 * 24)) : 0;
  }

  private checkPaymentReturn(): void {
    const paymentId = this.route.snapshot.queryParamMap.get('payment_id');
    const status = this.route.snapshot.queryParamMap.get('status');
    if (!paymentId || !status) {
      return;
    }
    if (!this.authService.isAuthenticated()) {
      return;
    }
    this.loading = true;
    this.error = '';
    this.pagoService.confirmarPago(paymentId).subscribe({
      next: () => {
        this.success = 'Pago confirmado y membresía renovada.';
        this.loading = false;
        this.membresiaService.getMembresiaActual().subscribe((m) => this.membresia = m);
      },
      error: () => {
        this.loading = false;
        this.error = 'No se pudo confirmar el pago. Si el cargo fue realizado, contacta soporte.';
      }
    });
  }
}
