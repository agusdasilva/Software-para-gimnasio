import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ClasesService, ClaseItem, MensajeClase } from './clases.service';
import { AuthService } from '../../core/auth/auth.service';
import { NotificacionService } from '../../core/services/notificacion.service';
import { RutinaResponse, RutinaService } from '../../core/services/rutina.service';

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
  rutinasClase: RutinaResponse[] = [];
  rutinasDisponibles: RutinaResponse[] = [];
  rutinaSeleccionadaId?: number;
  mensajeRutinas = '';
  invitacionesPendientes: Array<{ id: number; nombre: string }> = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private clasesService: ClasesService,
    private authService: AuthService,
    private notiService: NotificacionService,
    private rutinaService: RutinaService
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
    this.clasesService.refrescarMiembros(this.clase.id).subscribe({
      next: () => {
        this.refrescarClase();
        if (!this.puedeVerClase(this.clase!)) {
          this.router.navigate(['/clases']);
        }
      },
      error: () => {
        if (!this.puedeVerClase(this.clase!)) {
          this.router.navigate(['/clases']);
        }
      }
    });
    this.cargarUsuarios();
    this.cargarRutinasClase();
    if (this.puedeGestionar) {
      this.cargarRutinasDisponibles();
      this.cargarInvitacionesPendientes();
    }
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
    const userId = this.currentUserId();
    const esMiembro = clase.miembros.some(m => (userId ? m.id === userId : false) || m.nombre === nombreUsuario);
    return esMiembro;
  }

  entrarClase(clase: ClaseItem): void {
    if (!this.puedeUnirse(clase)) return;
    this.entrandoClase = true;
    const esStaff = this.authService.hasRole(['ADMIN']) || this.authService.hasRole(['ENTRENADOR']);
    const rol: any = this.authService.hasRole(['ADMIN']) ? 'ADMIN' : this.authService.hasRole(['ENTRENADOR']) ? 'ENTRENADOR' : 'USER';
    this.clasesService.registrarIngreso(clase.id, esStaff, this.nombreUsuario(), rol, this.currentUserId());
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
    if (!this.clase) return;
    const invitacion = this.buscarInvitacionPorNombre(nombre);
    if (!invitacion) return;
    this.clasesService.responderInvitacion(invitacion.id, true).subscribe({
      next: () => {
        this.clasesService.refrescarMiembros(this.clase!.id).subscribe({
          next: () => this.refrescarClase(),
          error: () => this.refrescarClase()
        });
        this.cargarInvitacionesPendientes();
        this.notificarUsuario(nombre, `Tu solicitud a ${this.clase?.titulo} fue aceptada.`);
      },
      error: () => {}
    });
  }

  rechazar(nombre: string): void {
    if (!this.clase || !this.puedeGestionar) return;
    const invitacion = this.buscarInvitacionPorNombre(nombre);
    if (!invitacion) return;
    this.clasesService.responderInvitacion(invitacion.id, false).subscribe({
      next: () => {
        this.clasesService.refrescarMiembros(this.clase!.id).subscribe({
          next: () => this.refrescarClase(),
          error: () => this.refrescarClase()
        });
        this.cargarInvitacionesPendientes();
        this.notificarUsuario(nombre, `Tu solicitud a ${this.clase?.titulo} fue rechazada.`);
      },
      error: () => {}
    });
  }

  remover(nombre: string, userId?: number): void {
    if (!this.clase || !this.puedeGestionar) return;
    this.clasesService.removerMiembro(this.clase.id, nombre, userId);
    this.refrescarClase();
  }

  enviarMensaje(): void {
    if (!this.clase || !this.puedeEnviarMensaje() || !this.nuevoMensaje.trim()) return;
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

  puedeEnviarMensaje(): boolean {
    if (!this.clase) return false;
    if (this.authService.hasRole(['ADMIN', 'ENTRENADOR'])) return true;
    const userId = this.currentUserId();
    const nombre = this.nombreUsuario();
    return this.clase.miembros.some(m => (userId ? m.id === userId : false) || m.nombre === nombre);
  }

  private nombreUsuario(): string {
    const usuario = this.authService.currentUser as any;
    return usuario?.nombreCompleto || usuario?.username || usuario?.nombre || 'Usuario';
  }

  private currentUserId(): number {
    return this.authService.currentUser?.id || 0;
  }

  puedeVerClase(clase: ClaseItem): boolean {
    if (this.authService.hasRole(['ADMIN'])) return true;
    if (this.authService.hasRole(['ENTRENADOR'])) {
      const userId = this.currentUserId();
      const nombre = this.nombreUsuario();
      return clase.entrenadores.includes(nombre) || clase.miembros.some(m => (userId ? m.id === userId : false) || m.nombre === nombre);
    }
    return true;
  }

  private refrescarClase(): void {
    if (!this.clase) return;
    const actualizada = this.clasesService.buscarPorId(this.clase.id);
    if (actualizada) {
      this.clase = { ...actualizada };
    }
  }

  private cargarRutinasClase(): void {
    if (!this.clase) return;
    this.clasesService.listarRutinasClase(this.clase.id).subscribe({
      next: res => {
        this.rutinasClase = Array.isArray(res) ? res : [];
      },
      error: () => {
        this.rutinasClase = [];
      }
    });
  }

  private cargarRutinasDisponibles(): void {
    this.rutinaService.listarMias().subscribe({
      next: res => {
        this.rutinasDisponibles = Array.isArray(res) ? res : [];
      },
      error: () => {
        this.rutinasDisponibles = [];
      }
    });
  }

  asignarRutinaClase(): void {
    if (!this.clase || !this.rutinaSeleccionadaId) return;
    this.clasesService.agregarRutinaClase(this.clase.id, this.rutinaSeleccionadaId).subscribe({
      next: () => {
        this.mensajeRutinas = 'Rutina asignada a la clase.';
        this.cargarRutinasClase();
      },
      error: () => {
        this.mensajeRutinas = 'No se pudo asignar la rutina.';
      }
    });
  }

  guardarRutina(rutina: RutinaResponse): void {
    if (!rutina?.id) return;
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login'], { queryParams: { returnUrl: '/clases/' + this.clase?.id } });
      return;
    }
    if (!this.clase) return;
    this.clasesService.guardarRutinaClase(this.clase.id, rutina.id).subscribe({
      next: () => {
        this.mensajeRutinas = 'Rutina guardada en tus rutinas.';
      },
      error: () => {
        this.mensajeRutinas = 'No se pudo guardar la rutina.';
      }
    });
  }

  private cargarUsuarios(): void {
    this.authService.getAllUsers().subscribe({
      next: users => {
        users.forEach(u => this.userIdByName[u.nombre] = u.id);
        if (this.puedeGestionar) {
          this.cargarInvitacionesPendientes();
        }
      },
      error: () => {}
    });
  }

  private cargarInvitacionesPendientes(): void {
    if (!this.puedeGestionar) {
      this.invitacionesPendientes = [];
      return;
    }
    if (!this.clase) return;
    this.clasesService.obtenerInvitaciones(this.clase.id).subscribe({
      next: invs => {
        const pendientes = Array.isArray(invs) ? invs.filter(i => i.estado === 'PENDIENTE') : [];
        this.invitacionesPendientes = pendientes.map((i: any) => ({
          id: i.idInvitacion,
          nombre: i.nombreUsuario || (this.userIdByName[i.idUsuario] ? Object.keys(this.userIdByName).find(k => this.userIdByName[k] === i.idUsuario) || 'Usuario' : 'Usuario')
        }));
      },
      error: () => {
        this.invitacionesPendientes = [];
      }
    });
  }

  private buscarInvitacionPorNombre(nombre: string): { id: number; nombre: string } | undefined {
    return this.invitacionesPendientes.find(i => i.nombre === nombre);
  }

  private notificarUsuario(nombre: string, mensaje: string): void {
    const id = this.userIdByName[nombre];
    if (id) {
      this.notiService.crear({ idUsuario: id, mensaje }).subscribe({ next: () => {}, error: () => {} });
    } else {
      // Fallback: recargar usuarios y reenviar si encontramos match
      this.authService.getAllUsers().subscribe({
        next: users => {
          users.forEach(u => this.userIdByName[u.nombre] = u.id);
          const encontrado = users.find(u => u.nombre === nombre || u.email === nombre || u.id.toString() === nombre);
          if (encontrado) {
            this.notiService.crear({ idUsuario: encontrado.id, mensaje }).subscribe({ next: () => {}, error: () => {} });
          }
        },
        error: () => {}
      });
    }
  }

  private notificarMiembros(mensaje: string): void {
    if (!this.clase) return;
    this.clase.miembros
      .filter(m => m.nombre !== this.nombreUsuario())
      .forEach(m => this.notificarUsuario(m.nombre, mensaje));
  }
}
