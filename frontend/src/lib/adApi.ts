import axios from 'axios';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

const adApi = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to admin requests
adApi.interceptors.request.use(
  (config) => {
    if (config.url?.includes('/admin/')) {
      const token = typeof window !== 'undefined' ? localStorage.getItem('token') : null;
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export const advertisementApi = {
  getByPosition: (position: string) =>
    adApi.get(`/advertisements/position/${position}`),
  
  trackImpression: (id: string) =>
    adApi.post(`/advertisements/${id}/impression`),
  
  trackClick: (id: string) =>
    adApi.post(`/advertisements/${id}/click`),
};

export const adminAdApi = {
  getAll: () => adApi.get('/admin/advertisements'),
  
  getById: (id: string) => adApi.get(`/admin/advertisements/${id}`),
  
  create: (data: any) => adApi.post('/admin/advertisements', data),
  
  update: (id: string, data: any) => adApi.put(`/admin/advertisements/${id}`, data),
  
  delete: (id: string) => adApi.delete(`/admin/advertisements/${id}`),
  
  getActive: () => adApi.get('/admin/advertisements/active'),
};

export default adApi;

