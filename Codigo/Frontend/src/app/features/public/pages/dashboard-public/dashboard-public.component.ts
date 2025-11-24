import { Component } from '@angular/core';
import { AuthService } from '../../../../core/auth/auth.service';

@Component({
  selector: 'app-dashboard-public',
  templateUrl: './dashboard-public.component.html',
  styleUrl: './dashboard-public.component.css'
})
export class DashboardPublicComponent {

  constructor(private authService: AuthService) {}

  isAuthenticated(): boolean {
    return this.authService.isAuthenticated();
  }
}
