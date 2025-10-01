'use client';

import { useEffect, useRef, useState } from 'react';
import { Advertisement } from '@/types/advertisement';
import { advertisementApi } from '@/lib/adApi';

interface AdUnitProps {
  ad: Advertisement;
  onImpression?: () => void;
  onClick?: () => void;
}

export default function AdUnit({ ad, onImpression, onClick }: AdUnitProps) {
  const [hasTrackedImpression, setHasTrackedImpression] = useState(false);
  const adRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    // Track impression when ad becomes visible
    if (!hasTrackedImpression && adRef.current) {
      const observer = new IntersectionObserver(
        (entries) => {
          entries.forEach((entry) => {
            if (entry.isIntersecting) {
              trackImpression();
              observer.disconnect();
            }
          });
        },
        { threshold: 0.5 }
      );

      observer.observe(adRef.current);

      return () => observer.disconnect();
    }
  }, [hasTrackedImpression]);

  const trackImpression = async () => {
    if (hasTrackedImpression) return;
    
    try {
      await advertisementApi.trackImpression(ad.id);
      setHasTrackedImpression(true);
      onImpression?.();
    } catch (error) {
      console.error('Error tracking impression:', error);
    }
  };

  const handleClick = async () => {
    try {
      await advertisementApi.trackClick(ad.id);
      onClick?.();
    } catch (error) {
      console.error('Error tracking click:', error);
    }
  };

  const renderAdContent = () => {
    switch (ad.format) {
      case 'IMAGE':
        return (
          <a
            href={ad.targetUrl || '#'}
            target={ad.openInNewTab ? '_blank' : '_self'}
            rel={ad.openInNewTab ? 'noopener noreferrer' : undefined}
            onClick={handleClick}
            className="block"
          >
            <img
              src={ad.imageUrl}
              alt={ad.title}
              className="w-full h-auto"
              style={{
                maxWidth: ad.width || '100%',
                maxHeight: ad.height || 'auto',
              }}
            />
          </a>
        );

      case 'HTML':
        return (
          <div
            dangerouslySetInnerHTML={{ __html: ad.htmlContent || '' }}
            onClick={handleClick}
          />
        );

      case 'SCRIPT':
        return (
          <div
            ref={(el) => {
              if (el && ad.scriptCode) {
                el.innerHTML = ad.scriptCode;
                // Execute scripts
                const scripts = el.getElementsByTagName('script');
                Array.from(scripts).forEach((script) => {
                  const newScript = document.createElement('script');
                  Array.from(script.attributes).forEach((attr) => {
                    newScript.setAttribute(attr.name, attr.value);
                  });
                  newScript.textContent = script.textContent;
                  script.parentNode?.replaceChild(newScript, script);
                });
              }
            }}
          />
        );

      default:
        return null;
    }
  };

  return (
    <div ref={adRef} className="ad-unit mb-4">
      <div className="text-xs text-gray-400 text-center mb-1">Advertisement</div>
      <div className="bg-gray-50 p-2 rounded">
        {renderAdContent()}
      </div>
    </div>
  );
}

