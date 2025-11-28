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
    const preseleccion = (history.state?.preseleccion as string[]) || [];
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
    this.router.navigate(['/rutinas/crear'], { state: { nuevosEjercicios: elegidos } });
  }

  cancelar(): void {
    this.router.navigate(['/rutinas/crear']);
  }

  crearPropio(): void {
    const custom: CatalogoEjercicio = {
      id: Date.now(),
      nombre: 'Ejercicio personalizado',
      grupoMuscular: 'Fullbody',
      nivel: 'Intermedio',
      equipamiento: 'Peso corporal',
      descripcion: 'Edita el nombre y agrega series al volver'
    };
    this.catalogo = [custom, ...this.catalogo];
    this.filtrar();
    this.seleccion.add(custom.id);
  }

  get totalSeleccionados(): number {
    return this.seleccion.size;
  }
}
