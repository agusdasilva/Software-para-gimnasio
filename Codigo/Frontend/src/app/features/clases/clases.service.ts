import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, catchError, map, throwError } from 'rxjs';
import { AuthService } from '../../core/auth/auth.service';

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
  private clases: ClaseItem[] = [];
  private baseUrl = 'http://localhost:8080/api/clases';

  private clasesSubject = new BehaviorSubject<ClaseItem[]>([...this.clases]);
  private joinedIds = new Set<number>();

  constructor(private http: HttpClient, private auth: AuthService) {
    this.cargarDesdeApi();
    this.limpiarClasesFantasma();
  }

  obtenerClases(): Observable<ClaseItem[]> {
    return this.clasesSubject.asObservable();
  }

  agregarClase(clase: ClaseItem): Observable<ClaseItem> {
    // Persistir en backend y luego reflejar en memoria
    const creadorId = this.auth.currentUser?.id;
    const payload = {
      titulo: clase.titulo,
      descripcion: clase.descripcion,
      cupo: clase.cupo,
      creadorId
    };
    return this.http.post<any>(this.baseUrl, payload).pipe(
      map(creada => {
        const nueva = this.mapBackendClase(creada);
        this.clases = [nueva, ...this.clases.filter(c => c.id !== nueva.id)];
        this.emit();
        return nueva;
      }),
      catchError(err => {
        return throwError(() => err);
      })
    );
  }

  eliminarClase(id: number): void {
    this.http.delete<void>(`${this.baseUrl}/${id}`).subscribe({
      next: () => {
        this.clases = this.clases.filter(c => c.id !== id);
        this.joinedIds.delete(id);
        this.emit();
      },
      error: () => {
        // Si falla el backend, al menos limpiamos local para no dejar basura
        this.clases = this.clases.filter(c => c.id !== id);
        this.joinedIds.delete(id);
        this.emit();
      }
    });
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
      this.joinedIds.add(id);
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

  misClasesDelUsuario(nombre: string): ClaseItem[] {
    if (!nombre) {
      return [];
    }
    return this.clases.filter(c => c.miembros.some(m => m.nombre === nombre));
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

  private cargarDesdeApi(): void {
    this.http.get<any[]>(this.baseUrl).subscribe({
      next: data => {
        const list = Array.isArray(data) ? data : [];
        this.clases = list.map(c => this.mapBackendClase(c));
        this.emit();
      },
      error: () => {
        // dejamos las clases en memoria si falla el backend
        this.emit();
      }
    });
  }

  private limpiarClasesFantasma(): void {
    const validas = this.clases.filter(c => c && typeof c.id === 'number' && !!c.titulo && !!c.descripcion);
    if (validas.length !== this.clases.length) {
      this.clases = validas;
      this.emit();
    }
  }

  private mapBackendClase(raw: any): ClaseItem {
    const currentId = this.auth.currentUser?.id;
    const nombreActual = this.auth.currentUser?.username || this.auth.currentUser?.email || '';
    const entrenadores = Array.isArray(raw?.entrenadores) ? raw.entrenadores.filter(Boolean) : [];
    if (raw?.creadorNombre) {
      entrenadores.push(raw.creadorNombre);
    }
    if (raw?.creadorId && raw.creadorId === currentId && nombreActual) {
      entrenadores.push(nombreActual);
    }
    const entrenadoresUnicos: string[] = Array.from(new Set(entrenadores)) as string[];
    return {
      id: raw?.id ?? Date.now(),
      titulo: raw?.titulo || 'Clase',
      descripcion: raw?.descripcion || '',
      entrenadores: entrenadoresUnicos,
      nivel: 'Inicial',
      duracionMin: 45,
      cupo: raw?.cupo ?? 10,
      ocupados: 0,
      horario: 'A coordinar',
      ubicacion: 'A confirmar',
      estado: 'ABIERTA',
      etiqueta: undefined,
      proximaFecha: 'Pronto a confirmar',
      miembros: [],
      solicitudesPendientes: [],
      mensajes: []
    };
  }
}
