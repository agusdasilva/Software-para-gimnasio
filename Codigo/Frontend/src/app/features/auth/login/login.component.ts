import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/auth/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  username = '';
  password = '';
  error = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    this.error = '';
    if (!this.username.trim() || !this.password.trim()) {
      this.error = 'Completa usuario y contrasena.';
      return;
    }

    this.authService.login({
      username: this.username.trim(),
      password: this.password
    }).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: (err: Error) => {
        this.error = err?.message || 'No se pudo iniciar sesion.';
      }
    });
  }
}
