import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PerfilComponent } from './perfil/perfil.component';
import { PerfilEditarComponent } from './perfil-editar/perfil-editar.component';

const routes: Routes = [
  { path: '', component: PerfilComponent },
  { path: 'editar', component: PerfilEditarComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PerfilRoutingModule { }
