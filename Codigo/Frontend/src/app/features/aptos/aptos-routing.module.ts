import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AptosComponent } from './aptos.component';

const routes: Routes = [
  { path: '', component: AptosComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AptosRoutingModule { }
