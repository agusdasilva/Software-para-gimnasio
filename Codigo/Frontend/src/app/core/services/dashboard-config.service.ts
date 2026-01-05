import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface DashboardConfig {
  noticias: string[];
  recordatorios: string[];
}

@Injectable({
  providedIn: 'root'
})
export class DashboardConfigService {
  private baseUrl = `${environment.apiBaseUrl}/dashboard-config`;

  constructor(private http: HttpClient) {}

  obtener(): Observable<DashboardConfig> {
    return this.http.get<DashboardConfig>(this.baseUrl);
  }

  actualizar(data: DashboardConfig): Observable<DashboardConfig> {
    return this.http.put<DashboardConfig>(this.baseUrl, data);
  }
}
