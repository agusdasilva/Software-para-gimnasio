import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { MembresiaResponse } from './membresia.service';

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

  confirmarPago(paymentId: string): Observable<MembresiaResponse> {
    return this.http.post<MembresiaResponse>(`${this.baseUrl}/mercadopago/confirmar`, null, {
      params: { paymentId }
    });
  }

  reconciliar(plan: string): Observable<MembresiaResponse> {
    return this.http.get<MembresiaResponse>(`${this.baseUrl}/mercadopago/reconciliar`, {
      params: { plan }
    });
  }
}
