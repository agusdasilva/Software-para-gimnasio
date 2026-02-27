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

  constructor(private pagoService: PagoService, private authService: AuthService) {}

  ngOnInit(): void {
    // Confirmar pago si la app se abre con parametros de MercadoPago, sin depender de la pantalla actual
    const params = new URLSearchParams(window.location.search);
    const paymentId = params.get('payment_id') || params.get('collection_id') || params.get('id');
    if (!paymentId) return;
    if (!this.authService.isAuthenticated()) return;

    this.pagoService.confirmarPago(paymentId).subscribe({
      next: () => {
        // noop, las pantallas concretas ya leen la membresia
      },
      error: () => {
        // No mostrar error global; las pantallas manejarán reconcilio si es necesario
      }
    });
  }
}
