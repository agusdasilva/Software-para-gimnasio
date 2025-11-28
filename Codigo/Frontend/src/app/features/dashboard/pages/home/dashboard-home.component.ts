import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../../core/auth/auth.service';
import { HorarioService, HorarioDia } from '../../../../core/services/horario.service';
import { DashboardConfigService, DashboardConfig } from '../../../../core/services/dashboard-config.service';

@Component({
  selector: 'app-dashboard-home',
  templateUrl: './dashboard-home.component.html',
  styleUrls: ['./dashboard-home.component.css']
})
export class DashboardHomeComponent implements OnInit {

  horario: HorarioDia[] = [];
  editMode = false;
  selectedDia = '';
  updateMessage = '';
  updateError = '';
  loading = false;
  config: DashboardConfig = { noticias: [], recordatorios: [] };
  editNoticias = false;
  editRecordatorios = false;
  noticiasMessage = '';
  noticiasError = '';
  recordatoriosMessage = '';
  recordatoriosError = '';
  noticiasString = '';
  recordatoriosString = '';
  readonly diasSemana = ['Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes', 'Sabado', 'Domingo'];

  constructor(
    private horarioService: HorarioService,
    private authService: AuthService,
    private dashboardConfigService: DashboardConfigService
  ) {}

  ngOnInit(): void {
    this.cargarHorario();
    this.cargarContenido();
  }

  get isAdmin(): boolean {
    return this.authService.hasRole(['ADMIN']);
  }

  cargarHorario(): void {
    this.horarioService.obtener().subscribe({
      next: h => {
        this.horario = this.completarDias([...h]);
        this.selectedDia = this.diasSemana[0];
      },
      error: () => this.updateError = 'No se pudo cargar el horario.'
    });
  }

  guardarHorario(): void {
    if (!this.horario || this.horario.length === 0) {
      return;
    }
    this.loading = true;
    this.updateMessage = '';
    this.updateError = '';
    this.horarioService.actualizar(this.horario).subscribe({
      next: h => {
        this.horario = this.completarDias([...h]);
        this.editMode = false;
        this.updateMessage = 'Horario actualizado. Se notifico a los usuarios.';
      },
      error: () => this.updateError = 'No se pudo actualizar el horario.',
      complete: () => this.loading = false
    });
  }

  cancelarEdicion(): void {
    this.editMode = false;
    this.updateError = '';
    this.updateMessage = '';
    this.cargarHorario();
  }

  cargarContenido(): void {
    this.dashboardConfigService.obtener().subscribe({
      next: cfg => {
        this.config = cfg;
        this.noticiasString = cfg.noticias.join('\n');
        this.recordatoriosString = cfg.recordatorios.join('\n');
      },
      error: () => {
        this.noticiasError = 'No se pudo cargar las noticias.';
        this.recordatoriosError = 'No se pudieron cargar los recordatorios.';
      }
    });
  }

  private parseLines(text: string): string[] {
    return (text || '')
      .split('\n')
      .map(t => t.trim())
      .filter(t => !!t);
  }

  seleccionarDia(dia: string): void {
    this.selectedDia = dia;
    this.updateMessage = '';
  }

  iniciarEdicionHorario(): void {
    this.editMode = true;
    this.selectedDia = this.selectedDia || this.diasSemana[0];
  }

  cancelarContenidoNoticias(): void {
    this.editNoticias = false;
    this.noticiasMessage = '';
    this.noticiasError = '';
    this.cargarContenido();
  }

  cancelarContenidoRecordatorios(): void {
    this.editRecordatorios = false;
    this.recordatoriosMessage = '';
    this.recordatoriosError = '';
    this.cargarContenido();
  }

  guardarNoticias(): void {
    this.noticiasMessage = '';
    this.noticiasError = '';
    this.config.noticias = this.parseLines(this.noticiasString);
    this.dashboardConfigService.actualizar(this.config).subscribe({
      next: cfg => {
        this.config = cfg;
        this.noticiasString = cfg.noticias.join('\n');
        this.editNoticias = false;
        this.noticiasMessage = 'Noticias actualizadas y notificadas.';
      },
      error: () => this.noticiasError = 'No se pudieron actualizar las noticias.'
    });
  }

  guardarRecordatorios(): void {
    this.recordatoriosMessage = '';
    this.recordatoriosError = '';
    this.config.recordatorios = this.parseLines(this.recordatoriosString);
    this.dashboardConfigService.actualizar(this.config).subscribe({
      next: cfg => {
        this.config = cfg;
        this.recordatoriosString = cfg.recordatorios.join('\n');
        this.editRecordatorios = false;
        this.recordatoriosMessage = 'Recordatorios actualizados y notificados.';
      },
      error: () => this.recordatoriosError = 'No se pudieron actualizar los recordatorios.'
    });
  }

  horarioDelDia(dia: string): HorarioDia | undefined {
    return this.horario.find(h => this.normalizarDia(h.dia) === this.normalizarDia(dia));
  }

  private completarDias(lista: HorarioDia[]): HorarioDia[] {
    const horarios = [...lista];
    const orden = this.diasSemana.map(d => this.normalizarDia(d));
    this.diasSemana.forEach(dia => {
      if (!horarios.find(h => this.normalizarDia(h.dia) === this.normalizarDia(dia))) {
        horarios.push({ dia, horaApertura: '08:00', horaCierre: '20:00' });
      }
    });
    return horarios.sort((a, b) => {
      const idxA = orden.indexOf(this.normalizarDia(a.dia));
      const idxB = orden.indexOf(this.normalizarDia(b.dia));
      return (idxA === -1 ? orden.length : idxA) - (idxB === -1 ? orden.length : idxB);
    });
  }

  private normalizarDia(nombre: string): string {
    return (nombre || '').normalize('NFD').replace(/[\u0300-\u036f]/g, '').toLowerCase();
  }
}
