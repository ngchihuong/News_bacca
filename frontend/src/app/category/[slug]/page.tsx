'use client';

import { useEffect, useState } from 'react';
import { useParams } from 'next/navigation';
import Link from 'next/link';
import Image from 'next/image';
import { newsApi, categoryApi } from '@/lib/api';
import { News, Category } from '@/types';
import Sidebar from '@/components/Sidebar';

export default function CategoryPage() {
  const params = useParams();
  const slug = params.slug as string;
  const [category, setCategory] = useState<Category | null>(null);
  const [news, setNews] = useState<News[]>([]);
  const [trendingNews, setTrendingNews] = useState<News[]>([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const categoryRes = await categoryApi.getBySlug(slug);
        const categoryData = categoryRes.data;
        setCategory(categoryData);

        const [newsRes, trendingRes] = await Promise.all([
          newsApi.getByCategory(categoryData.id, page, 12),
          newsApi.getTrending(0, 5),
        ]);

        setNews(newsRes.data.content || []);
        setTotalPages(newsRes.data.totalPages || 0);
        setTrendingNews(trendingRes.data.content || []);
      } catch (error) {
        console.error('Error fetching data:', error);
      } finally {
        setLoading(false);
      }
    };

    if (slug) {
      fetchData();
    }
  }, [slug, page]);

  if (loading) {
    return (
      <div className="container mx-auto px-4 py-8">
        <div className="text-center">Loading...</div>
      </div>
    );
  }

  if (!category) {
    return (
      <div className="container mx-auto px-4 py-8">
        <div className="text-center">Category not found</div>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-6">
      {/* Breadcrumb */}
      <nav className="mb-4">
        <ol className="flex space-x-2 text-sm text-gray-600">
          <li>
            <Link href="/" className="hover:text-primary">
              Home
            </Link>
          </li>
          <li>/</li>
          <li>
            <Link href="/categories" className="hover:text-primary">
              Categories
            </Link>
          </li>
          <li>/</li>
          <li className="text-gray-900">{category.name}</li>
        </ol>
      </nav>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Main Content */}
        <div className="lg:col-span-2">
          <div className="flex items-center justify-between bg-gray-100 py-2 px-4 mb-4">
            <h1 className="text-2xl font-bold">{category.name}</h1>
            <span className="text-sm text-gray-600">
              {news.length} articles
            </span>
          </div>

          {/* News Grid */}
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-6">
            {news.map((item) => (
              <div key={item.id} className="news-card">
                <Link href={`/news/${item.slug}`} className="block h-[200px] relative">
                  <Image
                    src={item.imageUrl || '/placeholder.jpg'}
                    alt={item.title}
                    fill
                    className="object-cover"
                  />
                </Link>
                <div className="bg-gray-50 p-3">
                  <div className="text-xs text-gray-600 mb-2">
                    <span>{item.categoryName}</span>
                    <span className="px-1">/</span>
                    <span>{new Date(item.publishedAt).toLocaleDateString()}</span>
                  </div>
                  <Link href={`/news/${item.slug}`}>
                    <h4 className="text-base font-semibold hover:text-primary transition-colors line-clamp-2">
                      {item.title}
                    </h4>
                  </Link>
                </div>
              </div>
            ))}
          </div>

          {/* Pagination */}
          {totalPages > 1 && (
            <div className="flex justify-center space-x-2">
              <button
                onClick={() => setPage(Math.max(0, page - 1))}
                disabled={page === 0}
                className="px-4 py-2 border border-gray-300 rounded disabled:opacity-50"
              >
                Previous
              </button>
              <span className="px-4 py-2">
                Page {page + 1} of {totalPages}
              </span>
              <button
                onClick={() => setPage(Math.min(totalPages - 1, page + 1))}
                disabled={page >= totalPages - 1}
                className="px-4 py-2 border border-gray-300 rounded disabled:opacity-50"
              >
                Next
              </button>
            </div>
          )}
        </div>

        {/* Sidebar */}
        <div className="lg:col-span-1">
          <Sidebar trendingNews={trendingNews} categories={[]} />
        </div>
      </div>
    </div>
  );
}

