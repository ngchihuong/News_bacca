'use client';

import Link from 'next/link';
import { FaFacebook, FaTwitter, FaLinkedin, FaInstagram, FaYoutube } from 'react-icons/fa';

export default function Footer() {
  return (
    <footer className="bg-gray-100 pt-12 pb-6 mt-12">
      <div className="container mx-auto px-4">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8 mb-8">
          {/* About */}
          <div>
            <Link href="/" className="text-2xl font-bold mb-4 inline-block">
              <span className="text-primary">News</span>
              <span className="text-secondary">Room</span>
            </Link>
            <p className="text-gray-600 mb-4">
              Your trusted source for the latest news and updates from around the world.
              Stay informed with comprehensive coverage of technology, business, sports, and more.
            </p>
            <div className="flex space-x-2">
              <a href="#" className="w-10 h-10 flex items-center justify-center border border-gray-300 rounded hover:bg-primary hover:text-white hover:border-primary transition-colors">
                <FaTwitter />
              </a>
              <a href="#" className="w-10 h-10 flex items-center justify-center border border-gray-300 rounded hover:bg-primary hover:text-white hover:border-primary transition-colors">
                <FaFacebook />
              </a>
              <a href="#" className="w-10 h-10 flex items-center justify-center border border-gray-300 rounded hover:bg-primary hover:text-white hover:border-primary transition-colors">
                <FaLinkedin />
              </a>
              <a href="#" className="w-10 h-10 flex items-center justify-center border border-gray-300 rounded hover:bg-primary hover:text-white hover:border-primary transition-colors">
                <FaInstagram />
              </a>
              <a href="#" className="w-10 h-10 flex items-center justify-center border border-gray-300 rounded hover:bg-primary hover:text-white hover:border-primary transition-colors">
                <FaYoutube />
              </a>
            </div>
          </div>

          {/* Categories */}
          <div>
            <h4 className="text-lg font-bold mb-4">Categories</h4>
            <div className="flex flex-wrap gap-2">
              {['Politics', 'Business', 'Technology', 'Sports', 'Health', 'Education', 'Science', 'Entertainment'].map((cat) => (
                <Link
                  key={cat}
                  href={`/category/${cat.toLowerCase()}`}
                  className="px-3 py-1 text-sm border border-gray-300 rounded hover:bg-primary hover:text-white hover:border-primary transition-colors"
                >
                  {cat}
                </Link>
              ))}
            </div>
          </div>

          {/* Tags */}
          <div>
            <h4 className="text-lg font-bold mb-4">Popular Tags</h4>
            <div className="flex flex-wrap gap-2">
              {['Breaking', 'Trending', 'Featured', 'Analysis', 'Opinion', 'Interview'].map((tag) => (
                <Link
                  key={tag}
                  href={`/tag/${tag.toLowerCase()}`}
                  className="px-3 py-1 text-sm border border-gray-300 rounded hover:bg-primary hover:text-white hover:border-primary transition-colors"
                >
                  {tag}
                </Link>
              ))}
            </div>
          </div>

          {/* Quick Links */}
          <div>
            <h4 className="text-lg font-bold mb-4">Quick Links</h4>
            <div className="space-y-2">
              <Link href="/about" className="block text-gray-600 hover:text-primary transition-colors">
                → About Us
              </Link>
              <Link href="/advertise" className="block text-gray-600 hover:text-primary transition-colors">
                → Advertise
              </Link>
              <Link href="/privacy" className="block text-gray-600 hover:text-primary transition-colors">
                → Privacy Policy
              </Link>
              <Link href="/terms" className="block text-gray-600 hover:text-primary transition-colors">
                → Terms & Conditions
              </Link>
              <Link href="/contact" className="block text-gray-600 hover:text-primary transition-colors">
                → Contact
              </Link>
            </div>
          </div>
        </div>

        {/* Copyright */}
        <div className="border-t border-gray-300 pt-6">
          <p className="text-center text-gray-600">
            © {new Date().getFullYear()} <span className="font-bold">NewsRoom</span>. All Rights Reserved.
          </p>
        </div>
      </div>
    </footer>
  );
}

