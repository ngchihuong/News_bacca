"use client";

import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import { App, ConfigProvider } from "antd";
import enUS from "antd/locale/en_US";
import { useState } from "react";

export default function Providers({ children }: { children: React.ReactNode }) {
  // Dùng useState trong client component
  const [queryClient] = useState(() => new QueryClient());

  return (
    <QueryClientProvider client={queryClient}>
      <ConfigProvider locale={enUS}>
        <App>{children}</App>
      </ConfigProvider>
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  );
}
