import { Component } from '@angular/core';
import { CrearRutinaRequest, RutinaResponse, RutinaService } from '../../../../core/services/rutina.service';

type Nivel = 'Principiante' | 'Intermedio' | 'Avanzado';
type Estado = 'ACTIVA' | 'BORRADOR';
type GrupoMuscular = 'Pecho' | 'Espalda' | 'Piernas' | 'Hombros' | 'Brazos' | 'Core' | 'Fullbody' | 'Cardio';
type Equipamiento = 'Peso corporal' | 'Mancuernas' | 'Barra' | 'Maquina' | 'Bandas' | 'Kettlebell';

interface CatalogoEjercicio {
  id: number;
  nombre: string;
  grupoMuscular: GrupoMuscular;
  nivel: Nivel;
  equipamiento: Equipamiento;
  descripcion?: string;
}

interface EjercicioForm {
  nombre: string;
  series: number;
  repeticiones: string;
  descanso: string;
  grupoMuscular?: GrupoMuscular;
  equipamiento?: Equipamiento;
  nivel?: Nivel;
}

interface BloqueForm {
  nombre: string;
  foco: string;
  ejercicios: EjercicioForm[];
}

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
  cargando = false;
  bloqueDestino = 0;

  filtroNombre = '';
  filtroGrupo: 'todos' | GrupoMuscular = 'todos';
  filtroNivel: 'todos' | Nivel = 'todos';
  filtroEquipamiento: 'todos' | Equipamiento = 'todos';

  catalogoEjercicios: CatalogoEjercicio[] = [
    { id: 1, nombre: 'Press banca', grupoMuscular: 'Pecho', nivel: 'Intermedio', equipamiento: 'Barra', descripcion: 'Press en banco plano con barra' },
    { id: 2, nombre: 'Fondos en paralelas', grupoMuscular: 'Pecho', nivel: 'Intermedio', equipamiento: 'Peso corporal', descripcion: 'Trabajo de pecho y triceps' },
    { id: 3, nombre: 'Dominadas pronas', grupoMuscular: 'Espalda', nivel: 'Intermedio', equipamiento: 'Peso corporal', descripcion: 'Dominadas agarre prono' },
    { id: 4, nombre: 'Remo con barra', grupoMuscular: 'Espalda', nivel: 'Intermedio', equipamiento: 'Barra', descripcion: 'Remo inclinado' },
    { id: 5, nombre: 'Sentadilla trasera', grupoMuscular: 'Piernas', nivel: 'Intermedio', equipamiento: 'Barra', descripcion: 'Sentadilla con barra' },
    { id: 6, nombre: 'Zancadas caminando', grupoMuscular: 'Piernas', nivel: 'Principiante', equipamiento: 'Mancuernas', descripcion: 'Lunges alternos' },
    { id: 7, nombre: 'Press militar', grupoMuscular: 'Hombros', nivel: 'Intermedio', equipamiento: 'Barra', descripcion: 'Press de hombro estricto' },
    { id: 8, nombre: 'Elevaciones laterales', grupoMuscular: 'Hombros', nivel: 'Principiante', equipamiento: 'Mancuernas', descripcion: 'Aislamiento de deltoide medio' },
    { id: 9, nombre: 'Curl biceps', grupoMuscular: 'Brazos', nivel: 'Principiante', equipamiento: 'Mancuernas', descripcion: 'Curl alterno' },
    { id: 10, nombre: 'Triceps en polea', grupoMuscular: 'Brazos', nivel: 'Principiante', equipamiento: 'Maquina', descripcion: 'Extension en polea' },
    { id: 11, nombre: 'Plancha', grupoMuscular: 'Core', nivel: 'Principiante', equipamiento: 'Peso corporal', descripcion: 'Isometrico' },
    { id: 12, nombre: 'Mountain climbers', grupoMuscular: 'Cardio', nivel: 'Principiante', equipamiento: 'Peso corporal', descripcion: 'Cardio + core' },
    { id: 13, nombre: 'Kettlebell swing', grupoMuscular: 'Fullbody', nivel: 'Intermedio', equipamiento: 'Kettlebell', descripcion: 'Potencia y acondicionamiento' },
    { id: 14, nombre: 'Peso muerto rumano', grupoMuscular: 'Piernas', nivel: 'Intermedio', equipamiento: 'Barra', descripcion: 'Femoral y gluteo' },
    { id: 15, nombre: 'Face pull', grupoMuscular: 'Hombros', nivel: 'Principiante', equipamiento: 'Bandas', descripcion: 'Estabilidad escapular' }
  ];
  ejerciciosFiltrados: CatalogoEjercicio[] = [];

  rutina: RutinaForm = {
    titulo: 'Rutina personalizada',
    objetivo: 'Fuerza e hipertrofia',
    nivel: 'Intermedio',
    semanas: 6,
    frecuencia: 4,
    duracionMin: 60,
    descripcion: 'Estructura push/pull/legs + acondicionamiento. Ajusta antes de enviar al cliente.',
    estado: 'BORRADOR',
    descansoSeg: 90,
    imagen: '',
    esGlobal: false,
    bloques: [
      {
        nombre: 'Push',
        foco: 'Pecho/hombro/triceps',
        ejercicios: [
          { nombre: 'Press banca', series: 4, repeticiones: '6-8', descanso: '120s', grupoMuscular: 'Pecho', equipamiento: 'Barra', nivel: 'Intermedio' },
          { nombre: 'Fondos en paralelas', series: 3, repeticiones: '8-10', descanso: '90s', grupoMuscular: 'Pecho', equipamiento: 'Peso corporal', nivel: 'Intermedio' }
        ]
      },
      {
        nombre: 'Pull',
        foco: 'Espalda/biceps',
        ejercicios: [
          { nombre: 'Dominadas pronas', series: 4, repeticiones: '6-10', descanso: '120s', grupoMuscular: 'Espalda', equipamiento: 'Peso corporal', nivel: 'Intermedio' },
          { nombre: 'Remo con barra', series: 3, repeticiones: '8-10', descanso: '90s', grupoMuscular: 'Espalda', equipamiento: 'Barra', nivel: 'Intermedio' }
        ]
      }
    ]
  };

  constructor(private rutinaService: RutinaService) {
    this.ejerciciosFiltrados = [...this.catalogoEjercicios];
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

  agregarBloque(): void {
    this.rutina.bloques.push({
      nombre: 'Nuevo bloque',
      foco: 'Foco',
      ejercicios: [
        { nombre: 'Ejercicio base', series: 3, repeticiones: '10-12', descanso: '90s' }
      ]
    });
    this.bloqueDestino = this.rutina.bloques.length - 1;
  }

  agregarEjercicio(index: number): void {
    this.rutina.bloques[index].ejercicios.push({
      nombre: 'Ejercicio extra',
      series: 3,
      repeticiones: '12-15',
      descanso: '60s'
    });
  }

  filtrarCatalogo(): void {
    const nombre = this.filtroNombre.toLowerCase().trim();
    this.ejerciciosFiltrados = this.catalogoEjercicios.filter(ej => {
      const coincideNombre = !nombre || ej.nombre.toLowerCase().includes(nombre);
      const coincideGrupo = this.filtroGrupo === 'todos' || ej.grupoMuscular === this.filtroGrupo;
      const coincideNivel = this.filtroNivel === 'todos' || ej.nivel === this.filtroNivel;
      const coincideEq = this.filtroEquipamiento === 'todos' || ej.equipamiento === this.filtroEquipamiento;
      return coincideNombre && coincideGrupo && coincideNivel && coincideEq;
    });
  }

  agregarDesdeCatalogo(ej: CatalogoEjercicio): void {
    const destino = this.rutina.bloques[this.bloqueDestino] || this.rutina.bloques[0];
    if (!destino) {
      this.error = 'Crea un bloque antes de agregar ejercicios.';
      return;
    }
    destino.ejercicios.push({
      nombre: ej.nombre,
      series: 3,
      repeticiones: '10-12',
      descanso: (this.rutina.descansoSeg || 90) + 's',
      grupoMuscular: ej.grupoMuscular,
      equipamiento: ej.equipamiento,
      nivel: ej.nivel
    });
  }

  quitarEjercicio(indexBloque: number, indexEjercicio: number): void {
    this.rutina.bloques[indexBloque].ejercicios.splice(indexEjercicio, 1);
  }
}
