import axios from "@/utils/axios-customize";
import { BaseOutput, UpdateProfileRequest, UserProfile, PublicUserProfile } from "@/types/backend";
import { News, PaginatedResponse } from "@/types";

const API_PREFIX = "/user";

export const userApi = {
  getProfile: () => {
    return axios.get<BaseOutput<UserProfile>>(`${API_PREFIX}/profile`);
  },

  updateProfile: (data: UpdateProfileRequest) => {
    return axios.put<BaseOutput<UserProfile>>(`${API_PREFIX}/profile`, data);
  },

  uploadAvatar: (file: File) => {
    const formData = new FormData();
    formData.append("file", file);
    return axios.post<BaseOutput<{ avatarUrl: string }>>(`${API_PREFIX}/avatar`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },

  getPublicProfile: (id: string) => {
    return axios.get<BaseOutput<PublicUserProfile>>(`${API_PREFIX}/${id}/public`);
  },

  getAuthorArticles: (id: string, page: number = 0, size: number = 10) => {
    return axios.get<BaseOutput<PaginatedResponse<News>>>(`${API_PREFIX}/${id}/articles`, {
      params: { page, size },
    });
  },
};
