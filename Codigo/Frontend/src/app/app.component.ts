import { Component, OnInit } from '@angular/core';
import { PagoService } from './core/services/pago.service';
import { AuthService } from './core/auth/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'Gimnasio';
  private readonly storageKey = 'last_plan_selected';

  constructor(private pagoService: PagoService, private authService: AuthService) {}

  ngOnInit(): void {
    // Confirmar pago si la app se abre con parametros de MercadoPago, sin depender de la pantalla actual
    const params = new URLSearchParams(window.location.search);
    const paymentId = params.get('payment_id') || params.get('collection_id') || params.get('id');
    if (paymentId) {
      this.pagoService.confirmarPago(paymentId).subscribe({
        next: () => {
          // noop, las pantallas concretas ya leen la membresia
        },
        error: () => {
          // No mostrar error global; las pantallas manejarán reconcilio si es necesario
        }
      });
      return;
    }

    // Si no hay paymentId pero quedó un plan seleccionado y el usuario está logueado, intentar reconciliar automáticamente
    if (this.authService.isAuthenticated()) {
      try {
        const raw = localStorage.getItem(this.storageKey);
        if (raw) {
          const data = JSON.parse(raw);
          const planId = data?.id;
          if (planId) {
            this.pagoService.reconciliar(planId.toString()).subscribe({
              next: () => {
                // limpiar storage al reconciliar con éxito
                localStorage.removeItem(this.storageKey);
              },
              error: () => {
                // silencioso
              }
            });
          }
        }
      } catch {
        // ignorar errores de parseo
      }
    }
  }
}
