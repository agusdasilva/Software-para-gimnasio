import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PublicRoutingModule } from './public-routing.module';
import { DashboardPublicComponent } from './pages/dashboard-public/dashboard-public.component';


@NgModule({
  declarations: [
    DashboardPublicComponent
  ],
  imports: [
    CommonModule,
    PublicRoutingModule
  ]
})
export class PublicModule { }
