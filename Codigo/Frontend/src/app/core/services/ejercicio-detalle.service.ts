import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  EjercicioDetalleResponse,
  SerieResponse
} from './rutina.service';

export interface SerieRequest {
  carga: string;
  repeticiones: number;
  orden: number;
}

export interface EjercicioDetalleRequest {
  idEjercicio: number;
  idRutinaDetalle: number;
  orden: number;
  series: SerieRequest[];
}

@Injectable({
  providedIn: 'root'
})
export class EjercicioDetalleService {

  private baseUrl = 'http://localhost:8080/api/ejercicio-detalle';

  constructor(private http: HttpClient) {}

  crear(request: EjercicioDetalleRequest): Observable<EjercicioDetalleResponse> {
    return this.http.post<EjercicioDetalleResponse>(this.baseUrl, request);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  agregarSerie(idDetalle: number, serie: SerieRequest): Observable<EjercicioDetalleResponse> {
    return this.http.post<EjercicioDetalleResponse>(`${this.baseUrl}/${idDetalle}/series`, serie);
  }

  modificarSerie(idSerie: number, serie: SerieRequest): Observable<SerieResponse> {
    return this.http.put<SerieResponse>(`${this.baseUrl}/serie/${idSerie}`, serie);
  }

  eliminarSerie(idSerie: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/serie/${idSerie}`);
  }

  modificarOrden(idDetalle: number, nuevoOrden: number): Observable<EjercicioDetalleResponse> {
    return this.http.put<EjercicioDetalleResponse>(`${this.baseUrl}/${idDetalle}/orden/${nuevoOrden}`, {});
  }
}
