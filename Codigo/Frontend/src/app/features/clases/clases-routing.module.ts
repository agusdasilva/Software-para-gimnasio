import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ClasesComponent } from './clases.component';
import { ClaseDetalleComponent } from './clase-detalle.component';

const routes: Routes = [
  {
    path: '',
    component: ClasesComponent
  },
  {
    path: ':id',
    component: ClaseDetalleComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClasesRoutingModule { }
