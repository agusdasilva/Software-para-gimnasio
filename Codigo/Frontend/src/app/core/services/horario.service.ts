import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface HorarioDia {
  id?: number;
  dia: string;
  horaApertura: string;
  horaCierre: string;
}

@Injectable({
  providedIn: 'root'
})
export class HorarioService {
  private baseUrl = `${environment.apiBaseUrl}/horario`;

  constructor(private http: HttpClient) {}

  obtener(): Observable<HorarioDia[]> {
    return this.http.get<HorarioDia[]>(this.baseUrl);
  }

  actualizar(dias: HorarioDia[]): Observable<HorarioDia[]> {
    return this.http.put<HorarioDia[]>(this.baseUrl, { dias });
  }
}
