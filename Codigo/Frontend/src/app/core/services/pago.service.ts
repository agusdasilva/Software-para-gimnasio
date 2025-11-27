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
export class PagoService {

  private baseUrl = 'http://localhost:8080/api/pagos';

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
