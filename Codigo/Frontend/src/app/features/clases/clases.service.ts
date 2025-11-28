import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export type ClaseEstado = 'ABIERTA' | 'LLENA' | 'CANCELADA';

export interface MiembroClase {
  nombre: string;
  lastOnline: string;
  rol: 'USER' | 'ENTRENADOR' | 'ADMIN';
}

export interface MensajeClase {
  autor: string;
  texto: string;
  fecha: string;
}

export interface ClaseItem {
  id: number;
  titulo: string;
  descripcion: string;
  entrenadores: string[];
  nivel: 'Inicial' | 'Intermedio' | 'Avanzado';
  duracionMin: number;
  cupo: number;
  ocupados: number;
  horario: string;
  ubicacion: string;
  estado: ClaseEstado;
  etiqueta?: string;
  proximaFecha: string;
  miembros: MiembroClase[];
  solicitudesPendientes: string[];
  mensajes: MensajeClase[];
}

@Injectable({
  providedIn: 'root'
})
export class ClasesService {
  private clases: ClaseItem[] = [
    {
      id: 1,
      titulo: 'HIIT explosivo',
      descripcion: 'Circuitos cortos, alta intensidad y control de tecnica.',
      entrenadores: [],
      nivel: 'Intermedio',
      duracionMin: 45,
      cupo: 10,
      ocupados: 0,
      horario: 'Lunes y Miercoles 19:00',
      ubicacion: 'Sala Functional',
      estado: 'ABIERTA',
      etiqueta: 'Quedan lugares',
      proximaFecha: 'Lunes 27 Nov - 19:00',
      miembros: [],
      solicitudesPendientes: [],
      mensajes: []
    },
    {
      id: 2,
      titulo: 'Powerlifting',
      descripcion: 'Tecnica y progresion en sentadilla, banca y peso muerto.',
      entrenadores: [],
      nivel: 'Avanzado',
      duracionMin: 60,
      cupo: 8,
      ocupados: 0,
      horario: 'Martes y Jueves 20:00',
      ubicacion: 'Sala Pesas',
      estado: 'ABIERTA',
      etiqueta: 'Lista de espera',
      proximaFecha: 'Martes 28 Nov - 20:00',
      miembros: [],
      solicitudesPendientes: [],
      mensajes: []
    },
    {
      id: 3,
      titulo: 'Movilidad y core',
      descripcion: 'Trabajo de estabilidad, movilidad y fuerza abdominal.',
      entrenadores: [],
      nivel: 'Inicial',
      duracionMin: 50,
      cupo: 12,
      ocupados: 0,
      horario: 'Sabado 10:00',
      ubicacion: 'Sala Yoga',
      estado: 'ABIERTA',
      etiqueta: 'Ideal principiantes',
      proximaFecha: 'Sabado 30 Nov - 10:00',
      miembros: [],
      solicitudesPendientes: [],
      mensajes: []
    },
    {
      id: 4,
      titulo: 'Cycling nocturno',
      descripcion: 'Sesiones por intervalos con musica y medidor de potencia.',
      entrenadores: [],
      nivel: 'Intermedio',
      duracionMin: 40,
      cupo: 14,
      ocupados: 0,
      horario: 'Lunes a Jueves 21:00',
      ubicacion: 'Sala Bikes',
      estado: 'ABIERTA',
      etiqueta: 'Alta demanda',
      proximaFecha: 'Lunes 27 Nov - 21:00',
      miembros: [],
      solicitudesPendientes: [],
      mensajes: []
    },
    {
      id: 5,
      titulo: 'Cross entrenamiento',
      descripcion: 'WODs variados, tecnica de levantamientos y cardio.',
      entrenadores: [],
      nivel: 'Avanzado',
      duracionMin: 55,
      cupo: 10,
      ocupados: 0,
      horario: 'Martes, Jueves y Sabado 18:00',
      ubicacion: 'Box Exterior',
      estado: 'ABIERTA',
      etiqueta: 'Clima permisivo',
      proximaFecha: 'Martes 28 Nov - 18:00',
      miembros: [],
      solicitudesPendientes: [],
      mensajes: []
    }
  ];

  private clasesSubject = new BehaviorSubject<ClaseItem[]>([...this.clases]);
  private joinedIds = new Set<number>();

  obtenerClases(): Observable<ClaseItem[]> {
    return this.clasesSubject.asObservable();
  }

  agregarClase(clase: ClaseItem): void {
    this.clases = [clase, ...this.clases];
    this.emit();
  }

  eliminarClase(id: number): void {
    this.clases = this.clases.filter(c => c.id !== id);
    this.joinedIds.delete(id);
    this.emit();
  }

