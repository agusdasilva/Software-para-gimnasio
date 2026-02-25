import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService, UsuarioResponse, UserRole } from '../../../core/auth/auth.service';
import { AptosService, Apto } from '../../../core/services/aptos.service';

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
  medicalFileName = '';
  medicalUploadedAt = '';

  constructor(
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router,
    private aptosService: AptosService
  ) {}

  ngOnInit(): void {
    this.loadProfile();
    this.loadMedicalProof();
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

  goToSettings(): void {
    if (this.viewOnly) {
      return;
    }
    this.router.navigate(['/perfil/ajustes']);
  }

  goToApto(): void {
    if (this.viewOnly) {
      return;
    }
    this.router.navigate(['/perfil/apto-medico']);
  }

  isAdmin(): boolean {
    const roles = this.authService.currentUser?.roles || [];
    return roles.includes('ADMIN' as UserRole);
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

  private loadMedicalProof(): void {
    this.aptosService.misAptos().subscribe({
      next: (aptos: Apto[]) => {
        const first = aptos[0];
        if (!first) {
          this.medicalFileName = '';
          this.medicalUploadedAt = '';
          return;
        }
        this.medicalFileName = first.nombreArchivo || '';
        this.medicalUploadedAt = first.fechaSubida || '';
      },
      error: () => {
        this.medicalFileName = '';
        this.medicalUploadedAt = '';
      }
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
