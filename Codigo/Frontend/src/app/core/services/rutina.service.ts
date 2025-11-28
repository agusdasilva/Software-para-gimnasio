import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../auth/auth.service';

export interface EjercicioDetalleResponse {
  id: number;
  ejercicio: string;
  series: SerieResponse[];
}

export interface SerieResponse {
  id: number;
  carga: string;
  repeticiones: number;
  orden: number;
}

export interface RutinaDetalleResponse {
  id: number;
  rutina: string;
  descripcion: string;
  imagen: string;
  descanso_seg: number;
  ejercicios: EjercicioDetalleResponse[];
}

export interface RutinaResponse {
  id: number;
  nombre: string;
  creador: string;
  detalle: RutinaDetalleResponse;
  esGlobal: boolean;
}

export interface CrearRutinaRequest {
  nombre: string;
  idCreador: number;
  descripcion: string;
  imagen?: string;
  descanso_seg: number;
  esGlobal: boolean;
}

export interface ModificarRutinaDetalleRequest {
  descripcion?: string;
  imagen?: string;
  descanso_seg?: number;
}

export interface ActualizarRutinaRequest {
  nombre?: string;
  descripcion?: string;
  imagen?: string;
  descanso_seg?: number;
}

@Injectable({
  providedIn: 'root'
})
export class RutinaService {

  private baseUrl = 'http://localhost:8080/api/rutina';

  constructor(private http: HttpClient, private authService: AuthService) { }

  crearRutina(data: CrearRutinaRequest): Observable<RutinaResponse> {
    return this.http.post<RutinaResponse>(this.baseUrl, data);
  }

  listarRutinas(): Observable<RutinaResponse[]> {
    return this.http.get<RutinaResponse[]>(this.baseUrl);
  }

  buscarPorId(id: number): Observable<RutinaResponse> {
    return this.http.get<RutinaResponse>(`${this.baseUrl}/id/${id}`);
  }

  buscarPorNombre(nombre: string): Observable<RutinaResponse> {
    return this.http.get<RutinaResponse>(`${this.baseUrl}/nombre/${nombre}`);
  }

  modificarDescanso(idRutina: number, nuevoDescanso: number): Observable<RutinaResponse> {
    return this.http.put<RutinaResponse>(`${this.baseUrl}/${idRutina}/modificar-descanso/${nuevoDescanso}`, {});
  }

  modificarDetalle(idRutina: number, data: ModificarRutinaDetalleRequest): Observable<RutinaResponse> {
    return this.http.put<RutinaResponse>(`${this.baseUrl}/${idRutina}/detalle`, data);
  }

  actualizarRutina(idRutina: number, data: ActualizarRutinaRequest): Observable<RutinaResponse> {
    return this.http.put<RutinaResponse>(`${this.baseUrl}/${idRutina}`, data);
  }

  eliminarRutina(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  getCurrentUserId(): number {
    const current = this.authService.currentUser;
    if (!current) {
      throw new Error('No hay usuario autenticado');
    }
    return current.id;
  }
}
