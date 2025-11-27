import { Component, OnInit } from '@angular/core';
import { RutinaResumen } from '../../components/rutina-card/rutina-card.component';

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

  ngOnInit(): void {
    this.cargarMock();
    this.aplicarFiltros();
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
    this.mensaje = `Rutina ${id} lista para abrir detalle (mock).`;
  }

  arrancarRutina(id: number): void {
    this.mensaje = `Rutina ${id} marcada como iniciada (mock).`;
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
          { nombre: 'Push', foco: 'Pecho y triceps', detalle: 'Press banca 5x5 · Fondos · Aperturas' },
          { nombre: 'Pull', foco: 'Espalda y biceps', detalle: 'Dominadas 4x8 · Remo · Facepull' },
          { nombre: 'Legs', foco: 'Fuerza inferior', detalle: 'Sentadilla 5x5 · Peso muerto rumano' }
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
        proximaSesion: 'Mañana 07:30 - HIIT',
        tags: ['HIIT', 'Cardio', 'Circuitos'],
        actualizado: 'hace 5 dias',
        bloques: [
          { nombre: 'Fullbody A', foco: 'Resistencia', detalle: 'Circuito con kettlebell y bici' },
          { nombre: 'Core y estabilidad', foco: 'Estabilidad', detalle: 'Plancha · Pallof · Bird-dog' },
          { nombre: 'Cardio intervalos', foco: 'Aerobico', detalle: 'Remo 500m · Assault bike' }
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
          { nombre: 'Olympic', foco: 'Tecnica', detalle: 'Power clean · Jerk · Snatch pull' },
          { nombre: 'Pliometria', foco: 'Explosivo', detalle: 'Box jump · Bounding · Sprint' },
          { nombre: 'Estabilidad', foco: 'Core', detalle: 'Farmer walk · Anti-rotacion' }
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
          { nombre: 'Movilidad cadera', foco: 'Flexibilidad', detalle: '90/90 · Psoas stretch · Cossack' },
          { nombre: 'Espalda sana', foco: 'Estabilidad', detalle: 'Bird-dog · Cat-cow · Hip hinge' },
          { nombre: 'Cardio suave', foco: 'Base aerobica', detalle: 'Caminata inclinada 20min' }
        ]
      }
    ];
  }
}
