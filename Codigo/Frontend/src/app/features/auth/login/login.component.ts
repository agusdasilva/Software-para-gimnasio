import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/auth/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  email = '';
  password = '';
  error = '';

  constructor(private authService: AuthService, private router: Router) {}

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
      next: () => this.router.navigate(['/dashboard']),
      error: (err) => {
        const apiMessage = err?.error?.message;
        this.error = apiMessage || 'Credenciales invalidas o usuario inactivo.';
      }
    });
  }
}
