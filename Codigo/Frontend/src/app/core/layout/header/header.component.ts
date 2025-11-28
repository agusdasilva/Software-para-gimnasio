import { Component, HostListener, OnInit } from '@angular/core';
import { AuthService, AuthUser, UserRole } from '../../auth/auth.service';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { ThemeService } from '../../services/theme.service';
import { NotificacionService, Notificacion } from '../../services/notificacion.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  user$: Observable<AuthUser | null>;
  showNotifications = false;
  cleared = false;
  notifications: Notificacion[] = [];
  showNotifSettings = false;
  notifPrefs = {
    solicitudes: true,
    mensajes: true,
    aceptaciones: true
  };

  constructor(
    private authService: AuthService,
    public router: Router,
    public themeService: ThemeService,
    private notificacionService: NotificacionService
  ) {
    this.user$ = this.authService.currentUser$;
  }

  ngOnInit(): void {
    const current = this.authService.currentUser;
    if (current) {
      this.cargarNotificaciones(current.id);
    }
    this.user$.subscribe(user => {
      if (user) {
        this.cargarNotificaciones(user.id);
      } else {
        this.notifications = [];
      }
    });
  }

  isAuthenticated(): boolean {
    return this.authService.isAuthenticated();
  }

  toggleNotifications(): void {
    this.showNotifications = !this.showNotifications;
    if (!this.showNotifications) {
      this.showNotifSettings = false;
    }
  }

  closeNotifications(): void {
    this.showNotifications = false;
  }

  get unreadCount(): number {
    return this.notifications.filter(n => !n.leida).length;
  }

  clearNotifications(): void {
    const toDelete = [...this.notifications];
    this.notifications = [];
    toDelete.forEach(n => this.notificacionService.eliminar(n.id).subscribe());
    this.cleared = true;
    setTimeout(() => this.cleared = false, 1500);
  }

  @HostListener('document:click', ['$event'])
  handleOutsideClick(event: MouseEvent): void {
    const target = event.target as HTMLElement | null;
    if (target && !target.closest('.notif-wrapper')) {
      this.closeNotifications();
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  hasRole(role: UserRole): boolean {
    const user = this.authService.currentUser;
    return !!user && user.roles?.includes(role);
  }

  isAdmin(): boolean {
    return this.hasRole('ADMIN');
  }

  isTrainer(): boolean {
    return this.hasRole('ENTRENADOR');
  }

  isMember(): boolean {
    return this.hasRole('CLIENTE');
  }

  get isDarkTheme(): boolean {
    return this.themeService.theme === 'dark';
  }

  toggleTheme(): void {
    this.themeService.toggleTheme();
  }

  private cargarNotificaciones(userId: number): void {
    this.notificacionService.listar(userId).subscribe(list => {
      this.notifications = list;
    });
  }

  toggleNotifSettings(): void {
    this.showNotifSettings = !this.showNotifSettings;
  }

  togglePref(key: 'solicitudes' | 'mensajes' | 'aceptaciones'): void {
    this.notifPrefs[key] = !this.notifPrefs[key];
  }
}
