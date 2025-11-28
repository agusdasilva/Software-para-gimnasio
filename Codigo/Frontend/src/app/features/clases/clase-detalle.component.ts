import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ClasesService, ClaseItem, MensajeClase } from './clases.service';
import { AuthService } from '../../core/auth/auth.service';
import { NotificacionService } from '../../core/services/notificacion.service';

@Component({
  selector: 'app-clase-detalle',
  templateUrl: './clase-detalle.component.html',
  styleUrls: ['./clase-detalle.component.css']
})
export class ClaseDetalleComponent implements OnInit {
  clase: ClaseItem | null = null;
  nuevoMensaje = '';
  userIdByName: Record<string, number> = {};
  entrandoClase = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private clasesService: ClasesService,
    private authService: AuthService,
    private notiService: NotificacionService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id) {
      this.router.navigate(['/clases']);
      return;
    }
    const encontrada = this.clasesService.buscarPorId(id);
    if (!encontrada) {
      this.router.navigate(['/clases']);
      return;
    }
    this.clase = encontrada;
    this.cargarUsuarios();
  }

  volver(): void {
    this.router.navigate(['/clases']);
  }

  puedeUnirse(clase: ClaseItem): boolean {
    const nombreUsuario = this.nombreUsuario();
    if (this.authService.hasRole(['ADMIN'])) {
      return true;
    }
    if (this.authService.hasRole(['ENTRENADOR'])) {
      return clase.entrenadores.includes(nombreUsuario);
    }
    const esMiembro = clase.miembros.some(m => m.nombre === nombreUsuario);
    return esMiembro;
  }

  entrarClase(clase: ClaseItem): void {
    if (!this.puedeUnirse(clase)) return;
    this.entrandoClase = true;
    const esStaff = this.authService.hasRole(['ADMIN']) || this.authService.hasRole(['ENTRENADOR']);
    const rol: any = this.authService.hasRole(['ADMIN']) ? 'ADMIN' : this.authService.hasRole(['ENTRENADOR']) ? 'ENTRENADOR' : 'USER';
    this.clasesService.registrarIngreso(clase.id, esStaff, this.nombreUsuario(), rol);
    this.entrandoClase = false;
    this.router.navigate(['/clases', clase.id]);
  }

  get puedeGestionar(): boolean {
    if (!this.clase) return false;
    return this.authService.hasRole(['ADMIN']) ||
      (this.authService.hasRole(['ENTRENADOR']) && this.clase.entrenadores.includes(this.nombreUsuario()));
  }

  aceptar(nombre: string): void {
    if (!this.clase || !this.puedeGestionar) return;
    const rol = this.authService.hasRole(['ENTRENADOR']) ? 'ENTRENADOR' : 'USER';
    this.clasesService.aceptarSolicitud(this.clase.id, nombre, rol as any);
    this.refrescarClase();
    this.notificarUsuario(nombre, `Tu solicitud a ${this.clase?.titulo} fue aceptada.`);
  }

  rechazar(nombre: string): void {
    if (!this.clase || !this.puedeGestionar) return;
    this.clasesService.rechazarSolicitud(this.clase.id, nombre);
    this.refrescarClase();
  }

  remover(nombre: string): void {
    if (!this.clase || !this.puedeGestionar) return;
    this.clasesService.removerMiembro(this.clase.id, nombre);
    this.refrescarClase();
  }

  enviarMensaje(): void {
    if (!this.clase || !this.puedeGestionar || !this.nuevoMensaje.trim()) return;
    const msg: MensajeClase = {
      autor: this.nombreUsuario(),
      texto: this.nuevoMensaje.trim(),
      fecha: new Date().toLocaleString()
    };
    this.clasesService.agregarMensaje(this.clase.id, msg);
    this.nuevoMensaje = '';
    this.refrescarClase();
    this.notificarMiembros(`Nuevo mensaje en ${this.clase?.titulo} de ${msg.autor}: ${msg.texto}`);
  }

  private nombreUsuario(): string {
    const usuario = this.authService.currentUser as any;
    return usuario?.nombreCompleto || usuario?.username || usuario?.nombre || 'Usuario';
  }

  private refrescarClase(): void {
    if (!this.clase) return;
    const actualizada = this.clasesService.buscarPorId(this.clase.id);
    if (actualizada) {
      this.clase = { ...actualizada };
    }
  }

  private cargarUsuarios(): void {
    this.authService.getAllUsers().subscribe({
      next: users => {
        users.forEach(u => this.userIdByName[u.nombre] = u.id);
      },
      error: () => {}
    });
  }

  private notificarUsuario(nombre: string, mensaje: string): void {
    const id = this.userIdByName[nombre];
    if (id) {
      this.notiService.crear({ idUsuario: id, mensaje }).subscribe({ next: () => {}, error: () => {} });
    }
  }

  private notificarMiembros(mensaje: string): void {
    if (!this.clase) return;
    this.clase.miembros
      .filter(m => m.nombre !== this.nombreUsuario())
      .forEach(m => this.notificarUsuario(m.nombre, mensaje));
  }
}
