import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Plan {
  id?: number;
  nombre: string;
  precio: number;
  periodo: string;
}

@Injectable({
  providedIn: 'root'
})
export class PlanService {
  private baseUrl = 'http://localhost:8080/api/planes';

  constructor(private http: HttpClient) {}

  listar(): Observable<Plan[]> {
    return this.http.get<Plan[]>(this.baseUrl);
  }

  crear(plan: Plan): Observable<Plan> {
    return this.http.post<Plan>(this.baseUrl, plan);
  }

  actualizar(plan: Plan): Observable<Plan> {
    if (!plan.id) {
      throw new Error('El plan a actualizar necesita un id');
    }
    return this.http.put<Plan>(`${this.baseUrl}/${plan.id}`, plan);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
