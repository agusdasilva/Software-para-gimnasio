import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CatalogoEjercicio, Equipamiento, GrupoMuscular, Nivel } from '../../models/ejercicios.model';

@Component({
  selector: 'app-crear-ejercicio-page',
  templateUrl: './crear-ejercicio.page.html',
  styleUrls: ['./crear-ejercicio.page.css']
})
export class CrearEjercicioPage {

  nombre = '';
  nivel: Nivel = 'Intermedio';
  equipamiento: Equipamiento = 'Peso corporal';
  grupo: GrupoMuscular = 'Fullbody';
  otrosMusculos = '';
  tipo = 'Fuerza';
  recurso = '';
  descripcion = '';
  error = '';
  seleccionPrev: number[] = [];
  private returnPath: string;
  private rutinaId?: number;

  constructor(private router: Router) {
    this.seleccionPrev = (history.state?.seleccionPrev as number[]) || [];
    this.returnPath = (history.state?.returnTo as string) || '/rutinas/crear/ejercicios';
    this.rutinaId = history.state?.rutinaId as number | undefined;
  }

  guardar(): void {
    this.error = '';
    if (!this.nombre.trim()) {
      this.error = 'Ponle un nombre al ejercicio.';
      return;
    }
    const nuevo: CatalogoEjercicio = {
      id: Date.now(),
      nombre: this.nombre.trim(),
      grupoMuscular: this.grupo,
      nivel: this.nivel,
      equipamiento: this.equipamiento,
      descripcion: this.buildDescripcion()
    };
    this.router.navigate([this.returnTo], {
      state: { ejercicioCreado: nuevo, seleccionPrev: this.seleccionPrev, rutinaId: this.rutinaId }
    });
  }

  cancelar(): void {
    this.router.navigate([this.returnTo], { state: { seleccionPrev: this.seleccionPrev, rutinaId: this.rutinaId } });
  }

  private buildDescripcion(): string {
    const partes = [
      this.tipo,
      this.descripcion.trim(),
      this.otrosMusculos.trim() ? 'Secundario: ' + this.otrosMusculos.trim() : ''
    ].filter(Boolean);
    return partes.join(' | ');
  }

  private get returnTo(): string {
    return this.returnPath;
  }
}
