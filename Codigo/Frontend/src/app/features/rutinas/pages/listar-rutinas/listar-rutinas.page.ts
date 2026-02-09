import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { RutinaResumen } from '../../components/rutina-card/rutina-card.component';
import { RutinaResponse, RutinaService } from '../../../../core/services/rutina.service';
import { AuthService } from '../../../../core/auth/auth.service';

type NivelFiltro = 'todos' | RutinaResumen['nivel'];
type EstadoFiltro = 'todos' | RutinaResumen['estado'];
type ModoVista = 'publicas' | 'mias';

@Component({
  selector: 'app-listar-rutinas-page',
  templateUrl: './listar-rutinas.page.html',
  styleUrls: ['./listar-rutinas.page.css']
})
export class ListarRutinasPage implements OnInit {

  rutinas: (RutinaResumen & { esGlobal: boolean })[] = [];
  filtradas: (RutinaResumen & { esGlobal: boolean })[] = [];
  modo: ModoVista = 'publicas';
  privadasGuardadas = new Map<number, number>();

  filtroTexto = '';
  filtroNivel: NivelFiltro = 'todos';
  filtroEstado: EstadoFiltro = 'todos';

  mensaje = '';
  cargando = false;

  constructor(private router: Router, private ruta: ActivatedRoute, private rutinaService: RutinaService, public authService: AuthService) {}

  ngOnInit(): void {
    this.modo = (this.ruta.snapshot.data['modo'] as ModoVista) || 'publicas';
    const stateMsg = history.state?.mensaje as string | undefined;
    if (stateMsg) {
      this.mensaje = stateMsg;
    }
    this.cargarGuardadas();
    this.cargarRutinas();
  }

  aplicarFiltros(): void {
    const termino = this.filtroTexto.toLowerCase().trim();
    this.filtradas = this.rutinas.filter(r => {
      const coincideTexto = !termino ||
        r.titulo.toLowerCase().includes(termino) ||
        r.objetivo.toLowerCase().includes(termino) ||
        r.entrenador.toLowerCase().includes(termino);
      const coincideNivel = this.filtroNivel === 'todos' || r.nivel === this.filtroNivel;
      const coincideEstado = this.filtroEstado === 'todos' || r.estado === this.filtroEstado;
      return coincideTexto && coincideNivel && coincideEstado;
    });
  }

  resetFiltros(): void {
    this.filtroTexto = '';
    this.filtroNivel = 'todos';
    this.filtroEstado = 'todos';
    this.aplicarFiltros();
  }

  verRutina(id: number): void {
    this.router.navigate(['/rutinas/detalle', id]);
  }

  arrancarRutina(id: number): void {
    this.router.navigate(['/rutinas/ejecutar', id]);
  }

  crearNueva(): void {
    if (this.modo === 'publicas') {
      this.router.navigate(['/rutinas/crear'], { state: { esGlobalForced: true } });
    } else {
      this.router.navigate(['/rutinas/crear']);
    }
  }

  puedeCrear(): boolean {
    if (this.modo === 'publicas') {
      return this.authService.hasRole(['ADMIN', 'ENTRENADOR']);
    }
    return this.authService.isAuthenticated();
  }

  get totalActivas(): number {
    return this.filtradas.filter(r => r.estado === 'ACTIVA').length;
  }

  get promedioAvance(): number {
    if (!this.filtradas.length) {
      return 0;
    }
    const total = this.filtradas.reduce((acc, r) => acc + r.avance, 0);
    return Math.round(total / this.filtradas.length);
  }

  get frecuenciaPromedio(): number {
    if (!this.filtradas.length) {
      return 0;
    }
    const total = this.filtradas.reduce((acc, r) => acc + r.frecuencia, 0);
    return Math.round((total / this.filtradas.length) * 10) / 10;
  }

