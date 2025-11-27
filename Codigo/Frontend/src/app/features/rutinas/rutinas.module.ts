import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { RutinasRoutingModule } from './rutinas-routing.module';
import { ListarRutinasPage } from './pages/listar-rutinas/listar-rutinas.page';
import { CrearRutinaPage } from './pages/crear-rutina/crear-rutina.page';
import { RutinaCardComponent } from './components/rutina-card/rutina-card.component';

@NgModule({
  declarations: [
    ListarRutinasPage,
    CrearRutinaPage,
    RutinaCardComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    RutinasRoutingModule
  ]
})
export class RutinasModule { }
