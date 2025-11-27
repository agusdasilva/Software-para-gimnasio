import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MembresiaService, MembresiaResponse } from '../../../../core/services/membresia.service';
import { AuthService } from '../../../../core/auth/auth.service';
import { PagoService } from '../../../../core/services/pago.service';

@Component({
  selector: 'app-planes-membresia',
  templateUrl: './planes.component.html',
  styleUrls: ['./planes.component.css']
})
export class PlanesComponent implements OnInit, OnDestroy {

  membresia: MembresiaResponse | null = null;
  loading = false;
  loadingPlan: string | null = null;
  error = '';
  success = '';
  private paymentPoll: number | null = null;

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

  ngOnDestroy(): void {
    if (this.paymentPoll !== null) {
      window.clearInterval(this.paymentPoll);
      this.paymentPoll = null;
    }
  }

  choosePlan(plan: string): void {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login'], { queryParams: { plan, returnUrl: '/membresias' } });
      return;
    }

    this.loading = true;
    this.loadingPlan = plan;
    this.error = '';
    this.pagoService.crearPreferencia(plan).subscribe({
      next: (pref) => {
        const paymentWindow = window.open(pref.initPoint, '_blank', 'width=520,height=780');
        // Si el navegador bloquea el popup, degradamos a la misma pestaña
        if (!paymentWindow) {
          window.location.href = pref.initPoint;
          return;
        }
        this.startPopupListener(paymentWindow);
        paymentWindow.focus();
        this.loading = false;
        this.loadingPlan = null;
      },
      error: () => {
        this.loading = false;
        this.loadingPlan = null;
        this.error = 'No se pudo iniciar el pago. Verifica tu sesión o inténtalo más tarde.';
      }
    });
  }

  isLoading(plan: string): boolean {
    return this.loadingPlan === plan;
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

  private startPopupListener(popup: Window): void {
    if (this.paymentPoll !== null) {
        window.clearInterval(this.paymentPoll);
    }
    this.paymentPoll = window.setInterval(() => {
      try {
        // Solo podemos leer la URL cuando el popup está en nuestro dominio (back_url)
        const sameOrigin = popup.location.origin === window.location.origin;
        if (popup.closed) {
          window.clearInterval(this.paymentPoll!);
          this.paymentPoll = null;
          this.loading = false;
          this.loadingPlan = null;
          return;
        }
        if (!sameOrigin) {
          return;
        }

        const url = new URL(popup.location.href);
        const paymentId = url.searchParams.get('payment_id');
        const status = url.searchParams.get('status');
        if (paymentId && status) {
          window.clearInterval(this.paymentPoll!);
          this.paymentPoll = null;
          popup.close();
          this.loading = true;
          this.error = '';
          this.pagoService.confirmarPago(paymentId).subscribe({
            next: () => {
              this.success = 'Pago confirmado y membresía renovada.';
              this.membresiaService.getMembresiaActual().subscribe((m) => this.membresia = m);
              this.loading = false;
            },
            error: () => {
              this.loading = false;
              this.error = 'No se pudo confirmar el pago. Si el cargo fue realizado, contacta soporte.';
            }
          });
        }
      } catch (err) {
        // Ignoramos errores de cross-origin hasta que vuelva al dominio
      }
    }, 800);
  }
}
