import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { PerfilRoutingModule } from './perfil-routing.module';
import { PerfilComponent } from './perfil/perfil.component';
import { PerfilEditarComponent } from './perfil-editar/perfil-editar.component';
import { AjustesComponent } from './ajustes/ajustes.component';
import { AptoMedicoComponent } from './apto-medico/apto-medico.component';


@NgModule({
  declarations: [
    PerfilComponent,
    PerfilEditarComponent,
    AjustesComponent,
    AptoMedicoComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    PerfilRoutingModule
  ]
})
export class PerfilModule { }
