import { Component, EventEmitter, Input, Output } from '@angular/core';

export type RutinaNivel = 'Principiante' | 'Intermedio' | 'Avanzado';
export type RutinaEstado = 'ACTIVA' | 'BORRADOR' | 'PAUSADA';

export interface RutinaBloque {
  nombre: string;
  foco: string;
  detalle: string;
}

export interface RutinaResumen {
  id: number;
  titulo: string;
  objetivo: string;
  nivel: RutinaNivel;
  estado: RutinaEstado;
  semanas: number;
  frecuencia: number;
  duracionMin: number;
  calorias?: number;
  avance: number;
  entrenador: string;
  proximaSesion: string;
  tags: string[];
  bloques: RutinaBloque[];
  actualizado: string;
}

@Component({
  selector: 'app-rutina-card',
  templateUrl: './rutina-card.component.html',
  styleUrls: ['./rutina-card.component.css']
})
export class RutinaCardComponent {
  @Input() rutina!: RutinaResumen;
  @Output() ver = new EventEmitter<number>();
  @Output() arrancar = new EventEmitter<number>();

  onVerDetalle(): void {
    if (this.rutina?.id) {
      this.ver.emit(this.rutina.id);
    }
  }

  onArrancar(): void {
    if (this.rutina?.id) {
      this.arrancar.emit(this.rutina.id);
    }
  }

  estadoLabel(estado: RutinaEstado): string {
    if (estado === 'BORRADOR') {
      return 'Borrador';
    }
    if (estado === 'PAUSADA') {
      return 'Pausada';
    }
    return 'Activa';
  }
}
