import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getProfile } from '../../shared/api/member';
import { getProgress } from '../../shared/api/quiz';
import ROUTES from '../../shared/constants/routes';
import styles from './HomePage.module.css';

const FEATURED_MENUS = [
  {
    label: 'AI 복약 상담',
    desc: '궁금한 약, AI에게 바로 물어보세요',
    icon: '💬',
    route: ROUTES.CHATBOT,
  },
  {
    label: '약국 찾기',
    desc: '내 주변 가까운 약국을 찾아드려요',
    icon: '🏥',
    route: ROUTES.PHARMACY,
  },
];

const QUICK_MENUS = [
  { label: '증상별 추천', icon: '🩺', route: ROUTES.RECOMMEND },
  { label: '복약 퀴즈',   icon: '📝', route: ROUTES.QUIZ },
  { label: '복약 기록',   icon: '💊', route: ROUTES.INTAKE_RECORD },
  { label: '약 검색',    icon: '🔍', route: ROUTES.MEDICINE },
  { label: '진단 테스트', icon: '📋', route: ROUTES.DIAGNOSIS },
  { label: '교육 영상',   icon: '🎬', route: ROUTES.VIDEO },
];

export default function HomePage() {
  const navigate = useNavigate();
  const [profile, setProfile] = useState(null);
  const [progress, setProgress] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([getProfile(), getProgress()])
      .then(([profileRes, progressRes]) => {
        setProfile(profileRes.data.body);
        setProgress(progressRes.data.body ?? []);
      })
      .catch(() => {})
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className={styles.page}>

      {/* 헤더 */}
      <header className={styles.header}>
        <div>
          <p className={styles.greeting}>안녕하세요 👋</p>
          <h1 className={styles.nickname}>
            {loading ? '...' : (profile?.nickname ?? '회원')}님
          </h1>
        </div>
        <button
          className={styles.profileBtn}
          onClick={() => navigate(ROUTES.MY_PAGE)}
          aria-label="마이페이지"
        >
          {profile?.profileImageUrl
            ? <img src={profile.profileImageUrl} alt="프로필" className={styles.profileImg} />
            : <span className={styles.profileDefault}>👤</span>
          }
        </button>
      </header>

      {/* 포인트 히어로 카드 */}
      {!loading && profile && (
        <div className={styles.heroCard}>
          <div className={styles.heroLeft}>
            <span className={styles.heroAppName}>💊 MediGuard</span>
            <span className={styles.heroSubtitle}>복약 안전 도우미</span>
          </div>
          <div className={styles.heroRight}>
            <span className={styles.heroPointLabel}>포인트</span>
            <span className={styles.heroPoint}>
              {(profile.points ?? 0).toLocaleString()}
              <span className={styles.heroPointUnit}>P</span>
            </span>
          </div>
        </div>
      )}

      {/* 주요 기능 */}
      <section className={styles.section}>
        <h2 className={styles.sectionTitle}>주요 기능</h2>
        <div className={styles.featuredGrid}>
          {FEATURED_MENUS.map(({ label, desc, icon, route }) => (
            <button
              key={route}
              className={styles.featuredCard}
              onClick={() => navigate(route)}
            >
              <span className={styles.featuredIcon}>{icon}</span>
              <p className={styles.featuredLabel}>{label}</p>
              <p className={styles.featuredDesc}>{desc}</p>
            </button>
          ))}
        </div>
      </section>

      {/* 바로가기 */}
      <section className={styles.section}>
        <h2 className={styles.sectionTitle}>바로가기</h2>
        <div className={styles.quickGrid}>
          {QUICK_MENUS.map(({ label, icon, route }) => (
            <button
              key={route}
              className={styles.quickItem}
              onClick={() => navigate(route)}
            >
              <span className={styles.quickIcon}>{icon}</span>
              <span className={styles.quickLabel}>{label}</span>
            </button>
          ))}
        </div>
      </section>

      {/* 퀴즈 진행률 */}
      {!loading && (
        <section className={styles.section}>
          <h2 className={styles.sectionTitle}>퀴즈 진행률</h2>
          {progress.length === 0 ? (
            <p className={styles.empty}>아직 푼 퀴즈가 없어요. 퀴즈를 시작해보세요!</p>
          ) : (
            <div className={styles.progressCard}>
              {progress.map((item) => (
                <div key={item.categoryId} className={styles.progressItem}>
                  <div className={styles.progressHeader}>
                    <span className={styles.progressCategory}>{item.categoryName}</span>
                    <span className={styles.progressPercent}>{item.progress}%</span>
                  </div>
                  <div className={styles.progressBar}>
                    <div
                      className={styles.progressFill}
                      style={{ width: `${item.progress}%` }}
                    />
                  </div>
                </div>
              ))}
            </div>
          )}
        </section>
      )}

    </div>
  );
}