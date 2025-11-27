import { Component } from '@angular/core';

interface EjercicioForm {
  nombre: string;
  series: number;
  repeticiones: string;
  descanso: string;
}

interface BloqueForm {
  nombre: string;
  foco: string;
  ejercicios: EjercicioForm[];
}

type Nivel = 'Principiante' | 'Intermedio' | 'Avanzado';
type Estado = 'ACTIVA' | 'BORRADOR';

interface RutinaForm {
  titulo: string;
  objetivo: string;
  nivel: Nivel;
  semanas: number;
  frecuencia: number;
  duracionMin: number;
  descripcion: string;
  estado: Estado;
  bloques: BloqueForm[];
}

@Component({
  selector: 'app-crear-rutina-page',
  templateUrl: './crear-rutina.page.html',
  styleUrls: ['./crear-rutina.page.css']
})
export class CrearRutinaPage {

  mensaje = '';
  error = '';

  rutina: RutinaForm = {
    titulo: 'Rutina personalizada',
    objetivo: 'Fuerza e hipertrofia',
    nivel: 'Intermedio',
    semanas: 6,
    frecuencia: 4,
    duracionMin: 60,
    descripcion: 'Estructura push/pull/legs + acondicionamiento. Ajusta antes de enviar al cliente.',
    estado: 'BORRADOR',
    bloques: [
      {
        nombre: 'Push',
        foco: 'Pecho/hombro/triceps',
        ejercicios: [
          { nombre: 'Press banca', series: 4, repeticiones: '6-8', descanso: '120s' },
          { nombre: 'Fondos', series: 3, repeticiones: '8-10', descanso: '90s' }
        ]
      },
      {
        nombre: 'Pull',
        foco: 'Espalda/biceps',
        ejercicios: [
          { nombre: 'Dominadas', series: 4, repeticiones: '6-10', descanso: '120s' },
          { nombre: 'Remo con barra', series: 3, repeticiones: '8-10', descanso: '90s' }
        ]
      }
    ]
  };

  guardar(): void {
    this.error = '';
    this.mensaje = '';

    if (!this.rutina.titulo.trim()) {
      this.error = 'Agrega un nombre para la rutina.';
      return;
    }

    if (!this.rutina.objetivo.trim()) {
      this.error = 'Define un objetivo concreto.';
      return;
    }

    this.mensaje = 'Rutina guardada localmente (mock). Lista para conectarla al back.';
  }

  agregarBloque(): void {
    this.rutina.bloques.push({
      nombre: 'Nuevo bloque',
      foco: 'Foco',
      ejercicios: [
        { nombre: 'Ejercicio base', series: 3, repeticiones: '10-12', descanso: '90s' }
      ]
    });
  }

  agregarEjercicio(index: number): void {
    this.rutina.bloques[index].ejercicios.push({
      nombre: 'Ejercicio extra',
      series: 3,
      repeticiones: '12-15',
      descanso: '60s'
    });
  }
}
