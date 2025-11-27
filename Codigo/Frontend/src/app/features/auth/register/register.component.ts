import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../core/auth/auth.service';
import { MercadoPagoService } from '../../../core/services/mercadopago.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  name = '';
  email = '';
  password = '';
  confirmPassword = '';
  error = '';
  private selectedPlan: string | null = null;

  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private mercadoPagoService: MercadoPagoService
  ) {}

  ngOnInit(): void {
    this.selectedPlan = this.route.snapshot.queryParamMap.get('plan');
  }

  onSubmit(): void {
    this.error = '';

    if (!this.name.trim() || !this.email.trim() || !this.password.trim() || !this.confirmPassword.trim()) {
      this.error = 'Completa todos los campos.';
      return;
    }

    if (!this.email.includes('@')) {
      this.error = 'Usa un correo valido (incluye @).';
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.error = 'Las contrasenas no coinciden.';
      return;
    }

    this.authService.register({
      nombre: this.name.trim(),
      email: this.email.trim(),
      password: this.password,
      rol: 'CLIENTE'
    }).subscribe({
      next: () => {
        if (this.selectedPlan) {
          this.mercadoPagoService.createPreference(this.selectedPlan).subscribe({
            next: (res) => {
              window.location.href = res.initPoint;
            },
            error: (err) => {
              const apiMessage = err?.error?.message;
              this.error = apiMessage || 'No pudimos iniciar el pago. Intenta nuevamente.';
            }
          });
          return;
        }
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        const apiMessage = err?.error?.message;
        this.error = apiMessage || 'No se pudo crear la cuenta.';
      }
    });
  }
}
