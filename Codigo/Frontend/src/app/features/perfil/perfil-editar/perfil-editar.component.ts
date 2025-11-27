import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService, UsuarioResponse } from '../../../core/auth/auth.service';

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
  phone = '';
  error = '';
  success = '';

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.authService.getProfile().subscribe(profile => {
      this.name = profile.nombre;
      this.email = profile.email;
      this.description = profile.descripcion || '';
      this.avatarUrl = profile.fotoUrl || '';
      this.phone = profile.telefono || '';
    });
  }

  save(): void {
    this.error = '';
    this.success = '';

    const trimmedName = this.name.trim();
    const trimmedEmail = this.email.trim();
    const trimmedPhone = this.phone ? this.phone.trim() : '';

    if (!trimmedName || !trimmedEmail) {
      this.error = 'Nombre y correo son obligatorios.';
      return;
    }

    if (trimmedName.length < 3) {
      this.error = 'El nombre debe tener al menos 3 caracteres.';
      return;
    }

    if (!trimmedEmail.includes('@')) {
      this.error = 'Ingresa un correo válido.';
      return;
    }

    if (this.password && this.password.length < 6) {
      this.error = 'La contraseña debe tener al menos 6 caracteres.';
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.error = 'Las contraseñas no coinciden.';
      return;
    }

    const current = this.authService.currentUser;
    if (!current) {
      this.error = 'No hay usuario autenticado.';
      return;
    }

    this.authService.updateProfile(current.id, {
      nombre: trimmedName,
      email: trimmedEmail,
      descripcion: this.description,
      fotoUrl: this.avatarUrl || undefined,
      telefono: trimmedPhone || undefined,
      password: this.password || undefined
    }).subscribe({
      next: (user: UsuarioResponse) => {
        this.success = 'Perfil actualizado.';
        this.password = '';
        this.confirmPassword = '';

        // Si cambió el correo, el token actual deja de ser válido (se requiere re-login)
        if (trimmedEmail !== current.email) {
          setTimeout(() => {
            this.authService.logout();
            this.router.navigate(['/login']);
          }, 700);
          return;
        }

        this.authService.getProfile().subscribe(); // refresca sesión local
        setTimeout(() => this.router.navigate(['/perfil']), 800);
      },
      error: (err) => {
        this.error = err?.error?.message || 'No se pudo actualizar el perfil.';
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/perfil']);
  }
}
