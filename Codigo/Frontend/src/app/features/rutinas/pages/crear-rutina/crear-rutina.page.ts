import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CrearRutinaRequest, RutinaResponse, RutinaService } from '../../../../core/services/rutina.service';
import { CatalogoEjercicio, EjercicioForm, Nivel, SerieForm } from '../../models/ejercicios.model';

interface RutinaForm {
  id?: number;
  titulo: string;
  objetivo: string;
  nivel: Nivel;
  semanas: number;
  frecuencia: number;
  duracionMin: number;
  descripcion: string;
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
    const state = (history.state || {}) as { nuevosEjercicios?: CatalogoEjercicio[]; rutinaBase?: RutinaResponse; rutinaId?: number };
    if (state.rutinaBase) {
      this.prefillDesdeDetalle(state.rutinaBase);
    }
    if (!this.rutina.id && state.rutinaId) {
      this.rutina.id = state.rutinaId;
    }
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

    if (this.rutina.id) {
      this.actualizarRutina();
    } else {
      this.crearRutina();
    }
  }

  irASelector(): void {
    this.router.navigate(['/rutinas/crear/ejercicios'], {
      state: {
        preseleccion: this.rutina.ejercicios.map(e => e.nombre),
        rutinaId: this.rutina.id
      }
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

  moverEjercicio(index: number, direction: 'up' | 'down'): void {
    const nuevoIndice = direction === 'up' ? index - 1 : index + 1;
    if (nuevoIndice < 0 || nuevoIndice >= this.rutina.ejercicios.length) {
      return;
    }
    const copia = [...this.rutina.ejercicios];
    const temp = copia[nuevoIndice];
    copia[nuevoIndice] = copia[index];
    copia[index] = temp;
    this.rutina.ejercicios = copia;
  }

  prefillDesdeDetalle(data: RutinaResponse): void {
    const ejercicios = data.detalle?.ejercicios || [];
    this.rutina = {
      id: data.id,
      titulo: data.nombre,
      objetivo: data.detalle?.descripcion || 'Objetivo no definido',
      nivel: 'Intermedio',
      semanas: Math.max(1, ejercicios.length || 4),
      frecuencia: Math.max(1, Math.min(7, ejercicios.length || 3)),
      duracionMin: 60,
      descripcion: data.detalle?.descripcion || '',
      descansoSeg: data.detalle?.descanso_seg || 90,
      imagen: data.detalle?.imagen || '',
      esGlobal: data.esGlobal,
      ejercicios: ejercicios.map(ej => ({
        nombre: ej.ejercicio,
        notas: '',
        grupoMuscular: undefined,
        equipamiento: undefined,
        nivel: 'Intermedio',
        series: (ej.series || []).map((s, idx) => ({
          orden: idx + 1,
          repeticiones: s.repeticiones,
          carga: s.carga,
          descansoSeg: data.detalle?.descanso_seg
        }))
      }))
    };
  }

  private crearSerie(orden: number, repeticiones = 12, carga = '15 kg'): SerieForm {
    return {
      orden,
      repeticiones,
      carga,
      descansoSeg: this.rutina.descansoSeg
    };
  }

  private crearRutina(): void {
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
        this.persistirEjerciciosLocal(res.id);
        this.cargando = false;
        this.router.navigate(['/rutinas'], { state: { mensaje: this.mensaje } });
      },
      error: (err: unknown) => {
        const mensaje = (err as { error?: { message?: string } })?.error?.message;
        this.error = mensaje || 'No se pudo guardar la rutina.';
        this.cargando = false;
      }
    });
  }

  private actualizarRutina(): void {
    if (!this.rutina.id) return;
    this.cargando = true;
    this.rutinaService.modificarDetalle(this.rutina.id, {
      descripcion: this.rutina.descripcion.trim(),
      imagen: this.rutina.imagen || '',
      descanso_seg: this.rutina.descansoSeg
    }).subscribe({
      next: (res: RutinaResponse) => {
        this.mensaje = 'Rutina "' + res.nombre + '" actualizada.';
        this.persistirEjerciciosLocal(this.rutina!.id!);
        this.cargando = false;
        this.router.navigate(['/rutinas'], { state: { mensaje: this.mensaje } });
      },
      error: (err: unknown) => {
        const mensaje = (err as { error?: { message?: string } })?.error?.message;
        this.error = mensaje || 'No se pudo actualizar la rutina.';
        this.cargando = false;
      }
    });
  }

  trackByIndex(index: number): number {
    return index;
  }

  get totalEjercicios(): number {
    return this.rutina.ejercicios.length;
  }

  private persistirEjerciciosLocal(idRutina: number): void {
    if (!idRutina || !this.rutina.ejercicios.length) {
      return;
    }
    const ejercicios = this.rutina.ejercicios.map((ej, idx) => ({
      id: Date.now() + idx,
      ejercicio: ej.nombre,
      series: (ej.series || []).map((s, i) => ({
        id: Date.now() + idx + i,
        orden: s.orden || i + 1,
        repeticiones: s.repeticiones || 0,
        carga: s.carga || ''
      }))
    }));
    try {
      localStorage.setItem('rutina-ejercicios-' + idRutina, JSON.stringify(ejercicios));
    } catch {
      // ignore quota/storage errors
    }
  }
}
