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
export class PagoService {

  private baseUrl = `${environment.apiBaseUrl}/pagos`;

  constructor(private http: HttpClient) {}

  crearPreferencia(plan: string): Observable<MercadoPagoPreferenceResponse> {
    return this.http.post<MercadoPagoPreferenceResponse>(`${this.baseUrl}/mercadopago/preferencia`, null, {
      params: { plan }
    });
  }

  confirmarPago(paymentId: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/mercadopago/confirmar`, null, {
      params: { paymentId }
    });
  }
}
