import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface MercadoPagoPreferenceResponse {
  preferenceId: string;
  initPoint: string;
}

@Injectable({
  providedIn: 'root'
})
export class MercadoPagoService {

  private baseUrl = `${environment.apiBaseUrl}/mercadopago`;

  constructor(private http: HttpClient) {}

  createPreference(planCode: string): Observable<MercadoPagoPreferenceResponse> {
    return this.http.post<MercadoPagoPreferenceResponse>(`${this.baseUrl}/preferencias`, {
      planCode
    });
  }
}
