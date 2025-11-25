import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of, throwError } from 'rxjs';

export interface AuthUser {
  id: number;
  username: string;
  email: string;
  roles: string[];
  description?: string;
  avatarUrl?: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  user: AuthUser;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
}

export interface StoredUser extends AuthUser {
  email: string;
  password: string;
}

const TOKEN_KEY = 'auth_token';
const USER_KEY = 'auth_user';
const USERS_KEY = 'auth_users';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private currentUserSubject = new BehaviorSubject<AuthUser | null>(this.getUserFromStorage());
  constructor() {}

  private getUserFromStorage(): AuthUser | null {
    const userJson = localStorage.getItem(USER_KEY);
    if (!userJson) {
      return null;
    }

    try {
      const parsed = JSON.parse(userJson) as Partial<AuthUser>;
      if (parsed && parsed.id) {
        return {
          id: parsed.id,
          username: parsed.username || '',
          email: parsed.email || '',
          roles: parsed.roles || [],
          description: parsed.description || '',
          avatarUrl: parsed.avatarUrl || ''
        };
      }
      return null;
    } catch {
      return null;
    }
  }

  setSession(token: string, user: AuthUser): void {
    localStorage.setItem(TOKEN_KEY, token);
    localStorage.setItem(USER_KEY, JSON.stringify(user));
    this.currentUserSubject.next(user);
  }

  private getStoredUsers(): StoredUser[] {
    const rawUsers = localStorage.getItem(USERS_KEY);
    if (!rawUsers) {
      const defaultAdmin: StoredUser = {
        id: 1,
        username: 'Administrador',
        email: 'admin@gym.com',
        password: 'admin123',
        roles: ['ADMIN'],
        description: 'Administrador del sistema',
        avatarUrl: ''
      };
      localStorage.setItem(USERS_KEY, JSON.stringify([defaultAdmin]));
      return [defaultAdmin];
    }

    try {
      const parsed = JSON.parse(rawUsers) as StoredUser[];
      return Array.isArray(parsed) ? parsed : [];
    } catch {
      return [];
    }
  }

  private saveUsers(users: StoredUser[]): void {
    localStorage.setItem(USERS_KEY, JSON.stringify(users));
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
    return roles.some(r => user.roles.includes(r));
  }

  login(data: LoginRequest): Observable<LoginResponse> {
    const users = this.getStoredUsers();
    const normalizedInput = data.username.trim().toLowerCase();
    const user = users.find(
      u =>
        u.email.toLowerCase() === normalizedInput ||
        u.username.toLowerCase() === normalizedInput
    );

    if (!user) {
      return throwError(() => new Error('Usuario no encontrado.'));
    }

    if (user.password !== data.password) {
      return throwError(() => new Error('Contrasena incorrecta.'));
    }

    const authUser: AuthUser = {
      id: user.id,
      username: user.username,
      email: user.email,
      roles: user.roles,
      description: user.description,
      avatarUrl: user.avatarUrl
    };
    const token = `local-token-${user.id}`;
    this.setSession(token, authUser);

    return of({ token, user: authUser });
  }

  register(data: RegisterRequest): Observable<LoginResponse> {
    const users = this.getStoredUsers();
    const email = data.email.trim().toLowerCase();

    if (users.some(u => u.email === email)) {
      return throwError(() => new Error('El correo ya esta registrado.'));
    }

    const newUser: StoredUser = {
      id: users.length ? Math.max(...users.map(u => u.id)) + 1 : 1,
      username: data.name.trim(),
      email,
      password: data.password,
      roles: ['CLIENTE'],
      description: '',
      avatarUrl: ''
    };

    const updatedUsers = [...users, newUser];
    this.saveUsers(updatedUsers);

    const authUser: AuthUser = {
      id: newUser.id,
      username: newUser.username,
      email: newUser.email,
      roles: newUser.roles,
      description: newUser.description,
      avatarUrl: newUser.avatarUrl
    };
    const token = `local-token-${newUser.id}`;
    this.setSession(token, authUser);

    return of({ token, user: authUser });
  }

  getAllUsers(): StoredUser[] {
    return this.getStoredUsers();
  }

  getUserById(id: number): StoredUser | null {
    const users = this.getStoredUsers();
    return users.find(u => u.id === id) || null;
  }

  promoteToTrainer(id: number): Observable<StoredUser> {
    const users = this.getStoredUsers();
    const index = users.findIndex(u => u.id === id);
    if (index === -1) {
      return throwError(() => new Error('Usuario no encontrado.'));
    }

    const roles = Array.from(new Set([...users[index].roles, 'ENTRENADOR']));
    users[index] = { ...users[index], roles };
    this.saveUsers(users);

    if (this.currentUser?.id === id) {
      this.setSession(this.getToken() || `local-token-${id}`, {
        id,
        username: users[index].username,
        email: users[index].email,
        roles,
        description: users[index].description,
        avatarUrl: users[index].avatarUrl
      });
    }

    return of(users[index]);
  }

  removeTrainerRole(id: number): Observable<StoredUser> {
    const users = this.getStoredUsers();
    const index = users.findIndex(u => u.id === id);
    if (index === -1) {
      return throwError(() => new Error('Usuario no encontrado.'));
    }

    if (users[index].roles.includes('ADMIN')) {
      return throwError(() => new Error('No se puede modificar el rol de un admin.'));
    }

    const roles = users[index].roles.filter(r => r !== 'ENTRENADOR' && r !== 'TRAINER');
    users[index] = { ...users[index], roles };
    this.saveUsers(users);

    if (this.currentUser?.id === id) {
      this.setSession(this.getToken() || `local-token-${id}`, {
        id,
        username: users[index].username,
        email: users[index].email,
        roles,
        description: users[index].description,
        avatarUrl: users[index].avatarUrl
      });
    }

    return of(users[index]);
  }

  deleteUser(id: number): Observable<void> {
    const users = this.getStoredUsers();
    const exists = users.some(u => u.id === id);
    if (!exists) {
      return throwError(() => new Error('Usuario no encontrado.'));
    }

    const remaining = users.filter(u => u.id !== id);
    this.saveUsers(remaining);

    if (this.currentUser?.id === id) {
      this.logout();
    }

    return of(void 0);
  }

  getProfile(): StoredUser | null {
    const current = this.currentUser;
    if (!current) {
      return null;
    }
    const users = this.getStoredUsers();
    return users.find(u => u.id === current.id) || null;
  }

  updateProfile(data: { name: string; email: string; password?: string; description?: string; avatarUrl?: string }): Observable<AuthUser> {
    const current = this.currentUser;
    if (!current) {
      return throwError(() => new Error('No hay sesion activa.'));
    }

    const users = this.getStoredUsers();
    const index = users.findIndex(u => u.id === current.id);
    if (index === -1) {
      return throwError(() => new Error('Usuario no encontrado.'));
    }

    const email = data.email.trim().toLowerCase();

    if (users.some(u => u.email === email && u.id !== current.id)) {
      return throwError(() => new Error('El correo ya esta en uso.'));
    }

    const updatedUser: StoredUser = {
      ...users[index],
      username: data.name.trim(),
      email,
      password: data.password ? data.password : users[index].password,
      description: data.description !== undefined ? data.description.trim() : (users[index].description || ''),
      avatarUrl: data.avatarUrl !== undefined ? data.avatarUrl.trim() : (users[index].avatarUrl || '')
    };

    users[index] = updatedUser;
    this.saveUsers(users);

    const authUser: AuthUser = {
      id: updatedUser.id,
      username: updatedUser.username,
      email: updatedUser.email,
      roles: updatedUser.roles,
      description: updatedUser.description,
      avatarUrl: updatedUser.avatarUrl
    };

    const token = this.getToken() || `local-token-${updatedUser.id}`;
    this.setSession(token, authUser);

    return of(authUser);
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    this.currentUserSubject.next(null);
  }
}
