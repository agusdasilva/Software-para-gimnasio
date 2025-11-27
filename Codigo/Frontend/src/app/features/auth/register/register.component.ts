import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/auth/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  name = '';
  email = '';
  password = '';
  confirmPassword = '';
  error = '';

  constructor(private authService: AuthService, private router: Router) {}

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
      next: () => this.router.navigate(['/dashboard']),
      error: (err) => {
        const apiMessage = err?.error?.message;
        this.error = apiMessage || 'No se pudo crear la cuenta.';
      }
    });
  }
}
