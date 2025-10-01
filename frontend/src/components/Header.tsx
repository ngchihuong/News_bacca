'use client';

import Link from 'next/link';
import { useState, useEffect } from 'react';
import { categoryApi } from '@/lib/api';
import { Category } from '@/types';

export default function Header() {
  const [categories, setCategories] = useState<Category[]>([]);
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  useEffect(() => {
    categoryApi.getActive().then((res) => {
      setCategories(res.data || []);
    }).catch(console.error);
  }, []);

  return (
    <header className="bg-white shadow-md">
      {/* Top Bar */}
      <div className="bg-gray-100 py-2">
        <div className="container mx-auto px-4">
          <div className="flex justify-between items-center">
            <div className="flex items-center space-x-4">
              <span className="bg-primary text-white px-4 py-1 text-sm font-semibold">
                Trending
              </span>
              <span className="text-sm text-gray-600 truncate">
                Latest news from around the world
              </span>
            </div>
            <div className="text-sm text-gray-600 hidden md:block">
              {new Date().toLocaleDateString('en-US', {
                weekday: 'long',
                year: 'numeric',
                month: 'long',
                day: 'numeric',
              })}
            </div>
          </div>
        </div>
      </div>

      {/* Logo & Ad Bar */}
      <div className="container mx-auto px-4 py-4">
        <div className="flex justify-between items-center">
          <Link href="/" className="text-3xl font-bold">
            <span className="text-primary">News</span>
            <span className="text-secondary">Room</span>
          </Link>
          <div className="hidden lg:block">
            <div className="bg-gray-200 px-8 py-4 text-center text-sm text-gray-600">
              Advertisement 728x90
            </div>
          </div>
        </div>
      </div>

      {/* Navigation */}
      <nav className="bg-gray-50 border-t border-gray-200">
        <div className="container mx-auto px-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-6">
              <Link
                href="/"
                className="py-4 px-2 text-gray-700 hover:text-primary font-medium border-b-2 border-transparent hover:border-primary transition-colors"
              >
                Home
              </Link>
              {categories.slice(0, 5).map((category) => (
                <Link
                  key={category.id}
                  href={`/category/${category.slug}`}
                  className="py-4 px-2 text-gray-700 hover:text-primary font-medium border-b-2 border-transparent hover:border-primary transition-colors hidden md:block"
                >
                  {category.name}
                </Link>
              ))}
              <Link
                href="/contact"
                className="py-4 px-2 text-gray-700 hover:text-primary font-medium border-b-2 border-transparent hover:border-primary transition-colors hidden md:block"
              >
                Contact
              </Link>
            </div>
            <div className="flex items-center">
              <input
                type="text"
                placeholder="Search..."
                className="px-4 py-2 border border-gray-300 rounded-l-md focus:outline-none focus:border-primary"
              />
              <button className="bg-primary text-white px-4 py-2 rounded-r-md hover:bg-primary/90 transition-colors">
                <svg
                  className="w-5 h-5"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                  />
                </svg>
              </button>
            </div>
          </div>
        </div>
      </nav>
    </header>
  );
}

