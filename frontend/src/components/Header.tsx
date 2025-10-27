"use client";

import { useState } from "react";
import Link from "next/link";
import { FaPencilAlt, FaSearch } from "react-icons/fa";
import MobilePanel from "./mobile/MobilePanel";
import MobileSearch from "./mobile/MobileSearch";
import DropdownMenu from "./DropdownMenu";

export default function Header() {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isMobileSearchOpen, setIsMobileSearchOpen] = useState(false);

  return (
    <>
      <header
        className="fixed top-0 left-0 right-0 h-14 bg-white border-b border-[#e0e0e0]
    flex justify-between items-center py-0 px-2 z-[200] shadow-sm md:h-20 md:px-3 xs:px-12"
      >
        {/* mobile panel */}
        <MobilePanel />
        {/* mobile panel */}

        <div className="flex items-center gap-3 xs:gap-8">
          <div className="font-bold text-[#e60023] cursor-pointer text-[16px] xs:text-[18px] md:text-2xl">
            Cacbac
          </div>
          <nav className="hidden md:flex gap-2">
            <Link
              href="/"
              className="text-black text-[16px] font-semibold py-3 px-4 rounded-3xl transition-colors duration-200 hover:bg-[#e9e9e9]"
            >
              Home
            </Link>
            <Link
              href="/trending"
              className="text-black text-[16px] font-semibold py-3 px-4 rounded-3xl transition-colors duration-200 hover:bg-[#e9e9e9]"
            >
              Trending
            </Link>
            <Link
              href="/following"
              className="text-black text-[16px] font-semibold py-3 px-4 rounded-3xl transition-colors duration-200 hover:bg-[#e9e9e9]"
            >
              Following
            </Link>
          </nav>
        </div>
        <div className="gap-2 flex items-center xs:gap-4">
          <div className="hidden md:relative md:block">
            <input
              type="text"
              className="w-72 py-3 px-4 border-none bg-[#efefef] rounded-3xl text-sm outline-none focus:bg-[#e0e0e0]"
              placeholder="Search news...."
            />
          </div>
          <div
            className="block md:hidden text-[20px] cursor-pointer p-2 text-black transition-colors duration-200 rounded-lg 
        hover:bg-[#f0f0f0]"
            onClick={() => setIsMobileSearchOpen(true)}
          >
            <FaSearch className="text-blue-600" />
          </div>
          <Link href="/post/create">
            <button
              className="hidden md:flex items-center gap-2 bg-[#1877f2] text-white border-none rounded-3xl py-2 px-4 
          font-semibold cursor-pointer transition-all duration-200 ease-in hover:bg-[#166fe5] transform hover:translate-y-[-1px] hover:shadow-xl"
            >
              <span className="text-[16px]">
                <FaPencilAlt />
              </span>
              <span className="text-[14px]">Write</span>
            </button>
          </Link>

          {/* Account setting */}
          <DropdownMenu isMenuOpen={isMenuOpen} setIsMenuOpen={setIsMenuOpen} />
          {/* Account setting */}
        </div>
      </header>
      <div
        className="hidden md:block fixed top-20 left-0 w-60 h-[calc(100vh-7rem)] bg-white border-r border-[#e0e0e0] 
      py-1 px-4 overflow-y-auto z-50 scrollbar-hover"
      >
        
      </div>
      {isMobileSearchOpen == true ? (
        <MobileSearch
          isMobileSearchOpen={isMobileSearchOpen}
          setIsMobileSearchOpen={setIsMobileSearchOpen}
        />
      ) : (
        <></>
      )}
    </>
  );
}
