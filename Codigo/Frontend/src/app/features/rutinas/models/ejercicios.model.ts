export type Nivel = 'Principiante' | 'Intermedio' | 'Avanzado';
export type GrupoMuscular = 'Pecho' | 'Espalda' | 'Piernas' | 'Hombros' | 'Brazos' | 'Core' | 'Fullbody' | 'Cardio';
export type Equipamiento = 'Peso corporal' | 'Mancuernas' | 'Barra' | 'Maquina' | 'Bandas' | 'Kettlebell';

export interface CatalogoEjercicio {
  id: number;
  nombre: string;
  grupoMuscular: GrupoMuscular;
  nivel: Nivel;
  equipamiento: Equipamiento;
  descripcion?: string;
}

export interface SerieForm {
  orden: number;
  repeticiones: number;
  carga: string;
  descansoSeg?: number;
}

export interface EjercicioForm {
  nombre: string;
  notas?: string;
  grupoMuscular?: GrupoMuscular;
  equipamiento?: Equipamiento;
  nivel?: Nivel;
  series: SerieForm[];
}
