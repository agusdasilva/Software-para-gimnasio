import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ClasesRoutingModule } from './clases-routing.module';
import { ClasesComponent } from './clases.component';

@NgModule({
  declarations: [ClasesComponent],
  imports: [
    CommonModule,
    FormsModule,
    ClasesRoutingModule
  ]
})
export class ClasesModule { }
