import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/auth/auth.service';
import { ClasesService, ClaseItem, ClaseEstado } from './clases.service';
import { NotificacionService } from '../../core/services/notificacion.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-clases',
  templateUrl: './clases.component.html',
  styleUrls: ['./clases.component.css']
})
export class ClasesComponent implements OnInit, OnDestroy {

  clases: ClaseItem[] = [];
  filtradas: ClaseItem[] = [];
  misClases: ClaseItem[] = [];
  misClasesEntrenador: ClaseItem[] = [];

  filtroTexto = '';
  filtroNivel: 'todos' | ClaseItem['nivel'] = 'todos';
  filtroEstado: 'todos' | ClaseEstado = 'todos';
  solicitudesEnviadas = new Set<number>();
  mensajeSolicitud = '';

  isAdminOrTrainer = false;
  isAdmin = false;
  creandoClase = false;
  nuevaClase: Partial<ClaseItem> = { entrenadores: [], nivel: 'Inicial', duracionMin: 45, cupo: 15, estado: 'ABIERTA' };
  entrenadoresDisponibles: string[] = [];
  usuariosDisponibles: string[] = [];
  editandoEntrenadoresId: number | null = null;
  seleccionEntrenadores: string[] = [];
  selectedClase: ClaseItem | null = null;
  entrandoClase = false;
  userIdByName: Record<string, number> = {};
  userNameById: Record<number, string> = {};
  solicitudesDetalle: Array<{ id: number; nombre: string }> = [];

  private subClases?: Subscription;

  constructor(
    private clasesService: ClasesService,
    public authService: AuthService,
    public router: Router,
    private notiService: NotificacionService
  ) {}

  ngOnInit(): void {
    this.isAdmin = this.authService.hasRole(['ADMIN']);
    this.isAdminOrTrainer = this.authService.hasRole(['ADMIN', 'ENTRENADOR']);

    this.subClases = this.clasesService.obtenerClases().subscribe(clases => {
      this.clases = clases;
      this.asegurarAdminMiembro();
      this.aplicarFiltros();
      this.actualizarMisClases();
    });

    this.cargarUsuariosReales();
  }

  ngOnDestroy(): void {
    this.subClases?.unsubscribe();
  }

  aplicarFiltros(): void {
    const texto = this.filtroTexto.toLowerCase().trim();

    this.filtradas = this.clases.filter(c => {
      const coincideTexto = !texto ||
        c.titulo.toLowerCase().includes(texto) ||
        c.entrenadores.some(e => e.toLowerCase().includes(texto));
      const coincideNivel = this.filtroNivel === 'todos' || c.nivel === this.filtroNivel;
      const coincideEstado = this.filtroEstado === 'todos' || c.estado === this.filtroEstado;
      return coincideTexto && coincideNivel && coincideEstado;
    });
  }

  capacidad(c: ClaseItem): string {
    return `${c.ocupados}/${c.cupo}`;
  }

  cupoDisponible(c: ClaseItem): number {
    return Math.max(c.cupo - c.ocupados, 0);
  }

  estadoClase(c: ClaseItem): string {
    if (c.estado === 'CANCELADA') return 'Cancelada';
    if (c.estado === 'LLENA') return 'Completa';
    return 'Abierta';
  }

  get totalDisponibles(): number {
    return this.filtradas.reduce((a, c) => a + this.cupoDisponible(c), 0);
  }

  get promedioCupo(): number {
    if (!this.filtradas.length) {
      return 0;
    }
    const totalCupo = this.filtradas.reduce((a, c) => a + c.cupo, 0);
    return totalCupo / this.filtradas.length;
  }

