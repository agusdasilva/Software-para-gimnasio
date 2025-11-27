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
  filtroRol: 'todos' | 'ADMIN' | 'ENTRENADOR' | 'CLIENTE' = 'todos';
  filtroEstado: 'todos' | 'ACTIVO' | 'PENDIENTE' | 'BLOQUEADO' = 'todos';
  filtroMembresia: 'todos' | 'activos' | 'inactivos' = 'todos';
  sortKey: 'nombre' | 'email' | 'rol' | 'estado' | 'miembro' = 'nombre';
  sortDir: 'asc' | 'desc' = 'asc';

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.refresh();
  }

  get filteredUsers() {
    const term = this.search.trim().toLowerCase();
    let list = this.users.filter(u => {
      const coincideTexto = !term || u.email.toLowerCase().includes(term) || u.nombre.toLowerCase().includes(term);
      const coincideRol = this.filtroRol === 'todos' || u.rol === this.filtroRol;
      const coincideEstado = this.filtroEstado === 'todos' || u.estado === this.filtroEstado;
      const esMiembro = !!u.miembroActivo;
      const coincideMembresia =
        this.filtroMembresia === 'todos' ||
        (this.filtroMembresia === 'activos' && esMiembro) ||
        (this.filtroMembresia === 'inactivos' && !esMiembro);
      return coincideTexto && coincideRol && coincideEstado && coincideMembresia;
    });

    const dir = this.sortDir === 'asc' ? 1 : -1;
    list = list.sort((a, b) => {
      const val = this.sortKey === 'miembro'
        ? Number(!!a.miembroActivo) - Number(!!b.miembroActivo)
        : (a[this.sortKey] || '').toString().localeCompare((b[this.sortKey] || '').toString(), 'es', { sensitivity: 'base' });
      return val * dir;
    });
    return list;
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

  setSort(key: 'nombre' | 'email' | 'rol' | 'estado' | 'miembro'): void {
    if (this.sortKey === key) {
      this.sortDir = this.sortDir === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortKey = key;
      this.sortDir = 'asc';
    }
  }

  miembroLabel(user: UsuarioResponse): string {
    if (user.rol !== 'CLIENTE') {
      return '-';
    }
    return user.miembroActivo ? 'Miembro activo' : 'Sin membresía';
  }
}
