import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CATALOGO_EJERCICIOS } from '../../models/catalogo-ejercicios';
import { CatalogoEjercicio, Equipamiento, GrupoMuscular, Nivel } from '../../models/ejercicios.model';

@Component({
  selector: 'app-seleccionar-ejercicios-page',
  templateUrl: './seleccionar-ejercicios.page.html',
  styleUrls: ['./seleccionar-ejercicios.page.css']
})
export class SeleccionarEjerciciosPage implements OnInit {

  catalogo: CatalogoEjercicio[] = [...CATALOGO_EJERCICIOS];
  filtrados: CatalogoEjercicio[] = [...CATALOGO_EJERCICIOS];

  seleccion = new Set<number>();

  filtroNombre = '';
  filtroGrupo: 'todos' | GrupoMuscular = 'todos';
  filtroNivel: 'todos' | Nivel = 'todos';
  filtroEquipamiento: 'todos' | Equipamiento = 'todos';

  constructor(private router: Router) {}

  ngOnInit(): void {
    const state = history.state || {};
    const preseleccion = (state.preseleccion as string[]) || [];
    const ejercicioCreado = state.ejercicioCreado as CatalogoEjercicio | undefined;
    const seleccionPrev = (state.seleccionPrev as number[]) || [];
    const rutinaId = state.rutinaId as number | undefined;
    if (rutinaId) {
      this.rutinaId = rutinaId;
    }
    if (ejercicioCreado) {
      this.catalogo = [ejercicioCreado, ...this.catalogo];
      this.seleccion.add(ejercicioCreado.id);
    }
    if (seleccionPrev.length) {
      seleccionPrev.forEach(id => this.seleccion.add(id));
    }
    if (preseleccion.length) {
      this.catalogo.forEach(ej => {
        if (preseleccion.includes(ej.nombre)) {
          this.seleccion.add(ej.id);
        }
      });
    }
    this.filtrar();
  }

  filtrar(): void {
    const nombre = this.filtroNombre.toLowerCase().trim();
    this.filtrados = this.catalogo.filter(ej => {
      const coincideNombre = !nombre || ej.nombre.toLowerCase().includes(nombre);
      const coincideGrupo = this.filtroGrupo === 'todos' || ej.grupoMuscular === this.filtroGrupo;
      const coincideNivel = this.filtroNivel === 'todos' || ej.nivel === this.filtroNivel;
      const coincideEq = this.filtroEquipamiento === 'todos' || ej.equipamiento === this.filtroEquipamiento;
      return coincideNombre && coincideGrupo && coincideNivel && coincideEq;
    });
  }

  toggleSeleccion(id: number): void {
    if (this.seleccion.has(id)) {
      this.seleccion.delete(id);
      return;
    }
    this.seleccion.add(id);
  }

  confirmar(): void {
    const elegidos = this.catalogo.filter(ej => this.seleccion.has(ej.id));
    this.router.navigate(['/rutinas/crear'], {
      state: { nuevosEjercicios: elegidos, rutinaId: this.rutinaId }
    });
  }

  cancelar(): void {
    this.router.navigate(['/rutinas/crear'], { state: { rutinaId: this.rutinaId } });
  }

  crearPropio(): void {
    this.router.navigate(['/rutinas/crear/ejercicios/nuevo'], {
      state: {
        returnTo: '/rutinas/crear/ejercicios',
        seleccionPrev: Array.from(this.seleccion),
        rutinaId: this.rutinaId
      }
    });
  }

  rutinaId?: number;

  get totalSeleccionados(): number {
    return this.seleccion.size;
  }
}
