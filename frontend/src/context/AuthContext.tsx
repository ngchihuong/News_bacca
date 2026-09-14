"use client";

import React, { createContext, useState, useContext, useEffect } from "react";
import * as api from "@/lib/authApi";
import {  useQuery } from "@tanstack/react-query";
import { UserLogin } from "@/types/backend";

interface User {
  id: string;
  type: string;
  username: string;
  role: string;
}

interface AuthContext {
  isAuthenticated: boolean;
  setIsAuthenticated: (v: boolean) => void;
  isAdmin: boolean;
  setIsAdmin: (v: boolean) => void;
  user: UserLogin | null;
  setUser: (v: UserLogin | null) => void;
  login: (username: string, password: string) => Promise<any>;
  logout: () => void;
}

const AuthContext = createContext<AuthContext | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<UserLogin | null>(null);
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [isAdmin, setIsAdmin] = useState<boolean>(false);

  const { data, error, isLoading } = useQuery({
    queryKey: ["account"],
    queryFn: api.getAccount,
  });
  useEffect(() => {
    if (data?.data?.data) {
      setUser(data.data.data);
      setIsAuthenticated(true);
      if (data.data.data.role === "admin" || data.data.data.role === "ADMIN" || data.data.data.role === "ROLE_ADMIN") {
        setIsAdmin(true);
      } else {
        setIsAdmin(false);
      }
    } else if (error) {
      setUser(null);
      setIsAuthenticated(false);
      setIsAdmin(false);
    }
  }, [data, error]);

  const login = async (username: string, password: string) => {
    const res = await api.login(username, password);
    if (res?.data?.data) {
      const userData = res.data.data.user;
      setUser(userData || null);
      setIsAuthenticated(true);
      if (res.data.data.access_token) {
        localStorage.setItem("access_token", res.data.data.access_token);
      }
      if (userData) {
        localStorage.setItem("user", JSON.stringify(userData));
        if (userData.role === "ADMIN" || userData.role === "admin" || userData.role === "ROLE_ADMIN") {
          setIsAdmin(true);
        } else {
          setIsAdmin(false);
        }
      }
    }
    return res;
  };

  const logout = () => {
    localStorage.removeItem("access_token");
    localStorage.removeItem("user");
    setUser(null);
    setIsAuthenticated(false);
    setIsAdmin(false);
  };

  return (
    <AuthContext.Provider
      value={{ user, setUser, isAuthenticated, setIsAuthenticated, isAdmin, setIsAdmin, login, logout }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAppContext() {
  const context = useContext(AuthContext);
  return context as AuthContext;
}

export const useAuth = useAppContext;
