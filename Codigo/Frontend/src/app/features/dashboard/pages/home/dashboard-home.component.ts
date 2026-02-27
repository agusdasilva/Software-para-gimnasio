import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService, UsuarioResponse } from '../../../../core/auth/auth.service';
import { HorarioService, HorarioDia } from '../../../../core/services/horario.service';
import { DashboardConfigService, DashboardConfig } from '../../../../core/services/dashboard-config.service';
import { MembresiaService, MembresiaResponse } from '../../../../core/services/membresia.service';
import { AptosService, Apto } from '../../../../core/services/aptos.service';

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
  userProfile: UsuarioResponse | null = null;
  membresia: MembresiaResponse | null = null;
  apto: Apto | null = null;

  constructor(
    private horarioService: HorarioService,
    private authService: AuthService,
    private dashboardConfigService: DashboardConfigService,
    private membresiaService: MembresiaService,
    private aptosService: AptosService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarHorario();
    this.cargarContenido();
    this.cargarPerfil();
    this.cargarMembresia();
    this.cargarApto();
  }

  get isAdmin(): boolean {
    return this.authService.hasRole(['ADMIN']);
  }

  get displayName(): string {
    return this.userProfile?.nombre || this.authService.currentUser?.username || 'Cliente';
  }

  get isMemberActive(): boolean {
    if (this.membresia?.estado?.toUpperCase() === 'ACTIVA' || this.membresia?.estado?.toUpperCase() === 'ACTIVO') {
      return true;
    }
    const profile = this.userProfile;
    if (!profile) {
      return false;
    }
    if (typeof profile.miembroActivo === 'boolean') {
      return profile.miembroActivo;
    }
    const estadoMembresia = (profile.estadoMembresia || '').toLowerCase();
    if (estadoMembresia) {
      return estadoMembresia === 'activo';
    }
    return profile.estado === 'ACTIVO';
  }

  get planName(): string {
    if (!this.isMemberActive) {
      return 'Sin plan';
    }
    return this.userProfile?.nombrePlan || 'Sin plan';
  }

  get membreStatusText(): string {
    if (this.authService.currentUser?.rol !== 'CLIENTE') return 'Gestiona tus membresías';
    return this.isMemberActive ? 'Membresía activa' : 'Sin membresía';
  }

  get membreDetail(): string {
    if (this.authService.currentUser?.rol !== 'CLIENTE') return 'Administra planes, precios y asignaciones.';
    if (!this.membresia) return 'Gestiona tu plan en Membresías.';
    const dias = this.diasRestantesMembresia();
    if (dias === null) {
      return `Plan ${this.membresia.nombrePlan}`;
    }
    const pref = dias > 0 ? `Restan ${dias} días` : 'Vence hoy';
    return `${pref} (fin ${new Date(this.membresia.fechaFin).toLocaleDateString()})`;
  }

  get aptoStatusText(): string {
    if (this.authService.currentUser?.rol === 'ADMIN') return 'Gestionar aptos médicos';
    const estado = (this.apto?.estado || 'PENDIENTE').toUpperCase();
    switch (estado) {
      case 'APROBADO': return 'Apto médico aprobado';
      case 'RECHAZADO': return 'Apto médico rechazado';
      case 'CANCELADO': return 'Apto cancelado';
      default: return 'Apto médico pendiente';
    }
  }

  get aptoDetail(): string {
    if (this.authService.currentUser?.rol === 'ADMIN') return 'Aprueba, rechaza o asigna vencimientos.';
    if (!this.apto) return 'Sube tu certificado para habilitar el ingreso.';
    if (this.apto.estado === 'APROBADO' && this.apto.fechaVencimiento) {
      const dias = this.diasHasta(this.apto.fechaVencimiento);
      const pref = dias > 0 ? `Vence en ${dias} días` : 'Vence hoy';
      return `${pref} (${new Date(this.apto.fechaVencimiento).toLocaleDateString()})`;
    }
    if (this.apto.estado === 'RECHAZADO') {
      return 'Vuelve a subir tu certificado.';
    }
    return 'En revisión.';
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

  private cargarPerfil(): void {
    this.authService.getProfile().subscribe({
      next: user => {
        this.userProfile = user;
      },
      error: () => {
        this.userProfile = null;
      }
    });
  }

  private cargarMembresia(): void {
    if (!this.authService.isAuthenticated()) return;
    this.membresiaService.getMembresiaActual().subscribe({
      next: m => this.membresia = m && m.estado?.toUpperCase() === 'ACTIVA' ? m : null,
      error: () => this.membresia = null
    });
  }

  private cargarApto(): void {
    if (!this.authService.isAuthenticated()) return;
    this.aptosService.misAptos().subscribe({
      next: aptos => this.apto = aptos && aptos.length ? aptos[0] : null,
      error: () => this.apto = null
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

  private diasRestantesMembresia(): number | null {
    if (!this.membresia?.fechaFin) return null;
    return this.diasHasta(this.membresia.fechaFin);
  }

  private diasHasta(fecha: string): number {
    const fin = new Date(fecha).getTime();
    const diff = fin - Date.now();
    return Math.max(0, Math.ceil(diff / (1000 * 60 * 60 * 24)));
  }

  goToMembresias(): void {
    const rol = this.authService.currentUser?.rol;
    this.router.navigate([rol === 'CLIENTE' ? '/membresias/detalle' : '/membresias']);
  }

  goToApto(): void {
    this.router.navigate(['/perfil/apto-medico']);
  }

}


