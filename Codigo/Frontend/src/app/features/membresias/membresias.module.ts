import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { MembresiasRoutingModule } from './membresias-routing.module';
import { PlanesComponent } from './pages/planes/planes.component';
import { MembresiaDetalleComponent } from './pages/detalle-membresia/membresia-detalle.component';


@NgModule({
  declarations: [
    PlanesComponent,
    MembresiaDetalleComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    MembresiasRoutingModule
  ]
})
export class MembresiasModule { }
