import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../auth/auth.service';

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

  private baseUrl = 'http://localhost:8080/api/membresias';

  constructor(private http: HttpClient, private authService: AuthService) { }

  getMembresiaActual(): Observable<MembresiaResponse> {
    const current = this.authService.currentUser;
    if (!current) {
      throw new Error('No hay usuario autenticado');
    }
    return this.http.get<MembresiaResponse>(`${this.baseUrl}/usuario/${current.id}/actual`);
  }
}
