import { Component } from '@angular/core';
import { AuthService, AuthUser } from '../../auth/auth.service';
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

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  goToProfile(): void {
    this.router.navigate(['/perfil']);
  }

  primaryRole(roles?: string[]): string {
    if (!roles || !roles.length) {
      return 'Sin rol';
    }
    return roles[0];
  }

  hasRole(role: string): boolean {
    const user = this.authService.currentUser;
    return !!user && user.roles?.includes(role);
  }

  // helpers por si querés usarlos en el HTML más legible
  isAdmin(): boolean {
    return this.hasRole('ADMIN');
  }

  isTrainer(): boolean {
    return this.hasRole('TRAINER') || this.hasRole('ENTRENADOR');
  }

  isMember(): boolean {
    return this.hasRole('SOCIO') || this.hasRole('MEMBER') || this.hasRole('USER') || this.hasRole('CLIENTE') || this.hasRole('CLIENT');
  }

  get isDarkTheme(): boolean {
    return this.themeService.theme === 'dark';
  }

  toggleTheme(): void {
    this.themeService.toggleTheme();
  }

}
