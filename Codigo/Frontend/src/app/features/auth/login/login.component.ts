import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService, AuthUser } from '../../../core/auth/auth.service';

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

    const mockUser: AuthUser = {
      id: Date.now(),
      username: this.username.trim(),
      roles: ['ADMIN']
    };
    this.authService.setSession('mock-token', mockUser);
    this.router.navigate(['/dashboard']);
  }
}
