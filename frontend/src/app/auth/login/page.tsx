"use client";

import { QueryClient, useMutation } from "@tanstack/react-query";
import { App } from "antd";
import { useForm } from "react-hook-form";
import { useRouter } from "next/navigation";
import { IoWarningOutline } from "react-icons/io5";
import { useState } from "react";
import * as authApiClient from "@/lib/authApi";
import { AuthResponse, BaseOutput } from "@/types/backend";
import { useAppContext } from "@/context/AuthContext";
import { set } from "date-fns";

type LoginFormData = {
  username: string;
  password: string;
};
export default function login() {
  const [loading, setLoading] = useState<boolean>(false);
  const queryClient = new QueryClient();
  const router = useRouter();
  const { notification } = App.useApp();
  const {setUser, setIsAuthenticated, setIsAdmin} = useAppContext();
  
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginFormData>();
  const mutation = useMutation({
    mutationFn: (data: { username: string; password: string }) =>
      authApiClient.login(data.username, data.password),
    onSuccess: async (data: any) => {
      notification.success({
        message: "Login Successful",
        description: data.message,
        duration: 5,
        placement: "topRight",
      });
      if (data.data) {
        setUser(data.data.user);
        setIsAuthenticated(true);

        localStorage.setItem("access_token", data!.data!.access_token!);
        localStorage.setItem("user", JSON.stringify(data!.data!.user!));
      }
      const role = data?.data?.user?.role;
      if (role === "ADMIN") {
        setIsAdmin(true);
        router.push("/admin");
      }else router.push("/");

      queryClient.clear();
    },
    onError: (error: Error) => {
      notification.error({
        message: "Failed Sign-in!",
        description: error.message,
        duration: 5,
        placement: "topRight",
      });
    },
  });
  const onSubmit = handleSubmit((data: any) => {
    mutation.mutate(data);
  });
  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-gray-100 to-gray-200">
      <div className="bg-white p-8 rounded-lg shadow-2xl w-full max-w-md">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-gray-800">
            <span className="text-primary">News</span>
            <span className="text-secondary">Room</span>
          </h1>
          <p className="text-gray-600 mt-2">Sign-in</p>
        </div>

        <form className="space-y-6" onSubmit={onSubmit}>
          <div>
            <label
              htmlFor="username"
              className="block text-sm font-medium text-gray-700 mb-2"
            >
              Email
            </label>
            <input
              id="username"
              type="text"
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-1"
              placeholder="Enter your username"
              {...register("username", { required: "Username is required" })}
            />
            {errors.username && (
              <span className="error mt-2 text-red-700 flex flex-row gap-x-1">
                <span className="flex items-center space-x-4">
                  <IoWarningOutline className=" text-2xl align-text-bottom" />
                  {errors.username.message}
                </span>
              </span>
            )}
          </div>

          <div>
            <label
              htmlFor="password"
              className="block text-sm font-medium text-gray-700 mb-2"
            >
              Password
            </label>
            <input
              id="password"
              type="password"
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-1"
              placeholder="Enter your password"
              {...register("password", { required: "Password is required" })}
            />
            {errors.password && (
              <span className="error mt-2 text-red-700 flex flex-row gap-x-1">
                <span className="flex items-center space-x-4">
                  <IoWarningOutline className=" text-2xl align-text-bottom" />
                  {errors.password.message}
                </span>
              </span>
            )}
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-primary hover:bg-primary/90 text-white font-semibold py-3 px-4 rounded-lg transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {loading ? "Logging in..." : "Login"}
          </button>
        </form>
      </div>
    </div>
  );
}
