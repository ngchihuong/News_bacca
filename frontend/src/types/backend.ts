export interface BaseOutput<T> {
  errors?: string[];
  status?: string;
  message: string;
  meta: any;
  data: T;
}
export interface UserLogin {
  type?: string;
  id?: string;
  name?: string;
  username?: string;
  role?: string;
}
export interface AuthResponse {
  user?: UserLogin;
  responseCookie?: string;
  access_token?: string;
}

export interface UserDTO {
  id: string;
  username?: string;
  fullName: string;
  email: string;
  phone?: string;
  roles?: string;
  age?: number;
  avatarUrl?: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  fullName: string;
  username?: string;
}
