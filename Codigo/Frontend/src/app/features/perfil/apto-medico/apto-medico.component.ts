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
  storedFileName = '';
  storedUploadedAt = '';
  statusMessage = '';
  submitMessage = '';

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
    if (!this.selectedFile) {
      this.statusMessage = 'Primero selecciona un PDF.';
      return;
    }
    const reader = new FileReader();
    reader.onload = () => {
      const base64 = reader.result as string;
      this.aptosService.subir({
        nombreArchivo: this.selectedFile!.name,
        base64
      }).subscribe({
        next: (apto) => {
          this.storedFileName = apto.nombreArchivo;
          this.storedUploadedAt = apto.fechaSubida;
          this.submitMessage = 'Su apto médico será revisado en las próximas 48 horas, le notificaremos la resolución.';
          this.statusMessage = '';
        },
        error: () => {
          this.statusMessage = 'No se pudo subir el apto. Intenta nuevamente.';
        }
      });
    };
    reader.readAsDataURL(this.selectedFile);
  }

  goBack(): void {
    this.router.navigate(['/perfil']);
  }

  private loadStored(): void {
    this.aptosService.misAptos().subscribe({
      next: (aptos: Apto[]) => {
        const first = aptos[0];
        if (first) {
          this.storedFileName = first.nombreArchivo;
          this.storedUploadedAt = first.fechaSubida;
          this.submitMessage = 'Su apto médico será revisado en las próximas 48 horas, le notificaremos la resolución.';
        }
      },
      error: () => {
        // ignore
      }
    });
  }
}
