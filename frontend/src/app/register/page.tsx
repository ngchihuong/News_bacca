"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { useForm } from "react-hook-form";
import { useMutation } from "@tanstack/react-query";
import { App, Spin } from "antd";
import { IoWarningOutline, IoCheckmarkCircleOutline } from "react-icons/io5";
import * as authApiClient from "@/lib/authApi";
import { useAppContext } from "@/context/AuthContext";

type RegisterFormData = {
  fullName: string;
  email: string;
  password: string;
  confirmPassword: string;
};

export default function RegisterPage() {
  const router = useRouter();
  const { notification } = App.useApp();
  const { isAuthenticated, isAdmin } = useAppContext();
  const [isCheckingAuth, setIsCheckingAuth] = useState<boolean>(true);

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
    watch,
    formState: { errors },
  } = useForm<RegisterFormData>({
    mode: "onBlur",
  });

  const password = watch("password");

  const mutation = useMutation({
    mutationFn: (data: { email: string; password: string; fullName: string }) =>
      authApiClient.register(data),
    onSuccess: (res: any) => {
      notification.success({
        message: "Đăng ký thành công!",
        description: "Tài khoản của bạn đã được tạo. Đang chuyển hướng sang trang đăng nhập...",
        icon: <IoCheckmarkCircleOutline className="text-green-500 text-2xl" />,
        duration: 4,
        placement: "topRight",
      });
      const registeredEmail = res?.data?.data?.email || "";
      setTimeout(() => {
        router.push(`/auth/login?email=${encodeURIComponent(registeredEmail)}`);
      }, 1200);
    },
    onError: (error: any) => {
      const errorMsg =
        error?.response?.data?.errors?.[0] ||
        error?.response?.data?.message ||
        error?.message ||
        "Đăng ký tài khoản thất bại. Vui lòng thử lại.";
      notification.error({
        message: "Đăng ký thất bại",
        description: errorMsg,
        duration: 5,
        placement: "topRight",
      });
    },
  });

  const onSubmit = handleSubmit((data: RegisterFormData) => {
    mutation.mutate({
      email: data.email.trim(),
      password: data.password,
      fullName: data.fullName.trim(),
    });
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
            Tạo tài khoản mới
          </h2>
          <p className="mt-2 text-sm text-slate-500 dark:text-slate-400">
            Tham gia cộng đồng tin tức & kết nối cùng các nhà báo xác thực
          </p>
        </div>

        {/* Form */}
        <form className="space-y-5" onSubmit={onSubmit}>
          {/* Full Name */}
          <div>
            <label
              htmlFor="fullName"
              className="block text-sm font-semibold text-slate-700 dark:text-slate-200 mb-1.5"
            >
              Họ và tên <span className="text-red-500">*</span>
            </label>
            <input
              id="fullName"
              type="text"
              className={`w-full px-4 py-2.5 bg-white dark:bg-slate-900 text-slate-900 dark:text-white border ${
                errors.fullName
                  ? "border-red-500 focus:ring-red-400"
                  : "border-slate-300 dark:border-slate-600 focus:border-[#FF6600] focus:ring-[#FF6600]/20"
              } rounded-xl shadow-sm focus:outline-none focus:ring-2 transition-all`}
              placeholder="Nguyễn Văn A"
              {...register("fullName", {
                required: "Vui lòng nhập họ và tên",
                minLength: {
                  value: 2,
                  message: "Họ và tên tối thiểu 2 ký tự",
                },
              })}
            />
            {errors.fullName && (
              <p className="mt-1.5 text-xs text-red-600 flex items-center gap-1 font-medium">
                <IoWarningOutline className="text-sm shrink-0" />
                {errors.fullName.message}
              </p>
            )}
          </div>

          {/* Email */}
          <div>
            <label
              htmlFor="email"
              className="block text-sm font-semibold text-slate-700 dark:text-slate-200 mb-1.5"
            >
              Địa chỉ Email <span className="text-red-500">*</span>
            </label>
            <input
              id="email"
              type="email"
              className={`w-full px-4 py-2.5 bg-white dark:bg-slate-900 text-slate-900 dark:text-white border ${
                errors.email
                  ? "border-red-500 focus:ring-red-400"
                  : "border-slate-300 dark:border-slate-600 focus:border-[#FF6600] focus:ring-[#FF6600]/20"
              } rounded-xl shadow-sm focus:outline-none focus:ring-2 transition-all`}
              placeholder="example@newsroom.vn"
              {...register("email", {
                required: "Vui lòng nhập địa chỉ email",
                pattern: {
                  value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                  message: "Địa chỉ email không đúng định dạng",
                },
              })}
            />
            {errors.email && (
              <p className="mt-1.5 text-xs text-red-600 flex items-center gap-1 font-medium">
                <IoWarningOutline className="text-sm shrink-0" />
                {errors.email.message}
              </p>
            )}
          </div>

          {/* Password */}
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
              placeholder="Tối thiểu 8 ký tự"
              {...register("password", {
                required: "Vui lòng nhập mật khẩu",
                minLength: {
                  value: 8,
                  message: "Mật khẩu phải có ít nhất 8 ký tự",
                },
              })}
            />
            {errors.password && (
              <p className="mt-1.5 text-xs text-red-600 flex items-center gap-1 font-medium">
                <IoWarningOutline className="text-sm shrink-0" />
                {errors.password.message}
              </p>
            )}
          </div>

          {/* Confirm Password */}
          <div>
            <label
              htmlFor="confirmPassword"
              className="block text-sm font-semibold text-slate-700 dark:text-slate-200 mb-1.5"
            >
              Xác nhận mật khẩu <span className="text-red-500">*</span>
            </label>
            <input
              id="confirmPassword"
              type="password"
              className={`w-full px-4 py-2.5 bg-white dark:bg-slate-900 text-slate-900 dark:text-white border ${
                errors.confirmPassword
                  ? "border-red-500 focus:ring-red-400"
                  : "border-slate-300 dark:border-slate-600 focus:border-[#FF6600] focus:ring-[#FF6600]/20"
              } rounded-xl shadow-sm focus:outline-none focus:ring-2 transition-all`}
              placeholder="Nhập lại mật khẩu"
              {...register("confirmPassword", {
                required: "Vui lòng xác nhận mật khẩu",
                validate: (value) =>
                  value === password || "Mật khẩu xác nhận không khớp",
              })}
            />
            {errors.confirmPassword && (
              <p className="mt-1.5 text-xs text-red-600 flex items-center gap-1 font-medium">
                <IoWarningOutline className="text-sm shrink-0" />
                {errors.confirmPassword.message}
              </p>
            )}
          </div>

          {/* Submit Button */}
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
                Đang tạo tài khoản...
              </span>
            ) : (
              "Đăng ký tài khoản"
            )}
          </button>
        </form>

        {/* Footer */}
        <div className="mt-6 text-center text-sm text-slate-600 dark:text-slate-400">
          Đã có tài khoản?{" "}
          <Link
            href="/auth/login"
            className="font-semibold text-[#FF6600] hover:underline"
          >
            Đăng nhập ngay
          </Link>
        </div>
      </div>
    </div>
  );
}
