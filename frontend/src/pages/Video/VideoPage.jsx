import { useState, useEffect } from 'react';
import {
  getAllVideos,
  getPopularVideos,
  getVideoCategories,
  getVideosByCategory,
  incrementViewCount,
} from '../../shared/api/video';
import styles from './VideoPage.module.css';

const BASE_TABS = [
  { key: 'all',     label: '전체' },
  { key: 'popular', label: '인기' },
];

function VideoCard({ video, onClick }) {
  const thumb = video.thumbnailUrl
    || (video.youtubeId ? `https://img.youtube.com/vi/${video.youtubeId}/mqdefault.jpg` : null);

  return (
    <button className={styles.card} onClick={() => onClick(video)}>
      <div className={styles.thumb}>
        {thumb
          ? <img src={thumb} alt={video.title} className={styles.thumbImg} />
          : <span className={styles.thumbFallback}>▶</span>
        }
        {video.duration && <span className={styles.duration}>{video.duration}</span>}
      </div>
      <div className={styles.cardInfo}>
        <p className={styles.cardTitle}>{video.title}</p>
        {video.categoryName && (
          <span className={styles.categoryBadge}>{video.categoryName}</span>
        )}
        {video.viewCount != null && (
          <span className={styles.viewCount}>조회 {video.viewCount.toLocaleString()}</span>
        )}
      </div>
    </button>
  );
}

function VideoPlayerSheet({ video, onClose }) {
  useEffect(() => {
    incrementViewCount(video.id).catch(() => {});
  }, [video.id]);

  const embedUrl = video.embedUrl
    || (video.youtubeId ? `https://www.youtube.com/embed/${video.youtubeId}` : null);

  return (
    <div className={styles.overlay}>
      <div className={styles.playerSheet}>
        <div className={styles.sheetHeader}>
          <h2 className={styles.sheetTitle}>{video.title}</h2>
          <button className={styles.closeBtn} onClick={onClose}>✕</button>
        </div>
        {embedUrl ? (
          <div className={styles.videoWrap}>
            <iframe
              src={embedUrl}
              title={video.title}
              frameBorder="0"
              allowFullScreen
              className={styles.iframe}
            />
          </div>
        ) : (
          <div className={styles.noPlayer}>
            <p>영상을 불러올 수 없습니다.</p>
          </div>
        )}
        {video.description && (
          <p className={styles.description}>{video.description}</p>
        )}
      </div>
    </div>
  );
}

export default function VideoPage() {
  const [tabs, setTabs] = useState(BASE_TABS);
  const [tab, setTab] = useState('all');
  const [videos, setVideos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selected, setSelected] = useState(null);

  // 카테고리 탭 로드
  useEffect(() => {
    getVideoCategories()
      .then(({ data }) => {
        const cats = (data.body ?? []).map((c) => ({
          key: `cat-${c.id}`,
          label: c.icon ? `${c.icon} ${c.name}` : c.name,
        }));
        setTabs([...BASE_TABS, ...cats]);
      })
      .catch(() => {});
  }, []);

  // 탭 변경 시 영상 목록 로드
  useEffect(() => {
    setLoading(true);
    let request;
    if (tab === 'popular') {
      request = getPopularVideos().then(({ data }) => data.body ?? []);
    } else if (tab.startsWith('cat-')) {
      const categoryId = tab.split('-')[1];
      request = getVideosByCategory(categoryId).then(({ data }) => data.body?.videos ?? []);
    } else {
      request = getAllVideos().then(({ data }) => data.body ?? []);
    }
    request
      .then(setVideos)
      .catch(() => setVideos([]))
      .finally(() => setLoading(false));
  }, [tab]);

  return (
    <div className={styles.page}>
      <header className={styles.header}>
        <h1 className={styles.title}>교육 영상</h1>
      </header>

      <div className={styles.tabs}>
        {tabs.map((t) => (
          <button
            key={t.key}
            className={`${styles.tab} ${tab === t.key ? styles.tabActive : ''}`}
            onClick={() => setTab(t.key)}
          >
            {t.label}
          </button>
        ))}
      </div>

      {loading ? (
        <p className={styles.center}>불러오는 중...</p>
      ) : videos.length === 0 ? (
        <div className={styles.emptyBox}>
          <span className={styles.emptyIcon}>🎬</span>
          <p>영상이 없습니다.</p>
        </div>
      ) : (
        <div className={styles.list}>
          {videos.map((v) => (
            <VideoCard key={v.id} video={v} onClick={setSelected} />
          ))}
        </div>
      )}

      {selected && (
        <VideoPlayerSheet video={selected} onClose={() => setSelected(null)} />
      )}
    </div>
  );
}