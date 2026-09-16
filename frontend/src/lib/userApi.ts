import axios from "@/utils/axios-customize";
import { BaseOutput, UpdateProfileRequest, UserProfile } from "@/types/backend";

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
};
