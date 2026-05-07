import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getProfile } from '../../shared/api/member';
import { getProgress } from '../../shared/api/quiz';
import ROUTES from '../../shared/constants/routes';
import styles from './HomePage.module.css';

const QUICK_MENUS = [
  { label: 'AI 복약 상담', icon: '💬', route: ROUTES.CHATBOT, color: '#eff6ff' },
  { label: '복약 퀴즈',   icon: '📝', route: ROUTES.QUIZ,    color: '#f0fdf4' },
  { label: '약국 찾기',   icon: '🏥', route: ROUTES.PHARMACY, color: '#fff7ed' },
  { label: '약 검색',    icon: '🔍', route: ROUTES.MEDICINE,  color: '#f0f9ff' },
  { label: '진단 테스트', icon: '📋', route: ROUTES.DIAGNOSIS, color: '#fefce8' },
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
      {/* 상단 헤더 */}
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

      {/* 포인트 카드 */}
      {!loading && profile && (
        <div className={styles.gradeCard}>
          <div className={styles.gradeInfo}>
            <span className={styles.gradeTitle}>💊 MediGuard</span>
            <span className={styles.gradeName}>복약 안전 도우미</span>
          </div>
          <div className={styles.pointsInfo}>
            <span className={styles.pointsLabel}>포인트</span>
            <span className={styles.pointsValue}>{profile.points?.toLocaleString() ?? 0}P</span>
          </div>
        </div>
      )}

      {/* 빠른 메뉴 */}
      <section className={styles.section}>
        <h2 className={styles.sectionTitle}>바로가기</h2>
        <div className={styles.menuGrid}>
          {QUICK_MENUS.map(({ label, icon, route, color }) => (
            <button
              key={route}
              className={styles.menuCard}
              style={{ background: color }}
              onClick={() => navigate(route)}
            >
              <span className={styles.menuIcon}>{icon}</span>
              <span className={styles.menuLabel}>{label}</span>
            </button>
          ))}
        </div>
      </section>

      {/* 퀴즈 진행률 */}
      <section className={styles.section}>
        <h2 className={styles.sectionTitle}>퀴즈 진행률</h2>
        {loading ? (
          <p className={styles.empty}>불러오는 중...</p>
        ) : progress.length === 0 ? (
          <p className={styles.empty}>아직 푼 퀴즈가 없어요. 퀴즈를 시작해보세요!</p>
        ) : (
          <div className={styles.progressList}>
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
    </div>
  );
}