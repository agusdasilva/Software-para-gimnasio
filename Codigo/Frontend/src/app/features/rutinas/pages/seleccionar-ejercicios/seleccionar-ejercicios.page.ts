import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CATALOGO_EJERCICIOS } from '../../models/catalogo-ejercicios';
import { CatalogoEjercicio, Equipamiento, GrupoMuscular, Nivel } from '../../models/ejercicios.model';
import { EjercicioService } from '../../../../core/services/ejercicio.service';

type CatalogoItem = CatalogoEjercicio & { uid: string; source: 'base' | 'api' | 'local' | 'nuevo' | 'draft' };
type RutinaFormLike = { ejercicios?: Array<Partial<CatalogoEjercicio>> };

@Component({
  selector: 'app-seleccionar-ejercicios-page',
  templateUrl: './seleccionar-ejercicios.page.html',
  styleUrls: ['./seleccionar-ejercicios.page.css']
})
export class SeleccionarEjerciciosPage implements OnInit {

  catalogo: CatalogoItem[] = [];
  filtrados: CatalogoItem[] = [];

  seleccion = new Set<string>();

  filtroNombre = '';
  filtroGrupo: 'todos' | GrupoMuscular = 'todos';
  filtroNivel: 'todos' | Nivel = 'todos';
  filtroEquipamiento: 'todos' | Equipamiento = 'todos';
  cargando = false;
  rutinaForm?: unknown;
  private rutinaEjercicios: CatalogoEjercicio[] = [];

  private preseleccionNombres: string[] = [];
  private seleccionPrev: string[] = [];

  constructor(private router: Router, private ejercicioService: EjercicioService) {}

