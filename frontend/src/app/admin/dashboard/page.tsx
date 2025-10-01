'use client';

import { useEffect, useState } from 'react';
import AdminNav from '@/components/admin/AdminNav';
import ProtectedRoute from '@/components/admin/ProtectedRoute';
import { adminNewsApi } from '@/lib/adminApi';
import { FaNewspaper, FaEye, FaStar, FaFileAlt } from 'react-icons/fa';

export default function AdminDashboard() {
  const [stats, setStats] = useState({
    totalNews: 0,
    publishedNews: 0,
    draftNews: 0,
    featuredNews: 0,
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {
      const response = await adminNewsApi.getStats();
      setStats(response.data);
    } catch (error) {
      console.error('Error fetching stats:', error);
    } finally {
      setLoading(false);
    }
  };

  const statCards = [
    { title: 'Total News', value: stats.totalNews, icon: FaNewspaper, color: 'bg-blue-500' },
    { title: 'Published', value: stats.publishedNews, icon: FaEye, color: 'bg-green-500' },
    { title: 'Draft', value: stats.draftNews, icon: FaFileAlt, color: 'bg-yellow-500' },
    { title: 'Featured', value: stats.featuredNews, icon: FaStar, color: 'bg-purple-500' },
  ];

  return (
    <ProtectedRoute>
      <div className="flex">
        <AdminNav />
        <div className="flex-1 ml-64 p-8">
          <div className="mb-8">
            <h1 className="text-3xl font-bold text-gray-800">Dashboard</h1>
            <p className="text-gray-600 mt-1">Welcome to NewsRoom Admin Panel</p>
          </div>

          {loading ? (
            <div className="text-center py-12">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto"></div>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
              {statCards.map((card) => {
                const Icon = card.icon;
                return (
                  <div key={card.title} className="bg-white rounded-lg shadow-md p-6">
                    <div className="flex items-center justify-between">
                      <div>
                        <p className="text-gray-600 text-sm">{card.title}</p>
                        <p className="text-3xl font-bold text-gray-800 mt-2">{card.value}</p>
                      </div>
                      <div className={`${card.color} p-4 rounded-lg`}>
                        <Icon className="text-white text-2xl" />
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>
          )}

          <div className="mt-8 bg-white rounded-lg shadow-md p-6">
            <h2 className="text-xl font-bold text-gray-800 mb-4">Quick Actions</h2>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <a href="/admin/news/create" className="btn-primary text-center">
                Create New Article
              </a>
              <a href="/admin/categories" className="btn-secondary text-center">
                Manage Categories
              </a>
              <a href="/admin/news" className="bg-gray-600 hover:bg-gray-700 text-white font-semibold py-2 px-4 rounded transition-colors text-center">
                View All News
              </a>
            </div>
          </div>
        </div>
      </div>
    </ProtectedRoute>
  );
}

