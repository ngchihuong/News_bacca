import axios from 'axios';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

const adminApi = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests
adminApi.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Handle 401 errors
adminApi.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/admin/login';
    }
    return Promise.reject(error);
  }
);

export const adminNewsApi = {
  getAll: (page: number = 0, size: number = 20, status?: string) => {
    const params = new URLSearchParams({ page: page.toString(), size: size.toString() });
    if (status) params.append('status', status);
    return adminApi.get(`/admin/news?${params}`);
  },
  
  create: (data: any) => adminApi.post('/admin/news', data),
  update: (id: string, data: any) => adminApi.put(`/admin/news/${id}`, data),
  delete: (id: string) => adminApi.delete(`/admin/news/${id}`),
  publish: (id: string) => adminApi.put(`/admin/news/${id}/publish`),
  unpublish: (id: string) => adminApi.put(`/admin/news/${id}/unpublish`),
  getStats: () => adminApi.get('/admin/news/stats'),
};

export const adminCategoryApi = {
  getAll: () => adminApi.get('/admin/categories'),
  create: (data: any) => adminApi.post('/admin/categories', data),
  update: (id: string, data: any) => adminApi.put(`/admin/categories/${id}`, data),
  delete: (id: string) => adminApi.delete(`/admin/categories/${id}`),
};

export const adminFileApi = {
  upload: (file: File) => {
    const formData = new FormData();
    formData.append('file', file);
    return adminApi.post('/admin/files/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
  },
  delete: (fileName: string) => adminApi.delete(`/admin/files/${fileName}`),
};

export default adminApi;

