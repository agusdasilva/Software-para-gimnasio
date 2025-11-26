import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService, UsuarioResponse } from '../../../core/auth/auth.service';

@Component({
  selector: 'app-usuarios',
  templateUrl: './usuarios.component.html',
  styleUrls: ['./usuarios.component.css']
})
export class UsuariosComponent implements OnInit {

  search = '';
  users: UsuarioResponse[] = [];
  error = '';
  success = '';

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.refresh();
  }

  get filteredUsers() {
    const term = this.search.trim().toLowerCase();
    if (!term) {
      return this.users;
    }
    return this.users.filter(u => u.email.toLowerCase().includes(term) || u.nombre.toLowerCase().includes(term));
  }

  refresh(): void {
    this.error = '';
    this.authService.getAllUsers().subscribe({
      next: users => this.users = users,
      error: () => this.error = 'No se pudo cargar usuarios.'
    });
  }

  goToProfile(id: number): void {
    this.router.navigate(['/perfil'], { queryParams: { userId: id } });
  }

  promote(id: number): void {
    this.error = '';
    this.success = '';
    this.authService.changeRole({ idUsuario: id, nuevoRol: 'ENTRENADOR' }).subscribe({
      next: () => {
        this.success = 'Usuario ascendido a entrenador.';
        this.refresh();
      },
      error: () => {
        this.error = 'No se pudo ascender al usuario.';
      }
    });
  }

  removeTrainer(id: number): void {
    this.error = '';
    this.success = '';
    this.authService.changeRole({ idUsuario: id, nuevoRol: 'CLIENTE' }).subscribe({
      next: () => {
        this.success = 'Usuario removido del rol de entrenador.';
        this.refresh();
      },
      error: () => {
        this.error = 'No se pudo modificar al usuario.';
      }
    });
  }

  delete(id: number): void {
    this.error = '';
    this.success = '';
    this.authService.changeEstado({ idUsuario: id, nuevoEstado: 'BLOQUEADO' }).subscribe({
      next: () => {
        this.success = 'Usuario bloqueado.';
        this.refresh();
      },
      error: () => {
        this.error = 'No se pudo bloquear al usuario.';
      }
    });
  }
}
