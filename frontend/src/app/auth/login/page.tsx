"use client";

import { useState, useEffect, Suspense } from "react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { App, Spin } from "antd";
import { useForm } from "react-hook-form";
import { useRouter, useSearchParams } from "next/navigation";
import Link from "next/link";
import { IoWarningOutline } from "react-icons/io5";
import * as authApiClient from "@/lib/authApi";
import { useAppContext } from "@/context/AuthContext";

type LoginFormData = {
  username: string;
  password: string;
};

function LoginForm() {
  const queryClient = useQueryClient();
  const router = useRouter();
  const searchParams = useSearchParams();
  const { notification } = App.useApp();
  const { isAuthenticated, isAdmin, setUser, setIsAuthenticated, setIsAdmin } =
    useAppContext();
  const [isCheckingAuth, setIsCheckingAuth] = useState<boolean>(true);

  const emailParam = searchParams.get("email") || "";

  // GuestGuard: Redirect if already logged in
  useEffect(() => {
    if (isAuthenticated) {
      notification.info({
        message: "Thông báo",
        description: "Bạn đã đăng nhập hệ thống.",
        duration: 3,
        placement: "topRight",
      });
      if (isAdmin) {
        router.replace("/admin");
      } else {
        router.replace("/");
      }
    } else {
      setIsCheckingAuth(false);
    }
  }, [isAuthenticated, isAdmin, router, notification]);

  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<LoginFormData>({
    defaultValues: {
      username: emailParam,
      password: "",
    },
  });

  useEffect(() => {
    if (emailParam) {
      setValue("username", emailParam);
    }
  }, [emailParam, setValue]);

  const mutation = useMutation({
    mutationFn: (data: LoginFormData) =>
      authApiClient.login(data.username.trim(), data.password),
    onSuccess: (res: any) => {
      notification.success({
        message: "Đăng nhập thành công",
        description: "Chào mừng bạn quay trở lại NewsRoom!",
        duration: 3,
        placement: "topRight",
      });

      const authData = res?.data?.access_token ? res.data : (res?.data?.data || res?.data || res);
      const userData = authData?.user;
      const accessToken = authData?.access_token;

      if (accessToken) {
        localStorage.setItem("access_token", accessToken);
      }
      if (userData) {
        setUser(userData);
        setIsAuthenticated(true);
        localStorage.setItem("user", JSON.stringify(userData));

        const role = userData?.role;
        if (role === "ADMIN" || role === "ROLE_ADMIN") {
          setIsAdmin(true);
          router.push("/admin");
        } else {
          setIsAdmin(false);
          router.push("/");
        }
      } else if (accessToken) {
        setIsAuthenticated(true);
        router.push("/");
      }

      queryClient.clear();
    },
    onError: (error: any) => {
      const errorMsg =
        error?.response?.data?.errors?.[0] ||
        error?.response?.data?.message ||
        error?.message ||
        "Đăng nhập thất bại. Vui lòng kiểm tra lại email hoặc mật khẩu.";
      notification.error({
        message: "Đăng nhập thất bại",
        description: errorMsg,
        duration: 5,
        placement: "topRight",
      });
    },
  });

  const onSubmit = handleSubmit((data: LoginFormData) => {
    mutation.mutate(data);
  });

  if (isCheckingAuth && isAuthenticated) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-slate-50 dark:bg-slate-900">
        <Spin size="large" tip="Đang kiểm tra phiên đăng nhập..." />
      </div>
    );
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-50 dark:bg-slate-900 py-12 px-4 sm:px-6 lg:px-8 transition-colors">
      <div className="max-w-md w-full bg-white dark:bg-slate-800 p-8 rounded-2xl shadow-xl border border-slate-200 dark:border-slate-700">
        {/* Header */}
        <div className="text-center mb-8">
          <Link href="/" className="inline-block">
            <h1 className="text-3xl font-extrabold tracking-tight">
              <span className="text-[#FF6600]">News</span>
              <span className="text-[#13357B] dark:text-blue-400">Room</span>
            </h1>
          </Link>
          <h2 className="mt-4 text-xl font-bold text-slate-900 dark:text-white">
            Đăng nhập tài khoản
          </h2>
          <p className="mt-2 text-sm text-slate-500 dark:text-slate-400">
            Khám phá dòng tin mới nhất và chia sẻ góc nhìn của bạn
          </p>
        </div>

        {/* Form */}
        <form className="space-y-5" onSubmit={onSubmit}>
          <div>
            <label
              htmlFor="username"
              className="block text-sm font-semibold text-slate-700 dark:text-slate-200 mb-1.5"
            >
              Email hoặc Số điện thoại <span className="text-red-500">*</span>
            </label>
            <input
              id="username"
              type="text"
              className={`w-full px-4 py-2.5 bg-white dark:bg-slate-900 text-slate-900 dark:text-white border ${
                errors.username
                  ? "border-red-500 focus:ring-red-400"
                  : "border-slate-300 dark:border-slate-600 focus:border-[#FF6600] focus:ring-[#FF6600]/20"
              } rounded-xl shadow-sm focus:outline-none focus:ring-2 transition-all`}
              placeholder="Nhập email hoặc số điện thoại"
              {...register("username", {
                required: "Vui lòng nhập email hoặc số điện thoại",
              })}
            />
            {errors.username && (
              <p className="mt-1.5 text-xs text-red-600 flex items-center gap-1 font-medium">
                <IoWarningOutline className="text-sm shrink-0" />
                {errors.username.message}
              </p>
            )}
          </div>

          <div>
            <label
              htmlFor="password"
              className="block text-sm font-semibold text-slate-700 dark:text-slate-200 mb-1.5"
            >
              Mật khẩu <span className="text-red-500">*</span>
            </label>
            <input
              id="password"
              type="password"
              className={`w-full px-4 py-2.5 bg-white dark:bg-slate-900 text-slate-900 dark:text-white border ${
                errors.password
                  ? "border-red-500 focus:ring-red-400"
                  : "border-slate-300 dark:border-slate-600 focus:border-[#FF6600] focus:ring-[#FF6600]/20"
              } rounded-xl shadow-sm focus:outline-none focus:ring-2 transition-all`}
              placeholder="Nhập mật khẩu"
              {...register("password", {
                required: "Vui lòng nhập mật khẩu",
              })}
            />
            {errors.password && (
              <p className="mt-1.5 text-xs text-red-600 flex items-center gap-1 font-medium">
                <IoWarningOutline className="text-sm shrink-0" />
                {errors.password.message}
              </p>
            )}
          </div>

          <button
            type="submit"
            disabled={mutation.isPending}
            className="w-full bg-[#FF6600] hover:bg-[#FF6600]/90 text-white font-semibold py-3 px-4 rounded-xl shadow-md hover:shadow-lg transition-all disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center mt-2"
          >
            {mutation.isPending ? (
              <span className="flex items-center gap-2">
                <svg
                  className="animate-spin h-5 w-5 text-white"
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                >
                  <circle
                    className="opacity-25"
                    cx="12"
                    cy="12"
                    r="10"
                    stroke="currentColor"
                    strokeWidth="4"
                  ></circle>
                  <path
                    className="opacity-75"
                    fill="currentColor"
                    d="M4 12a8 8 0 018-8v8H4z"
                  ></path>
                </svg>
                Đang đăng nhập...
              </span>
            ) : (
              "Đăng nhập"
            )}
          </button>
        </form>

        {/* Footer */}
        <div className="mt-6 text-center text-sm text-slate-600 dark:text-slate-400">
          Chưa có tài khoản?{" "}
          <Link
            href="/register"
            className="font-semibold text-[#FF6600] hover:underline"
          >
            Đăng ký ngay
          </Link>
        </div>
      </div>
    </div>
  );
}

export default function LoginPage() {
  return (
    <Suspense
      fallback={
        <div className="min-h-screen flex items-center justify-center bg-slate-50 dark:bg-slate-900">
          <Spin size="large" />
        </div>
      }
    >
      <LoginForm />
    </Suspense>
  );
}
