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
  email?: string;
  phone?: string;
  role?: string;
  avatarUrl?: string;
  bio?: string;
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
  bio?: string;
}

export interface UserProfile {
  id: string;
  username: string;
  email: string;
  fullName?: string;
  phone?: string;
  bio?: string;
  avatarUrl?: string;
  role?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface UpdateProfileRequest {
  fullName?: string;
  phone?: string;
  bio?: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  fullName: string;
  username?: string;
}

export interface PublicUserProfile {
  id: string;
  username: string;
  fullName?: string;
  avatarUrl?: string;
  bio?: string;
  isJournalistVerified: boolean;
  journalistOrganization?: string;
  followersCount: number;
  followingCount: number;
  createdAt?: string;
}

