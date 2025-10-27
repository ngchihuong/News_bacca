"use client";

import Link from "next/link";
import { useEffect, useRef, useState } from "react";
import { IoIosClose } from "react-icons/io";
import { IoMenu } from "react-icons/io5";

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
        className="block bg-none border-none text-2xl cursor-pointer text-black 
      transition-colors duration-200 rounded-lg hover:bg-[#f0f0f0] md:hidden"
        onClick={() => setIsMenuOpen(!isMenuOpen)}
      >
        <IoMenu className="w-7 h-7 text-grey-300" />
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
            className={`h-[calc(100vh-3.5rem)] fixed left-0 w-[17.5rem] xs:w-60 bg-white border-r border-[#e0e0e0]
              py-6 px-4 overflow-y-auto shadow-[2px_0px_10px_rgba(0,0,0,0.1)] transition-transform duration-300 ease-in
              ${isMenuOpen ? "translate-x-0" : "-translate-x-full"}`}
            onClick={(e) => e.stopPropagation()}
          >
            <div className="relative mb-8">
              <h3 className="text-sm font-bold text-[#111] mb-4 uppercase tracking-wider">
                Categories
              </h3>
              <Link
                href="#"
                className="block py-3 px-4 text-[#333] rounded-lg mb-1 text-sm transition-colors duration-200
                hover:bg-[#f0f0f0]"
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
              className="absolute top-0 right-0 p-1"
              onClick={() => setIsMenuOpen(false)}
            >
              <IoIosClose className="text-3xl font-bold" />
            </div>
            <div className="mb-8">
              <h3 className="text-sm font-bold text-[#111] mb-4 uppercase tracking-wider">
                Group
              </h3>
              <div className="flex gap-3 rounded-lg mb-2 transition-colors duration-200 cursor-pointer hover:bg-[#f0f0f0]">
                <div
                  className="w-10 h-10 text-[16px] xs:w-9 xs:h-9 xs:text-lg md:w-12 md:h-12 rounded-full bg-[#efefef] flex items-center
                        justify-center text-2xl cursor-pointer transition-colors duration-200 hover:bg-[#e0e0e0]"
                >
                  <img
                    src="https://ngchihuong.github.io/cv/assets/thuxinhdep-nlvzQFLc.jpg"
                    alt=""
                    className="rounded-full relative"
                  />
                </div>
                <div className="flex-1 min-w-0">
                  <p className="text-sm text-[#333] mb-1 overflow-hidden text-ellipsis whitespace-nowrap">
                    Gờ rúp các bác
                  </p>
                  <span className="text-xs text-[#767676]">36 members</span>
                </div>
              </div>
            </div>
          </aside>
        </div>
      </div>
    </>
  );
}
