import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MembresiasRoutingModule } from './membresias-routing.module';
import { PlanesComponent } from './pages/planes/planes.component';


@NgModule({
  declarations: [PlanesComponent],
  imports: [
    CommonModule,
    MembresiasRoutingModule
  ]
})
export class MembresiasModule { }
