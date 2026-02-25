import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AptosRoutingModule } from './aptos-routing.module';
import { AptosComponent } from './aptos.component';

@NgModule({
  declarations: [AptosComponent],
  imports: [
    CommonModule,
    FormsModule,
    AptosRoutingModule
  ]
})
export class AptosModule { }
