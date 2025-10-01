import axios from 'axios';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const newsApi = {
  getAll: (page: number = 0, size: number = 10) =>
    api.get(`/news?page=${page}&size=${size}`),
  
  getBySlug: (slug: string) =>
    api.get(`/news/slug/${slug}`),
  
  getByCategory: (categoryId: string, page: number = 0, size: number = 10) =>
    api.get(`/news/category/${categoryId}?page=${page}&size=${size}`),
  
  getFeatured: (page: number = 0, size: number = 10) =>
    api.get(`/news/featured?page=${page}&size=${size}`),
  
  getTrending: (page: number = 0, size: number = 10) =>
    api.get(`/news/trending?page=${page}&size=${size}`),
};

export const categoryApi = {
  getAll: () => api.get('/categories'),
  
  getActive: () => api.get('/categories/active'),
  
  getBySlug: (slug: string) => api.get(`/categories/slug/${slug}`),
};

export default api;

