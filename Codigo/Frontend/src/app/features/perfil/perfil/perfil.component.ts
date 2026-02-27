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
  joinedAtDisplay = '';
  viewOnly = false;
  medicalFileName = '';
  medicalUploadedAt = '';
  medicalVence = '';
  medicalEstado = '';
  aptoId: number | null = null;
  aptoMessage = '';
  aptoLoading = false;

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
    const rol = this.authService.currentUser?.rol;
    this.router.navigate([rol === 'CLIENTE' ? '/membresias/detalle' : '/membresias']);
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
        this.loadMedicalProof(user.id);
      });
      return;
    }

    this.authService.getProfile().subscribe(user => {
      this.fillProfile(user);
      this.viewOnly = false;
      this.loadMedicalProof(user.id);
    });
  }

  private loadMedicalProof(userId?: number): void {
    const targetId = userId ?? this.authService.currentUser?.id;
    if (!targetId) return;

    this.aptoLoading = true;
    this.aptoMessage = '';

    const isSelf = !this.viewOnly || targetId === this.authService.currentUser?.id;

    if (isSelf) {
      this.aptosService.misAptos().subscribe({
        next: (aptos: Apto[]) => this.setAptoFromList(aptos),
        error: () => this.clearApto(),
        complete: () => this.aptoLoading = false
      });
      return;
    }

    // Admin viendo otro usuario: buscar en aprobados y luego pendientes
    this.aptosService.aprobados().subscribe({
      next: (aprob: Apto[]) => {
        const match = (aprob || []).find(a => a.usuarioId === targetId);
        if (match) {
          this.setApto(match);
          this.aptoLoading = false;
        } else {
          this.aptosService.pendientes().subscribe({
            next: (pend: Apto[]) => {
              const m2 = (pend || []).find(a => a.usuarioId === targetId);
              if (m2) this.setApto(m2); else this.clearApto();
            },
            error: () => this.clearApto(),
            complete: () => this.aptoLoading = false
          });
        }
      },
      error: () => {
        this.clearApto();
        this.aptoLoading = false;
      }
    });
  }

  private clearApto(): void {
    this.aptoId = null;
    this.medicalFileName = '';
    this.medicalUploadedAt = '';
    this.medicalVence = '';
    this.medicalEstado = '';
  }

  private setAptoFromList(aptos: Apto[]): void {
    const first = aptos && aptos.length ? aptos[0] : null;
    if (first) this.setApto(first); else this.clearApto();
  }

  private setApto(apto: Apto): void {
    this.aptoId = apto.id;
    this.medicalFileName = apto.nombreArchivo || '';
    this.medicalUploadedAt = apto.fechaSubida ? new Date(apto.fechaSubida).toLocaleString() : '';
    this.medicalVence = apto.fechaVencimiento ? new Date(apto.fechaVencimiento).toLocaleDateString() : '';
    this.medicalEstado = apto.estado || '';
    this.aptoMessage = '';
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
    this.joinedAtDisplay = this.joinedAt ? new Date(this.joinedAt).toLocaleDateString() : '';
  }

  cancelarApto(): void {
    if (!this.aptoId) return;
    this.aptoLoading = true;
    this.aptoMessage = '';
    this.aptosService.cancelar(this.aptoId).subscribe({
      next: () => {
        this.aptoMessage = 'Apto cancelado. El usuario podrá subir un nuevo certificado.';
        this.clearApto();
      },
      error: () => this.aptoMessage = 'No se pudo cancelar el apto.',
      complete: () => this.aptoLoading = false
    });
  }
}
