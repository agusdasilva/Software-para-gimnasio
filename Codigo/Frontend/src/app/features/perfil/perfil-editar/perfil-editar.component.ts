import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService, AuthUser } from '../../../core/auth/auth.service';

@Component({
  selector: 'app-perfil-editar',
  templateUrl: './perfil-editar.component.html',
  styleUrls: ['./perfil-editar.component.css']
})
export class PerfilEditarComponent implements OnInit {

  name = '';
  email = '';
  description = '';
  avatarUrl = '';
  password = '';
  confirmPassword = '';
  error = '';
  success = '';

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    const profile = this.authService.getProfile();
    if (profile) {
      this.name = profile.username;
      this.email = profile.email;
      this.description = profile.description || '';
      this.avatarUrl = profile.avatarUrl || '';
    }
  }

  private isValidEmail(email: string): boolean {
    return /^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(email);
  }

  save(): void {
    this.error = '';
    this.success = '';

    if (!this.name.trim() || !this.email.trim()) {
      this.error = 'Nombre y correo son obligatorios.';
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

    if (this.password || this.confirmPassword) {
      if (this.password !== this.confirmPassword) {
        this.error = 'Las contrasenas no coinciden.';
        return;
      }
      if (this.password.length < 8) {
        this.error = 'La contrasena debe tener al menos 8 caracteres.';
        return;
      }
    }

    this.authService.updateProfile({
      name: this.name.trim(),
      email: this.email.trim(),
      password: this.password || undefined,
      description: this.description,
      avatarUrl: this.avatarUrl
    }).subscribe({
      next: (user: AuthUser) => {
        this.success = 'Perfil actualizado.';
        this.password = '';
        this.confirmPassword = '';
        setTimeout(() => this.router.navigate(['/perfil']), 500);
      },
      error: (err: Error) => {
        this.error = err?.message || 'No se pudo actualizar el perfil.';
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/perfil']);
  }
}
