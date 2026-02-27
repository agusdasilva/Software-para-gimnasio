import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../auth/auth.service';
import { environment } from '../../../environments/environment';

export interface MembresiaResponse {
  id: number;
  idUsuario: number;
  nombreUsuario: string;
  idPlan: number;
  nombrePlan: string;
  estado: string;
  fechaInicio: string;
  fechaFin: string;
}

@Injectable({
  providedIn: 'root'
})
export class MembresiaService {

  private baseUrl = `${environment.apiBaseUrl}/membresias`;

  constructor(private http: HttpClient, private authService: AuthService) { }

  getMembresiaActual(): Observable<MembresiaResponse> {
    const current = this.authService.currentUser;
    if (!current) {
      throw new Error('No hay usuario autenticado');
    }
    return this.http.get<MembresiaResponse>(`${this.baseUrl}/usuario/${current.id}/actual`);
  }

  crear(idPlan: number): Observable<MembresiaResponse> {
    const current = this.authService.currentUser;
    if (!current) {
      throw new Error('No hay usuario autenticado');
    }
    return this.http.post<MembresiaResponse>(this.baseUrl, {
      idUsuario: current.id,
      idPlan
    });
  }

  /**
   * Cambia el estado de una membresía existente.
   * Estados disponibles en el backend: PENDIENTE, ACTIVA, VENCIDA.
   */
  cambiarEstado(idMembresia: number, estado: 'PENDIENTE' | 'ACTIVA' | 'VENCIDA'): Observable<void> {
    return this.http.patch<void>(`${this.baseUrl}/${idMembresia}/estado`, null, {
      params: { estado }
    });
  }

  cancelar(idMembresia: number): Observable<void> {
    return this.cambiarEstado(idMembresia, 'VENCIDA');
  }
}
