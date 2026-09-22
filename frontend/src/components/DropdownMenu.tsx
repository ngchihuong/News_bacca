"use client";

import { useAppContext } from "@/context/AuthContext";
import Link from "next/link";
import { useEffect, useRef } from "react";
import { FaAngleRight, FaRegUserCircle, FaShieldAlt } from "react-icons/fa";
import { IoIosArrowDropdownCircle, IoIosLogIn, IoIosLogOut } from "react-icons/io";
import { IoSettingsOutline } from "react-icons/io5";
import { useRouter } from "next/navigation";

type Props = {
  isMenuOpen: boolean;
  setIsMenuOpen: (v: boolean) => void;
};

export default function DropdownMenu({ isMenuOpen, setIsMenuOpen }: Props) {
  const { user, isAuthenticated, isAdmin, logout } = useAppContext();
  const router = useRouter();
  const dropdownRef = useRef<HTMLDivElement | null>(null);

  const toggle = () => setIsMenuOpen(!isMenuOpen);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        dropdownRef.current &&
        !dropdownRef.current.contains(event.target as Node)
      ) {
        setIsMenuOpen(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, [setIsMenuOpen]);

  const handleLogout = () => {
    logout();
    setIsMenuOpen(false);
    router.push("/auth/login");
  };

  const getAvatarSrc = () => {
    if (user?.avatarUrl) {
      if (user.avatarUrl.startsWith("http")) {
        return user.avatarUrl;
      }
      const backendUrl = process.env.NEXT_PUBLIC_API_URL?.replace("/api/v1", "") || "http://localhost:8080";
      return `${backendUrl}${user.avatarUrl}`;
    }
    return "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&auto=format&fit=crop&q=80";
  };

  return (
    <div ref={dropdownRef} className="relative">
      {/* Button toggle avatar */}
      <div
        className="w-9 h-9 md:w-11 md:h-11 rounded-full bg-gray-100 flex items-center justify-center cursor-pointer transition-all duration-200 hover:ring-2 hover:ring-orange-500 relative"
        onClick={toggle}
      >
        {isAuthenticated && user ? (
          <img
            src={getAvatarSrc()}
            alt="Avatar"
            className="w-full h-full object-cover rounded-full"
            onError={(e) => {
              (e.target as HTMLImageElement).src =
                "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&auto=format&fit=crop&q=80";
            }}
          />
        ) : (
          <div className="w-full h-full rounded-full bg-gray-200 flex items-center justify-center text-gray-600">
            <FaRegUserCircle className="text-xl" />
          </div>
        )}
        <div className="absolute bg-white shadow h-4 w-4 rounded-full right-0 bottom-0 flex items-center justify-center border border-gray-200">
          <IoIosArrowDropdownCircle className="text-gray-700 text-xs" />
        </div>
      </div>

      {/* Dropdown Box */}
      {isMenuOpen && (
        <div className="absolute right-0 mt-2 bg-white dark:bg-slate-900 shadow-xl rounded-xl w-72 p-2 border border-gray-100 dark:border-slate-800 z-50 text-gray-800 dark:text-slate-100 animate-in fade-in duration-150">
          {isAuthenticated && user ? (
            <>
              {/* Logged-in Header */}
              <div className="px-3 py-2.5 bg-gradient-to-r from-orange-50 to-amber-50 dark:from-slate-800 dark:to-slate-850 rounded-lg mb-2">
                <div className="font-semibold text-gray-900 dark:text-slate-100 truncate">
                  {user.name || user.username || "Thành viên"}
                </div>
                <div className="text-xs text-gray-500 dark:text-slate-400 truncate">
                  {user.email || user.username}
                </div>
                {user.role && (
                  <span className="inline-block mt-1 px-2 py-0.5 text-[11px] font-medium bg-orange-100 dark:bg-orange-950/60 text-orange-700 dark:text-orange-300 rounded-full">
                    {user.role.replace("ROLE_", "")}
                  </span>
                )}
              </div>

              {/* Navigation Items */}
              <div className="space-y-1">
                {isAdmin && (
                  <Link
                    href="/admin"
                    onClick={() => setIsMenuOpen(false)}
                    className="flex items-center justify-between px-3 py-2 text-sm font-medium text-gray-700 dark:text-slate-200 hover:bg-orange-50 dark:hover:bg-slate-800 hover:text-orange-600 dark:hover:text-orange-400 rounded-lg transition-colors"
                  >
                    <div className="flex items-center gap-2.5">
                      <div className="bg-orange-100 dark:bg-orange-950/60 text-orange-600 dark:text-orange-400 p-1.5 rounded-lg">
                        <FaShieldAlt className="text-base" />
                      </div>
                      <span>Trang quản trị (Admin)</span>
                    </div>
                    <FaAngleRight className="text-xs text-gray-400" />
                  </Link>
                )}

                <Link
                  href="/settings/profile"
                  onClick={() => setIsMenuOpen(false)}
                  className="flex items-center justify-between px-3 py-2 text-sm font-medium text-gray-700 dark:text-slate-200 hover:bg-gray-50 dark:hover:bg-slate-800 hover:text-orange-600 dark:hover:text-orange-400 rounded-lg transition-colors"
                >
                  <div className="flex items-center gap-2.5">
                    <div className="bg-gray-100 dark:bg-slate-800 text-gray-600 dark:text-slate-300 p-1.5 rounded-lg">
                      <IoSettingsOutline className="text-base" />
                    </div>
                    <span>Cài đặt thông tin cá nhân</span>
                  </div>
                  <FaAngleRight className="text-xs text-gray-400" />
                </Link>
              </div>

              {/* Logout Button */}
              <div className="pt-2 mt-2 border-t border-gray-100 dark:border-slate-800">
                <button
                  onClick={handleLogout}
                  className="w-full flex items-center gap-2.5 px-3 py-2 text-sm font-medium text-red-600 dark:text-red-400 hover:bg-red-50 dark:hover:bg-red-950/40 rounded-lg transition-colors"
                >
                  <div className="bg-red-100 dark:bg-red-950/60 text-red-600 dark:text-red-400 p-1.5 rounded-lg">
                    <IoIosLogOut className="text-base" />
                  </div>
                  <span>Đăng xuất</span>
                </button>
              </div>
            </>
          ) : (
            <>
              {/* Not Logged In */}
              <div className="p-2 mb-2 bg-gray-50 dark:bg-slate-800 rounded-lg text-center">
                <p className="text-xs text-gray-500 dark:text-slate-400 mb-2">Đăng nhập để trải nghiệm đầy đủ các tính năng</p>
                <div className="flex gap-2">
                  <Link
                    href="/auth/login"
                    onClick={() => setIsMenuOpen(false)}
                    className="flex-1 py-1.5 bg-orange-600 text-white rounded-lg text-xs font-semibold hover:bg-orange-700 text-center transition"
                  >
                    Đăng nhập
                  </Link>
                  <Link
                    href="/auth/register"
                    onClick={() => setIsMenuOpen(false)}
                    className="flex-1 py-1.5 bg-white dark:bg-slate-700 border border-gray-300 dark:border-slate-600 text-gray-700 dark:text-slate-200 rounded-lg text-xs font-semibold hover:bg-gray-50 dark:hover:bg-slate-600 text-center transition"
                  >
                    Đăng ký
                  </Link>
                </div>
              </div>

              <div className="space-y-1">
                <Link
                  href="/auth/login"
                  onClick={() => setIsMenuOpen(false)}
                  className="flex items-center justify-between px-3 py-2 text-sm font-medium text-gray-700 dark:text-slate-200 hover:bg-gray-50 dark:hover:bg-slate-800 rounded-lg transition-colors"
                >
                  <div className="flex items-center gap-2.5">
                    <div className="bg-gray-100 dark:bg-slate-800 text-gray-600 dark:text-slate-300 p-1.5 rounded-lg">
                      <IoIosLogIn className="text-base" />
                    </div>
                    <span>Đăng nhập tài khoản</span>
                  </div>
                  <FaAngleRight className="text-xs text-gray-400" />
                </Link>
                <Link
                  href="/auth/register"
                  onClick={() => setIsMenuOpen(false)}
                  className="flex items-center justify-between px-3 py-2 text-sm font-medium text-gray-700 dark:text-slate-200 hover:bg-gray-50 dark:hover:bg-slate-800 rounded-lg transition-colors"
                >
                  <div className="flex items-center gap-2.5">
                    <div className="bg-orange-100 dark:bg-orange-950/60 text-orange-600 dark:text-orange-400 p-1.5 rounded-lg">
                      <FaRegUserCircle className="text-base" />
                    </div>
                    <span>Tạo tài khoản mới</span>
                  </div>
                  <FaAngleRight className="text-xs text-gray-400" />
                </Link>
              </div>
            </>
          )}
        </div>
      )}
    </div>
  );
}
