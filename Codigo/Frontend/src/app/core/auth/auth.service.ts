import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, map, tap, throwError } from 'rxjs';
import { HttpClient } from '@angular/common/http';

export type UserRole = 'ADMIN' | 'ENTRENADOR' | 'CLIENTE';
export type UserState = 'ACTIVO' | 'PENDIENTE' | 'BLOQUEADO';

export interface AuthUser {
  id: number;
  email: string;
  rol: UserRole;
  roles: UserRole[];
  username?: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  id: number;
  email: string;
  rol: UserRole;
}

export interface UsuarioResponse {
  id: number;
  nombre: string;
  email: string;
  rol: UserRole;
  estado: UserState;
  fechaAlta?: string;
  descripcion?: string;
  fotoUrl?: string;
  telefono?: string;
  instagram?: string;
}

export interface UsuarioPerfilUpdateRequest {
  nombre: string;
  descripcion?: string;
  fotoUrl?: string;
  telefono?: string;
  instagram?: string;
}

export interface UsuarioChangeRolRequest {
  idUsuario: number;
  nuevoRol: UserRole;
}

export interface UsuarioChangeEstadoRequest {
  idUsuario: number;
  nuevoEstado: UserState;
}

const TOKEN_KEY = 'auth_token';
const USER_KEY = 'auth_user';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private currentUserSubject = new BehaviorSubject<AuthUser | null>(this.getUserFromStorage());

  private authBaseUrl = 'http://localhost:8080/api/auth';
  private usersBaseUrl = 'http://localhost:8080/api/usuarios';

  constructor(private http: HttpClient) {}

  private getUserFromStorage(): AuthUser | null {
    const userJson = localStorage.getItem(USER_KEY);
    return userJson ? JSON.parse(userJson) as AuthUser : null;
  }

  setSession(token: string, user: AuthUser): void {
    localStorage.setItem(TOKEN_KEY, token);
    localStorage.setItem(USER_KEY, JSON.stringify(user));
    this.currentUserSubject.next(user);
  }

  get currentUser$(): Observable<AuthUser | null> {
    return this.currentUserSubject.asObservable();
  }

  get currentUser(): AuthUser | null {
    return this.currentUserSubject.value;
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  hasRole(roles: string[]): boolean {
    const user = this.currentUser;
    if (!user || !user.roles) {
      return false;
    }
    return roles.some(r => user.roles.includes(r as UserRole));
  }

  login(data: LoginRequest): Observable<AuthUser> {
    return this.http.post<LoginResponse>(`${this.authBaseUrl}/login`, data).pipe(
      tap(res => {
        const user = this.mapResponseToUser(res);
        this.setSession(res.token, user);
      }),
      map(res => this.mapResponseToUser(res))
    );
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    this.currentUserSubject.next(null);
  }

  private mapResponseToUser(res: LoginResponse): AuthUser {
    const rol = res.rol;
    return {
      id: res.id,
      email: res.email,
      rol,
      roles: [rol],
      username: res.email
    };
  }

  getProfile(): Observable<UsuarioResponse> {
    const current = this.currentUser;
    if (!current) {
      return throwError(() => new Error('No hay usuario autenticado'));
    }
    return this.getUserById(current.id).pipe(
      tap(user => {
        // Guardamos nombre/email para mostrar en header
        const updated: AuthUser = {
          id: user.id,
          email: user.email,
          rol: user.rol,
          roles: [user.rol],
          username: user.nombre
        };
        this.setSession(this.getToken() || '', updated);
      })
    );
  }

  getUserById(id: number): Observable<UsuarioResponse> {
    return this.http.get<UsuarioResponse>(`${this.usersBaseUrl}/${id}`);
  }

  getAllUsers(): Observable<UsuarioResponse[]> {
    return this.http.get<UsuarioResponse[]>(this.usersBaseUrl);
  }

  updateProfile(id: number, data: UsuarioPerfilUpdateRequest): Observable<UsuarioResponse> {
    return this.http.put<UsuarioResponse>(`${this.usersBaseUrl}/${id}/perfil`, data);
  }

  changeRole(request: UsuarioChangeRolRequest): Observable<UsuarioResponse> {
    return this.http.patch<UsuarioResponse>(`${this.usersBaseUrl}/rol`, request);
  }

  changeEstado(request: UsuarioChangeEstadoRequest): Observable<UsuarioResponse> {
    return this.http.patch<UsuarioResponse>(`${this.usersBaseUrl}/estado`, request);
  }
}
