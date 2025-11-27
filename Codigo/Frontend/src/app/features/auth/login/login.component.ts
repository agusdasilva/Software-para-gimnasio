import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../core/auth/auth.service';
import { MercadoPagoService } from '../../../core/services/mercadopago.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  email = '';
  password = '';
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
    if (!this.email.trim() || !this.password.trim()) {
      this.error = 'Completa correo y contrasena.';
      return;
    }

    this.authService.login({
      email: this.email.trim(),
      password: this.password
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
        this.error = apiMessage || 'Credenciales invalidas o usuario inactivo.';
      }
    });
  }
}
