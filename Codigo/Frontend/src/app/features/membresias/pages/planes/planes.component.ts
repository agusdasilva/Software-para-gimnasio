import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MembresiaService, MembresiaResponse } from '../../../../core/services/membresia.service';
import { AuthService } from '../../../../core/auth/auth.service';
import { PagoService } from '../../../../core/services/pago.service';
import { PlanService, Plan } from '../../../../core/services/plan.service';

@Component({
  selector: 'app-planes-membresia',
  templateUrl: './planes.component.html',
  styleUrls: ['./planes.component.css']
})
export class PlanesComponent implements OnInit, OnDestroy {

  membresia: MembresiaResponse | null = null;
  loading = false;
  loadingPlan: string | null = null;
  error = '';
  success = '';
  private paymentPoll: number | null = null;
  planes: Plan[] = [];
  planesLoading = false;
  planesError = '';
  planesMessage = '';
  private selectedPlanId: number | null = null;
  private selectedPlanName: string | null = null;
  private readonly storageKey = 'last_plan_selected';
  private membershipLoaded = false;
  @ViewChild('planesTrack', { static: false }) planesTrack?: ElementRef<HTMLDivElement>;
  editandoPlanId: number | null = null;
  planDraft: Plan = this.nuevoPlan();
  nuevoPlanDraft: Plan = this.nuevoPlan();
  readonly periodos = ['DIARIO', 'SEMANAL', 'MENSUAL', 'ANUAL'];
  featuredPlanKey: string | null = null;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private membresiaService: MembresiaService,
    public authService: AuthService,
    private pagoService: PagoService,
    private planService: PlanService
  ) {}

  ngOnInit(): void {
    this.checkPaymentReturn();
    if (this.authService.isAuthenticated()) {
      this.loading = true;
      this.membresiaService.getMembresiaActual().subscribe({
        next: (m) => {
          this.membresia = this.filtrarMembresia(m);
          this.loading = false;
          this.error = '';
          this.membershipLoaded = true;
          this.ensureMembresiaFromStored();
        },
        error: () => {
          this.loading = false;
          this.membresia = null;
          this.error = '';
          this.membershipLoaded = true;
          this.ensureMembresiaFromStored();
        }
      });
    }
    this.cargarPlanes();
  }

  ngOnDestroy(): void {
    if (this.paymentPoll !== null) {
      window.clearInterval(this.paymentPoll);
      this.paymentPoll = null;
    }
  }

  get isAdmin(): boolean {
    return this.authService.hasRole(['ADMIN']);
  }

  cargarPlanes(): void {
    this.planesLoading = true;
    this.planesError = '';
    this.planService.listar().subscribe({
      next: planes => {
        this.planes = planes || [];
        this.planesLoading = false;
        this.definirDestacado();
      },
      error: () => {
        this.planesLoading = false;
        this.planesError = 'No se pudieron cargar los planes.';
        this.planes = [];
      }
    });
  }

  choosePlan(plan: Plan): void {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login'], { queryParams: { plan: plan.id ?? plan.nombre, returnUrl: '/membresias' } });
      return;
    }

    this.loading = true;
    this.loadingPlan = plan.id ? plan.id.toString() : plan.nombre;
    this.selectedPlanId = plan.id ?? null;
    this.selectedPlanName = plan.nombre;
    this.storeSelectedPlan();
    this.error = '';
    this.pagoService.crearPreferencia(this.loadingPlan).subscribe({
      next: (pref) => {
        const paymentWindow = window.open(pref.initPoint, '_blank', 'width=520,height=780');
        // Si el navegador bloquea el popup, degradamos a la misma pestana
        if (!paymentWindow) {
          window.location.href = pref.initPoint;
          return;
        }
        this.startPopupListener(paymentWindow);
        paymentWindow.focus();
        this.loading = false;
        this.loadingPlan = null;
      },
      error: () => {
        this.loading = false;
        this.loadingPlan = null;
        this.error = 'No se pudo iniciar el pago. Verifica tu sesion o intentalo mas tarde.';
        // Fallback: intentar crear la membresia directamente
        this.crearMembresiaLocal();
      }
    });
  }

  scrollPlans(direction: number): void {
    const track = this.planesTrack?.nativeElement;
    if (!track) {
      return;
    }
    const card = track.querySelector<HTMLElement>('.plan-card');
    const gap = 18;
    const scrollAmount = card ? card.offsetWidth + gap : 240;
    track.scrollBy({ left: direction * scrollAmount, behavior: 'smooth' });
  }

  isLoading(plan: Plan): boolean {
    const id = plan.id ? plan.id.toString() : plan.nombre;
    return this.loadingPlan === id;
  }

  diasRestantes(): number | null {
    if (!this.membresia?.fechaFin) return null;
    const fin = new Date(this.membresia.fechaFin).getTime();
    const ahora = Date.now();
    const diff = fin - ahora;
    return diff > 0 ? Math.ceil(diff / (1000 * 60 * 60 * 24)) : 0;
  }

  empezarEditarPlan(plan: Plan): void {
    if (!plan.id) {
      return;
    }
    this.editandoPlanId = plan.id;
    this.planDraft = { ...plan };
    this.planesMessage = '';
    this.planesError = '';
  }

  cancelarEdicionPlan(): void {
    this.editandoPlanId = null;
    this.planDraft = this.nuevoPlan();
    this.planesMessage = '';
    this.planesError = '';
    this.cargarPlanes();
  }

  guardarPlan(): void {
    if (!this.editandoPlanId) {
      return;
    }
    this.planesMessage = '';
    this.planesError = '';
    const payload: Plan = { ...this.planDraft, id: this.editandoPlanId, precio: Number(this.planDraft.precio || 0) };
    const validation = this.validarPlan(payload);
    if (validation) {
      this.planesError = validation;
      return;
    }
    this.planesLoading = true;
    this.planService.actualizar(payload).subscribe({
      next: () => {
        this.planesMessage = 'Plan actualizado.';
        this.editandoPlanId = null;
        this.planDraft = this.nuevoPlan();
        this.cargarPlanes();
      },
      error: () => {
        this.planesLoading = false;
        this.planesError = 'No se pudo actualizar el plan.';
      },
      complete: () => this.planesLoading = false
    });
  }

  crearPlan(): void {
    this.planesMessage = '';
    this.planesError = '';
    const payload: Plan = { ...this.nuevoPlanDraft, precio: Number(this.nuevoPlanDraft.precio || 0) };
    const validation = this.validarPlan(payload);
    if (validation) {
      this.planesError = validation;
      return;
    }
    this.planesLoading = true;
    this.planService.crear(payload).subscribe({
      next: () => {
        this.planesMessage = 'Plan creado.';
        this.nuevoPlanDraft = this.nuevoPlan();
        this.cargarPlanes();
      },
      error: () => {
        this.planesLoading = false;
        this.planesError = 'No se pudo crear el plan.';
      },
      complete: () => this.planesLoading = false
    });
  }

  eliminarPlan(plan: Plan): void {
    if (!plan.id) {
      return;
    }
    const confirmar = window.confirm(`Eliminar el plan "${plan.nombre}"?`);
    if (!confirmar) {
      return;
    }
    this.planesLoading = true;
    this.planesMessage = '';
    this.planesError = '';
    this.planService.eliminar(plan.id).subscribe({
      next: () => {
        this.planesMessage = 'Plan eliminado.';
        this.cargarPlanes();
      },
      error: () => {
        this.planesLoading = false;
        this.planesError = 'No se pudo eliminar el plan.';
      },
      complete: () => this.planesLoading = false
    });
  }

  limpiarNuevoPlan(): void {
    this.nuevoPlanDraft = this.nuevoPlan();
    this.planesMessage = '';
    this.planesError = '';
  }

  periodoLabel(periodo: string): string {
    const value = (periodo || '').toUpperCase();
    switch (value) {
      case 'DIARIO': return 'Diario';
      case 'SEMANAL': return 'Semanal';
      case 'MENSUAL': return 'Mensual';
      case 'ANUAL': return 'Anual';
      default: return periodo || 'Mensual';
    }
  }

  private validarPlan(plan: Plan): string | null {
    const nombre = (plan.nombre || '').trim();
    if (!nombre) {
      return 'La descripción es obligatoria.';
    }
    const precio = Number(plan.precio || 0);
    if (isNaN(precio) || precio <= 0) {
      return 'El precio debe ser mayor a 0.';
    }
    return null;
  }

  private checkPaymentReturn(): void {
    const paymentId = this.route.snapshot.queryParamMap.get('payment_id')
      || this.route.snapshot.queryParamMap.get('collection_id')
      || this.route.snapshot.queryParamMap.get('id');
    if (!paymentId) {
      return;
    }
    if (!this.authService.isAuthenticated()) {
      return;
    }
    this.loading = true;
    this.error = '';
    this.pagoService.confirmarPago(paymentId).subscribe({
      next: (membresia) => {
        this.success = 'Pago confirmado y membresia renovada.';
        this.handlePagoConfirmado(membresia);
      },
      error: () => {
        this.loading = false;
        this.error = 'No se pudo confirmar el pago. Intentando reconciliar...';
        this.restoreSelectedPlan();
        const planCode = this.selectedPlanId ? this.selectedPlanId.toString() : (this.selectedPlanName || this.loadingPlan || '');
        if (planCode) {
          this.pagoService.reconciliar(planCode).subscribe({
            next: (res) => {
              this.success = 'Pago reconciliado y membresia activada.';
              this.handlePagoConfirmado(res);
            },
            error: () => {
              this.error = 'No se pudo confirmar el pago. Si el cargo fue realizado, contacta soporte.';
            }
          });
        }
      }
    });
  }

  private nuevoPlan(): Plan {
    return { nombre: '', precio: 0, periodo: 'MENSUAL' };
  }

  private startPopupListener(popup: Window): void {
    if (this.paymentPoll !== null) {
        window.clearInterval(this.paymentPoll);
    }
    this.paymentPoll = window.setInterval(() => {
      try {
        const sameOrigin = popup.location.origin === window.location.origin;
        if (popup.closed) {
          window.clearInterval(this.paymentPoll!);
          this.paymentPoll = null;
          this.loading = false;
          this.loadingPlan = null;
          return;
        }
        if (!sameOrigin) {
          return;
        }

        const url = new URL(popup.location.href);
        const paymentId = url.searchParams.get('payment_id')
          || url.searchParams.get('collection_id')
          || url.searchParams.get('id');
        if (paymentId) {
          window.clearInterval(this.paymentPoll!);
          this.paymentPoll = null;
          popup.close();
          this.loading = true;
          this.error = '';
          this.pagoService.confirmarPago(paymentId).subscribe({
            next: (membresia) => {
              this.success = 'Pago confirmado y membresia renovada.';
              this.handlePagoConfirmado(membresia);
            },
            error: () => {
              this.loading = false;
              this.error = 'No se pudo confirmar el pago. Si el cargo fue realizado, contacta soporte.';
            }
          });
        }
      } catch (err) {
        // Ignoramos errores de cross-origin hasta que vuelva al dominio
      }
    }, 800);
  }

  private ensureMembresiaFromStored(): void {
    if (!this.membershipLoaded) return;
    if (this.membresia) return;
    if (!this.authService.isAuthenticated()) return;
    this.restoreSelectedPlan();
    if (this.selectedPlanId) {
      this.crearMembresiaLocal();
    }
  }

  private storeSelectedPlan(): void {
    const payload = {
      id: this.selectedPlanId,
      name: this.selectedPlanName
    };
    localStorage.setItem(this.storageKey, JSON.stringify(payload));
  }

  private restoreSelectedPlan(): void {
    const raw = localStorage.getItem(this.storageKey);
    if (!raw) return;
    try {
      const data = JSON.parse(raw);
      this.selectedPlanId = data?.id ?? null;
      this.selectedPlanName = data?.name ?? null;
    } catch {
      this.selectedPlanId = null;
      this.selectedPlanName = null;
    }
  }

  private clearStoredPlan(): void {
    localStorage.removeItem(this.storageKey);
  }

  private crearMembresiaLocal(): void {
    if (!this.selectedPlanId) {
      this.restoreSelectedPlan();
    }
    if (!this.selectedPlanId) {
      this.error = 'No se pudo detectar el plan seleccionado.';
      return;
    }
    this.loading = true;
    this.error = '';
    this.membresiaService.crear(this.selectedPlanId).subscribe({
      next: (m) => {
        this.membresia = this.filtrarMembresia(m);
        this.success = `Membresia "${this.selectedPlanName || ''}" activada.`;
        this.clearStoredPlan();
        this.irADetalleSiCliente();
      },
      error: () => {
        this.error = 'No se pudo crear la membresia en el servidor.';
      },
      complete: () => {
        this.loading = false;
        this.loadingPlan = null;
      }
    });
  }

  private handlePagoConfirmado(membresia: MembresiaResponse | null): void {
    const filtrada = this.filtrarMembresia(membresia);
    if (filtrada) {
      this.membresia = filtrada;
      this.clearStoredPlan();
      this.loading = false;
      this.loadingPlan = null;
      this.irADetalleSiCliente();
      return;
    }
    // Si MP no devolvió la membresía, reintentamos conciliando contra MercadoPago
    this.restoreSelectedPlan();
    const planCode = this.selectedPlanId ? this.selectedPlanId.toString() : (this.selectedPlanName || this.loadingPlan || '');
    if (!planCode) {
      this.crearMembresiaLocal();
      return;
    }
    this.pagoService.reconciliar(planCode).subscribe({
      next: (res) => {
        const ok = this.filtrarMembresia(res);
        if (ok) {
          this.membresia = ok;
          this.clearStoredPlan();
          this.irADetalleSiCliente();
        } else {
          this.crearMembresiaLocal();
        }
      },
      error: () => this.crearMembresiaLocal()
    });
  }

  private irADetalleSiCliente(): void {
    if (this.authService.hasRole(['CLIENTE'])) {
      this.router.navigate(['/membresias/detalle']);
    }
  }

  private filtrarMembresia(m: MembresiaResponse | null): MembresiaResponse | null {
    if (!m) return null;
    const estado = (m.estado || '').toUpperCase();
    if (estado !== 'ACTIVA') return null;
    if (m.fechaFin) {
      const fin = new Date(m.fechaFin).getTime();
      if (fin <= Date.now()) return null;
    }
    return m;
  }

  private definirDestacado(): void {
    type MejorPlan = { clave: string; peso: number; precio: number };
    let mejor: MejorPlan | undefined;
    this.planes.forEach((plan, idx) => {
      const peso = this.obtenerPesoPeriodo(plan.periodo);
      const precio = Number(plan.precio || 0);
      const clave = plan.id ? `id-${plan.id}` : `idx-${idx}`;
      if (!mejor || peso > mejor.peso || (peso === mejor.peso && precio > mejor.precio)) {
        mejor = { clave, peso, precio };
      }
    });
    this.featuredPlanKey = mejor?.clave ?? null;
  }

  esDestacado(plan: Plan, idx: number): boolean {
    const clave = plan.id ? `id-${plan.id}` : `idx-${idx}`;
    return this.featuredPlanKey === clave;
  }

  private obtenerPesoPeriodo(periodo: string | null | undefined): number {
    const val = (periodo || '').toUpperCase();
    switch (val) {
      case 'ANUAL': return 4;
      case 'MENSUAL': return 3;
      case 'SEMANAL': return 2;
      case 'DIARIO': return 1;
      default: return 0;
    }
  }
}
