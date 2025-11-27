import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../core/auth/auth.service';

type ClaseEstado = 'ABIERTA' | 'LLENA' | 'CANCELADA';

interface ClaseItem {
  id: number;
  titulo: string;
  descripcion: string;
  entrenador: string;
  nivel: 'Inicial' | 'Intermedio' | 'Avanzado';
  duracionMin: number;
  cupo: number;
  ocupados: number;
  horario: string;
  ubicacion: string;
  estado: ClaseEstado;
  etiqueta?: string;
  proximaFecha: string;
}

@Component({
  selector: 'app-clases',
  templateUrl: './clases.component.html',
  styleUrls: ['./clases.component.css']
})
export class ClasesComponent implements OnInit {

  clases: ClaseItem[] = [];
  filtradas: ClaseItem[] = [];

  filtroTexto = '';
  filtroNivel: 'todos' | ClaseItem['nivel'] = 'todos';
  filtroEstado: 'todos' | ClaseEstado = 'todos';

  isAdminOrTrainer = false;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    const user = this.authService.currentUser;
    this.isAdminOrTrainer = !!user && user.roles?.some(r => r === 'ADMIN' || r === 'ENTRENADOR');

    // mock estatico; luego reemplazar por consumo HTTP
    this.clases = [
      {
        id: 1,
        titulo: 'HIIT explosivo',
        descripcion: 'Circuitos cortos, alta intensidad y control de tecnica.',
        entrenador: 'Camila Duarte',
        nivel: 'Intermedio',
        duracionMin: 45,
        cupo: 18,
        ocupados: 12,
        horario: 'Lunes y Miercoles 19:00',
        ubicacion: 'Sala Functional',
        estado: 'ABIERTA',
        etiqueta: 'Quedan lugares',
        proximaFecha: 'Lunes 27 Nov - 19:00'
      },
      {
        id: 2,
        titulo: 'Powerlifting',
        descripcion: 'Tecnica y progresion en sentadilla, banca y peso muerto.',
        entrenador: 'Juan Vega',
        nivel: 'Avanzado',
        duracionMin: 60,
        cupo: 12,
        ocupados: 12,
        horario: 'Martes y Jueves 20:00',
        ubicacion: 'Sala Pesas',
        estado: 'LLENA',
        etiqueta: 'Lista de espera',
        proximaFecha: 'Martes 28 Nov - 20:00'
      },
      {
        id: 3,
        titulo: 'Movilidad y core',
        descripcion: 'Trabajo de estabilidad, movilidad y fuerza abdominal.',
        entrenador: 'Lucia Prieto',
        nivel: 'Inicial',
        duracionMin: 50,
        cupo: 20,
        ocupados: 9,
        horario: 'Sabado 10:00',
        ubicacion: 'Sala Yoga',
        estado: 'ABIERTA',
        etiqueta: 'Ideal principiantes',
        proximaFecha: 'Sabado 30 Nov - 10:00'
      },
      {
        id: 4,
        titulo: 'Cycling nocturno',
        descripcion: 'Sesiones por intervalos con musica y medidor de potencia.',
        entrenador: 'Mario Torres',
        nivel: 'Intermedio',
        duracionMin: 40,
        cupo: 25,
        ocupados: 21,
        horario: 'Lunes a Jueves 21:00',
        ubicacion: 'Sala Bikes',
        estado: 'ABIERTA',
        etiqueta: 'Alta demanda',
        proximaFecha: 'Lunes 27 Nov - 21:00'
      },
      {
        id: 5,
        titulo: 'Cross entrenamiento',
        descripcion: 'WODs variados, tecnica de levantamientos y cardio.',
        entrenador: 'Sofia Ledesma',
        nivel: 'Avanzado',
        duracionMin: 55,
        cupo: 16,
        ocupados: 7,
        horario: 'Martes, Jueves y Sabado 18:00',
        ubicacion: 'Box Exterior',
        estado: 'ABIERTA',
        etiqueta: 'Clima permisivo',
        proximaFecha: 'Martes 28 Nov - 18:00'
      }
    ];

    this.aplicarFiltros();
  }

  aplicarFiltros(): void {
    const texto = this.filtroTexto.toLowerCase().trim();

    this.filtradas = this.clases.filter(c => {
      const coincideTexto = !texto || c.titulo.toLowerCase().includes(texto) || c.entrenador.toLowerCase().includes(texto);
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
}
