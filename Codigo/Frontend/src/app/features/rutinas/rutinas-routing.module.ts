import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListarRutinasPage } from './pages/listar-rutinas/listar-rutinas.page';
import { CrearRutinaPage } from './pages/crear-rutina/crear-rutina.page';

const routes: Routes = [
  { path: '', component: ListarRutinasPage },
  { path: 'crear', component: CrearRutinaPage }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RutinasRoutingModule { }
