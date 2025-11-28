import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RutinaResponse, RutinaService } from '../../../../core/services/rutina.service';

@Component({
  selector: 'app-detalle-rutina-page',
  templateUrl: './detalle-rutina.page.html',
  styleUrls: ['./detalle-rutina.page.css']
})
export class DetalleRutinaPage implements OnInit {

  rutina?: RutinaResponse;
  cargando = false;
  error = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private rutinaService: RutinaService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id) {
      this.error = 'ID de rutina invalido';
      return;
    }
    this.cargarRutina(id);
  }

  cargarRutina(id: number): void {
    this.cargando = true;
    this.error = '';
    this.rutinaService.buscarPorId(id).subscribe({
      next: (res: RutinaResponse) => {
        this.rutina = res;
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se pudo cargar la rutina';
        this.cargando = false;
      }
    });
  }

  ejecutar(): void {
    if (this.rutina?.id) {
      this.router.navigate(['/rutinas/ejecutar', this.rutina.id]);
    }
  }

  volver(): void {
    this.router.navigate(['/rutinas']);
  }

  get descansoSeg(): number {
    return this.rutina?.detalle?.descanso_seg || 0;
  }
}
