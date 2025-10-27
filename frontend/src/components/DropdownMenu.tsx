"use client";

import { useAppContext } from "@/context/AuthContext";
import Link from "next/link";
import { useEffect, useRef, useState } from "react";
import { FaAngleRight, FaRegMoon, FaRegUserCircle } from "react-icons/fa";
import { IoIosArrowDropdownCircle, IoIosLogIn } from "react-icons/io";
import { IoSettingsOutline } from "react-icons/io5";
import { MdOutlineContactSupport } from "react-icons/md";

type Props = {
  isMenuOpen: boolean;
  setIsMenuOpen: (v: boolean) => void;
};
export default function DropdownMenu({ isMenuOpen, setIsMenuOpen }: Props) {
  const { user, isAuthenticated } = useAppContext();

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
    <div ref={dropdownRef} className="relative">
      {/* Nút menu */}
      <div
        className="w-8 h-8 text-[16px] xs:w-9 xs:h-9 xs:text-lg md:w-12 md:h-12 rounded-full bg-[#efefef] flex items-center
         justify-center text-2xl cursor-pointer transition-colors duration-200 hover:bg-[#e0e0e0]"
        onClick={toggle}
      >
        <img
          src="https://ngchihuong.github.io/cv/assets/thuxinhdep-nlvzQFLc.jpg"
          alt=""
          className="rounded-full relative"
        />
        <div className="absolute bg-gray-100 h-4 w-4 rounded-full right-0 bottom-0 flex items-center justify-center">
          <IoIosArrowDropdownCircle className="font-bold text-gray-800 rounded-full" />
        </div>
      </div>
      {/* Dropdown */}
      {isMenuOpen && (
        <div
          className="absolute right-0 mt-2 bg-gray-100 shadow-md rounded-md w-72 p-1 divide-y-2 divide-gray-400 border
           border-gray-200 "
        >
          <div className="flex items-center  gap-1 md:gap-0 py-1 px-1 m-1 hover:bg-gray-200 rounded-md">
            {/* isAuthenticated == true then "gap-2 "*/}
            <div
              className="w-4 h-4 text-[16px] xs:w-5 xs:h-5 xs:text-lg md:w-8 md:h-8 rounded-full flex items-center
         justify-start text-2xl cursor-pointer transition-colors duration-200 hover:bg-[#e0e0e0]"
            >
              {/* <img
                  src="https://ngchihuong.github.io/cv/assets/thuxinhdep-nlvzQFLc.jpg"
                  alt=""
                  className="rounded-full relative"
                /> */}
              <FaRegUserCircle className="rounded-full relative text-2xl" />
            </div>
            <Link
              href={`/auth/login`}
              className="block text-lg font-semibold text-black hover:text-gray-600"
            >
              Not account?
            </Link>
          </div>
          {isAuthenticated == true && user != null ? (
            <>
              {/* IsLogged In */}
              <div className="px-4 py-3 text-sm text-gray-900">
                <div>Bonnie Green</div>
                <div className="font-medium truncate">name@flowbite.com</div>
              </div>
              <ul
                className="py-2 text-sm text-gray-900"
                aria-labelledby="dropdownUserAvatarButton"
              >
                <li>
                  <a
                    href="#"
                    className="block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white"
                  >
                    Dashboard
                  </a>
                </li>
                <li>
                  <a
                    href="#"
                    className="block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white"
                  >
                    Settings
                  </a>
                </li>
                <li>
                  <a
                    href="#"
                    className="block px-4 py-2 hover:bg-gray-100 dark:hover:bg-gray-600 dark:hover:text-white"
                  >
                    Earnings
                  </a>
                </li>
              </ul>
              <div className="py-2">
                <a
                  href="#"
                  className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 dark:hover:bg-gray-600 dark:text-gray-500 dark:hover:text-white"
                >
                  Sign out
                </a>
              </div>
              {/* IsLogged In */}
            </>
          ) : (
            <>
              {/* Not Logged In */}
              <div className="">
                <div className="py-1 flex items-center  m-1 hover:bg-gray-200 justify-between rounded-md">
                  <Link
                    href={`/auth/login`}
                    className="flex items-center px-1 py-2 text-sm font-semibold text-black hover:text-gray-600 gap-2"
                  >
                    <div className="bg-gray-300 rounded-full p-1.5 flex items-center justify-start ">
                      <IoSettingsOutline className="text-xl font-bold" />
                    </div>
                    Setting
                  </Link>
                  <FaAngleRight className="text-xl" />
                </div>
                <div className="py-1 flex items-center  m-1 hover:bg-gray-200 justify-between rounded-md">
                  <Link
                    href={`/auth/login`}
                    className="flex items-center px-1 py-2 text-sm font-semibold text-black hover:text-gray-600 gap-2"
                  >
                    <div className="bg-gray-300 rounded-full p-1.5 flex items-center justify-start ">
                      <MdOutlineContactSupport className="text-xl font-bold" />
                    </div>
                    Help and support
                  </Link>
                  <FaAngleRight className="text-xl" />
                </div>
                <div className="py-1 flex items-center  m-1 hover:bg-gray-200 justify-between rounded-md">
                  <Link
                    href={`/auth/login`}
                    className="flex items-center px-1 py-2 text-sm font-semibold text-black hover:text-gray-600 gap-2"
                  >
                    <div className="bg-gray-300 rounded-full p-1.5 flex items-center justify-start ">
                      <FaRegMoon className="text-xl font-bold" />
                    </div>
                    Display and accessibility
                  </Link>
                  <FaAngleRight className="text-xl" />
                </div>
                <div className="py-1 flex items-center  m-1 hover:bg-gray-200 justify-between rounded-md">
                  <Link
                    href={`/auth/login`}
                    className="flex items-center px-1 py-2 text-sm font-semibold text-black hover:text-gray-600 gap-2"
                  >
                    <div className="bg-gray-300 rounded-full p-1.5 flex items-center justify-start ">
                      <IoIosLogIn className="text-xl font-bold" />
                    </div>
                    Sign-in to Cacbac
                  </Link>
                  <FaAngleRight className="text-xl" />
                </div>
              </div>
              {/* <div className="py-2">
              <a
                href="#"
                className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 dark:hover:bg-gray-600 dark:text-gray-500 dark:hover:text-white"
              >
                Register
              </a>
            </div> */}
              {/* Not Logged In */}
            </>
          )}
        </div>
      )}
    </div>
  );
}
