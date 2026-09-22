"use client";

import React, { createContext, useState, useContext, useEffect } from "react";
import * as api from "@/lib/authApi";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import { UserLogin } from "@/types/backend";

interface AuthContext {
  isAuthenticated: boolean;
  setIsAuthenticated: (v: boolean) => void;
  isAdmin: boolean;
  setIsAdmin: (v: boolean) => void;
  user: UserLogin | null;
  setUser: (v: UserLogin | null) => void;
  login: (username: string, password: string) => Promise<any>;
  logout: () => void;
  refetchAccount: () => Promise<any>;
}

const AuthContext = createContext<AuthContext | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const queryClient = useQueryClient();

  // Khởi tạo state từ localStorage để giao diện tức thì, không bị nhấp nháy
  const [user, setUser] = useState<UserLogin | null>(() => {
    if (typeof window !== "undefined") {
      try {
        const saved = localStorage.getItem("user");
        if (saved) return JSON.parse(saved);
      } catch (e) {}
    }
    return null;
  });

  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(() => {
    if (typeof window !== "undefined") {
      return !!localStorage.getItem("access_token");
    }
    return false;
  });

  const [isAdmin, setIsAdmin] = useState<boolean>(() => {
    if (typeof window !== "undefined") {
      try {
        const saved = localStorage.getItem("user");
        if (saved) {
          const u = JSON.parse(saved);
          return u.role === "ADMIN" || u.role === "admin" || u.role === "ROLE_ADMIN";
        }
      } catch (e) {}
    }
    return false;
  });

  // Gọi API lấy thông tin account khi có token hoặc khi trạng thái đã đăng nhập
  const hasToken = typeof window !== "undefined" && !!localStorage.getItem("access_token");
  const { data, error, refetch: refetchAccount } = useQuery({
    queryKey: ["account"],
    queryFn: api.getAccount,
    enabled: typeof window !== "undefined" && (isAuthenticated || hasToken),
    retry: 1,
    staleTime: 30000,
  });

  useEffect(() => {
    if (data) {
      // Hỗ trợ cả trường hợp res.data (do interceptor) lẫn res.data.data
      const raw: any = data;
      const accountData: UserLogin = raw?.data?.id ? raw.data : (raw?.data?.data || raw?.data || raw);

      if (accountData && (accountData.id || accountData.username || accountData.email)) {
        setUser(accountData);
        setIsAuthenticated(true);
        if (typeof window !== "undefined") {
          localStorage.setItem("user", JSON.stringify(accountData));
        }
        if (accountData.role === "admin" || accountData.role === "ADMIN" || accountData.role === "ROLE_ADMIN") {
          setIsAdmin(true);
        } else {
          setIsAdmin(false);
        }
      }
    } else if (error) {
      // Nếu API 401 hoặc lỗi mà không có token thì xóa state
      if (typeof window !== "undefined" && !localStorage.getItem("access_token")) {
        setUser(null);
        setIsAuthenticated(false);
        setIsAdmin(false);
      }
    }
  }, [data, error]);

  const login = async (username: string, password: string) => {
    const res: any = await api.login(username, password);
    // Bóc tách dữ liệu an toàn
    const authData = res?.data?.access_token ? res.data : (res?.data?.data || res?.data || res);
    const userData: UserLogin = authData?.user;
    const accessToken = authData?.access_token;

    if (accessToken) {
      localStorage.setItem("access_token", accessToken);
    }
    if (userData) {
      setUser(userData);
      setIsAuthenticated(true);
      localStorage.setItem("user", JSON.stringify(userData));
      if (userData.role === "ADMIN" || userData.role === "admin" || userData.role === "ROLE_ADMIN") {
        setIsAdmin(true);
      } else {
        setIsAdmin(false);
      }
    } else if (accessToken) {
      setIsAuthenticated(true);
      refetchAccount();
    }
    return res;
  };

  const logout = () => {
    localStorage.removeItem("access_token");
    localStorage.removeItem("user");
    setUser(null);
    setIsAuthenticated(false);
    setIsAdmin(false);
    queryClient.removeQueries({ queryKey: ["account"] });
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        setUser,
        isAuthenticated,
        setIsAuthenticated,
        isAdmin,
        setIsAdmin,
        login,
        logout,
        refetchAccount,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAppContext() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAppContext must be used within an AuthProvider");
  }
  return context;
}

export const useAuth = useAppContext;