  ngOnInit(): void {
    const state = history.state || {};
    this.preseleccionNombres = (state.preseleccion as string[]) || [];
    const ejercicioCreado = state.ejercicioCreado as CatalogoEjercicio | undefined;
    this.seleccionPrev = (state.seleccionPrev as string[]) || [];
    const rutinaId = state.rutinaId as number | undefined;
    this.rutinaForm = state.rutinaForm;
    this.rutinaEjercicios = this.extraerEjerciciosRutina(this.rutinaForm as RutinaFormLike | undefined);
    if (rutinaId) {
      this.rutinaId = rutinaId;
    }
    this.cargarCatalogo(ejercicioCreado);
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

  toggleSeleccion(uid: string): void {
    if (this.seleccion.has(uid)) {
      this.seleccion.delete(uid);
      return;
    }
    this.seleccion.add(uid);
  }

  confirmar(): void {
    const elegidos = this.catalogo
      .filter(ej => this.seleccion.has(ej.uid))
      .map(ej => this.toEjercicio(ej));
    elegidos.forEach(ej => this.guardarLocales(ej));
    this.router.navigate(['/rutinas/crear'], {
      state: { nuevosEjercicios: elegidos, rutinaId: this.rutinaId, rutinaForm: this.rutinaForm }
    });
  }

  cancelar(): void {
    this.router.navigate(['/rutinas/crear'], { state: { rutinaId: this.rutinaId, rutinaForm: this.rutinaForm } });
  }

  crearPropio(): void {
    this.router.navigate(['/rutinas/crear/ejercicios/nuevo'], {
      state: {
        returnTo: '/rutinas/crear/ejercicios',
        seleccionPrev: Array.from(this.seleccion),
        rutinaId: this.rutinaId,
        rutinaForm: this.rutinaForm
      }
    });
  }

  rutinaId?: number;

  get totalSeleccionados(): number {
    return this.seleccion.size;
  }

  private cargarCatalogo(ejercicioCreado?: CatalogoEjercicio): void {
    this.cargando = true;
    this.catalogo = this.mergeCatalogos(
      this.mapearConUid(CATALOGO_EJERCICIOS, 'base'),
      this.mapearConUid(this.leerLocales(), 'local')
    );
    if (this.rutinaEjercicios.length) {
      this.catalogo = this.mergeCatalogos(this.mapearConUid(this.rutinaEjercicios, 'draft'), this.catalogo);
    }

    if (ejercicioCreado) {
      const itemNuevo = this.mapearConUid([ejercicioCreado], 'nuevo')[0];
      this.catalogo = this.mergeCatalogos([itemNuevo], this.catalogo);
      this.seleccion.add(itemNuevo.uid);
      this.guardarLocales(ejercicioCreado);
    }

    this.ejercicioService.listar().subscribe({
      next: (res: CatalogoEjercicio[]) => {
        const apiItems = this.mapearConUid(res, 'api');
        this.catalogo = this.mergeCatalogos(this.catalogo, apiItems);
        this.aplicarPreselecciones();
        this.filtrar();
        this.cargando = false;
      },
      error: () => {
        this.aplicarPreselecciones();
        this.filtrar();
        this.cargando = false;
      }
    });
  }

  private aplicarPreselecciones(): void {
    if (this.seleccionPrev.length) {
      this.seleccionPrev.forEach(uid => this.seleccion.add(uid));
    }
    if (this.preseleccionNombres.length) {
      this.catalogo.forEach(ej => {
        if (this.preseleccionNombres.includes(ej.nombre)) {
          this.seleccion.add(ej.uid);
        }
      });
    }
  }

  private mergeCatalogos(base: CatalogoItem[], otros: CatalogoItem[]): CatalogoItem[] {
    const mapa = new Map<string, CatalogoItem>();
    [...base, ...otros].forEach(ej => {
      const key = ej.nombre.toLowerCase();
      if (!mapa.has(key)) {
        mapa.set(key, ej);
      }
    });
    return Array.from(mapa.values());
  }

  private guardarLocales(ejercicio: CatalogoEjercicio): void {
    try {
      const lista = this.leerLocales();
      const filtrada = lista.filter(e => e.nombre.toLowerCase() !== ejercicio.nombre.toLowerCase());
      filtrada.unshift(ejercicio);
      localStorage.setItem('catalogo-ejercicios-personal', JSON.stringify(filtrada));
    } catch {
      // ignorar errores de almacenamiento
    }
  }

  private leerLocales(): CatalogoEjercicio[] {
    try {
      const raw = localStorage.getItem('catalogo-ejercicios-personal');
      return raw ? JSON.parse(raw) as CatalogoEjercicio[] : [];
    } catch {
      return [];
    }
  }

  private mapearConUid(lista: CatalogoEjercicio[], source: CatalogoItem['source']): CatalogoItem[] {
    return lista.map(ej => ({
      ...ej,
      nivel: ej.nivel || 'Intermedio',
      uid: this.buildUid(source, ej),
      source
    }));
  }

  private buildUid(source: CatalogoItem['source'], ej: CatalogoEjercicio): string {
    const safeId = ej.id ?? Math.abs(this.hashNombre(ej.nombre));
    return `${source}:${safeId}:${ej.nombre.toLowerCase()}`;
  }

  private hashNombre(nombre: string): number {
    let hash = 0;
    for (let i = 0; i < nombre.length; i++) {
      hash = (hash << 5) - hash + nombre.charCodeAt(i);
      hash |= 0;
    }
    return hash || Date.now();
  }

  private toEjercicio(item: CatalogoItem): CatalogoEjercicio {
    const { uid, source, ...ej } = item;
    return ej;
  }

  private extraerEjerciciosRutina(rutina?: RutinaFormLike): CatalogoEjercicio[] {
    if (!rutina?.ejercicios?.length) return [];
    return rutina.ejercicios.map((ej, idx) => ({
      id: idx + 100000,
      nombre: ej.nombre || 'Ejercicio sin nombre',
      grupoMuscular: ej.grupoMuscular || 'Fullbody',
      nivel: (ej.nivel as Nivel) || 'Intermedio',
      equipamiento: (ej.equipamiento as Equipamiento) || 'Peso corporal',
      descripcion: (ej as { descripcion?: string; notas?: string }).descripcion || (ej as { notas?: string }).notas || ''
    }));
  }
}
