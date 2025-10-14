import { AuthResponse, BaseOutput } from "@/types/backend";
import { notification } from "antd";
import { Mutex } from "async-mutex";
import axiosClient from "axios";

interface AccessTokenResponse {
  access_token: string;
}

const instance = axiosClient.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL as string,
  withCredentials: true,
});

const mutex = new Mutex();
const NO_RETRY_HEADER = "x-no-retry";

const handleRefreshToken = async (): Promise<string | null> => {
  return await mutex.runExclusive(async () => {
    const res = await instance.get<BaseOutput<AuthResponse>>("/auth/refresh");
    if (res && res.data) {
      // return res.data.data.access_token;
      return res.data.data.access_token ?? null;
    } else {
      return null;
    }
  });
};
instance.interceptors.request.use(function (config) {
  if (
    typeof window !== "undefined" &&
    window &&
    window.localStorage &&
    window.localStorage.getItem("access_token")
  ) {
    config.headers.Authorization = `Bearer ${window.localStorage.getItem(
      "access_token"
    )}`;
  }
  if (!config.headers.Accept && config.headers["Content-Type"]) {
    config.headers.Accept = "application/json";
    config.headers["Content-Type"] = "application/json; charset=utf-8";
  }
  return config;
});

/**
 * Handle all responses. It is possible to add handlers
 * for requests, but it is omitted here for brevity.
 */
instance.interceptors.response.use(
  (res) => res.data,
  async (error) => {
    if (
      error.config &&
      error.response &&
      +error.response.status === 401 &&
      // error.errors === "Bad credentials" &&
      error.response.status === "FAILED" &&
      error.config.url !== "/auth/login" &&
      !error.config.headers[NO_RETRY_HEADER]
    ) {
      const access_token = await handleRefreshToken();
      error.config.headers[NO_RETRY_HEADER] = "true";
      if (access_token) {
        error.config.headers["Authorization"] = `Bearer ${access_token}`;
        localStorage.setItem("access_token", access_token);
        return instance(error.config);
      }
      if (
        error.config &&
        error.response &&
        error.response.status === "FAILED" &&
        +error.errors === 400 &&
        // error.errors === "Bad Request" &&
        error.config.url === "/auth/refresh" &&
        location.pathname.startsWith("/admin")
      ) {
        const message = error?.response?.data?.error ?? "Please login again";
      }
      if (error.response.status === "FAILED" && error.errors === "Forbidden") {
        notification.error({
          message: error?.response?.message ?? "",
          description: error?.response?.error ?? "",
        });
      }
    }
    return error?.response?.data ?? Promise.reject(error);
  }
);

/**
 * Replaces main `axios` instance with the custom-one.
 *
 * @param cfg - Axios configuration object.
 * @returns A promise object of a response of the HTTP request with the 'data' object already
 * destructured.
 */
// const axios = <T>(cfg: AxiosRequestConfig) => instance.request<any, T>(cfg);

// export default axios;

export default instance;