  toggleGuardar(rutina: RutinaResumen): void {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login'], { queryParams: { returnUrl: '/rutinas/publicas' } });
      return;
    }
    if (this.estaGuardada(rutina.id)) {
      return;
    }
    this.rutinaService.suscribirRutina(rutina.id).subscribe({
      next: (res) => {
        this.privadasGuardadas.set(rutina.id, res.id);
        this.persistirGuardadas();
        if (this.modo === 'publicas') {
          this.router.navigate(['/rutinas/mias'], { state: { mensaje: 'Rutina guardada en tus rutinas' } });
        }
      },
      error: () => {
        this.mensaje = 'No se pudo guardar la rutina.';
      }
    });
  }

  estaGuardada(id: number): boolean {
    return this.privadasGuardadas.has(id);
  }

  private cargarRutinas(): void {
    this.cargando = true;
    this.mensaje = '';
    const fuente = this.modo === 'publicas'
      ? this.rutinaService.listarGlobales()
      : this.rutinaService.listarMias();
    fuente.subscribe({
      next: (res: RutinaResponse[]) => {
        const mapeadas = res.map(r => this.mapearRutina(r));
        this.rutinas = mapeadas;
        this.aplicarFiltros();
        if (!this.rutinas.length) {
          this.mensaje = this.modo === 'publicas'
            ? 'No hay rutinas públicas disponibles.'
            : 'No tienes rutinas aún. Crea una o guarda alguna pública.';
        }
        this.cargando = false;
      },
      error: () => {
        this.rutinas = [];
        this.aplicarFiltros();
        this.mensaje = 'No se pudieron cargar las rutinas.';
        this.cargando = false;
      }
    });
  }

  private mapearRutina(r: RutinaResponse): RutinaResumen & { esGlobal: boolean } {
    const ejercicios = (r.detalle?.ejercicios && r.detalle.ejercicios.length)
      ? r.detalle.ejercicios
      : this.leerEjerciciosLocal(r.id);
    const primerEjercicio = ejercicios[0];
    const progresoLocal = this.leerProgresoLocal(r.id);
    const nivelLocal = this.leerNivelLocal(r.id);
    const avanceCalc = progresoLocal.total
      ? Math.round((progresoLocal.completadas / progresoLocal.total) * 100)
      : 0;
    return {
      id: r.id,
      titulo: r.nombre,
      objetivo: r.detalle?.descripcion || 'Sin descripcion',
      nivel: nivelLocal || 'Intermedio',
      estado: 'ACTIVA',
      semanas: Math.max(1, ejercicios.length || 4),
      frecuencia: Math.max(1, Math.min(7, ejercicios.length || 3)),
      duracionMin: 60,
      calorias: undefined,
      avance: avanceCalc,
      entrenador: r.creador || 'Sin datos',
      proximaSesion: primerEjercicio ? primerEjercicio.ejercicio : 'Define tus sesiones',
      tags: [r.esGlobal ? 'Pública' : 'Privada', `Descanso ${r.detalle?.descanso_seg || 0}s`],
      bloques: ejercicios.map(ej => ({
        nombre: ej.ejercicio,
        foco: `Series ${ej.series?.length || 0}`,
        detalle: ej.series?.map(s => `${s.repeticiones} reps - ${s.carga}`).join(' | ') || 'Sin series'
      })),
      actualizado: 'Reciente',
      esGlobal: r.esGlobal
    };
  }

  private leerEjerciciosLocal(idRutina: number): RutinaResponse['detalle']['ejercicios'] {
    try {
      const raw = localStorage.getItem('rutina-ejercicios-' + idRutina);
      if (!raw) return [];
      const data = JSON.parse(raw) as RutinaResponse['detalle']['ejercicios'];
      return Array.isArray(data) ? data : [];
    } catch {
      return [];
    }
  }

  private leerNivelLocal(id: number): RutinaResumen['nivel'] | null {
    try {
      const raw = localStorage.getItem('rutina-form-' + id);
      if (!raw) return null;
      const data = JSON.parse(raw) as { nivel?: RutinaResumen['nivel'] };
      if (data?.nivel === 'Principiante' || data?.nivel === 'Intermedio' || data?.nivel === 'Avanzado') {
        return data.nivel;
      }
      return null;
    } catch {
      return null;
    }
  }

  private leerProgresoLocal(id: number): { completadas: number; total: number } {
    try {
      const raw = localStorage.getItem('rutina-progreso-' + id);
      if (!raw) return { completadas: 0, total: 0 };
      return JSON.parse(raw);
    } catch {
      return { completadas: 0, total: 0 };
    }
  }

  private cargarGuardadas(): void {
    try {
      const key = this.guardadasKey();
      if (!key) {
        this.privadasGuardadas = new Map();
        return;
      }
      const raw = localStorage.getItem(key);
      if (!raw) return;
      const entries = JSON.parse(raw) as Array<[number, number]>;
      this.privadasGuardadas = new Map(entries);
    } catch {
      this.privadasGuardadas = new Map();
    }
  }

  private persistirGuardadas(): void {
    try {
      const key = this.guardadasKey();
      if (!key) return;
      localStorage.setItem(key, JSON.stringify(Array.from(this.privadasGuardadas.entries())));
    } catch {
      // ignore
    }
  }

  private guardadasKey(): string | null {
    const userId = this.authService.currentUser?.id;
    if (!userId) return null;
    return `rutinas-guardadas-${userId}`;
  }

}

