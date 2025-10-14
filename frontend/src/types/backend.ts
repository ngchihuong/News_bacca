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
  role?: string;
}
export interface AuthResponse {
  user?: UserLogin;
  responseCookie?: string;
  access_token?: string;
}
