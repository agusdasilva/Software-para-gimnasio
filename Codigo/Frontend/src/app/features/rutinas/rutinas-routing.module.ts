import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListarRutinasPage } from './pages/listar-rutinas/listar-rutinas.page';
import { CrearRutinaPage } from './pages/crear-rutina/crear-rutina.page';
import { DetalleRutinaPage } from './pages/detalle-rutina/detalle-rutina.page';
import { EjecutarRutinaPage } from './pages/ejecutar-rutina/ejecutar-rutina.page';
import { SeleccionarEjerciciosPage } from './pages/seleccionar-ejercicios/seleccionar-ejercicios.page';
import { CrearEjercicioPage } from './pages/crear-ejercicio/crear-ejercicio.page';

const routes: Routes = [
  { path: '', component: ListarRutinasPage },
  { path: 'crear/ejercicios/nuevo', component: CrearEjercicioPage },
  { path: 'crear/ejercicios', component: SeleccionarEjerciciosPage },
  { path: 'crear', component: CrearRutinaPage },
  { path: 'detalle/:id', component: DetalleRutinaPage },
  { path: 'ejecutar/:id', component: EjecutarRutinaPage }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RutinasRoutingModule { }
