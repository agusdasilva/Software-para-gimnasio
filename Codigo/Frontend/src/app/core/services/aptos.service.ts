import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export type AptoEstado = 'PENDIENTE' | 'APROBADO' | 'RECHAZADO' | 'CANCELADO';

export interface Apto {
  id: number;
  usuarioId: number;
  usuarioNombre: string;
  usuarioEmail: string;
  estado: AptoEstado;
  fechaSubida: string;
  fechaVencimiento?: string;
  nombreArchivo: string;
}

export interface AptoUploadRequest {
  nombreArchivo: string;
  base64: string;
}

export interface AptoAprobarRequest {
  fechaVencimiento: string;
}

@Injectable({
  providedIn: 'root'
})
export class AptosService {
  private baseUrl = `${environment.apiBaseUrl}/aptos`;

  constructor(private http: HttpClient) {}

  subir(data: AptoUploadRequest): Observable<Apto> {
    return this.http.post<Apto>(this.baseUrl, data);
  }

  misAptos(): Observable<Apto[]> {
    return this.http.get<Apto[]>(`${this.baseUrl}/mios`);
  }

  pendientes(): Observable<Apto[]> {
    return this.http.get<Apto[]>(`${this.baseUrl}/pendientes`);
  }

  aprobados(): Observable<Apto[]> {
    return this.http.get<Apto[]>(`${this.baseUrl}/aprobados`);
  }

  aprobar(id: number, fechaVencimiento: string): Observable<Apto> {
    const body: AptoAprobarRequest = { fechaVencimiento };
    return this.http.post<Apto>(`${this.baseUrl}/${id}/aprobar`, body);
  }

  rechazar(id: number): Observable<Apto> {
    return this.http.post<Apto>(`${this.baseUrl}/${id}/rechazar`, {});
  }

  cancelar(id: number): Observable<Apto> {
    return this.http.post<Apto>(`${this.baseUrl}/${id}/cancelar`, {});
  }
}
