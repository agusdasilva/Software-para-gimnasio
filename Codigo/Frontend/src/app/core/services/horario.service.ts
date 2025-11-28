import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

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
  private baseUrl = 'http://localhost:8080/api/horario';

  constructor(private http: HttpClient) {}

  obtener(): Observable<HorarioDia[]> {
    return this.http.get<HorarioDia[]>(this.baseUrl);
  }

  actualizar(dias: HorarioDia[]): Observable<HorarioDia[]> {
    return this.http.put<HorarioDia[]>(this.baseUrl, { dias });
  }
}
