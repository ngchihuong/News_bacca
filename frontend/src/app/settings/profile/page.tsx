"use client";

import { useEffect, useState, useRef } from "react";
import { useAppContext } from "@/context/AuthContext";
import { userApi } from "@/lib/userApi";
import { UserProfile } from "@/types/backend";
import { useRouter } from "next/navigation";
import { 
  FaCamera, 
  FaUser, 
  FaEnvelope, 
  FaPhone, 
  FaLock, 
  FaCheckCircle, 
  FaExclamationCircle, 
  FaSpinner, 
  FaCloudUploadAlt 
} from "react-icons/fa";

export default function ProfileSettingsPage() {
  const { user, isAuthenticated, setUser } = useAppContext();
  const router = useRouter();

  const [profile, setProfile] = useState<UserProfile | null>(null);
  const [fullName, setFullName] = useState("");
  const [phone, setPhone] = useState("");
  const [bio, setBio] = useState("");
  
  const [isLoadingProfile, setIsLoadingProfile] = useState(true);
  const [isSaving, setIsSaving] = useState(false);
  const [isUploadingAvatar, setIsUploadingAvatar] = useState(false);
  const [statusMessage, setStatusMessage] = useState<{ type: "success" | "error"; text: string } | null>(null);
  const [avatarPreview, setAvatarPreview] = useState<string | null>(null);

  const fileInputRef = useRef<HTMLInputElement | null>(null);

  const backendHost = process.env.NEXT_PUBLIC_API_URL?.replace("/api/v1", "") || "http://localhost:8080";

  const getFullAvatarUrl = (url?: string) => {
    if (!url) return "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200&auto=format&fit=crop&q=80";
    if (url.startsWith("http")) return url;
    return `${backendHost}${url}`;
  };

  useEffect(() => {
    const token = typeof window !== "undefined" ? localStorage.getItem("access_token") : null;
    if (!token && !isAuthenticated) {
      router.push("/auth/login?redirect=/settings/profile");
      return;
    }

    const fetchProfile = async () => {
      try {
        setIsLoadingProfile(true);
        const res: any = await userApi.getProfile();
        const profileData = res?.data || res;
        if (profileData) {
          setProfile(profileData);
          setFullName(profileData.fullName || "");
          setPhone(profileData.phone || "");
          setBio(profileData.bio || "");
          if (profileData.avatarUrl) {
            setAvatarPreview(getFullAvatarUrl(profileData.avatarUrl));
          }
        }
      } catch (err: any) {
        setStatusMessage({
          type: "error",
          text: err?.response?.data?.message || "Không thể tải thông tin hồ sơ",
        });
      } finally {
        setIsLoadingProfile(false);
      }
    };

    fetchProfile();
  }, [isAuthenticated, router]);

  const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;

    // Validate kích thước 2MB
    const MAX_SIZE = 2 * 1024 * 1024;
    if (file.size > MAX_SIZE) {
      setStatusMessage({
        type: "error",
        text: "Kích thước ảnh vượt quá giới hạn 2MB. Vui lòng chọn tệp nhỏ hơn.",
      });
      return;
    }

    // Validate MIME type
    const validTypes = ["image/jpeg", "image/png", "image/webp"];
    if (!validTypes.includes(file.type.toLowerCase())) {
      setStatusMessage({
        type: "error",
        text: "Định dạng ảnh không hợp lệ. Chỉ chấp nhận JPG, PNG hoặc WebP.",
      });
      return;
    }

    // Preview tức thì
    const reader = new FileReader();
    reader.onload = () => {
      setAvatarPreview(reader.result as string);
    };
    reader.readAsDataURL(file);

    // Tiến hành upload lên server & MinIO
    try {
      setIsUploadingAvatar(true);
      setStatusMessage(null);
      const res: any = await userApi.uploadAvatar(file);
      const newAvatarUrl = res?.data?.avatarUrl || res?.avatarUrl;
      if (newAvatarUrl) {
        setAvatarPreview(getFullAvatarUrl(newAvatarUrl));
        // Cập nhật profile state
        setProfile((prev) => prev ? { ...prev, avatarUrl: newAvatarUrl } : null);
        // Đồng bộ cập nhật vào AuthContext và localStorage cho DropdownMenu & Header
        if (user) {
          const updatedUser = { ...user, avatarUrl: newAvatarUrl };
          setUser(updatedUser);
          if (typeof window !== "undefined") {
            localStorage.setItem("user", JSON.stringify(updatedUser));
          }
        }
        setStatusMessage({
          type: "success",
          text: "Cập nhật ảnh đại diện lên MinIO thành công!",
        });
      }
    } catch (err: any) {
      setStatusMessage({
        type: "error",
        text: err?.response?.data?.message || "Tải ảnh đại diện thất bại. Vui lòng thử lại.",
      });
    } finally {
      setIsUploadingAvatar(false);
    }
  };

  const handleSaveProfile = async (e: React.FormEvent) => {
    e.preventDefault();
    if (bio.length > 500) {
      setStatusMessage({
        type: "error",
        text: "Tiểu sử không được vượt quá 500 ký tự.",
      });
      return;
    }

    try {
      setIsSaving(true);
      setStatusMessage(null);
      const res: any = await userApi.updateProfile({
        fullName: fullName.trim(),
        phone: phone.trim(),
        bio: bio.trim(),
      });
      const updatedData = res?.data || res;
      if (updatedData) {
        setProfile(updatedData);
        if (user) {
          const updatedUser = {
            ...user,
            name: updatedData.fullName || user.name,
            phone: updatedData.phone || user.phone,
            bio: updatedData.bio || user.bio,
          };
          setUser(updatedUser);
          if (typeof window !== "undefined") {
            localStorage.setItem("user", JSON.stringify(updatedUser));
          }
        }
        setStatusMessage({
          type: "success",
          text: "Thông tin cá nhân đã được lưu thành công!",
        });
      }
    } catch (err: any) {
      setStatusMessage({
        type: "error",
        text: err?.response?.data?.message || "Cập nhật thông tin thất bại. Vui lòng thử lại.",
      });
    } finally {
      setIsSaving(false);
    }
  };

  if (isLoadingProfile) {
    return (
      <div className="max-w-4xl mx-auto px-4 py-12 flex flex-col items-center justify-center min-h-[50vh]">
        <FaSpinner className="animate-spin text-4xl text-orange-600 mb-4" />
        <p className="text-gray-600 font-medium">Đang tải thông tin hồ sơ của bạn...</p>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      {/* Breadcrumb / Title */}
      <div className="mb-8">
        <h1 className="text-2xl sm:text-3xl font-bold text-gray-900">
          Cài đặt thông tin cá nhân
        </h1>
        <p className="mt-1 text-sm text-gray-500">
          Quản lý ảnh đại diện, thông tin hiển thị và chi tiết tài khoản của bạn trên hệ thống NewsRoom.
        </p>
      </div>

      {/* Alert status notification */}
      {statusMessage && (
        <div
          className={`mb-6 p-4 rounded-xl flex items-center gap-3 transition-all ${
            statusMessage.type === "success"
              ? "bg-green-50 border border-green-200 text-green-800"
              : "bg-red-50 border border-red-200 text-red-800"
          }`}
        >
          {statusMessage.type === "success" ? (
            <FaCheckCircle className="text-lg text-green-600 flex-shrink-0" />
          ) : (
            <FaExclamationCircle className="text-lg text-red-600 flex-shrink-0" />
          )}
          <span className="text-sm font-medium">{statusMessage.text}</span>
        </div>
      )}

      <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
        {/* Left Column: Avatar & Account Summary */}
        <div className="md:col-span-1">
          <div className="bg-white border border-gray-200 rounded-2xl p-6 shadow-sm flex flex-col items-center text-center">
            {/* Avatar container */}
            <div className="relative group">
              <div className="w-32 h-32 rounded-full ring-4 ring-orange-100 overflow-hidden shadow-inner bg-gray-50">
                <img
                  src={avatarPreview || getFullAvatarUrl(profile?.avatarUrl)}
                  alt="Avatar"
                  className="w-full h-full object-cover"
                  onError={(e) => {
                    (e.target as HTMLImageElement).src =
                      "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200&auto=format&fit=crop&q=80";
                  }}
                />
              </div>

              {/* Upload trigger button */}
              <button
                type="button"
                onClick={() => fileInputRef.current?.click()}
                disabled={isUploadingAvatar}
                className="absolute bottom-0 right-0 bg-orange-600 text-white p-2.5 rounded-full shadow-lg hover:bg-orange-700 transition duration-200 disabled:opacity-50"
                title="Tải ảnh đại diện mới"
              >
                {isUploadingAvatar ? (
                  <FaSpinner className="animate-spin text-sm" />
                ) : (
                  <FaCamera className="text-sm" />
                )}
              </button>

              <input
                ref={fileInputRef}
                type="file"
                accept="image/jpeg,image/png,image/webp"
                className="hidden"
                onChange={handleFileChange}
              />
            </div>

            <div className="mt-4">
              <h2 className="text-lg font-bold text-gray-900">
                {profile?.fullName || profile?.username}
              </h2>
              <p className="text-sm text-gray-500">@{profile?.username}</p>
            </div>

            <div className="mt-3">
              <span className="inline-flex items-center px-2.5 py-1 rounded-full text-xs font-semibold bg-orange-100 text-orange-800">
                {profile?.role?.replace("ROLE_", "") || "USER"}
              </span>
            </div>

            <div className="mt-6 pt-6 border-t border-gray-100 w-full text-left text-xs text-gray-500 space-y-2">
              <div className="flex items-center justify-between">
                <span>Trạng thái:</span>
                <span className="font-semibold text-green-600 flex items-center gap-1">
                  <FaCheckCircle className="text-[10px]" /> Hoạt động
                </span>
              </div>
              <div className="flex items-center justify-between">
                <span>Lưu trữ ảnh:</span>
                <span className="font-medium text-gray-700 flex items-center gap-1">
                  <FaCloudUploadAlt className="text-orange-500" /> MinIO Storage
                </span>
              </div>
            </div>

            <button
              type="button"
              onClick={() => fileInputRef.current?.click()}
              disabled={isUploadingAvatar}
              className="mt-6 w-full py-2 px-3 border border-orange-200 text-orange-700 bg-orange-50 hover:bg-orange-100 rounded-xl text-xs font-semibold transition disabled:opacity-50 flex items-center justify-center gap-2"
            >
              {isUploadingAvatar ? (
                <>
                  <FaSpinner className="animate-spin" /> Đang tải lên MinIO...
                </>
              ) : (
                <>
                  <FaCloudUploadAlt className="text-base" /> Đổi ảnh đại diện
                </>
              )}
            </button>
            <p className="mt-1.5 text-[11px] text-gray-400">
              Chấp nhận JPG, PNG, WebP (Tối đa 2MB)
            </p>
          </div>
        </div>

        {/* Right Column: Edit Profile Form */}
        <div className="md:col-span-2">
          <form
            onSubmit={handleSaveProfile}
            className="bg-white border border-gray-200 rounded-2xl p-6 sm:p-8 shadow-sm space-y-6"
          >
            <div className="border-b border-gray-100 pb-4">
              <h3 className="text-lg font-semibold text-gray-900">
                Thông tin cơ bản
              </h3>
              <p className="text-sm text-gray-500">
                Cập nhật thông tin nhận diện hiển thị trên các bài viết và bình luận.
              </p>
            </div>

            {/* Username (Read only) */}
            <div>
              <label className="block text-xs font-semibold uppercase tracking-wider text-gray-500 mb-1.5">
                Tên đăng nhập (Username)
              </label>
              <div className="relative rounded-xl">
                <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-gray-400">
                  <FaUser />
                </div>
                <input
                  type="text"
                  disabled
                  value={profile?.username || ""}
                  className="w-full pl-10 pr-10 py-2.5 bg-gray-50 border border-gray-200 rounded-xl text-sm text-gray-500 cursor-not-allowed"
                />
                <div className="absolute inset-y-0 right-0 pr-3.5 flex items-center pointer-events-none text-gray-400">
                  <FaLock className="text-xs" />
                </div>
              </div>
              <span className="text-[11px] text-gray-400 mt-1 block">
                Tên người dùng là duy nhất và không thể thay đổi sau khi đăng ký.
              </span>
            </div>

            {/* Email (Read only) */}
            <div>
              <label className="block text-xs font-semibold uppercase tracking-wider text-gray-500 mb-1.5">
                Địa chỉ Email
              </label>
              <div className="relative rounded-xl">
                <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-gray-400">
                  <FaEnvelope />
                </div>
                <input
                  type="email"
                  disabled
                  value={profile?.email || ""}
                  className="w-full pl-10 pr-10 py-2.5 bg-gray-50 border border-gray-200 rounded-xl text-sm text-gray-500 cursor-not-allowed"
                />
                <div className="absolute inset-y-0 right-0 pr-3.5 flex items-center pointer-events-none text-gray-400">
                  <FaLock className="text-xs" />
                </div>
              </div>
              <span className="text-[11px] text-gray-400 mt-1 block">
                Email dùng để đăng nhập và nhận thông báo bảo mật từ hệ thống.
              </span>
            </div>

            {/* Full Name (Editable) */}
            <div>
              <label
                htmlFor="fullName"
                className="block text-xs font-semibold uppercase tracking-wider text-gray-700 mb-1.5"
              >
                Họ và tên
              </label>
              <div className="relative rounded-xl">
                <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-gray-400">
                  <FaUser />
                </div>
                <input
                  id="fullName"
                  type="text"
                  required
                  minLength={2}
                  maxLength={100}
                  value={fullName}
                  onChange={(e) => setFullName(e.target.value)}
                  placeholder="Nhập họ và tên đầy đủ"
                  className="w-full pl-10 pr-4 py-2.5 border border-gray-300 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent transition"
                />
              </div>
            </div>

            {/* Phone (Editable) */}
            <div>
              <label
                htmlFor="phone"
                className="block text-xs font-semibold uppercase tracking-wider text-gray-700 mb-1.5"
              >
                Số điện thoại
              </label>
              <div className="relative rounded-xl">
                <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-gray-400">
                  <FaPhone />
                </div>
                <input
                  id="phone"
                  type="tel"
                  value={phone}
                  onChange={(e) => setPhone(e.target.value)}
                  placeholder="Ví dụ: 0912345678"
                  className="w-full pl-10 pr-4 py-2.5 border border-gray-300 rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent transition"
                />
              </div>
            </div>

            {/* Bio (Editable, max 500 chars) */}
            <div>
              <div className="flex justify-between items-center mb-1.5">
                <label
                  htmlFor="bio"
                  className="block text-xs font-semibold uppercase tracking-wider text-gray-700"
                >
                  Tiểu sử (Bio)
                </label>
                <span
                  className={`text-xs font-medium ${
                    bio.length > 500 ? "text-red-500" : "text-gray-400"
                  }`}
                >
                  {bio.length}/500 ký tự
                </span>
              </div>
              <textarea
                id="bio"
                rows={4}
                maxLength={500}
                value={bio}
                onChange={(e) => setBio(e.target.value)}
                placeholder="Giới thiệu đôi nét về bản thân, sở thích hoặc chuyên môn của bạn..."
                className={`w-full p-3 border rounded-xl text-sm focus:outline-none focus:ring-2 focus:border-transparent transition ${
                  bio.length > 500
                    ? "border-red-500 focus:ring-red-500"
                    : "border-gray-300 focus:ring-orange-500"
                }`}
              />
            </div>

            {/* Submit button */}
            <div className="pt-4 flex justify-end">
              <button
                type="submit"
                disabled={isSaving || bio.length > 500}
                className="px-6 py-2.5 bg-orange-600 hover:bg-orange-700 text-white text-sm font-semibold rounded-xl shadow-md hover:shadow transition duration-200 disabled:opacity-50 flex items-center gap-2"
              >
                {isSaving && <FaSpinner className="animate-spin text-sm" />}
                Lưu thay đổi
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
