import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ShellComponent } from './core/layout/shell/shell.component';
import { AuthGuard } from './core/auth/auth.guard';

const routes: Routes = [
  {
    path: 'login',
    loadChildren: () =>
      import('./features/auth/auth.module').then(m => m.AuthModule)
  },
  {
    path: '',
    component: ShellComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        loadChildren: () =>
          import('./features/dashboard/dashboard.module').then(m => m.DashboardModule)
      },
      {
        path: 'usuarios',
        loadChildren: () =>
          import('./features/usuarios/usuarios.module').then(m => m.UsuariosModule)
      },
      {
        path: 'clases',
        loadChildren: () =>
          import('./features/clases/clases.module').then(m => m.ClasesModule)
      },
      {
        path: 'rutinas',
        loadChildren: () =>
          import('./features/rutinas/rutinas.module').then(m => m.RutinasModule)
      },
      {
        path: 'membresias',
        loadChildren: () =>
          import('./features/membresias/membresias.module').then(m => m.MembresiasModule)
      },
      {
        path: 'pagos',
        loadChildren: () =>
          import('./features/pagos/pagos.module').then(m => m.PagosModule)
      },
      {
        path: 'notificaciones',
        loadChildren: () =>
          import('./features/notificaciones/notificaciones.module').then(m => m.NotificacionesModule)
      },
      {
        path: 'perfil',
        loadChildren: () =>
          import('./features/perfil/perfil.module').then(m => m.PerfilModule)
      }
    ]
  },
  // cualquier ruta que no exista → redirige a dashboard o login
  {
    path: '**',
    redirectTo: ''
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
