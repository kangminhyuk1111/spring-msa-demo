export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
}

export interface LoginResponse {
  accessToken: string;
  expiresAt: number;
}

export interface User {
  id: number;
  name: string;
  email: string;
}