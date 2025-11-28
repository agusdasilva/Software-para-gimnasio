import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CrearRutinaRequest, RutinaResponse, RutinaService } from '../../../../core/services/rutina.service';
import { CatalogoEjercicio, EjercicioForm, Nivel, SerieForm } from '../../models/ejercicios.model';

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
  descansoSeg: number;
  imagen: string;
  esGlobal: boolean;
  ejercicios: EjercicioForm[];
}

@Component({
  selector: 'app-crear-rutina-page',
  templateUrl: './crear-rutina.page.html',
  styleUrls: ['./crear-rutina.page.css']
})
export class CrearRutinaPage implements OnInit {

  mensaje = '';
  error = '';
  cargando = false;

  rutina: RutinaForm = {
    titulo: 'Rutina personalizada',
    objetivo: 'Fuerza e hipertrofia',
    nivel: 'Intermedio',
    semanas: 6,
    frecuencia: 4,
    duracionMin: 60,
    descripcion: 'Estructura lista para editar. Ajusta ejercicios, series y peso.',
    estado: 'BORRADOR',
    descansoSeg: 90,
    imagen: '',
    esGlobal: false,
    ejercicios: [
      {
        nombre: 'Press banca',
        grupoMuscular: 'Pecho',
        equipamiento: 'Barra',
        nivel: 'Intermedio',
        notas: 'Controla el arco y el tempo.',
        series: [
          { orden: 1, repeticiones: 8, carga: '40 kg', descansoSeg: 90 },
          { orden: 2, repeticiones: 8, carga: '40 kg', descansoSeg: 90 },
          { orden: 3, repeticiones: 10, carga: '35 kg', descansoSeg: 90 }
        ]
      },
      {
        nombre: 'Remo con barra',
        grupoMuscular: 'Espalda',
        equipamiento: 'Barra',
        nivel: 'Intermedio',
        notas: 'Espalda neutra y codos pegados.',
        series: [
          { orden: 1, repeticiones: 10, carga: '35 kg', descansoSeg: 75 },
          { orden: 2, repeticiones: 10, carga: '35 kg', descansoSeg: 75 }
        ]
      }
    ]
  };

  constructor(private rutinaService: RutinaService, private router: Router) {}

  ngOnInit(): void {
    const state = (history.state || {}) as { nuevosEjercicios?: CatalogoEjercicio[] };
    if (state.nuevosEjercicios?.length) {
      this.insertarSeleccion(state.nuevosEjercicios);
    }
  }

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

    if (!this.rutina.descripcion.trim()) {
      this.error = 'Describe brevemente la rutina.';
      return;
    }

    if (!this.rutina.descansoSeg || this.rutina.descansoSeg <= 0) {
      this.error = 'Define el descanso entre ejercicios en segundos.';
      return;
    }

    let idCreador: number;
    try {
      idCreador = this.rutinaService.getCurrentUserId();
    } catch (e: any) {
      this.error = e?.message || 'No se pudo obtener el usuario autenticado.';
      return;
    }

    const payload: CrearRutinaRequest = {
      nombre: this.rutina.titulo.trim(),
      descripcion: this.rutina.descripcion.trim(),
      descanso_seg: this.rutina.descansoSeg,
      imagen: this.rutina.imagen || '',
      esGlobal: this.rutina.esGlobal,
      idCreador
    };

    this.cargando = true;
    this.rutinaService.crearRutina(payload).subscribe({
      next: (res: RutinaResponse) => {
        this.mensaje = 'Rutina "' + res.nombre + '" guardada en el backend.';
        this.cargando = false;
      },
      error: (err: unknown) => {
        const mensaje = (err as { error?: { message?: string } })?.error?.message;
        this.error = mensaje || 'No se pudo guardar la rutina.';
        this.cargando = false;
      }
    });
  }

  irASelector(): void {
    this.router.navigate(['/rutinas/crear/ejercicios'], {
      state: { preseleccion: this.rutina.ejercicios.map(e => e.nombre) }
    });
  }

  agregarEjercicioManual(): void {
    this.rutina.ejercicios.push({
      nombre: 'Nuevo ejercicio',
      notas: '',
      grupoMuscular: 'Fullbody',
      equipamiento: 'Peso corporal',
      nivel: 'Principiante',
      series: [this.crearSerie(1)]
    });
  }

  insertarSeleccion(lista: CatalogoEjercicio[]): void {
    lista.forEach(ej => {
      this.rutina.ejercicios.push({
        nombre: ej.nombre,
        notas: ej.descripcion || '',
        grupoMuscular: ej.grupoMuscular,
        equipamiento: ej.equipamiento,
        nivel: ej.nivel,
        series: [this.crearSerie(1)]
      });
    });
  }

  quitarEjercicio(index: number): void {
    this.rutina.ejercicios.splice(index, 1);
  }

  agregarSerie(indexEjercicio: number): void {
    const destino = this.rutina.ejercicios[indexEjercicio];
    if (!destino) return;
    const orden = (destino.series?.length || 0) + 1;
    destino.series = [...(destino.series || []), this.crearSerie(orden)];
  }

  quitarSerie(indexEjercicio: number, indexSerie: number): void {
    const destino = this.rutina.ejercicios[indexEjercicio];
    if (!destino || !destino.series?.length) return;
    destino.series.splice(indexSerie, 1);
    destino.series = destino.series.map((s, i) => ({ ...s, orden: i + 1 }));
  }

  private crearSerie(orden: number, repeticiones = 12, carga = '15 kg'): SerieForm {
    return {
      orden,
      repeticiones,
      carga,
      descansoSeg: this.rutina.descansoSeg
    };
  }

  trackByIndex(index: number): number {
    return index;
  }

  get totalEjercicios(): number {
    return this.rutina.ejercicios.length;
  }
}
