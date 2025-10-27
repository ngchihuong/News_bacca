"use client";

import { useEffect, useRef, useState } from "react";
import { IoSearch } from "react-icons/io5";
import { MdOutlineCancel, MdOutlineKeyboardVoice } from "react-icons/md";

type Props = {
  isMobileSearchOpen: boolean;
  setIsMobileSearchOpen: (v: boolean) => void;
};
export default function MobileSearch({isMobileSearchOpen, setIsMobileSearchOpen}: Props) {
  const dropdownRefIsOpenMobileSearch = useRef<HTMLDivElement | null>(null);

  const toggleMobileSearch = () => setIsMobileSearchOpen(!isMobileSearchOpen);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        dropdownRefIsOpenMobileSearch.current &&
        !dropdownRefIsOpenMobileSearch.current.contains(event.target as Node)
      ) {
        setIsMobileSearchOpen(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);
  return (
    <div
      ref={dropdownRefIsOpenMobileSearch}
      className={`fixed inset-0 bg-[rgba(0,0,0,0.2)] backdrop-blur-sm transition-opacity duration-300 z-[9999] max-w-[500px]
          mx-auto my-0 py-10  px-4 gap-3 shadow-[0_4px_20px_rgba(0,0,0,0.2)] md:hidden
  ${isMobileSearchOpen ? "opacity-100 visible" : "opacity-0 invisible"}`}
      onClick={toggleMobileSearch}
    >
      <form
        className="flex-1 items-center max-w-lg mx-auto"
        onClick={(e) => e.stopPropagation()}
      >
        <label htmlFor="voice-search" className="sr-only">
          Search
        </label>
        <div className="relative w-full">
          <div className="absolute inset-y-0 start-0 flex items-center ps-3 pointer-events-none">
            <IoSearch className="text-xl text-gray-100" />
          </div>
          <input
            type="text"
            id="voice-search"
            className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500
               focus:border-blue-500 block w-full ps-10 p-2.5  dark:bg-gray-700 dark:border-gray-600 min-h-14
                dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
            placeholder="Search news..."
            required
          />
          <button
            type="button"
            className="absolute inset-y-0 end-0 flex items-center pe-3"
          >
            <MdOutlineKeyboardVoice className="text-xl text-gray-100" />
          </button>
        </div>
        <div className="flex justify-end py-2">
          <button
            type="submit"
            className="inline-flex items-center py-2.5 px-3 ms-2 text-sm font-medium text-white bg-blue-700 rounded-lg 
            border border-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 dark:bg-blue-600
             dark:hover:bg-blue-700 dark:focus:ring-blue-800"
            onClick={() => setIsMobileSearchOpen(!isMobileSearchOpen)}
          >
            <IoSearch className="text-xl text-gray-100" />
            Search
          </button>
        </div>
      </form>
      <div className="flex items-center justify-center pt-10">
        <div
          id="toast-simple"
          className="flex items-center w-full max-w-xs p-4 space-x-4 rtl:space-x-reverse   divide-x 
              rtl:divide-x-reverse"
          role="alert"
        >
          <MdOutlineCancel className="text-6xl text-black font-bold " />
          <div className="ps-4 text-xl text-black font-normal ">
            Click outside to dismiss the search.
          </div>
        </div>
      </div>
    </div>
  );
}
