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
  creandoClase = false;
  nuevaClase: Partial<ClaseItem> = { entrenadores: [], nivel: 'Inicial', duracionMin: 45, cupo: 15, estado: 'ABIERTA' };
  entrenadoresDisponibles: string[] = [];
  usuariosDisponibles: string[] = [];
  editandoEntrenadoresId: number | null = null;
  seleccionEntrenadores: string[] = [];
  selectedClase: ClaseItem | null = null;
  entrandoClase = false;
  userIdByName: Record<string, number> = {};

  private subClases?: Subscription;

  constructor(
    private clasesService: ClasesService,
    public authService: AuthService,
    public router: Router,
    private notiService: NotificacionService
  ) {}

  ngOnInit(): void {
    const user = this.authService.currentUser;
    this.isAdminOrTrainer = !!user && user.roles?.some(r => r === 'ADMIN' || r === 'ENTRENADOR');

    this.subClases = this.clasesService.obtenerClases().subscribe(clases => {
      this.clases = clases;
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
    if (clase.estado !== 'ABIERTA') {
      return;
    }
    const nombre = this.nombreUsuario();
    this.clasesService.agregarSolicitud(clase.id, nombre);
    this.solicitudesEnviadas.add(clase.id);
    this.mensajeSolicitud = `Solicitud enviada para ${clase.titulo}. Un entrenador revisara tu pedido.`;
    this.notificarATrainers(clase, `${nombre} solicito unirse a ${clase.titulo}`);
  }

  comenzarCrearClase(): void {
    this.creandoClase = true;
    this.nuevaClase = { entrenadores: [], nivel: 'Inicial', duracionMin: 45, cupo: 15, estado: 'ABIERTA' };
  }

  cancelarCrearClase(): void {
    this.creandoClase = false;
    this.nuevaClase = { entrenadores: [], nivel: 'Inicial', duracionMin: 45, cupo: 15, estado: 'ABIERTA' };
  }

  guardarClase(): void {
    if (!this.nuevaClase.titulo || !this.nuevaClase.descripcion) {
      return;
    }
    const nuevoId = this.clases.length ? Math.max(...this.clases.map(c => c.id)) + 1 : 1;
    const nueva: ClaseItem = {
      id: nuevoId,
      titulo: this.nuevaClase.titulo,
      descripcion: this.nuevaClase.descripcion,
      entrenadores: [...(this.nuevaClase.entrenadores || [])],
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
    this.clasesService.agregarClase(nueva);
    this.creandoClase = false;
    this.aplicarFiltros();
    this.actualizarMisClases();
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
    this.editandoEntrenadoresId = clase.id;
    this.seleccionEntrenadores = [...clase.entrenadores];
  }

  cancelarAsignarEntrenadores(): void {
    this.editandoEntrenadoresId = null;
    this.seleccionEntrenadores = [];
  }

  guardarAsignacion(clase: ClaseItem): void {
    this.clasesService.asignarEntrenadores(clase.id, this.seleccionEntrenadores);
    this.editandoEntrenadoresId = null;
    this.aplicarFiltros();
  }

  toggleEntrenadorAsignacion(nombre: string): void {
    const lista = this.seleccionEntrenadores || [];
    if (lista.includes(nombre)) {
      this.seleccionEntrenadores = lista.filter(t => t !== nombre);
    } else {
      this.seleccionEntrenadores = [...lista, nombre];
    }
  }

  verDetalle(clase: ClaseItem): void {
    this.selectedClase = clase;
  }

  cerrarDetalle(): void {
    this.selectedClase = null;
    this.entrandoClase = false;
  }

  eliminarClase(clase: ClaseItem): void {
    if (!this.isAdminOrTrainer) {
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
    const nombreUsuario = this.nombreUsuario();
    if (this.authService.hasRole(['ADMIN'])) {
      return true;
    }
    if (this.authService.hasRole(['ENTRENADOR'])) {
      return clase.entrenadores.includes(nombreUsuario);
    }
    const esMiembro = clase.miembros.some(m => m.nombre === nombreUsuario);
    return esMiembro;
  }

  entrarClase(clase: ClaseItem): void {
    if (!this.puedeUnirse(clase)) {
      return;
    }
    this.entrandoClase = true;
    const esStaff = this.authService.hasRole(['ADMIN']) || this.authService.hasRole(['ENTRENADOR']);
    const rol: any = this.authService.hasRole(['ADMIN']) ? 'ADMIN' : this.authService.hasRole(['ENTRENADOR']) ? 'ENTRENADOR' : 'USER';
    this.clasesService.registrarIngreso(clase.id, esStaff, this.nombreUsuario(), rol);
    this.actualizarMisClases();
    this.selectedClase = null;
    this.router.navigate(['/clases', clase.id]);
  }

  private actualizarMisClases(): void {
    this.misClases = this.clasesService.misClases();
    const nombre = this.nombreUsuario();
    this.misClasesEntrenador = this.clases.filter(c => c.entrenadores.includes(nombre));
  }

  private nombreUsuario(): string {
    const usuario = this.authService.currentUser as any;
    return usuario?.nombreCompleto || usuario?.username || usuario?.nombre || 'Usuario';
  }

  private cargarUsuariosReales(): void {
    this.authService.getAllUsers().subscribe({
      next: usuarios => {
        const entrenadores = usuarios.filter(u => u.rol === 'ENTRENADOR').map(u => u.nombre);
        const clientes = usuarios.filter(u => u.rol !== 'ENTRENADOR').map(u => u.nombre);
        usuarios.forEach(u => this.userIdByName[u.nombre] = u.id);
        const setEntrenadores = new Set([...this.entrenadoresDisponibles, ...entrenadores]);
        this.entrenadoresDisponibles = Array.from(setEntrenadores).filter(Boolean);
        this.usuariosDisponibles = clientes;
      },
      error: () => {
        // mantener listas locales si falla
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
