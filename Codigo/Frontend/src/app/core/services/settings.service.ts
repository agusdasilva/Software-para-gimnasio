import { Injectable } from '@angular/core';

const AUTOPAY_KEY = 'auto_pay_enabled';

@Injectable({
  providedIn: 'root'
})
export class SettingsService {

  getAutoPay(): boolean {
    return localStorage.getItem(AUTOPAY_KEY) === 'true';
  }

  setAutoPay(enabled: boolean): void {
    localStorage.setItem(AUTOPAY_KEY, enabled ? 'true' : 'false');
  }
}
