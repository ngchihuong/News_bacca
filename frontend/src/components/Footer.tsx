"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { AiOutlineFire, AiOutlineHome, AiOutlinePlus } from "react-icons/ai";
import { PiVideoLight } from "react-icons/pi";
import { IoPersonOutline } from "react-icons/io5";

export default function Footer() {
  const pathname = usePathname();

  const links = [
    { href: "/", icon: <AiOutlineHome />, label: "Home" },
    { href: "/trending", icon: <AiOutlineFire />, label: "Trending" },
    { href: "/create", icon: <AiOutlinePlus />, label: "Create" },
    { href: "/watch", icon: <PiVideoLight />, label: "Watch" },
    { href: "/profile", icon: <IoPersonOutline />, label: "Profile" },
  ];

  return (
    <div className="fixed bottom-0 left-0 right-0">
      {/* Footer desktop */}
      <footer className="hidden md:block bg-gray-100 dark:bg-slate-900 border-t border-[#e0e0e0] dark:border-slate-800 pt-1 pb-1 transition-colors duration-200">
        <p className="text-center text-gray-600 dark:text-slate-400 text-sm">
          © {new Date().getFullYear()} <span className="font-bold">Cacbac</span>
          . All Rights Reserved.
        </p>
      </footer>

      {/* Footer mobile */}
      <nav
        className="flex md:hidden bg-gray-100 dark:bg-slate-900 border-t border-[#e0e0e0] dark:border-slate-800 py-2 px-0 z-10 
        shadow-[0px_-2px_10px_rgba(0,0,0,0.05)] transition-colors duration-200"
      >
        {links.map((link: any) => {
          const isActive = pathname === link.href;

          return (
            <Link
              key={link.href}
              href={link.href}
              className={`flex-1 flex flex-col items-center justify-center py-2 px-1 gap-1 transition-colors duration-200 
                ${
                  isActive
                    ? "text-primary dark:text-orange-500 bg-gray-200 dark:bg-slate-800 font-semibold"
                    : "text-[#767676] dark:text-slate-400 hover:text-[#111] dark:hover:text-slate-100"
                }`}
            >
              <span className="text-lg md:text-xl">{link.icon}</span>
              <span className="text-xs md:text-sm">{link.label}</span>
            </Link>
          );
        })}
      </nav>
    </div>
  );
}
