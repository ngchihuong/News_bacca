import axios from "axios";
import { Mutex } from "async-mutex";
import { notification } from "antd";
import { BaseOutput, AuthResponse } from "@/types/backend";

const API_URL = process.env.NEXT_PUBLIC_API_URL as string;

const instance = axios.create({
  baseURL: API_URL,
  withCredentials: true, // bắt buộc cho cookie refresh_token
});

const mutex = new Mutex();
const NO_RETRY_HEADER = "x-no-retry";

const handleRefreshToken = async (): Promise<string | null | undefined> => {
  return await mutex.runExclusive(async () => {
    try {
      const res = await axios.get<BaseOutput<AuthResponse>>(
        `${API_URL}/auth/refresh`,
        { withCredentials: true }
      );
      return res.data.data.access_token;
    } catch (err) {
      return null;
    }
  });
};

// 🧩 Request interceptor
instance.interceptors.request.use((config) => {
  if (
    typeof window !== "undefined" &&
    window &&
    window.localStorage &&
    window.localStorage.getItem("access_token")
  ) {
    config.headers.Authorization =
      "Bearer " + window.localStorage.getItem("access_token");
  }
  if (!config.headers.Accept && config.headers["Content-Type"]) {
    config.headers.Accept = "application/json";
    config.headers["Content-Type"] = "application/json; charset=utf-8";
  }
  return config;
});

// 🧩 Response interceptor
instance.interceptors.response.use(
  (res) => res.data,
  async (error) => {
    if (
      error.config &&
      error.response &&
      +error.response.status === 401 &&
      error.config.url !== "/api/v1/auth/login" &&
      !error.config.headers[NO_RETRY_HEADER]
    ) {
      const access_token = await handleRefreshToken();
      error.config.headers[NO_RETRY_HEADER] = "true";
      if (access_token) {
        error.config.headers["Authorization"] = `Bearer ${access_token}`;
        localStorage.setItem("access_token", access_token);
        return instance.request(error.config);
      }
    }

    if (
      error.config &&
      error.response &&
      +error.response.status === 400 &&
      error.config.url === "/api/v1/auth/refresh" &&
      location.pathname.startsWith("/admin")
    ) {
      const message =
        error?.response?.data?.error ?? "Có lỗi xảy ra, vui lòng login.";
      //dispatch redux action
    }

    if (+error.response.status === 403) {
      notification.error({
        message: error?.response?.data?.message ?? "",
        description: error?.response?.data?.error ?? "",
      });
    }

    return error?.response?.data ?? Promise.reject(error);
  }
);

export default instance;
