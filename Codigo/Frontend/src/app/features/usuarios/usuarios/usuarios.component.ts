import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService, StoredUser } from '../../../core/auth/auth.service';

@Component({
  selector: 'app-usuarios',
  templateUrl: './usuarios.component.html',
  styleUrls: ['./usuarios.component.css']
})
export class UsuariosComponent implements OnInit {

  search = '';
  users: StoredUser[] = [];
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
    return this.users.filter(u => u.email.toLowerCase().includes(term));
  }

  refresh(): void {
    this.users = this.authService.getAllUsers();
  }

  goToProfile(id: number): void {
    this.router.navigate(['/perfil'], { queryParams: { userId: id } });
  }

  promote(id: number): void {
    this.error = '';
    this.success = '';
    this.authService.promoteToTrainer(id).subscribe({
      next: () => {
        this.success = 'Usuario ascendido a entrenador.';
        this.refresh();
      },
      error: (err: Error) => {
        this.error = err?.message || 'No se pudo ascender al usuario.';
      }
    });
  }

  removeTrainer(id: number): void {
    this.error = '';
    this.success = '';
    this.authService.removeTrainerRole(id).subscribe({
      next: () => {
        this.success = 'Usuario removido del rol de entrenador.';
        this.refresh();
      },
      error: (err: Error) => {
        this.error = err?.message || 'No se pudo modificar al usuario.';
      }
    });
  }

  delete(id: number): void {
    this.error = '';
    this.success = '';
    this.authService.deleteUser(id).subscribe({
      next: () => {
        this.success = 'Usuario eliminado.';
        this.refresh();
      },
      error: (err: Error) => {
        this.error = err?.message || 'No se pudo eliminar al usuario.';
      }
    });
  }
}
