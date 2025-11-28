import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { CatalogoEjercicio, Equipamiento, GrupoMuscular, Nivel } from '../../features/rutinas/models/ejercicios.model';

interface EjercicioResponse {
  id: number;
  nombre: string;
  grupo_muscular: string;
  equipamiento: string;
  es_global: boolean;
}

export interface CrearEjercicioRequest {
  nombre: string;
  grupo_muscular: string;
  equipamiento: string;
  es_global: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class EjercicioService {

  private baseUrl = 'http://localhost:8080/api/ejercicio';

  constructor(private http: HttpClient) {}

  crear(request: CrearEjercicioRequest): Observable<CatalogoEjercicio> {
    return this.http.post<EjercicioResponse>(this.baseUrl, request)
      .pipe(map(res => this.mapResponse(res)));
  }

  listar(): Observable<CatalogoEjercicio[]> {
    return this.http.get<EjercicioResponse[]>(this.baseUrl)
      .pipe(map(res => res.map(ej => this.mapResponse(ej))));
  }

  private mapResponse(res: EjercicioResponse): CatalogoEjercicio {
    return {
      id: res.id,
      nombre: res.nombre,
      grupoMuscular: res.grupo_muscular as GrupoMuscular,
      nivel: 'Intermedio' as Nivel,
      equipamiento: res.equipamiento as Equipamiento,
      descripcion: ''
    };
  }
}