  solicitarUnirse(clase: ClaseItem): void {
    if (clase.estado !== 'ABIERTA' || this.esMiembro(clase)) {
      return;
    }
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login'], { queryParams: { returnUrl: '/clases' } });
      return;
    }
    const nombre = this.nombreUsuario();
    this.clasesService.solicitarUnirseBackend(clase.id).subscribe({
      next: (res: any) => {
        this.solicitudesEnviadas.add(clase.id);
        const msg = typeof res === 'string' && res.trim().length ? res : `Solicitud enviada para ${clase.titulo}. Un entrenador revisara tu pedido.`;
        this.mensajeSolicitud = msg;
        this.notificarATrainers(clase, `${nombre} solicito unirse a ${clase.titulo}`);
        if (this.puedeGestionarSolicitudes(clase)) {
          this.cargarInvitacionesClase(clase);
        }
      },
      error: err => {
        const msg = err?.error;
        this.mensajeSolicitud = typeof msg === 'string' ? msg : 'No se pudo enviar la solicitud.';
      }
    });
  }

  comenzarCrearClase(): void {
    if (!this.isAdminOrTrainer) {
      this.router.navigate(['/login'], { queryParams: { returnUrl: '/clases' } });
      return;
    }
    this.creandoClase = true;
    const entrenadorActual = this.nombreUsuario();
    this.nuevaClase = { entrenadores: [entrenadorActual], nivel: 'Inicial', duracionMin: 45, cupo: 15, estado: 'ABIERTA' };
  }

  cancelarCrearClase(): void {
    this.creandoClase = false;
    this.nuevaClase = { entrenadores: [], nivel: 'Inicial', duracionMin: 45, cupo: 15, estado: 'ABIERTA' };
  }

  guardarClase(): void {
    if (!this.nuevaClase.titulo || !this.nuevaClase.descripcion) {
      return;
    }
    if (!this.isAdminOrTrainer) {
      this.router.navigate(['/login'], { queryParams: { returnUrl: '/clases' } });
      return;
    }
    const nueva: ClaseItem = {
      // id provisional, se reemplazara por el backend
      id: Date.now(),
      titulo: this.nuevaClase.titulo,
      descripcion: this.nuevaClase.descripcion,
      entrenadores: Array.from(new Set([this.nombreUsuario(), ...(this.nuevaClase.entrenadores || [])].filter(Boolean))),
      nivel: this.nuevaClase.nivel as ClaseItem['nivel'],
      duracionMin: Number(this.nuevaClase.duracionMin || 45),
      cupo: Number(this.nuevaClase.cupo || 15),
      ocupados: 0,
      horario: this.nuevaClase.horario || 'A coordinar',
      ubicacion: this.nuevaClase.ubicacion || 'A confirmar',
      estado: 'ABIERTA',
      proximaFecha: 'Pronto a confirmar',
      miembros: [],
      solicitudesPendientes: [],
      mensajes: []
    };
    this.mensajeSolicitud = '';
    this.clasesService.agregarClase(nueva).subscribe({
      next: () => {
        this.creandoClase = false;
        this.aplicarFiltros();
        this.actualizarMisClases();
        this.mensajeSolicitud = 'Clase creada correctamente.';
      },
      error: err => {
        const msg = err?.error ?? 'No se pudo crear la clase en el servidor.';
        this.mensajeSolicitud = typeof msg === 'string' ? msg : 'No se pudo crear la clase en el servidor.';
      }
    });
  }

  toggleEntrenadorSeleccion(nombre: string): void {
    const lista = this.nuevaClase.entrenadores || [];
    if (lista.includes(nombre)) {
      this.nuevaClase.entrenadores = lista.filter(t => t !== nombre);
    } else {
      this.nuevaClase.entrenadores = [...lista, nombre];
    }
  }

  iniciarAsignarEntrenadores(clase: ClaseItem): void {
    if (!this.isAdmin) return;
    this.editandoEntrenadoresId = clase.id;
    this.seleccionEntrenadores = [...clase.entrenadores];
  }

  cancelarAsignarEntrenadores(): void {
    this.editandoEntrenadoresId = null;
    this.seleccionEntrenadores = [];
  }

  guardarAsignacion(clase: ClaseItem): void {
    if (!this.isAdmin) return;
    this.clasesService.asignarEntrenadores(clase.id, this.seleccionEntrenadores);
    this.editandoEntrenadoresId = null;
    this.aplicarFiltros();
  }

  toggleEntrenadorAsignacion(nombre: string): void {
    if (!this.isAdmin) return;
    const lista = this.seleccionEntrenadores || [];
    if (lista.includes(nombre)) {
      this.seleccionEntrenadores = lista.filter(t => t !== nombre);
    } else {
      this.seleccionEntrenadores = [...lista, nombre];
    }
  }

  verDetalle(clase: ClaseItem): void {
    if (!this.puedeVerClase(clase)) {
      return;
    }
    this.selectedClase = clase;
    this.cargarInvitacionesClase(clase);
  }

  cerrarDetalle(): void {
    this.selectedClase = null;
    this.entrandoClase = false;
  }

  eliminarClase(clase: ClaseItem): void {
    if (!this.isAdmin) {
      return;
    }
    const confirmar = window.confirm(`Eliminar la clase "${clase.titulo}"?`);
    if (!confirmar) return;
    this.clasesService.eliminarClase(clase.id);
    this.aplicarFiltros();
    if (this.selectedClase?.id === clase.id) {
      this.selectedClase = null;
    }
    this.actualizarMisClases();
  }

  ocupacionPorcentaje(c: ClaseItem): number {
    if (c.cupo === 0) return 0;
    return Math.round((c.ocupados / c.cupo) * 100);
  }

  puedeUnirse(clase: ClaseItem): boolean {
    if (this.isAdmin) return true;
    const nombreUsuario = this.nombreUsuario();
    if (this.authService.hasRole(['ENTRENADOR'])) {
      return clase.entrenadores.includes(nombreUsuario) || this.esMiembro(clase);
    }
    return this.esMiembro(clase);
  }

  puedeEntrarDirecto(clase: ClaseItem): boolean {
    if (this.isAdmin) return true;
    const nombreUsuario = this.nombreUsuario();
    if (this.authService.hasRole(['ENTRENADOR'])) {
      return clase.entrenadores.includes(nombreUsuario) || this.esMiembro(clase);
    }
    return this.esMiembro(clase);
  }

  entrarClase(clase: ClaseItem): void {
    if (!this.puedeUnirse(clase)) {
      return;
    }
    this.entrandoClase = true;
    const esStaff = this.authService.hasRole(['ADMIN']) || this.authService.hasRole(['ENTRENADOR']);
    const rol: any = this.authService.hasRole(['ADMIN']) ? 'ADMIN' : this.authService.hasRole(['ENTRENADOR']) ? 'ENTRENADOR' : 'USER';
    this.clasesService.registrarIngreso(clase.id, esStaff, this.nombreUsuario(), rol, this.currentUserId());
    this.actualizarMisClases();
    this.selectedClase = null;
    this.router.navigate(['/clases', clase.id]);
  }

  private actualizarMisClases(): void {
    const nombre = this.nombreUsuario();
    const userId = this.currentUserId();
    this.misClases = this.clasesService.misClasesDelUsuario(userId, nombre);
    // Si ya es miembro, limpiamos cualquier bandera de solicitud previa
    this.misClases.forEach(c => this.solicitudesEnviadas.delete(c.id));
    this.misClasesEntrenador = this.clases.filter(c => c.entrenadores.includes(nombre));
  }

  private currentUserId(): number {
    return this.authService.currentUser?.id || 0;
  }

  nombreUsuario(): string {
    const usuario = this.authService.currentUser as any;
    return usuario?.nombreCompleto || usuario?.username || usuario?.nombre || 'Usuario';
  }

  esMiembro(clase: ClaseItem): boolean {
    const userId = this.currentUserId();
    const nombre = this.nombreUsuario();
    return clase.miembros.some(m => (userId ? m.id === userId : false) || m.nombre === nombre);
  }

  puedeVerClase(clase: ClaseItem): boolean {
    const nombre = this.nombreUsuario();
    if (this.isAdmin) return true;
    if (this.authService.hasRole(['ENTRENADOR'])) {
      return clase.entrenadores.includes(nombre) || this.esMiembro(clase);
    }
    return true;
  }

  puedeGestionarSolicitudes(clase: ClaseItem): boolean {
    if (this.isAdmin) return true;
    if (this.authService.hasRole(['ENTRENADOR'])) {
      return clase.entrenadores.includes(this.nombreUsuario());
    }
    return false;
  }

  aceptarSolicitudUI(clase: ClaseItem, nombre: string): void {
    if (!this.puedeGestionarSolicitudes(clase)) return;
    const inv = this.solicitudesDetalle.find(s => s.nombre === nombre);
    if (!inv) return;
    this.clasesService.responderInvitacion(inv.id, true).subscribe({
      next: () => {
        this.cargarInvitacionesClase(clase);
        this.clasesService.refrescarMiembros(clase.id).subscribe({
          next: () => this.actualizarMisClases(),
          error: () => this.actualizarMisClases()
        });
      },
      error: () => {
        this.mensajeSolicitud = 'No se pudo aceptar la solicitud.';
      }
    });
  }

  rechazarSolicitudUI(clase: ClaseItem, nombre: string): void {
    if (!this.puedeGestionarSolicitudes(clase)) return;
    const inv = this.solicitudesDetalle.find(s => s.nombre === nombre);
    if (!inv) return;
    this.clasesService.responderInvitacion(inv.id, false).subscribe({
      next: () => {
        this.cargarInvitacionesClase(clase);
        this.clasesService.refrescarMiembros(clase.id).subscribe({ next: () => {}, error: () => {} });
      },
      error: () => {
        this.mensajeSolicitud = 'No se pudo rechazar la solicitud.';
      }
    });
  }

  private asegurarAdminMiembro(): void {
    if (!this.isAdmin) return;
    const adminNombre = this.nombreUsuario();
    this.clases.forEach(c => {
      const ya = c.miembros.some(m => m.nombre === adminNombre);
      if (!ya) {
        this.clasesService.registrarIngreso(c.id, true, adminNombre, 'ADMIN');
      }
    });
  }

  private cargarUsuariosReales(): void {
    this.authService.getAllUsers().subscribe({
      next: usuarios => {
        const entrenadores = usuarios.filter(u => u.rol === 'ENTRENADOR').map(u => u.nombre);
        const clientes = usuarios.filter(u => u.rol !== 'ENTRENADOR').map(u => u.nombre);
        usuarios.forEach(u => {
          this.userIdByName[u.nombre] = u.id;
          this.userNameById[u.id] = u.nombre;
        });
        const setEntrenadores = new Set([...this.entrenadoresDisponibles, ...entrenadores]);
        this.entrenadoresDisponibles = Array.from(setEntrenadores).filter(Boolean);
        this.usuariosDisponibles = clientes;
      },
      error: () => {
        // mantener listas locales si falla
      }
    });
  }

  private cargarInvitacionesClase(clase: ClaseItem): void {
    this.clasesService.obtenerInvitaciones(clase.id).subscribe({
      next: invs => {
        const pendientes = Array.isArray(invs) ? invs.filter(i => i.estado === 'PENDIENTE') : [];
        this.solicitudesDetalle = pendientes.map((i: any) => ({
          id: i.idInvitacion,
          nombre: i.nombreUsuario || this.userNameById[i.idUsuario] || 'Usuario'
        }));
      },
      error: () => {
        this.solicitudesDetalle = [];
      }
    });
  }

  private notificarATrainers(clase: ClaseItem, mensaje: string): void {
    clase.entrenadores.forEach(nombre => {
      const id = this.userIdByName[nombre];
      if (id) {
        this.notiService.crear({ idUsuario: id, mensaje }).subscribe({ next: () => {}, error: () => {} });
      }
    });
  }
}
