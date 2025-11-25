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

  private isValidEmail(email: string): boolean {
    return /^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(email);
  }

  onSubmit(): void {
    this.error = '';

    if (!this.name.trim() || !this.email.trim() || !this.password.trim() || !this.confirmPassword.trim()) {
      this.error = 'Completa todos los campos.';
      return;
    }

    if (this.name.trim().length < 3) {
      this.error = 'El nombre debe tener al menos 3 caracteres.';
      return;
    }

    if (!this.isValidEmail(this.email.trim())) {
      this.error = 'Usa un correo valido (ej: persona@mail.com).';
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.error = 'Las contrasenas no coinciden.';
      return;
    }

    if (this.password.length < 8) {
      this.error = 'La contrasena debe tener al menos 8 caracteres.';
      return;
    }

    this.authService.register({
      name: this.name.trim(),
      email: this.email.trim(),
      password: this.password
    }).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: (err: Error) => {
        this.error = err?.message || 'No se pudo crear la cuenta.';
      }
    });
  }
}
