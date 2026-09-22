"use client";

import { useTheme } from "@/context/ThemeContext";
import { IoMoonOutline, IoSunnyOutline } from "react-icons/io5";

interface ThemeToggleProps {
  className?: string;
  showLabel?: boolean;
}

export default function ThemeToggle({
  className = "",
  showLabel = false,
}: ThemeToggleProps) {
  const { theme, toggleTheme, mounted } = useTheme();

  const isDark = mounted ? theme === "dark" : false;

  return (
    <button
      type="button"
      onClick={toggleTheme}
      className={`relative inline-flex items-center justify-center p-2 rounded-full text-slate-700 dark:text-slate-200 hover:bg-slate-100 dark:hover:bg-slate-800 transition-colors duration-200 focus:outline-none focus:ring-2 focus:ring-primary focus:ring-offset-2 dark:focus:ring-offset-slate-900 cursor-pointer ${className}`}
      aria-label={isDark ? "Chuyển sang giao diện sáng" : "Chuyển sang giao diện tối"}
      title={isDark ? "Giao diện sáng (Light mode)" : "Giao diện tối (Dark mode)"}
    >
      <div className="relative w-5 h-5 flex items-center justify-center">
        {isDark ? (
          <IoSunnyOutline className="w-5 h-5 text-amber-400 transform transition-transform duration-300 rotate-0 hover:rotate-45" />
        ) : (
          <IoMoonOutline className="w-5 h-5 text-slate-700 dark:text-slate-200 transform transition-transform duration-300 hover:-rotate-12" />
        )}
      </div>
      {showLabel && (
        <span className="ml-2 text-sm font-medium text-slate-800 dark:text-slate-200">
          {isDark ? "Giao diện Sáng" : "Giao diện Tối"}
        </span>
      )}
    </button>
  );
}
