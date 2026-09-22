import { AuthResponse, BaseOutput, RegisterRequest, UserDTO, UserLogin } from "@/types/backend";
import axios from "@/utils/axios-customize";

const API_URL =
  process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080/api/v1";

export const login = (username: string, password: string) => {
  return axios.post<BaseOutput<AuthResponse>>(`${API_URL}/auth/login`, {
    username,
    password,
  });
};

export const register = (data: RegisterRequest) => {
  return axios.post<BaseOutput<UserDTO>>(`${API_URL}/auth/register`, data);
};

export const getAccount = () => {
  return axios.get<BaseOutput<UserLogin>>(`${API_URL}/auth/account`);
};
