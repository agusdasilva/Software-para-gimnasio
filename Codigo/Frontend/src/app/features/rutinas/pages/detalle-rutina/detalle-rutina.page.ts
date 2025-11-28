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
        this.rutina = this.normalizarEjercicios(res);
        this.inyectarDesdeLocal(id);
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

  editar(): void {
    if (!this.rutina) {
      return;
    }
    this.router.navigate(['/rutinas/crear'], { state: { rutinaBase: this.rutina } });
  }

  eliminar(): void {
    if (!this.rutina?.id) return;
    this.cargando = true;
    this.rutinaService.eliminarRutina(this.rutina.id).subscribe({
      next: () => this.router.navigate(['/rutinas'], { state: { mensaje: 'Rutina eliminada' } }),
      error: () => {
        this.error = 'No se pudo eliminar la rutina';
        this.cargando = false;
      }
    });
  }

  volver(): void {
    this.router.navigate(['/rutinas']);
  }

  get descansoSeg(): number {
    return this.rutina?.detalle?.descanso_seg || 0;
  }

  get ejercicios(): RutinaResponse['detalle']['ejercicios'] {
    const nested = this.rutina?.detalle?.ejercicios || [];
    const flat = (this.rutina as unknown as { ejercicios?: RutinaResponse['detalle']['ejercicios'] })?.ejercicios || [];
    return nested.length ? nested : flat;
  }

  moverEjercicio(index: number, direction: 'up' | 'down'): void {
    if (!this.rutina?.detalle?.ejercicios?.length) {
      return;
    }
    const ejercicios = [...this.rutina.detalle.ejercicios];
    const newIndex = direction === 'up' ? index - 1 : index + 1;
    if (newIndex < 0 || newIndex >= ejercicios.length) {
      return;
    }
    const tmp = ejercicios[newIndex];
    ejercicios[newIndex] = ejercicios[index];
    ejercicios[index] = tmp;
    this.rutina = {
      ...this.rutina,
      detalle: {
        ...this.rutina.detalle,
        ejercicios
      }
    };
  }

  private normalizarEjercicios(rutina: RutinaResponse): RutinaResponse {
    if (!rutina.detalle) {
      rutina.detalle = {
        id: rutina.id,
        rutina: rutina.nombre,
        descripcion: '',
        imagen: '',
        descanso_seg: 0,
        ejercicios: []
      };
    }
    const planos = (rutina as unknown as { ejercicios?: RutinaResponse['detalle']['ejercicios'] }).ejercicios;
    if (!rutina.detalle.ejercicios?.length && planos?.length) {
      rutina = {
        ...rutina,
        detalle: {
          ...rutina.detalle,
          ejercicios: planos
        }
      };
    }
    return rutina;
  }

  private inyectarDesdeLocal(idRutina: number): void {
    if (!this.rutina?.detalle || (this.rutina.detalle.ejercicios?.length || 0) > 0) {
      return;
    }
    try {
      const raw = localStorage.getItem('rutina-ejercicios-' + idRutina);
      if (!raw) {
        this.rutina.detalle.ejercicios = this.mockEjercicios();
        return;
      }
      const ejercicios = JSON.parse(raw) as RutinaResponse['detalle']['ejercicios'];
      this.rutina = {
        ...this.rutina,
        detalle: {
          ...this.rutina.detalle,
          ejercicios: ejercicios || []
        }
      };
    } catch {
      this.rutina.detalle.ejercicios = this.mockEjercicios();
    }
  }

  private mockEjercicios(): RutinaResponse['detalle']['ejercicios'] {
    return [
      {
        id: Date.now(),
        ejercicio: 'Press banca',
        series: [
          { id: Date.now() + 1, orden: 1, repeticiones: 8, carga: '40 kg' },
          { id: Date.now() + 2, orden: 2, repeticiones: 8, carga: '40 kg' },
          { id: Date.now() + 3, orden: 3, repeticiones: 10, carga: '35 kg' }
        ]
      },
      {
        id: Date.now() + 10,
        ejercicio: 'Remo con barra',
        series: [
          { id: Date.now() + 11, orden: 1, repeticiones: 10, carga: '35 kg' },
          { id: Date.now() + 12, orden: 2, repeticiones: 10, carga: '35 kg' }
        ]
      }
    ];
  }
}
