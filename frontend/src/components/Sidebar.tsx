'use client';

import Link from 'next/link';
import Image from 'next/image';
import { News, Category } from '@/types';
import { FaFacebook, FaTwitter, FaLinkedin, FaInstagram, FaYoutube } from 'react-icons/fa';
import AdContainer from './ads/AdContainer';

interface SidebarProps {
  trendingNews: News[];
  categories: Category[];
}

export default function Sidebar({ trendingNews, categories }: SidebarProps) {
  return (
    <div className="space-y-6">
      {/* Ad - Sidebar Top */}
      <AdContainer position="SIDEBAR_TOP" />
      {/* Social Follow */}
      <div>
        <div className="bg-gray-100 py-2 px-4 mb-3">
          <h3 className="text-lg font-bold">Follow Us</h3>
        </div>
        <div className="grid grid-cols-2 gap-2">
          <a href="#" className="flex items-center justify-center py-3 bg-blue-600 text-white text-sm rounded hover:bg-blue-700 transition-colors">
            <FaFacebook className="mr-2" /> Facebook
          </a>
          <a href="#" className="flex items-center justify-center py-3 bg-sky-500 text-white text-sm rounded hover:bg-sky-600 transition-colors">
            <FaTwitter className="mr-2" /> Twitter
          </a>
          <a href="#" className="flex items-center justify-center py-3 bg-blue-700 text-white text-sm rounded hover:bg-blue-800 transition-colors">
            <FaLinkedin className="mr-2" /> LinkedIn
          </a>
          <a href="#" className="flex items-center justify-center py-3 bg-pink-600 text-white text-sm rounded hover:bg-pink-700 transition-colors">
            <FaInstagram className="mr-2" /> Instagram
          </a>
        </div>
      </div>

      {/* Newsletter */}
      <div>
        <div className="bg-gray-100 py-2 px-4 mb-3">
          <h3 className="text-lg font-bold">Newsletter</h3>
        </div>
        <div className="bg-gray-50 p-4 rounded">
          <p className="text-sm text-gray-600 mb-3">
            Subscribe to our newsletter to get the latest news delivered to your inbox.
          </p>
          <div className="flex">
            <input
              type="email"
              placeholder="Your Email"
              className="flex-1 px-3 py-2 border border-gray-300 rounded-l focus:outline-none focus:border-primary"
            />
            <button className="bg-primary text-white px-4 py-2 rounded-r hover:bg-primary/90 transition-colors">
              Subscribe
            </button>
          </div>
        </div>
      </div>

      {/* Trending News */}
      {trendingNews && trendingNews.length > 0 && (
        <div>
          <div className="bg-gray-100 py-2 px-4 mb-3">
            <h3 className="text-lg font-bold">Trending</h3>
          </div>
          <div className="space-y-3">
            {trendingNews.slice(0, 5).map((item) => (
              <div key={item.id} className="flex space-x-3">
                <div className="relative w-24 h-24 flex-shrink-0">
                  <Image
                    src={item.imageUrl || '/placeholder.jpg'}
                    alt={item.title}
                    fill
                    className="object-cover rounded"
                  />
                </div>
                <div className="flex-1 bg-gray-50 p-3 rounded">
                  <div className="text-xs text-gray-600 mb-1">
                    <Link href={`/category/${item.categoryId}`} className="hover:text-primary">
                      {item.categoryName}
                    </Link>
                    <span className="px-1">/</span>
                    <span>{new Date(item.publishedAt).toLocaleDateString()}</span>
                  </div>
                  <Link href={`/news/${item.slug}`}>
                    <h5 className="text-sm font-semibold hover:text-primary transition-colors line-clamp-2">
                      {item.title}
                    </h5>
                  </Link>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Tags */}
      <div>
        <div className="bg-gray-100 py-2 px-4 mb-3">
          <h3 className="text-lg font-bold">Tags</h3>
        </div>
        <div className="flex flex-wrap gap-2">
          {['Politics', 'Business', 'Technology', 'Sports', 'Health', 'Education', 'Science', 'Entertainment', 'Travel', 'Lifestyle'].map((tag) => (
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

      {/* Ad - Sidebar Bottom */}
      <AdContainer position="SIDEBAR_BOTTOM" />
    </div>
  );
}

