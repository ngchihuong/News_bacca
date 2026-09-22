"use client";

import { AuthProvider } from "@/context/AuthContext";
import { ThemeProvider, useTheme } from "@/context/ThemeContext";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import { App, ConfigProvider, theme as antdTheme } from "antd";
import enUS from "antd/locale/en_US";
import { useState } from "react";

function AntdConfigProvider({ children }: { children: React.ReactNode }) {
  const { theme, mounted } = useTheme();

  const isDark = mounted ? theme === "dark" : false;

  return (
    <ConfigProvider
      locale={enUS}
      theme={{
        algorithm: isDark ? antdTheme.darkAlgorithm : antdTheme.defaultAlgorithm,
        token: {
          colorPrimary: "#FF6600",
          borderRadius: 8,
          colorBgContainer: isDark ? "#1E293B" : "#FFFFFF",
          colorBgElevated: isDark ? "#1E293B" : "#FFFFFF",
          colorBgLayout: isDark ? "#0F172A" : "#F8FAFC",
        },
      }}
    >
      <App>{children}</App>
    </ConfigProvider>
  );
}

export default function Providers({ children }: { children: React.ReactNode }) {
  // Dùng useState trong client component
  const [queryClient] = useState(() => new QueryClient());

  return (
    <QueryClientProvider client={queryClient}>
      <ThemeProvider>
        <AuthProvider>
          <AntdConfigProvider>{children}</AntdConfigProvider>
          <ReactQueryDevtools initialIsOpen={false} />
        </AuthProvider>
      </ThemeProvider>
    </QueryClientProvider>
  );
}

