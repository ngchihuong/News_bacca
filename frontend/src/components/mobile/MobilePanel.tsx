"use client";

import Link from "next/link";
import { useEffect, useRef, useState } from "react";
import { IoIosClose } from "react-icons/io";
import { IoMenu } from "react-icons/io5";
import ThemeToggle from "../ThemeToggle";

export default function MobilePanel() {
  const [isMenuOpen, setIsMenuOpen] = useState(false);

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
  }, []);
  return (
    <>
      <button
        className="block bg-none border-none text-2xl cursor-pointer text-black dark:text-slate-200 
      transition-colors duration-200 rounded-lg hover:bg-[#f0f0f0] dark:hover:bg-slate-800 md:hidden"
        onClick={() => setIsMenuOpen(!isMenuOpen)}
        aria-label="Mở menu"
      >
        <IoMenu className="w-7 h-7 text-grey-300 dark:text-slate-300" />
      </button>
      <div
        className={`fixed top-14 md:hidden bottom-0 left-0 right-0 bg-[rgba(0,0,0,0.5)] z-[140] transition-all duration-300
  ${
    isMenuOpen
      ? "opacity-100 visible pointer-events-auto"
      : "opacity-0 invisible pointer-events-none"
  }`}
      >
        <div className=" h-screen" ref={dropdownRef} onClick={toggle}>
          <aside
            className={`h-[calc(100vh-3.5rem)] fixed left-0 w-[17.5rem] xs:w-60 bg-white dark:bg-slate-900 border-r border-[#e0e0e0] dark:border-slate-800
              py-6 px-4 overflow-y-auto shadow-[2px_0px_10px_rgba(0,0,0,0.1)] transition-transform duration-300 ease-in text-slate-900 dark:text-slate-100
              ${isMenuOpen ? "translate-x-0" : "-translate-x-full"}`}
            onClick={(e) => e.stopPropagation()}
          >
            {/* Theme switcher section for mobile */}
            <div className="mb-6 pb-4 border-b border-gray-100 dark:border-slate-800">
              <h3 className="text-sm font-bold text-[#111] dark:text-slate-100 mb-3 uppercase tracking-wider">
                Giao diện
              </h3>
              <div className="flex items-center p-2 rounded-lg bg-gray-50 dark:bg-slate-800">
                <ThemeToggle showLabel={true} className="!p-0 !justify-start w-full" />
              </div>
            </div>

            <div className="relative mb-8">
              <h3 className="text-sm font-bold text-[#111] dark:text-slate-100 mb-4 uppercase tracking-wider">
                Categories
              </h3>
              <Link
                href="#"
                className="block py-3 px-4 text-[#333] dark:text-slate-300 rounded-lg mb-1 text-sm transition-colors duration-200
                hover:bg-[#f0f0f0] dark:hover:bg-slate-800"
              >
                🔬 Technology
              </Link>
              <Link
                href="#"
                className="block py-3 px-4 text-[#333] rounded-lg mb-1 text-sm transition-colors duration-200
                hover:bg-[#f0f0f0]"
              >
                🌍 Environment
              </Link>
              <Link
                href="#"
                className="block py-3 px-4 text-[#333] rounded-lg mb-1 text-sm transition-colors duration-200
                hover:bg-[#f0f0f0]"
              >
                ⚽ Sports
              </Link>
              <Link
                href="#"
                className="block py-3 px-4 text-[#333] rounded-lg mb-1 text-sm transition-colors duration-200
                hover:bg-[#f0f0f0]"
              >
                💼 Business
              </Link>
              <Link
                href="#"
                className="block py-3 px-4 text-[#333] rounded-lg mb-1 text-sm transition-colors duration-200
                hover:bg-[#f0f0f0]"
              >
                🏥 Health
              </Link>
              <Link
                href="#"
                className="block py-3 px-4 text-[#333] rounded-lg mb-1 text-sm transition-colors duration-200
                hover:bg-[#f0f0f0]"
              >
                🎨 Culture
              </Link>
            </div>
            <div
              className="absolute top-2 right-2 p-1 text-slate-700 dark:text-slate-200 cursor-pointer hover:bg-slate-100 dark:hover:bg-slate-800 rounded-lg"
              onClick={() => setIsMenuOpen(false)}
            >
              <IoIosClose className="text-3xl font-bold" />
            </div>
            <div className="mb-8">
              <h3 className="text-sm font-bold text-[#111] dark:text-slate-100 mb-4 uppercase tracking-wider">
                Group
              </h3>
              <div className="flex gap-3 rounded-lg mb-2 p-1 transition-colors duration-200 cursor-pointer hover:bg-[#f0f0f0] dark:hover:bg-slate-800">
                <div
                  className="w-10 h-10 text-[16px] xs:w-9 xs:h-9 xs:text-lg md:w-12 md:h-12 rounded-full bg-[#efefef] dark:bg-slate-700 flex items-center
                        justify-center text-2xl cursor-pointer transition-colors duration-200 hover:bg-[#e0e0e0] dark:hover:bg-slate-600"
                >
                  <img
                    src="https://ngchihuong.github.io/cv/assets/thuxinhdep-nlvzQFLc.jpg"
                    alt=""
                    className="rounded-full relative"
                  />
                </div>
                <div className="flex-1 min-w-0">
                  <p className="text-sm text-[#333] dark:text-slate-200 mb-1 overflow-hidden text-ellipsis whitespace-nowrap">
                    Gờ rúp các bác
                  </p>
                  <span className="text-xs text-[#767676] dark:text-slate-400">36 members</span>
                </div>
              </div>
            </div>
          </aside>
        </div>
      </div>
    </>
  );
}
