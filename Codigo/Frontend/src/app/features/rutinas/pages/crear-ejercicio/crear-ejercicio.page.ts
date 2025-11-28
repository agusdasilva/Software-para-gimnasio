import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CatalogoEjercicio, Equipamiento, GrupoMuscular, Nivel } from '../../models/ejercicios.model';
import { CrearEjercicioRequest, EjercicioService } from '../../../../core/services/ejercicio.service';

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
  seleccionPrev: string[] = [];
  guardando = false;
  private rutinaForm?: unknown;
  private returnPath: string;
  private rutinaId?: number;

  constructor(private router: Router, private ejercicioService: EjercicioService) {
    this.seleccionPrev = (history.state?.seleccionPrev as string[]) || [];
    this.returnPath = (history.state?.returnTo as string) || '/rutinas/crear/ejercicios';
    this.rutinaId = history.state?.rutinaId as number | undefined;
    this.rutinaForm = history.state?.rutinaForm;
  }

  guardar(): void {
    this.error = '';
    if (this.guardando) return;
    if (!this.nombre.trim()) {
      this.error = 'Ponle un nombre al ejercicio.';
      return;
    }
    const payload: CrearEjercicioRequest = {
      nombre: this.nombre.trim(),
      grupo_muscular: this.grupo,
      equipamiento: this.equipamiento,
      es_global: false
    };
    this.guardando = true;
    this.ejercicioService.crear(payload).subscribe({
      next: (res: CatalogoEjercicio) => {
        const nuevo: CatalogoEjercicio = {
          ...res,
          descripcion: this.buildDescripcion(),
          nivel: this.nivel
        };
        this.persistirLocal(nuevo);
        this.router.navigate([this.returnTo], {
          state: { ejercicioCreado: nuevo, seleccionPrev: this.seleccionPrev, rutinaId: this.rutinaId, rutinaForm: this.rutinaForm }
        });
        this.guardando = false;
      },
      error: () => {
        this.error = 'No se pudo guardar el ejercicio. Intenta de nuevo.';
        this.guardando = false;
      }
    });
  }

  cancelar(): void {
    this.router.navigate([this.returnTo], { state: { seleccionPrev: this.seleccionPrev, rutinaId: this.rutinaId, rutinaForm: this.rutinaForm } });
  }

  private buildDescripcion(): string {
    const partes = [
      this.tipo,
      this.descripcion.trim(),
      this.otrosMusculos.trim() ? 'Secundario: ' + this.otrosMusculos.trim() : ''
    ].filter(Boolean);
    return partes.join(' | ');
  }

  private persistirLocal(ejercicio: CatalogoEjercicio): void {
    try {
      const raw = localStorage.getItem('catalogo-ejercicios-personal');
      const lista = raw ? JSON.parse(raw) as CatalogoEjercicio[] : [];
      const filtrada = lista.filter(e => e.id !== ejercicio.id && e.nombre.toLowerCase() !== ejercicio.nombre.toLowerCase());
      filtrada.unshift(ejercicio);
      localStorage.setItem('catalogo-ejercicios-personal', JSON.stringify(filtrada));
    } catch {
      // ignorar persistencia local si falla
    }
  }

  private get returnTo(): string {
    return this.returnPath;
  }
}
