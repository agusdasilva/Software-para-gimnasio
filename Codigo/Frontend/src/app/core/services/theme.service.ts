import { Injectable } from '@angular/core';

export type Theme = 'light' | 'dark';

const THEME_KEY = 'app_theme';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {

  private currentTheme: Theme = 'dark';

  constructor() {
    const stored = localStorage.getItem(THEME_KEY) as Theme | null;

    if (stored === 'light' || stored === 'dark') {
      this.currentTheme = stored;
    } else {
      this.currentTheme = 'dark'; // tema por defecto
    }

    this.applyTheme(this.currentTheme);
  }

  get theme(): Theme {
    return this.currentTheme;
  }

  setTheme(theme: Theme): void {
    this.currentTheme = theme;
    localStorage.setItem(THEME_KEY, theme);
    this.applyTheme(theme);
  }

  toggleTheme(): void {
    const next: Theme = this.currentTheme === 'dark' ? 'light' : 'dark';
    this.setTheme(next);
  }

  private applyTheme(theme: Theme): void {
    const body = document.body;
    body.classList.remove('theme-light', 'theme-dark');
    body.classList.add(theme === 'dark' ? 'theme-dark' : 'theme-light');
  }
}
