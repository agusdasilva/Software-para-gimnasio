import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RutinaResponse, RutinaService } from '../../../../core/services/rutina.service';

@Component({
  selector: 'app-ejecutar-rutina-page',
  templateUrl: './ejecutar-rutina.page.html',
  styleUrls: ['./ejecutar-rutina.page.css']
})
export class EjecutarRutinaPage implements OnInit {

  rutina?: RutinaResponse;
  cargando = false;
  error = '';
  progreso: Record<number, boolean[]> = {};

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
      next: res => {
        this.rutina = res;
        this.initProgreso();
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se pudo cargar la rutina';
        this.cargando = false;
      }
    });
  }

  initProgreso(): void {
    this.progreso = {};
    const ejercicios = this.rutina?.detalle?.ejercicios || [];
    ejercicios.forEach(ej => {
      const totalSeries = ej.series?.length || 0;
      this.progreso[ej.id] = new Array(totalSeries).fill(false);
    });
  }

  toggleSerie(idEjercicio: number, index: number, checked: boolean): void {
    const series = this.progreso[idEjercicio];
    if (!series || index < 0 || index >= series.length) return;
    series[index] = checked;
  }

  isSerieCompletada(idEjercicio: number, index: number): boolean {
    return !!this.progreso[idEjercicio]?.[index];
  }

  reiniciar(): void {
    this.initProgreso();
  }

  volver(): void {
    if (this.rutina?.id) {
      this.router.navigate(['/rutinas/detalle', this.rutina.id]);
      return;
    }
    this.router.navigate(['/rutinas']);
  }

  get totalSeries(): number {
    return (this.rutina?.detalle?.ejercicios || []).reduce((acc, ej) => acc + (ej.series?.length || 0), 0);
  }

  get seriesCompletadas(): number {
    return Object.values(this.progreso).reduce((acc, arr) => acc + arr.filter(Boolean).length, 0);
  }

  get progresoPorc(): number {
    if (!this.totalSeries) return 0;
    return Math.round((this.seriesCompletadas / this.totalSeries) * 100);
  }
}
