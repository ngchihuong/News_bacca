import React from 'react';

interface JournalistBadgeProps {
  organization?: string;
  size?: 'sm' | 'md';
  className?: string;
}

export default function JournalistBadge({
  organization,
  size = 'md',
  className = '',
}: JournalistBadgeProps) {
  const isSmall = size === 'sm';

  return (
    <span
      className={`inline-flex items-center gap-1 font-semibold rounded-full bg-[#E0F2FE] dark:bg-sky-950/60 text-[#0284C7] dark:text-sky-300 border border-[#BAE6FD] dark:border-sky-800 transition-colors ${
        isSmall ? 'text-[11px] px-2 py-0.5' : 'text-xs px-2.5 py-1'
      } ${className}`}
      title={organization ? `Nhà báo xác thực thuộc ${organization}` : 'Nhà báo đã được xác thực danh tính'}
    >
      <svg
        className={isSmall ? 'w-3 h-3 flex-shrink-0 text-[#0284C7] dark:text-sky-300' : 'w-3.5 h-3.5 flex-shrink-0 text-[#0284C7] dark:text-sky-300'}
        viewBox="0 0 20 20"
        fill="currentColor"
        aria-hidden="true"
      >
        <path
          fillRule="evenodd"
          d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.857-9.809a.75.75 0 00-1.214-.882l-3.483 4.79-1.88-1.88a.75.75 0 10-1.06 1.061l2.5 2.5a.75.75 0 001.137-.089l4-5.5z"
          clipRule="evenodd"
        />
      </svg>
      <span>Nhà báo xác thực</span>
      {organization && (
        <>
          <span className="text-sky-300 dark:text-sky-400 font-bold">·</span>
          <span className="font-medium text-sky-800 dark:text-sky-200">{organization}</span>
        </>
      )}
    </span>
  );
}
