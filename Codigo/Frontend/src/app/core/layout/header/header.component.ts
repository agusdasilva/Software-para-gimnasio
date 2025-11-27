import { Component, HostListener } from '@angular/core';
import { AuthService, AuthUser, UserRole } from '../../auth/auth.service';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { ThemeService } from '../../services/theme.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  user$: Observable<AuthUser | null>;
  showNotifications = false;
  notifications: Array<{ id: number; title: string; message: string; time: string; read: boolean }> = [
    { id: 1, title: 'Pago confirmado', message: 'Tu membresia fue activada correctamente.', time: 'Hace 1 min', read: false },
    { id: 2, title: 'Clase agendada', message: 'Recordatorio de tu clase funcional manana.', time: 'Hoy', read: true }
  ];

  constructor(
    private authService: AuthService,
    public router: Router,
    public themeService: ThemeService
  ) {
    this.user$ = this.authService.currentUser$;
  }

  isAuthenticated(): boolean {
    return this.authService.isAuthenticated();
  }

  toggleNotifications(): void {
    this.showNotifications = !this.showNotifications;
  }

  closeNotifications(): void {
    this.showNotifications = false;
  }

  get unreadCount(): number {
    return this.notifications.filter(n => !n.read).length;
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
}
