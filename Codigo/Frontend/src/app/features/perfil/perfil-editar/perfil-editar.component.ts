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
  error = '';
  success = '';

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.authService.getProfile().subscribe(profile => {
      this.name = profile.nombre;
      this.email = profile.email;
      this.description = profile.descripcion || '';
      this.avatarUrl = profile.fotoUrl || '';
    });
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

    const current = this.authService.currentUser;
    if (!current) {
      this.error = 'No hay usuario autenticado.';
      return;
    }

    this.authService.updateProfile(current.id, {
      nombre: this.name.trim(),
      descripcion: this.description,
      fotoUrl: this.avatarUrl || undefined
    }).subscribe({
      next: (user: UsuarioResponse) => {
        this.success = 'Perfil actualizado.';
        this.password = '';
        this.confirmPassword = '';
        setTimeout(() => this.router.navigate(['/perfil']), 500);
      },
      error: () => {
        this.error = 'No se pudo actualizar el perfil.';
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/perfil']);
  }
}
