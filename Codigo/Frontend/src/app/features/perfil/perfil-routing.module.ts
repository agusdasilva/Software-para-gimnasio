import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PerfilComponent } from './perfil/perfil.component';
import { PerfilEditarComponent } from './perfil-editar/perfil-editar.component';
import { AjustesComponent } from './ajustes/ajustes.component';
import { AptoMedicoComponent } from './apto-medico/apto-medico.component';

const routes: Routes = [
  { path: '', component: PerfilComponent },
  { path: 'editar', component: PerfilEditarComponent },
  { path: 'ajustes', component: AjustesComponent },
  { path: 'apto-medico', component: AptoMedicoComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PerfilRoutingModule { }
