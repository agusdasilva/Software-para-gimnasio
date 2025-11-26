import { Component, OnDestroy, OnInit } from '@angular/core';
import { AuthService } from '../../../../core/auth/auth.service';

@Component({
  selector: 'app-dashboard-public',
  templateUrl: './dashboard-public.component.html',
  styleUrl: './dashboard-public.component.css'
})
export class DashboardPublicComponent implements OnInit, OnDestroy {

  photos = [
    '/assets/fotosGym%20(1).jpg',
    '/assets/fotosGym%20(2).jpg',
    '/assets/fotosGym%20(3).jpg',
    '/assets/fotosGym%20(4).jpg'
  ];

  banners = [
    "url('/assets/banner_desktop.jpeg')",
    "url('/assets/banner_desktop%20(1).jpeg')",
    "url('/assets/banner_desktop%20(2).jpeg')",
    "url('/assets/banner_desktop%20(3).jpeg')"
  ];

  lightboxOpen = false;
  currentIndex = 0;
  currentBanner = 0;
  private bannerTimer?: any;
  reviews = [
    { user: 'Lucia M.', text: 'Excelente ambiente y profesores muy atentos.', score: 4.8, timeAgo: 'hace 2 dias' },
    { user: 'Juan P.', text: 'Las maquinas siempre limpias y en buen estado.', score: 4.7, timeAgo: 'hace 3 dias' },
    { user: 'Carla R.', text: 'Me armaron una rutina a medida y funciona.', score: 4.9, timeAgo: 'hace 1 semana' },
    { user: 'Diego S.', text: 'Buen horario, abierto temprano y hasta tarde.', score: 4.6, timeAgo: 'hace 4 dias' },
    { user: 'Rocio A.', text: 'Clases de HIIT intensas, me encantan.', score: 4.8, timeAgo: 'hace 5 dias' },
    { user: 'Matias L.', text: 'El staff siempre con buena onda.', score: 4.7, timeAgo: 'hace 6 dias' },
    { user: 'Sofia T.', text: 'Vestuario limpio y duchas impecables.', score: 4.9, timeAgo: 'hace 1 dia' },
    { user: 'Gaston V.', text: 'Gran variedad de pesos libres y racks.', score: 4.6, timeAgo: 'hace 2 semanas' },
    { user: 'Florencia C.', text: 'Recomiendo las clases de movilidad.', score: 4.8, timeAgo: 'hace 3 semanas' },
    { user: 'Pablo G.', text: 'Precio justo por el servicio que dan.', score: 4.5, timeAgo: 'hace 1 semana' },
    { user: 'Ana F.', text: 'Me siento segura entrenando aca.', score: 4.9, timeAgo: 'hace 4 dias' },
    { user: 'Ezequiel D.', text: 'Siempre hay alguien para corregir tecnica.', score: 4.7, timeAgo: 'hace 5 dias' },
    { user: 'Mariana J.', text: 'Las bicis y cintas son nuevas, se nota.', score: 4.8, timeAgo: 'hace 2 semanas' },
    { user: 'Nico B.', text: 'Prolijos con la musica y la ventilacion.', score: 4.6, timeAgo: 'hace 6 dias' },
    { user: 'Valen H.', text: 'Me ayudaron a mejorar mi postura.', score: 4.7, timeAgo: 'hace 3 dias' },
    { user: 'Cami S.', text: 'Hay espacio para estirar sin molestar.', score: 4.8, timeAgo: 'hace 1 semana' },
    { user: 'Leo R.', text: 'App sencilla para reservar clases.', score: 4.5, timeAgo: 'hace 2 semanas' },
    { user: 'Julieta V.', text: 'Me gusta que siempre hay agua fria disponible.', score: 4.6, timeAgo: 'hace 1 dia' },
    { user: 'Agustin D.', text: 'Los entrenadores tienen mucha paciencia.', score: 4.9, timeAgo: 'hace 2 dias' },
    { user: 'Hernan C.', text: 'El mejor gym del barrio, sin dudas.', score: 5.0, timeAgo: 'hace 3 dias' }
  ];
  reviewsPerPage = 5;
  currentReviewSlide = 0;
  private reviewTimer?: any;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.startBannerRotation();
    this.startReviewRotation();
  }

  ngOnDestroy(): void {
    if (this.bannerTimer) {
      clearInterval(this.bannerTimer);
    }
    if (this.reviewTimer) {
      clearInterval(this.reviewTimer);
    }
  }

  isAuthenticated(): boolean {
    return this.authService.isAuthenticated();
  }

  openLightbox(index: number): void {
    this.currentIndex = index;
    this.lightboxOpen = true;
  }

  closeLightbox(): void {
    this.lightboxOpen = false;
  }

  next(): void {
    this.currentIndex = (this.currentIndex + 1) % this.photos.length;
  }

  prev(): void {
    this.currentIndex = (this.currentIndex - 1 + this.photos.length) % this.photos.length;
  }

  get currentPhoto(): string {
    return this.photos[this.currentIndex];
  }

  changeBanner(nextIndex: number): void {
    this.currentBanner = (nextIndex + this.banners.length) % this.banners.length;
    this.startBannerRotation();
  }

  private startBannerRotation(): void {
    if (this.bannerTimer) {
      clearInterval(this.bannerTimer);
    }
    this.bannerTimer = setInterval(() => {
      this.currentBanner = (this.currentBanner + 1) % this.banners.length;
    }, 5000);
  }

  get currentReviews() {
    const result = [];
    for (let i = 0; i < this.reviewsPerPage; i++) {
      const idx = (this.currentReviewSlide + i) % this.reviews.length;
      result.push(this.reviews[idx]);
    }
    return result;
  }

  nextReviews(): void {
    const totalSlides = Math.ceil(this.reviews.length / this.reviewsPerPage);
    this.currentReviewSlide = (this.currentReviewSlide + 1) % totalSlides;
    this.startReviewRotation();
  }

  prevReviews(): void {
    const totalSlides = Math.ceil(this.reviews.length / this.reviewsPerPage);
    this.currentReviewSlide = (this.currentReviewSlide - 1 + totalSlides) % totalSlides;
    this.startReviewRotation();
  }

  private startReviewRotation(): void {
    const totalSlides = Math.ceil(this.reviews.length / this.reviewsPerPage);
    if (this.reviewTimer) {
      clearInterval(this.reviewTimer);
    }
    this.reviewTimer = setInterval(() => {
      this.currentReviewSlide = (this.currentReviewSlide + 1) % totalSlides;
    }, 6000);
  }
}
