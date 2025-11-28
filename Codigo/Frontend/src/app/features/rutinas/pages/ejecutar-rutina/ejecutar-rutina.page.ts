import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RutinaResponse, RutinaService } from '../../../../core/services/rutina.service';

@Component({
  selector: 'app-ejecutar-rutina-page',
  templateUrl: './ejecutar-rutina.page.html',
  styleUrls: ['./ejecutar-rutina.page.css']
})
export class EjecutarRutinaPage implements OnInit, OnDestroy {

  rutina?: RutinaResponse;
  cargando = false;
  error = '';
  progreso: Record<number, boolean[]> = {};
  descansoTotal = 0;
  descansoRestante = 0;
  temporizador?: any;
  mensaje = '';

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
        this.rutina = this.normalizarEjercicios(res);
        this.inyectarDesdeLocal(id);
        this.descansoTotal = this.rutina?.detalle?.descanso_seg || 0;
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
    if (checked) {
      this.iniciarDescanso();
    }
  }

  isSerieCompletada(idEjercicio: number, index: number): boolean {
    return !!this.progreso[idEjercicio]?.[index];
  }

  agregarSerie(idEjercicio: number): void {
    const ejercicio = this.findEjercicio(idEjercicio);
    if (!ejercicio) return;
    const nuevoOrden = (ejercicio.series?.length || 0) + 1;
    const nuevaSerie = {
      id: Date.now(),
      carga: 'Peso a definir',
      repeticiones: 10,
      orden: nuevoOrden
    };
    ejercicio.series = [...ejercicio.series, nuevaSerie];
    this.ajustarProgreso(idEjercicio, ejercicio.series.length);
  }

  eliminarSerie(idEjercicio: number, index: number): void {
    const ejercicio = this.findEjercicio(idEjercicio);
    if (!ejercicio?.series?.length) return;
    ejercicio.series.splice(index, 1);
    ejercicio.series = ejercicio.series.map((s, idx) => ({ ...s, orden: idx + 1 }));
    this.ajustarProgreso(idEjercicio, ejercicio.series.length);
  }

  actualizarSerie(idEjercicio: number, index: number, campo: 'repeticiones' | 'carga', valor: number | string): void {
    const ejercicio = this.findEjercicio(idEjercicio);
    if (!ejercicio?.series?.[index]) return;
    const serie = ejercicio.series[index];
    ejercicio.series[index] = { ...serie, [campo]: valor };
  }

  moverEjercicio(index: number, direction: 'up' | 'down'): void {
    if (!this.rutina?.detalle?.ejercicios?.length) {
      return;
    }
    const nuevoIndice = direction === 'up' ? index - 1 : index + 1;
    if (nuevoIndice < 0 || nuevoIndice >= this.rutina.detalle.ejercicios.length) {
      return;
    }
    const copia = [...this.rutina.detalle.ejercicios];
    const temp = copia[nuevoIndice];
    copia[nuevoIndice] = copia[index];
    copia[index] = temp;
    this.rutina = {
      ...this.rutina,
      detalle: {
        ...this.rutina.detalle,
        ejercicios: copia
      }
    };
  }

  reiniciar(): void {
    this.initProgreso();
    this.detenerDescanso();
    this.mensaje = '';
  }

  finalizar(): void {
    const payload = {
      fecha: new Date().toISOString(),
      completadas: this.seriesCompletadas,
      total: this.totalSeries
    };
    try {
      localStorage.setItem('rutina-progreso-' + (this.rutina?.id || 'tmp'), JSON.stringify(payload));
      this.mensaje = 'Progreso guardado (' + payload.completadas + '/' + payload.total + ').';
    } catch {
      this.mensaje = 'Progreso guardado en memoria.';
    }
    this.detenerDescanso();
    this.router.navigate(['/rutinas'], { state: { mensaje: this.mensaje } });
  }

  iniciarDescanso(): void {
    if (!this.descansoTotal) return;
    this.descansoRestante = this.descansoTotal;
    this.detenerDescanso();
    this.temporizador = setInterval(() => {
      if (this.descansoRestante > 0) {
        this.descansoRestante -= 1;
      }
      if (this.descansoRestante <= 0) {
        this.detenerDescanso();
      }
    }, 1000);
  }

  ajustarDescanso(delta: number): void {
    this.descansoRestante = Math.max(0, this.descansoRestante + delta);
  }

  omitirDescanso(): void {
    this.descansoRestante = 0;
    this.detenerDescanso();
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

  get descansoActivo(): boolean {
    return this.descansoRestante > 0;
  }

  trackById(_index: number, item: { id: number }): number {
    return item.id;
  }

  private findEjercicio(idEjercicio: number) {
    return this.rutina?.detalle?.ejercicios.find(ej => ej.id === idEjercicio);
  }

  private ajustarProgreso(idEjercicio: number, totalSeries: number): void {
    const actuales = this.progreso[idEjercicio] || [];
    const recalculado = new Array(totalSeries).fill(false);
    actuales.forEach((flag, idx) => {
      if (idx < totalSeries) {
        recalculado[idx] = flag;
      }
    });
    this.progreso[idEjercicio] = recalculado;
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
    const ejerciciosPlanos = (rutina as unknown as { ejercicios?: RutinaResponse['detalle']['ejercicios'] }).ejercicios;
    if (!rutina.detalle.ejercicios?.length && ejerciciosPlanos?.length) {
      rutina = {
        ...rutina,
        detalle: {
          ...rutina.detalle,
          ejercicios: ejerciciosPlanos
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

  private detenerDescanso(): void {
    if (this.temporizador) {
      clearInterval(this.temporizador);
      this.temporizador = undefined;
    }
  }

  ngOnDestroy(): void {
    this.detenerDescanso();
  }
}
