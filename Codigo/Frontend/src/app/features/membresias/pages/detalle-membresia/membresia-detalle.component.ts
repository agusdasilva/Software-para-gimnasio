import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MembresiaService, MembresiaResponse } from '../../../../core/services/membresia.service';
import { AuthService } from '../../../../core/auth/auth.service';
import { PagoService } from '../../../../core/services/pago.service';

@Component({
  selector: 'app-membresia-detalle',
  templateUrl: './membresia-detalle.component.html',
  styleUrls: ['./membresia-detalle.component.css']
})
export class MembresiaDetalleComponent implements OnInit {
  membresia: MembresiaResponse | null = null;
  loading = false;
  error = '';
  cancelMessage = '';

  constructor(
    private membresiaService: MembresiaService,
    private authService: AuthService,
    private pagoService: PagoService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login'], { queryParams: { returnUrl: '/membresias/detalle' } });
      return;
    }
    this.checkPaymentReturn();
    this.cargar();
  }

  get diasRestantes(): number | null {
    if (!this.membresia?.fechaFin) return null;
    const fin = new Date(this.membresia.fechaFin).getTime();
    const diff = fin - Date.now();
    return diff > 0 ? Math.ceil(diff / (1000 * 60 * 60 * 24)) : 0;
  }

  get puedeCancelar(): boolean {
    const rol = this.authService.currentUser?.rol;
    return rol === 'CLIENTE' && !!this.membresia;
  }

  get puedeMejorar(): boolean {
    return this.puedeCancelar;
  }

  mejorar(): void {
    this.router.navigate(['/membresias']);
  }

  cancelar(): void {
    if (!this.membresia) {
      return;
    }
    const ok = window.confirm('Estas seguro de cancelar la membresia? No podras entrar mas al gym');
    if (!ok) return;

    this.loading = true;
    this.error = '';
    this.cancelMessage = '';

    this.membresiaService.cancelar(this.membresia.id).subscribe({
      next: () => {
        this.membresiaService.getMembresiaActual().subscribe({
          next: (m) => {
            this.membresia = m && m.estado === 'ACTIVA' ? m : null;
            this.cancelMessage = 'Tu membresía fue cancelada. Ya no podrás ingresar al gimnasio.';
          },
          error: () => {
            this.membresia = null;
            this.cancelMessage = 'Tu membresía fue cancelada. Ya no podrás ingresar al gimnasio.';
          },
          complete: () => this.loading = false
        });
      },
      error: () => {
        this.loading = false;
        this.error = 'No se pudo cancelar la membresía. Intenta de nuevo.';
      }
    });
  }

  private cargar(): void {
    this.loading = true;
    this.error = '';
    this.membresiaService.getMembresiaActual().subscribe({
      next: m => {
        const estado = (m?.estado || '').toUpperCase();
        if (estado === 'ACTIVA' && (!m?.fechaFin || new Date(m.fechaFin).getTime() > Date.now())) {
          this.membresia = m;
        } else {
          this.membresia = null;
        }
      },
      error: () => this.error = 'No se pudo cargar tu membresía.',
      complete: () => this.loading = false
    });
  }

  private checkPaymentReturn(): void {
    const paymentId = this.route.snapshot.queryParamMap.get('payment_id')
      || this.route.snapshot.queryParamMap.get('collection_id')
      || this.route.snapshot.queryParamMap.get('id');
    if (!paymentId) return;
    this.loading = true;
    this.pagoService.confirmarPago(paymentId).subscribe({
      next: (m) => {
        this.membresia = m;
        this.loading = false;
      },
      error: () => {
        // intentar reconciliar con el plan que pudiera estar almacenado
        this.loading = false;
      }
    });
  }
}
