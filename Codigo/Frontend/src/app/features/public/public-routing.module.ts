import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardPublicComponent } from './pages/dashboard-public/dashboard-public.component';

const routes: Routes = [
  { path: '', component: DashboardPublicComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PublicRoutingModule {}
