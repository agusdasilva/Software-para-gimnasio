import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Notificacion {
  id: number;
  mensaje: string;
  leida: boolean;
  fecha: string;
}

@Injectable({
  providedIn: 'root'
})
export class NotificacionService {

  private baseUrl = 'http://localhost:8080/api/notificaciones';

  constructor(private http: HttpClient) { }

  listar(idUsuario: number): Observable<Notificacion[]> {
    return this.http.get<Notificacion[]>(`${this.baseUrl}/${idUsuario}`);
  }

  marcarLeida(id: number): Observable<string> {
    return this.http.patch(`${this.baseUrl}/${id}/leer`, null, { responseType: 'text' });
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
