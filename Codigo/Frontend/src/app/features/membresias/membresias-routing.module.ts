import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PlanesComponent } from './pages/planes/planes.component';
import { MembresiaDetalleComponent } from './pages/detalle-membresia/membresia-detalle.component';

const routes: Routes = [
  { path: '', component: PlanesComponent },
  { path: 'detalle', component: MembresiaDetalleComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MembresiasRoutingModule { }
