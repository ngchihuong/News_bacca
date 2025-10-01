'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { useAuth } from '@/context/AuthContext';
import { 
  FaHome, 
  FaNewspaper, 
  FaFolderOpen, 
  FaTags, 
  FaUsers, 
  FaSignOutAlt,
  FaChartBar,
  FaBullhorn
} from 'react-icons/fa';

export default function AdminNav() {
  const pathname = usePathname();
  const { user, logout } = useAuth();

  const navItems = [
    { href: '/admin/dashboard', label: 'Dashboard', icon: FaChartBar },
    { href: '/admin/news', label: 'News', icon: FaNewspaper },
    { href: '/admin/categories', label: 'Categories', icon: FaFolderOpen },
    { href: '/admin/tags', label: 'Tags', icon: FaTags },
    { href: '/admin/advertisements', label: 'Advertisements', icon: FaBullhorn },
  ];

  if (user?.role === 'ADMIN') {
    navItems.push({ href: '/admin/users', label: 'Users', icon: FaUsers });
  }

  return (
    <nav className="bg-secondary text-white w-64 min-h-screen fixed left-0 top-0 p-4">
      <div className="mb-8">
        <Link href="/admin/dashboard" className="text-2xl font-bold block">
          <span className="text-primary">News</span>Room
        </Link>
        <p className="text-gray-300 text-sm mt-1">Admin Panel</p>
      </div>

      <div className="space-y-2">
        {navItems.map((item) => {
          const Icon = item.icon;
          const isActive = pathname === item.href;
          
          return (
            <Link
              key={item.href}
              href={item.href}
              className={`flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors ${
                isActive
                  ? 'bg-primary text-white'
                  : 'text-gray-300 hover:bg-gray-700 hover:text-white'
              }`}
            >
              <Icon className="text-lg" />
              <span>{item.label}</span>
            </Link>
          );
        })}

        <Link
          href="/"
          className="flex items-center space-x-3 px-4 py-3 rounded-lg text-gray-300 hover:bg-gray-700 hover:text-white transition-colors"
        >
          <FaHome className="text-lg" />
          <span>View Site</span>
        </Link>
      </div>

      <div className="absolute bottom-4 left-4 right-4">
        <div className="border-t border-gray-600 pt-4">
          <div className="text-sm text-gray-300 mb-3">
            <p className="font-semibold">{user?.username}</p>
            <p className="text-xs">{user?.role}</p>
          </div>
          <button
            onClick={logout}
            className="flex items-center space-x-2 text-gray-300 hover:text-white transition-colors w-full"
          >
            <FaSignOutAlt />
            <span>Logout</span>
          </button>
        </div>
      </div>
    </nav>
  );
}

