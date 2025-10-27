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
      <footer className="hidden md:block bg-gray-100 pt-1 pb-1">
        <p className="text-center text-gray-600">
          © {new Date().getFullYear()} <span className="font-bold">Cacbac</span>
          . All Rights Reserved.
        </p>
      </footer>

      {/* Footer mobile */}
      <nav
        className="flex md:hidden bg-gray-100 border-t border-[#e0e0e0] py-2 px-0 z-10 
        shadow-[0px_-2px_10px_rgba(0,0,0,0.05)]"
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
                    ? "text-red-600 bg-gray-200 font-semibold"
                    : "text-[#767676] hover:text-[#111]"
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
