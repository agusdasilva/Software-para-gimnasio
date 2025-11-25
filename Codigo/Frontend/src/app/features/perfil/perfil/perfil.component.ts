import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService, StoredUser } from '../../../core/auth/auth.service';

@Component({
  selector: 'app-perfil',
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css']
})
export class PerfilComponent implements OnInit {

  name = '';
  email = '';
  description = '';
  avatarUrl = '';
  roles: string[] = [];
  viewOnly = false;

  constructor(private authService: AuthService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    const queryId = Number(this.route.snapshot.queryParamMap.get('userId'));
    const current = this.authService.currentUser;

    let profile: StoredUser | null = null;
    if (queryId && current?.roles.includes('ADMIN')) {
      profile = this.authService.getUserById(queryId);
      this.viewOnly = profile ? profile.id !== current?.id : false;
    }

    if (!profile) {
      profile = this.authService.getProfile();
      this.viewOnly = false;
    }

    if (profile) {
      this.name = profile.username;
      this.email = profile.email;
      this.description = profile.description || '';
      this.avatarUrl = profile.avatarUrl || '';
      this.roles = profile.roles || [];
    }
  }

  goToEdit(): void {
    if (this.viewOnly) {
      return;
    }
    this.router.navigate(['/perfil/editar']);
  }

  avatarFallback(): string {
    return this.name ? this.name.charAt(0).toUpperCase() : 'P';
  }
}