  asignarEntrenadores(id: number, entrenadores: string[]): void {
    const idx = this.clases.findIndex(c => c.id === id);
    if (idx >= 0) {
      this.clases[idx] = { ...this.clases[idx], entrenadores: [...entrenadores] };
      this.emit();
    }
  }

  registrarIngreso(id: number, esStaff: boolean, nombre?: string, rol?: MiembroClase['rol']): void {
    this.joinedIds.add(id);
    const idx = this.clases.findIndex(c => c.id === id);
    if (idx >= 0) {
      const clase = this.clases[idx];
      const miembroNombre = nombre || 'Usuario';
      const miembroRol = rol || (esStaff ? 'ENTRENADOR' : 'USER');
      const yaMiembro = clase.miembros.some(m => m.nombre === miembroNombre);
      const miembros = yaMiembro ? clase.miembros : [...clase.miembros, { nombre: miembroNombre, rol: miembroRol, lastOnline: 'hace 1 min' }];
      let ocupados = clase.ocupados;
      if (!esStaff && ocupados < clase.cupo) {
        ocupados = ocupados + 1;
      }
      this.clases[idx] = { ...clase, miembros, ocupados };
      this.actualizarEstado(id);
      this.emit();
    }
  }

  agregarSolicitud(id: number, nombre: string): void {
    const idx = this.clases.findIndex(c => c.id === id);
    if (idx >= 0) {
      const set = new Set(this.clases[idx].solicitudesPendientes);
      set.add(nombre);
      this.clases[idx] = { ...this.clases[idx], solicitudesPendientes: [...set] };
      this.emit();
    }
  }

  aceptarSolicitud(id: number, nombre: string, rol: MiembroClase['rol'] = 'USER'): void {
    const idx = this.clases.findIndex(c => c.id === id);
    if (idx >= 0) {
      const clase = this.clases[idx];
      const solicitudes = clase.solicitudesPendientes.filter(s => s !== nombre);
      const miembros = clase.miembros.some(m => m.nombre === nombre) ? clase.miembros : [...clase.miembros, { nombre, lastOnline: 'hace 1 min', rol }];
      const ocupa = rol === 'ADMIN' || rol === 'ENTRENADOR' ? 0 : 1;
      const nuevoOcupados = Math.min(clase.cupo, clase.ocupados + ocupa);
      this.clases[idx] = { ...clase, solicitudesPendientes: solicitudes, miembros, ocupados: nuevoOcupados };
      this.actualizarEstado(id);
      this.emit();
    }
  }

  rechazarSolicitud(id: number, nombre: string): void {
    const idx = this.clases.findIndex(c => c.id === id);
    if (idx >= 0) {
      const clase = this.clases[idx];
      const solicitudes = clase.solicitudesPendientes.filter(s => s !== nombre);
      this.clases[idx] = { ...clase, solicitudesPendientes: solicitudes };
      this.emit();
    }
  }

  removerMiembro(id: number, nombre: string): void {
    const idx = this.clases.findIndex(c => c.id === id);
    if (idx >= 0) {
      const clase = this.clases[idx];
      const miembro = clase.miembros.find(m => m.nombre === nombre);
      const miembros = clase.miembros.filter(m => m.nombre !== nombre);
      const ocupaCupo = miembro ? miembro.rol === 'USER' : true;
      const ocupados = Math.max(0, clase.ocupados - (ocupaCupo ? 1 : 0));
      this.joinedIds.delete(id);
      this.clases[idx] = { ...clase, miembros, ocupados };
      this.actualizarEstado(id);
      this.emit();
    }
  }

  agregarMensaje(id: number, mensaje: MensajeClase): void {
    const idx = this.clases.findIndex(c => c.id === id);
    if (idx >= 0) {
      const clase = this.clases[idx];
      this.clases[idx] = { ...clase, mensajes: [...clase.mensajes, mensaje] };
      this.emit();
    }
  }

  misClases(): ClaseItem[] {
    return this.clases.filter(c => this.joinedIds.has(c.id));
  }

  buscarPorId(id: number): ClaseItem | undefined {
    return this.clases.find(c => c.id === id);
  }

  private actualizarEstado(id: number): void {
    const idx = this.clases.findIndex(c => c.id === id);
    if (idx >= 0) {
      const clase = this.clases[idx];
      const estado = clase.ocupados >= clase.cupo ? 'LLENA' : 'ABIERTA';
      this.clases[idx] = { ...clase, estado };
    }
  }

  private emit(): void {
    this.clasesSubject.next([...this.clases]);
  }
}
