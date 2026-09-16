"use client";

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import Image from "next/image";
import Link from "next/link";
import { userApi } from "@/lib/userApi";
import { PublicUserProfile } from "@/types/backend";
import { News, PaginatedResponse } from "@/types";
import JournalistBadge from "@/components/JournalistBadge";
import {
  FaCalendarAlt,
  FaUsers,
  FaUserPlus,
  FaNewspaper,
  FaEye,
  FaArrowLeft,
  FaShareAlt,
  FaCheck,
  FaExclamationCircle,
  FaSpinner,
} from "react-icons/fa";

export default function AuthorProfilePage() {
  const params = useParams();
  const router = useRouter();
  const userId = params.id as string;

  const [profile, setProfile] = useState<PublicUserProfile | null>(null);
  const [articles, setArticles] = useState<PaginatedResponse<News> | null>(null);
  const [currentPage, setCurrentPage] = useState(0);
  const pageSize = 6;

  const [isLoadingProfile, setIsLoadingProfile] = useState(true);
  const [isLoadingArticles, setIsLoadingArticles] = useState(true);
  const [isNotFound, setIsNotFound] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const [isFollowing, setIsFollowing] = useState(false);
  const [copiedLink, setCopiedLink] = useState(false);

  const backendHost =
    process.env.NEXT_PUBLIC_API_URL?.replace("/api/v1", "") ||
    "http://localhost:8080";

  const getFullAvatarUrl = (url?: string) => {
    if (!url) {
      return "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200&auto=format&fit=crop&q=80";
    }
    if (url.startsWith("http")) return url;
    return `${backendHost}${url}`;
  };

  const getFullImageUrl = (url?: string) => {
    if (!url) return "/placeholder.jpg";
    if (url.startsWith("http")) return url;
    return `${backendHost}${url}`;
  };

  // Fetch Public Profile
  useEffect(() => {
    if (!userId) return;

    const fetchPublicProfile = async () => {
      try {
        setIsLoadingProfile(true);
        setIsNotFound(false);
        const res: any = await userApi.getPublicProfile(userId);
        const data = res?.data || res;
        if (data) {
          setProfile(data);
        } else {
          setIsNotFound(true);
        }
      } catch (err: any) {
        console.error("Error fetching author public profile:", err);
        setIsNotFound(true);
        setErrorMessage(
          err?.response?.data?.message || "Hồ sơ người dùng không khả dụng hoặc đã bị vô hiệu hóa."
        );
      } finally {
        setIsLoadingProfile(false);
      }
    };

    fetchPublicProfile();
  }, [userId]);

  // Fetch Author's Published Articles
  useEffect(() => {
    if (!userId) return;

    const fetchArticles = async () => {
      try {
        setIsLoadingArticles(true);
        const res: any = await userApi.getAuthorArticles(userId, currentPage, pageSize);
        const data = res?.data || res;
        if (data) {
          setArticles(data);
        }
      } catch (err) {
        console.error("Error fetching author articles:", err);
      } finally {
        setIsLoadingArticles(false);
      }
    };

    fetchArticles();
  }, [userId, currentPage]);

  const handleShare = () => {
    if (typeof window !== "undefined") {
      navigator.clipboard.writeText(window.location.href);
      setCopiedLink(true);
      setTimeout(() => setCopiedLink(false), 2500);
    }
  };

  const handleToggleFollow = () => {
    setIsFollowing((prev) => !prev);
  };

  const formatDate = (isoString?: string) => {
    if (!isoString) return "";
    try {
      const date = new Date(isoString);
      return date.toLocaleDateString("vi-VN", {
        year: "numeric",
        month: "long",
        day: "numeric",
      });
    } catch {
      return isoString;
    }
  };

  // 404 Not Found State
  if (!isLoadingProfile && isNotFound) {
    return (
      <div className="min-h-[70vh] flex items-center justify-center px-4 py-16 bg-slate-50">
        <div className="max-w-md w-full bg-white rounded-2xl shadow-sm border border-slate-200 p-8 text-center">
          <div className="w-16 h-16 bg-amber-100 text-amber-600 rounded-full flex items-center justify-center mx-auto mb-4 text-2xl">
            <FaExclamationCircle />
          </div>
          <h2 className="text-2xl font-bold text-slate-800 mb-2">
            Không tìm thấy tác giả
          </h2>
          <p className="text-slate-600 text-sm mb-6">
            {errorMessage || "Hồ sơ tác giả này không tồn tại hoặc đã bị vô hiệu hóa."}
          </p>
          <div className="flex gap-3 justify-center">
            <button
              onClick={() => router.back()}
              className="inline-flex items-center gap-2 px-4 py-2 text-sm font-semibold rounded-full border border-slate-300 text-slate-700 hover:bg-slate-50 transition"
            >
              <FaArrowLeft className="text-xs" /> Quay lại
            </button>
            <Link
              href="/"
              className="inline-flex items-center gap-2 px-5 py-2 text-sm font-semibold rounded-full bg-[#FF6600] text-white hover:bg-[#E05A00] transition"
            >
              Về Trang chủ
            </Link>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-slate-50 pb-16">
      {/* Cover Banner */}
      <div className="h-48 md:h-64 w-full bg-gradient-to-r from-slate-900 via-sky-950 to-orange-950 relative overflow-hidden">
        <div className="absolute inset-0 bg-[radial-gradient(#ffffff15_1px,transparent_1px)] [background-size:16px_16px] opacity-40"></div>
        <div className="container mx-auto px-4 h-full relative flex items-end">
          <button
            onClick={() => router.back()}
            className="absolute top-6 left-4 md:left-6 flex items-center gap-2 px-3 py-1.5 rounded-full bg-black/40 hover:bg-black/60 text-white text-xs font-medium backdrop-blur-sm transition"
          >
            <FaArrowLeft /> Quay lại
          </button>
        </div>
      </div>

      <div className="container mx-auto px-4 max-w-5xl">
        {/* Profile Header Card */}
        <div className="bg-white rounded-2xl shadow-sm border border-slate-200 -mt-20 md:-mt-24 p-6 md:p-8 relative z-10">
          {isLoadingProfile ? (
            <div className="animate-pulse flex flex-col md:flex-row gap-6 items-start">
              <div className="w-28 h-28 md:w-32 md:h-32 bg-slate-200 rounded-full"></div>
              <div className="flex-1 space-y-3 w-full">
                <div className="h-7 bg-slate-200 rounded w-1/3"></div>
                <div className="h-4 bg-slate-200 rounded w-1/4"></div>
                <div className="h-16 bg-slate-200 rounded w-full"></div>
                <div className="h-4 bg-slate-200 rounded w-1/2"></div>
              </div>
            </div>
          ) : profile ? (
            <div className="flex flex-col md:flex-row gap-6 items-start md:items-center justify-between">
              <div className="flex flex-col md:flex-row gap-6 items-start md:items-center">
                {/* Avatar */}
                <div className="relative w-28 h-28 md:w-32 md:h-32 rounded-full overflow-hidden border-4 border-white shadow-md bg-slate-100 flex-shrink-0">
                  <Image
                    src={getFullAvatarUrl(profile.avatarUrl)}
                    alt={profile.fullName || profile.username}
                    fill
                    className="object-cover"
                    sizes="128px"
                    priority
                  />
                </div>

                {/* Author Info */}
                <div className="space-y-2">
                  <div className="flex flex-wrap items-center gap-2.5">
                    <h1 className="text-2xl md:text-3xl font-bold text-slate-900">
                      {profile.fullName || profile.username}
                    </h1>
                    {profile.isJournalistVerified && (
                      <JournalistBadge
                        organization={profile.journalistOrganization}
                        size="md"
                      />
                    )}
                  </div>

                  <p className="text-sm text-slate-500 font-medium">
                    @{profile.username}
                  </p>

                  {/* Bio */}
                  {profile.bio && (
                    <p className="text-sm md:text-base text-slate-700 max-w-2xl leading-relaxed whitespace-pre-line pt-1">
                      {profile.bio}
                    </p>
                  )}

                  {/* Metadata & Stats */}
                  <div className="flex flex-wrap items-center gap-y-2 gap-x-6 pt-3 text-xs md:text-sm text-slate-600">
                    {profile.createdAt && (
                      <div className="flex items-center gap-1.5 text-slate-500">
                        <FaCalendarAlt className="text-slate-400" />
                        <span>Tham gia {formatDate(profile.createdAt)}</span>
                      </div>
                    )}
                    <div className="flex items-center gap-1.5 font-medium">
                      <FaUsers className="text-slate-400" />
                      <span className="text-slate-900 font-semibold">
                        {profile.followersCount + (isFollowing ? 1 : 0)}
                      </span>
                      <span>người theo dõi</span>
                    </div>
                    <div className="flex items-center gap-1.5 font-medium">
                      <span className="text-slate-900 font-semibold">
                        {profile.followingCount}
                      </span>
                      <span>đang theo dõi</span>
                    </div>
                    <div className="flex items-center gap-1.5 font-medium">
                      <FaNewspaper className="text-slate-400" />
                      <span className="text-slate-900 font-semibold">
                        {articles?.totalElements ?? articles?.content?.length ?? 0}
                      </span>
                      <span>bài viết</span>
                    </div>
                  </div>
                </div>
              </div>

              {/* Action Buttons */}
              <div className="flex items-center gap-3 w-full md:w-auto mt-2 md:mt-0 pt-4 md:pt-0 border-t md:border-t-0 border-slate-100">
                <button
                  onClick={handleToggleFollow}
                  className={`flex-1 md:flex-none inline-flex items-center justify-center gap-2 px-6 py-2.5 rounded-full text-sm font-bold shadow-sm transition ${
                    isFollowing
                      ? "bg-slate-100 text-slate-800 hover:bg-slate-200 border border-slate-300"
                      : "bg-[#FF6600] text-white hover:bg-[#E05A00]"
                  }`}
                >
                  <FaUserPlus className="text-xs" />
                  <span>{isFollowing ? "Đang theo dõi" : "Theo dõi"}</span>
                </button>

                <button
                  onClick={handleShare}
                  className="p-2.5 rounded-full border border-slate-200 text-slate-600 hover:text-slate-900 hover:bg-slate-100 transition relative"
                  title="Sao chép liên kết trang tác giả"
                >
                  {copiedLink ? (
                    <FaCheck className="text-emerald-600 w-4 h-4" />
                  ) : (
                    <FaShareAlt className="w-4 h-4" />
                  )}
                  {copiedLink && (
                    <span className="absolute -bottom-8 right-0 text-[11px] bg-slate-900 text-white px-2 py-0.5 rounded shadow whitespace-nowrap">
                      Đã sao chép!
                    </span>
                  )}
                </button>
              </div>
            </div>
          ) : null}
        </div>

        {/* Author Showcase: Articles Section */}
        <div className="mt-8">
          <div className="flex items-center justify-between mb-6">
            <div className="flex items-center gap-2.5">
              <h2 className="text-xl md:text-2xl font-bold text-slate-900">
                Bài viết đã xuất bản
              </h2>
              {articles && articles.totalElements > 0 && (
                <span className="bg-orange-100 text-[#FF6600] text-xs font-bold px-2.5 py-0.5 rounded-full">
                  {articles.totalElements}
                </span>
              )}
            </div>
          </div>

          {/* Articles Grid / List */}
          {isLoadingArticles ? (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {[1, 2, 3].map((i) => (
                <div
                  key={i}
                  className="bg-white rounded-xl border border-slate-200 p-4 animate-pulse space-y-3"
                >
                  <div className="h-44 bg-slate-200 rounded-lg w-full"></div>
                  <div className="h-5 bg-slate-200 rounded w-3/4"></div>
                  <div className="h-4 bg-slate-200 rounded w-full"></div>
                  <div className="h-4 bg-slate-200 rounded w-1/2"></div>
                </div>
              ))}
            </div>
          ) : articles && articles.content && articles.content.length > 0 ? (
            <>
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {articles.content.map((item) => (
                  <article
                    key={item.id}
                    className="bg-white rounded-xl overflow-hidden border border-slate-200 shadow-sm hover:shadow-md transition duration-200 flex flex-col group"
                  >
                    <Link
                      href={`/news/${item.slug}`}
                      className="block relative h-48 w-full overflow-hidden bg-slate-100"
                    >
                      <Image
                        src={getFullImageUrl(item.imageUrl)}
                        alt={item.title}
                        fill
                        className="object-cover group-hover:scale-105 transition-transform duration-300"
                        sizes="(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw"
                      />
                      {item.categoryName && (
                        <span className="absolute top-3 left-3 bg-black/60 backdrop-blur-sm text-white text-[11px] font-semibold px-2.5 py-0.5 rounded-full">
                          {item.categoryName}
                        </span>
                      )}
                    </Link>

                    <div className="p-5 flex-1 flex flex-col justify-between">
                      <div>
                        <div className="flex items-center gap-2 text-xs text-slate-500 mb-2">
                          <span>{formatDate(item.createdAt || item.publishedAt)}</span>
                          {item.viewCount !== undefined && (
                            <>
                              <span>•</span>
                              <span className="flex items-center gap-1">
                                <FaEye className="text-slate-400" /> {item.viewCount}
                              </span>
                            </>
                          )}
                        </div>

                        <Link href={`/news/${item.slug}`}>
                          <h3 className="text-base font-bold text-slate-900 group-hover:text-[#FF6600] transition line-clamp-2 mb-2 leading-snug">
                            {item.title}
                          </h3>
                        </Link>

                        {item.excerpt && (
                          <p className="text-xs text-slate-600 line-clamp-2 leading-relaxed mb-4">
                            {item.excerpt}
                          </p>
                        )}
                      </div>

                      <div className="pt-3 border-t border-slate-100 flex items-center justify-between text-xs text-slate-500">
                        <Link
                          href={`/news/${item.slug}`}
                          className="text-[#FF6600] font-semibold hover:underline"
                        >
                          Đọc tiếp →
                        </Link>
                      </div>
                    </div>
                  </article>
                ))}
              </div>

              {/* Pagination */}
              {articles.totalPages > 1 && (
                <div className="mt-8 flex justify-center items-center gap-3">
                  <button
                    disabled={currentPage === 0}
                    onClick={() => setCurrentPage((p) => Math.max(0, p - 1))}
                    className="px-4 py-2 text-xs md:text-sm font-semibold rounded-full border border-slate-300 bg-white text-slate-700 disabled:opacity-40 disabled:cursor-not-allowed hover:bg-slate-50 transition"
                  >
                    ← Trang trước
                  </button>
                  <span className="text-xs md:text-sm text-slate-600 font-medium">
                    Trang {currentPage + 1} / {articles.totalPages}
                  </span>
                  <button
                    disabled={currentPage >= articles.totalPages - 1}
                    onClick={() => setCurrentPage((p) => p + 1)}
                    className="px-4 py-2 text-xs md:text-sm font-semibold rounded-full border border-slate-300 bg-white text-slate-700 disabled:opacity-40 disabled:cursor-not-allowed hover:bg-slate-50 transition"
                  >
                    Trang sau →
                  </button>
                </div>
              )}
            </>
          ) : (
            /* Empty State */
            <div className="bg-white rounded-2xl border border-slate-200 p-12 text-center max-w-xl mx-auto shadow-sm">
              <div className="w-16 h-16 bg-orange-50 text-[#FF6600] rounded-full flex items-center justify-center mx-auto mb-4 text-2xl">
                <FaNewspaper />
              </div>
              <h3 className="text-lg font-bold text-slate-800 mb-1">
                Tác giả chưa xuất bản bài viết nào
              </h3>
              <p className="text-sm text-slate-500 max-w-sm mx-auto mb-6">
                Khi tác giả chia sẻ các tin tức hoặc bài phân tích mới, bạn sẽ nhìn thấy chúng xuất hiện ở đây.
              </p>
              <Link
                href="/"
                className="inline-flex items-center gap-2 px-5 py-2 text-xs md:text-sm font-semibold rounded-full bg-slate-100 hover:bg-slate-200 text-slate-700 transition"
              >
                Khám phá bài viết khác
              </Link>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
