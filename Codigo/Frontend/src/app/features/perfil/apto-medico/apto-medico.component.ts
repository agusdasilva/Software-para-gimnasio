import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AptosService, Apto } from '../../../core/services/aptos.service';
import { AuthService } from '../../../core/auth/auth.service';

@Component({
  selector: 'app-apto-medico',
  templateUrl: './apto-medico.component.html',
  styleUrls: ['./apto-medico.component.css']
})
export class AptoMedicoComponent implements OnInit {

  selectedFile?: File;
  apto: Apto | null = null;
  statusMessage = '';
  submitMessage = '';
  loading = false;
  sending = false;

  constructor(
    private router: Router,
    private aptosService: AptosService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    if (this.authService.currentUser?.roles?.includes('ADMIN')) {
      this.router.navigate(['/aptos']);
      return;
    }
    this.loadStored();
  }

  onFileChange(event: Event): void {
    if (this.isPending) {
      this.statusMessage = 'Ya enviaste un apto. Cancélalo para subir otro.';
      this.selectedFile = undefined;
      return;
    }
    const file = (event.target as HTMLInputElement | null)?.files?.[0];
    if (!file) return;
    if (file.type !== 'application/pdf') {
      this.statusMessage = 'El archivo debe ser un PDF.';
      this.selectedFile = undefined;
      return;
    }
    this.statusMessage = `Listo para enviar: ${file.name}`;
    this.selectedFile = file;
  }

  send(): void {
    if (this.isPending) {
      this.statusMessage = 'Tu apto está en revisión. Cancélalo si necesitas reemplazarlo.';
      return;
    }
    if (!this.selectedFile) {
      this.statusMessage = 'Primero selecciona un PDF.';
      return;
    }
    this.sending = true;
    const reader = new FileReader();
    reader.onload = () => {
      const base64 = reader.result as string;
      this.aptosService.subir({
        nombreArchivo: this.selectedFile!.name,
        base64
      }).subscribe({
        next: (apto) => {
          this.apto = apto;
          this.selectedFile = undefined;
          this.updateMessages();
        },
        error: () => {
          this.statusMessage = 'No se pudo subir el apto. Intenta nuevamente.';
        },
        complete: () => this.sending = false
      });
    };
    reader.readAsDataURL(this.selectedFile);
  }

  cancel(): void {
    if (!this.apto?.id || !this.isPending) return;
    this.loading = true;
    this.aptosService.cancelar(this.apto.id).subscribe({
      next: () => {
        this.apto = null;
        this.submitMessage = '';
        this.statusMessage = 'Solicitud cancelada. Sube un nuevo PDF cuando quieras.';
      },
      error: () => this.statusMessage = 'No se pudo cancelar. Intenta de nuevo.',
      complete: () => this.loading = false
    });
  }

  renew(): void {
    // permite subir un nuevo PDF sin borrar el actual; se reemplazará al ser aprobado por el admin
    this.statusMessage = 'Selecciona un PDF para renovar tu apto.';
    this.submitMessage = this.submitMessage; // mantiene mensaje vigente
  }

  goBack(): void {
    this.router.navigate(['/perfil']);
  }

  private loadStored(): void {
    this.aptosService.misAptos().subscribe({
      next: (aptos: Apto[]) => {
        this.apto = aptos && aptos.length ? aptos[0] : null;
        this.updateMessages();
      },
      error: () => {
        this.apto = null;
      }
    });
  }

  get isPending(): boolean {
    return (this.apto?.estado || '').toUpperCase() === 'PENDIENTE';
  }

  get isApproved(): boolean {
    return (this.apto?.estado || '').toUpperCase() === 'APROBADO';
  }

  get isRejected(): boolean {
    return (this.apto?.estado || '').toUpperCase() === 'RECHAZADO';
  }

  get canUpload(): boolean {
    return !this.isPending;
  }

  get showRenew(): boolean {
    return this.isApproved;
  }

  private updateMessages(): void {
    if (!this.apto) {
      this.submitMessage = '';
      return;
    }
    const estado = (this.apto.estado || '').toUpperCase();
    if (estado === 'APROBADO') {
      const vence = this.apto.fechaVencimiento ? new Date(this.apto.fechaVencimiento).toLocaleDateString() : '';
      this.submitMessage = `Su apto médico fue aprobado. Gracias por la espera. ${vence ? 'Vigente hasta ' + vence : ''}`;
      this.statusMessage = '';
    } else if (estado === 'RECHAZADO') {
      this.submitMessage = 'Su apto médico fue desaprobado, contacte con soporte para más información. Gracias por la espera.';
      this.statusMessage = '';
    } else if (estado === 'PENDIENTE') {
      this.submitMessage = 'Su apto médico será revisado en las próximas 48 horas, le notificaremos la resolución.';
      this.statusMessage = '';
    } else {
      this.submitMessage = '';
    }
  }
}
