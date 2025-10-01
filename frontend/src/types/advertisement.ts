export interface Advertisement {
  id: string;
  title: string;
  description: string;
  adType: string;
  position: string;
  format: string;
  imageUrl?: string;
  htmlContent?: string;
  scriptCode?: string;
  targetUrl?: string;
  openInNewTab: boolean;
  width?: string;
  height?: string;
  startDate?: string;
  endDate?: string;
  active: boolean;
  priority: number;
  impressions: number;
  clicks: number;
  displayFrequency: number;
  maxDailyImpressions: number;
  createdAt: string;
  updatedAt: string;
}

export type AdPosition = 
  | 'SIDEBAR_TOP'
  | 'SIDEBAR_MIDDLE'
  | 'SIDEBAR_BOTTOM'
  | 'TOP_BANNER'
  | 'BOTTOM_BANNER'
  | 'IN_FEED'
  | 'FOOTER';

export type AdFormat = 'IMAGE' | 'HTML' | 'SCRIPT';

