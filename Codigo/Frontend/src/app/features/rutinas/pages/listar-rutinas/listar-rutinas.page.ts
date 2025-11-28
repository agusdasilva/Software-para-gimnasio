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
          this.mensaje = 'No hay rutinas guardadas aun. Usa las precargadas de ejemplo.';
          this.cargarMock();
          this.aplicarFiltros();
        }
        this.cargando = false;
      },
      error: () => {
        this.cargarMock();
        this.aplicarFiltros();
        this.mensaje = 'Mostrando rutinas pre cargadas. No se pudieron cargar las guardadas.';
        this.cargando = false;
      }
    });
  }

  private cargarMock(): void {
    this.rutinas = [
      {
        id: 1,
        titulo: 'Fuerza base 6 semanas',
        objetivo: 'Hipertrofia y control tecnico',
        nivel: 'Intermedio',
        estado: 'ACTIVA',
        semanas: 6,
        frecuencia: 4,
        duracionMin: 60,
        calorias: 480,
        avance: 62,
        entrenador: 'Agus Roldan',
        proximaSesion: 'Hoy 19:00 - Push',
        tags: ['Barra', 'Progresion 5x5', 'Core'],
        actualizado: 'hace 2 dias',
        bloques: [
          { nombre: 'Push', foco: 'Pecho y triceps', detalle: 'Press banca 5x5 - Fondos - Aperturas' },
          { nombre: 'Pull', foco: 'Espalda y biceps', detalle: 'Dominadas 4x8 - Remo - Facepull' },
          { nombre: 'Legs', foco: 'Fuerza inferior', detalle: 'Sentadilla 5x5 - Peso muerto rumano' }
        ]
      },
      {
        id: 2,
        titulo: 'Definicion express',
        objetivo: 'Quema grasa manteniendo musculo',
        nivel: 'Principiante',
        estado: 'BORRADOR',
        semanas: 4,
        frecuencia: 5,
        duracionMin: 45,
        calorias: 380,
        avance: 18,
        entrenador: 'Carla Torres',
        proximaSesion: 'Manana 07:30 - HIIT',
        tags: ['HIIT', 'Cardio', 'Circuitos'],
        actualizado: 'hace 5 dias',
        bloques: [
          { nombre: 'Fullbody A', foco: 'Resistencia', detalle: 'Circuito con kettlebell y bici' },
          { nombre: 'Core y estabilidad', foco: 'Estabilidad', detalle: 'Plancha - Pallof - Bird-dog' },
          { nombre: 'Cardio intervalos', foco: 'Aerobico', detalle: 'Remo 500m - Assault bike' }
        ]
      },
      {
        id: 3,
        titulo: 'Power para atletas',
        objetivo: 'Potencia y velocidad',
        nivel: 'Avanzado',
        estado: 'ACTIVA',
        semanas: 8,
        frecuencia: 3,
        duracionMin: 75,
        calorias: 520,
        avance: 41,
        entrenador: 'Juan Vega',
        proximaSesion: 'Jueves 18:30 - Lower',
        tags: ['Olimpicos', 'Pliometria', 'Velocidad'],
        actualizado: 'ayer',
        bloques: [
          { nombre: 'Olympic', foco: 'Tecnica', detalle: 'Power clean - Jerk - Snatch pull' },
          { nombre: 'Pliometria', foco: 'Explosivo', detalle: 'Box jump - Bounding - Sprint' },
          { nombre: 'Estabilidad', foco: 'Core', detalle: 'Farmer walk - Anti-rotacion' }
        ]
      },
      {
        id: 4,
        titulo: 'Movimiento sano',
        objetivo: 'Movilidad y fuerza ligera',
        nivel: 'Principiante',
        estado: 'PAUSADA',
        semanas: 5,
        frecuencia: 3,
        duracionMin: 40,
        avance: 80,
        entrenador: 'Lucia Prieto',
        proximaSesion: 'Reanudar cuando decidas',
        tags: ['Movilidad', 'Bandas', 'Recuperacion'],
        actualizado: 'hace 3 semanas',
        bloques: [
          { nombre: 'Movilidad cadera', foco: 'Flexibilidad', detalle: '90/90 - Psoas stretch - Cossack' },
          { nombre: 'Espalda sana', foco: 'Estabilidad', detalle: 'Bird-dog - Cat-cow - Hip hinge' },
          { nombre: 'Cardio suave', foco: 'Base aerobica', detalle: 'Caminata inclinada 20min' }
        ]
      }
    ];
  }

  private mapearRutina(r: RutinaResponse): RutinaResumen {
    const ejercicios = r.detalle?.ejercicios || [];
    const primerEjercicio = ejercicios[0];
    return {
      id: r.id,
      titulo: r.nombre,
      objetivo: r.detalle?.descripcion || 'Sin descripcion',
      nivel: 'Intermedio',
      estado: ejercicios.length ? 'ACTIVA' : 'BORRADOR',
      semanas: Math.max(1, ejercicios.length || 4),
      frecuencia: Math.max(1, Math.min(7, ejercicios.length || 3)),
      duracionMin: 60,
      calorias: undefined,
      avance: 0,
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
}
