import { Component, OnInit } from '@angular/core';
import { AptosService, Apto } from '../../core/services/aptos.service';

@Component({
  selector: 'app-aptos',
  templateUrl: './aptos.component.html',
  styleUrls: ['./aptos.component.css']
})
export class AptosComponent implements OnInit {

  pendientes: Apto[] = [];
  aprobados: Apto[] = [];
  fechas: Record<number, string> = {};
  loading = false;
  errorMsg = '';

  constructor(private aptosService: AptosService) {}

  ngOnInit(): void {
    this.refresh();
  }

  refresh(): void {
    this.loading = true;
    this.errorMsg = '';
    this.aptosService.pendientes().subscribe({
      next: res => {
        this.pendientes = res;
        const defaultDate = this.defaultExpiry();
        res.forEach(a => {
          if (!this.fechas[a.id]) {
            this.fechas[a.id] = defaultDate;
          }
        });
      },
      error: () => this.errorMsg = 'No se pudieron cargar las solicitudes'
    });
    this.aptosService.aprobados().subscribe({
      next: res => this.aprobados = res,
      error: () => this.errorMsg = 'No se pudieron cargar los aptos aprobados',
      complete: () => this.loading = false
    });
  }

  aprobar(apto: Apto): void {
    const fecha = this.fechas[apto.id];
    if (!fecha) {
      this.errorMsg = 'Elegí una fecha de vencimiento';
      return;
    }
    this.aptosService.aprobar(apto.id, fecha).subscribe({
      next: () => this.refresh(),
      error: () => this.errorMsg = 'No se pudo aprobar el apto'
    });
  }

  rechazar(apto: Apto): void {
    this.aptosService.rechazar(apto.id).subscribe({
      next: () => this.refresh(),
      error: () => this.errorMsg = 'No se pudo rechazar el apto'
    });
  }

  cancelar(apto: Apto): void {
    this.aptosService.cancelar(apto.id).subscribe({
      next: () => this.refresh(),
      error: () => this.errorMsg = 'No se pudo cancelar el apto'
    });
  }

  private defaultExpiry(): string {
    const d = new Date();
    d.setFullYear(d.getFullYear() + 1);
    return d.toISOString().slice(0, 10);
  }
}
