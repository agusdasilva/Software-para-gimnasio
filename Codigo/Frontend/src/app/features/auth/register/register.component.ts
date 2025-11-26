import { Component } from '@angular/core';

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

    
  }
}
