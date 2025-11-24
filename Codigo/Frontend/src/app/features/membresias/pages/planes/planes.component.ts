import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-planes-membresia',
  templateUrl: './planes.component.html',
  styleUrls: ['./planes.component.css']
})
export class PlanesComponent {

  constructor(private router: Router) {}

  choosePlan(plan: string): void {
    this.router.navigate(['/login'], { queryParams: { plan } });
  }
}
