'use client';

import { useEffect, useState } from 'react';
import Link from 'next/link';
import AdminNav from '@/components/admin/AdminNav';
import ProtectedRoute from '@/components/admin/ProtectedRoute';
import { adminAdApi } from '@/lib/adApi';
import { Advertisement } from '@/types/advertisement';
import { FaEdit, FaTrash, FaEye, FaEyeSlash, FaPlus, FaChartLine } from 'react-icons/fa';

export default function AdminAdvertisementsPage() {
  const [ads, setAds] = useState<Advertisement[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchAds();
  }, []);

  const fetchAds = async () => {
    setLoading(true);
    try {
      const response = await adminAdApi.getAll();
      setAds(response.data);
    } catch (error) {
      console.error('Error fetching ads:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: string) => {
    if (!confirm('Are you sure you want to delete this advertisement?')) return;
    
    try {
      await adminAdApi.delete(id);
      fetchAds();
    } catch (error) {
      console.error('Error deleting ad:', error);
      alert('Failed to delete advertisement');
    }
  };

  const getCTR = (ad: Advertisement) => {
    if (ad.impressions === 0) return '0.00';
    return ((ad.clicks / ad.impressions) * 100).toFixed(2);
  };

  return (
    <ProtectedRoute>
      <div className="flex">
        <AdminNav />
        <div className="flex-1 ml-64 p-8">
          <div className="flex justify-between items-center mb-8">
            <div>
              <h1 className="text-3xl font-bold text-gray-800">Advertisement Management</h1>
              <p className="text-gray-600 mt-1">Manage all your advertisements</p>
            </div>
            <Link
              href="/admin/advertisements/create"
              className="btn-primary flex items-center space-x-2"
            >
              <FaPlus />
              <span>Create Advertisement</span>
            </Link>
          </div>

          {loading ? (
            <div className="text-center py-12">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto"></div>
            </div>
          ) : (
            <div className="bg-white rounded-lg shadow-md overflow-hidden">
              <table className="w-full">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                      Title
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                      Position
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                      Format
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                      Status
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                      Impressions
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                      Clicks
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                      CTR
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                      Actions
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {ads.map((ad) => (
                    <tr key={ad.id}>
                      <td className="px-6 py-4">
                        <div className="text-sm font-medium text-gray-900">{ad.title}</div>
                        <div className="text-xs text-gray-500">{ad.description}</div>
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-500">
                        {ad.position}
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-500">
                        {ad.format}
                      </td>
                      <td className="px-6 py-4">
                        <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                          ad.active 
                            ? 'bg-green-100 text-green-800' 
                            : 'bg-red-100 text-red-800'
                        }`}>
                          {ad.active ? 'Active' : 'Inactive'}
                        </span>
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-500">
                        {ad.impressions.toLocaleString()}
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-500">
                        {ad.clicks.toLocaleString()}
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-500">
                        {getCTR(ad)}%
                      </td>
                      <td className="px-6 py-4 text-sm font-medium">
                        <div className="flex space-x-2">
                          <Link
                            href={`/admin/advertisements/${ad.id}/edit`}
                            className="text-blue-600 hover:text-blue-900"
                            title="Edit"
                          >
                            <FaEdit />
                          </Link>
                          <button
                            onClick={() => handleDelete(ad.id)}
                            className="text-red-600 hover:text-red-900"
                            title="Delete"
                          >
                            <FaTrash />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}

          {/* Summary Stats */}
          <div className="mt-8 grid grid-cols-1 md:grid-cols-4 gap-4">
            <div className="bg-white p-6 rounded-lg shadow-md">
              <div className="text-gray-600 text-sm">Total Ads</div>
              <div className="text-3xl font-bold text-gray-800 mt-2">{ads.length}</div>
            </div>
            <div className="bg-white p-6 rounded-lg shadow-md">
              <div className="text-gray-600 text-sm">Active Ads</div>
              <div className="text-3xl font-bold text-green-600 mt-2">
                {ads.filter(ad => ad.active).length}
              </div>
            </div>
            <div className="bg-white p-6 rounded-lg shadow-md">
              <div className="text-gray-600 text-sm">Total Impressions</div>
              <div className="text-3xl font-bold text-blue-600 mt-2">
                {ads.reduce((sum, ad) => sum + ad.impressions, 0).toLocaleString()}
              </div>
            </div>
            <div className="bg-white p-6 rounded-lg shadow-md">
              <div className="text-gray-600 text-sm">Total Clicks</div>
              <div className="text-3xl font-bold text-purple-600 mt-2">
                {ads.reduce((sum, ad) => sum + ad.clicks, 0).toLocaleString()}
              </div>
            </div>
          </div>
        </div>
      </div>
    </ProtectedRoute>
  );
}

