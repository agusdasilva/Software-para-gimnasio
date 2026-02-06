import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { RutinaResumen } from '../../components/rutina-card/rutina-card.component';
import { RutinaResponse, RutinaService } from '../../../../core/services/rutina.service';

type NivelFiltro = 'todos' | RutinaResumen['nivel'];
type EstadoFiltro = 'todos' | RutinaResumen['estado'];

@Component({
  selector: 'app-listar-rutinas-page',
  templateUrl: './listar-rutinas.page.html',
  styleUrls: ['./listar-rutinas.page.css']
})
export class ListarRutinasPage implements OnInit {

  rutinas: RutinaResumen[] = [];
  filtradas: RutinaResumen[] = [];

  filtroTexto = '';
  filtroNivel: NivelFiltro = 'todos';
  filtroEstado: EstadoFiltro = 'todos';

  mensaje = '';
  cargando = false;

  constructor(private router: Router, private rutinaService: RutinaService) {}

  ngOnInit(): void {
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

  private cargarRutinas(): void {
    this.cargando = true;
    this.mensaje = '';
    this.rutinaService.listarRutinas().subscribe({
      next: (res: RutinaResponse[]) => {
        this.rutinas = res.map(r => this.mapearRutina(r));
        this.aplicarFiltros();
        if (!this.rutinas.length) {
          this.mensaje = 'No hay rutinas guardadas todavía.';
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

  private mapearRutina(r: RutinaResponse): RutinaResumen {
    const ejercicios = r.detalle?.ejercicios || [];
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
      proximaSesion: primerEjercicio ? 'Siguiente: ' + primerEjercicio.ejercicio : 'Define tus sesiones',
      tags: [r.esGlobal ? 'Global' : 'Local', `Descanso ${r.detalle?.descanso_seg || 0}s`],
      bloques: ejercicios.map(ej => ({
        nombre: ej.ejercicio,
        foco: `Series ${ej.series?.length || 0}`,
        detalle: ej.series?.map(s => `${s.repeticiones} reps - ${s.carga}`).join(' | ') || 'Sin series'
      })),
      actualizado: 'Reciente'
    };
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
}
