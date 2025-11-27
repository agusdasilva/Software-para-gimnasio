import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService, UsuarioResponse } from '../../../core/auth/auth.service';

@Component({
  selector: 'app-perfil',
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css']
})
export class PerfilComponent implements OnInit {

  name = '';
  email = '';
  description = '';
  avatarUrl = '';
  roles: string[] = [];
  status = '';
  phone = '';
  joinedAt = '';
  viewOnly = false;

  constructor(private authService: AuthService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    this.loadProfile();
  }

  goToEdit(): void {
    if (this.viewOnly) {
      return;
    }
    this.router.navigate(['/perfil/editar']);
  }

  goToMembresias(): void {
    this.router.navigate(['/membresias']);
  }

  avatarFallback(): string {
    return this.name ? this.name.charAt(0).toUpperCase() : 'P';
  }

  private loadProfile(): void {
    const queryId = Number(this.route.snapshot.queryParamMap.get('userId'));
    const current = this.authService.currentUser;

    if (queryId && current?.roles.includes('ADMIN')) {
      this.authService.getUserById(queryId).subscribe(user => {
        this.fillProfile(user);
        this.viewOnly = user.id !== current?.id;
      });
      return;
    }

    this.authService.getProfile().subscribe(user => {
      this.fillProfile(user);
      this.viewOnly = false;
    });
  }

  private fillProfile(user: UsuarioResponse): void {
    this.name = user.nombre;
    this.email = user.email;
    this.description = user.descripcion || '';
    this.avatarUrl = user.fotoUrl || '';
    this.roles = [user.rol];
    this.status = user.estado;
    this.phone = user.telefono || '';
    this.joinedAt = user.fechaAlta || '';
  }
}
