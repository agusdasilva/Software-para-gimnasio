import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface MercadoPagoPreferenceResponse {
  preferenceId: string;
  initPoint: string;
}

@Injectable({
  providedIn: 'root'
})
export class MercadoPagoService {

  private baseUrl = 'http://localhost:8080/api/mercadopago';

  constructor(private http: HttpClient) {}

  createPreference(planCode: string): Observable<MercadoPagoPreferenceResponse> {
    return this.http.post<MercadoPagoPreferenceResponse>(`${this.baseUrl}/preferencias`, {
      planCode
    });
  }
}
