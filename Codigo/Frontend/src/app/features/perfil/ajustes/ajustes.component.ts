import { Component, OnInit } from '@angular/core';
import { ThemeService } from '../../../core/services/theme.service';
import { SettingsService } from '../../../core/services/settings.service';
import { AuthService } from '../../../core/auth/auth.service';
import { Router } from '@angular/router';

interface FaqItem {
  q: string;
  a: string;
  open?: boolean;
}

@Component({
  selector: 'app-ajustes',
  templateUrl: './ajustes.component.html',
  styleUrls: ['./ajustes.component.css']
})
export class AjustesComponent implements OnInit {

  isDarkTheme = false;
  autoPayEnabled = false;

  faqItems: FaqItem[] = [
    { q: '¿Cómo cambio mi plan?', a: 'Ve a Membresías, elige un plan y confirma el pago. El cambio se aplica al finalizar tu período actual.' },
    { q: '¿Puedo pausar mi membresía?', a: 'Por ahora no, pero puedes desactivar el pago automático y reactivar cuando quieras.' },
    { q: '¿Dónde veo mis recibos?', a: 'En Perfil > Membresías encontrarás cada pago con su comprobante.' }
  ];

  aboutText = 'Somos un equipo que combina entrenamiento de fuerza, clases grupales y planes personalizados. Queremos que tengas todo en un solo lugar.';
  legalText = 'Al usar la app aceptas nuestros términos, la política de privacidad y las reglas del gimnasio. Consulta recepción para la versión firmada.';
  supportEmail = 'soporte@gym.com';
  supportPhone = '5492236689337';

  constructor(
    private themeService: ThemeService,
    private settingsService: SettingsService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.isDarkTheme = this.themeService.theme === 'dark';
    this.autoPayEnabled = this.settingsService.getAutoPay();
  }

  toggleTheme(event: Event): void {
    const value = (event.target as HTMLInputElement | null)?.checked ?? false;
    this.isDarkTheme = value;
    this.themeService.setTheme(value ? 'dark' : 'light');
  }

  toggleAutoPay(event: Event): void {
    const value = (event.target as HTMLInputElement | null)?.checked ?? false;
    this.autoPayEnabled = value;
    this.settingsService.setAutoPay(value);
  }

  toggleFaq(item: FaqItem): void {
    item.open = !item.open;
  }

  openSupport(): void {
    const message = encodeURIComponent('Hola, necesito ayuda con mi cuenta.');
    const url = `https://wa.me/${this.supportPhone}?text=${message}`;
    window.open(url, '_blank');
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
