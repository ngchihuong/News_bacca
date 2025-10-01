'use client';

import { useEffect, useState } from 'react';
import { newsApi, categoryApi } from '@/lib/api';
import { News, Category, PaginatedResponse } from '@/types';
import MainSlider from '@/components/MainSlider';
import FeaturedNews from '@/components/FeaturedNews';
import CategorySection from '@/components/CategorySection';
import LatestNewsWithAds from '@/components/LatestNewsWithAds';
import Sidebar from '@/components/Sidebar';
import AdContainer from '@/components/ads/AdContainer';

export default function Home() {
  const [featuredNews, setFeaturedNews] = useState<News[]>([]);
  const [latestNews, setLatestNews] = useState<News[]>([]);
  const [trendingNews, setTrendingNews] = useState<News[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [featuredRes, latestRes, trendingRes, categoriesRes] = await Promise.all([
          newsApi.getFeatured(0, 5),
          newsApi.getAll(0, 10),
          newsApi.getTrending(0, 5),
          categoryApi.getActive(),
        ]);

        setFeaturedNews(featuredRes.data.content || []);
        setLatestNews(latestRes.data.content || []);
        setTrendingNews(trendingRes.data.content || []);
        setCategories(categoriesRes.data || []);
      } catch (error) {
        console.error('Error fetching data:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-2xl">Loading...</div>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-6">
      {/* Top Banner Ad */}
      <AdContainer position="TOP_BANNER" className="mb-4" />

      {/* Main Slider */}
      <MainSlider news={featuredNews} categories={categories.slice(0, 4)} />

      {/* Featured News */}
      <FeaturedNews news={featuredNews} />

      {/* Category Sections */}
      {categories.slice(0, 4).map((category) => (
        <CategorySection key={category.id} category={category} />
      ))}

      {/* News with Sidebar */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mt-8">
        <div className="lg:col-span-2">
          {/* Latest News with In-Feed Ads (1 ad per 10 posts) */}
          <LatestNewsWithAds news={latestNews} adFrequency={10} />
        </div>
        <div className="lg:col-span-1">
          {/* Sidebar with Ads */}
          <Sidebar trendingNews={trendingNews} categories={categories} />
        </div>
      </div>

      {/* Bottom Banner Ad */}
      <AdContainer position="BOTTOM_BANNER" className="mt-8" />
    </div>
  );
}

