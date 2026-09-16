'use client';

import { useEffect, useState } from 'react';
import { useParams } from 'next/navigation';
import Image from 'next/image';
import Link from 'next/link';
import { newsApi } from '@/lib/api';
import { News } from '@/types';
import Sidebar from '@/components/Sidebar';

export default function NewsDetailPage() {
  const params = useParams();
  const slug = params.slug as string;
  const [news, setNews] = useState<News | null>(null);
  const [trendingNews, setTrendingNews] = useState<News[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [newsRes, trendingRes] = await Promise.all([
          newsApi.getBySlug(slug),
          newsApi.getTrending(0, 5),
        ]);

        setNews(newsRes.data);
        setTrendingNews(trendingRes.data.content || []);
      } catch (error) {
        console.error('Error fetching news:', error);
      } finally {
        setLoading(false);
      }
    };

    if (slug) {
      fetchData();
    }
  }, [slug]);

  if (loading) {
    return (
      <div className="container mx-auto px-4 py-8">
        <div className="text-center">Loading...</div>
      </div>
    );
  }

  if (!news) {
    return (
      <div className="container mx-auto px-4 py-8">
        <div className="text-center">News not found</div>
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
            <Link href={`/category/${news.categoryId}`} className="hover:text-primary">
              {news.categoryName}
            </Link>
          </li>
          <li>/</li>
          <li className="text-gray-900">{news.title}</li>
        </ol>
      </nav>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Main Content */}
        <div className="lg:col-span-2">
          <article className="bg-white rounded-lg shadow-md overflow-hidden">
            <div className="relative h-[435px]">
              <Image
                src={news.imageUrl || '/placeholder.jpg'}
                alt={news.title}
                fill
                className="object-cover"
              />
            </div>
            
            <div className="p-6">
              <div className="mb-4 flex flex-wrap items-center justify-between gap-3 text-sm text-gray-600">
                <div className="flex items-center gap-2">
                  <Link href={`/category/${news.categoryId}`} className="hover:text-primary font-medium">
                    {news.categoryName}
                  </Link>
                  <span className="text-gray-300">/</span>
                  <span>{new Date(news.publishedAt).toLocaleDateString('vi-VN', {
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric',
                  })}</span>
                </div>

                {news.authorId && (
                  <Link
                    href={`/user/${news.authorId}`}
                    className="inline-flex items-center gap-2 group hover:opacity-90 transition"
                  >
                    <div className="w-7 h-7 rounded-full bg-slate-200 overflow-hidden relative border border-slate-300">
                      <Image
                        src="https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&auto=format&fit=crop&q=80"
                        alt={news.authorName || 'Tác giả'}
                        fill
                        className="object-cover"
                      />
                    </div>
                    <span className="font-semibold text-slate-800 group-hover:text-primary transition">
                      {news.authorName || 'Tác giả'}
                    </span>
                  </Link>
                )}
              </div>

              <h1 className="text-3xl font-bold mb-4">{news.title}</h1>

              <div className="prose max-w-none mb-6" dangerouslySetInnerHTML={{ __html: news.content }} />

              {/* Tags */}
              {news.tagNames && news.tagNames.length > 0 && (
                <div className="mt-8 pt-6 border-t border-gray-200">
                  <h3 className="text-lg font-semibold mb-3">Tags:</h3>
                  <div className="flex flex-wrap gap-2">
                    {news.tagNames.map((tag) => (
                      <Link
                        key={tag}
                        href={`/tag/${tag.toLowerCase()}`}
                        className="px-3 py-1 text-sm bg-gray-100 hover:bg-primary hover:text-white rounded transition-colors"
                      >
                        {tag}
                      </Link>
                    ))}
                  </div>
                </div>
              )}

              {/* Author Showcase Box */}
              {news.authorId && (
                <div className="mt-8 pt-6 border-t border-gray-100 flex flex-col sm:flex-row sm:items-center justify-between gap-4 bg-slate-50 rounded-xl p-4 border border-slate-200/60">
                  <Link
                    href={`/user/${news.authorId}`}
                    className="flex items-center gap-3 group"
                  >
                    <div className="w-12 h-12 rounded-full bg-slate-200 overflow-hidden relative border-2 border-white shadow-sm flex-shrink-0">
                      <Image
                        src="https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&auto=format&fit=crop&q=80"
                        alt={news.authorName || "Tác giả"}
                        fill
                        className="object-cover"
                      />
                    </div>
                    <div>
                      <p className="text-xs text-slate-500 font-medium">Tác giả bài viết</p>
                      <h4 className="text-sm font-bold text-slate-900 group-hover:text-primary transition">
                        {news.authorName || "Tác giả"}
                      </h4>
                    </div>
                  </Link>

                  <Link
                    href={`/user/${news.authorId}`}
                    className="inline-flex items-center justify-center text-xs font-semibold px-4 py-2 rounded-full border border-slate-300 bg-white text-slate-700 hover:bg-slate-100 transition shadow-sm"
                  >
                    Xem hồ sơ tác giả →
                  </Link>
                </div>
              )}
            </div>
          </article>

          {/* Comments Section */}
          <div className="mt-6 bg-white rounded-lg shadow-md p-6">
            <h3 className="text-xl font-bold mb-4">Comments</h3>
            <p className="text-gray-600">Comments feature coming soon...</p>
          </div>
        </div>

        {/* Sidebar */}
        <div className="lg:col-span-1">
          <Sidebar trendingNews={trendingNews} categories={[]} />
        </div>
      </div>
    </div>
  );
}

