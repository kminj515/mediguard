import { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { getProfile, updateProfile } from '../../shared/api/member';
import { getLevels } from '../../shared/api/quiz';
import useAuthStore from '../../store/authStore';
import ROUTES from '../../shared/constants/routes';
import styles from './MyPage.module.css';

const LEVEL_COLOR = {
  1: { bg: '#f0fdf4', color: '#16a34a' },
  2: { bg: '#fff7ed', color: '#d97706' },
  3: { bg: '#fef2f2', color: '#dc2626' },
};

// ──────────────────────────────────────────
// 프로필 수정 폼
// ──────────────────────────────────────────
function EditProfileSheet({ profile, onClose, onSaved }) {
  const [form, setForm] = useState({
    nickname: profile.nickname ?? '',
    currentPassword: '',
    password: '',
  });
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const handleChange = (e) => {
    setForm((p) => ({ ...p, [e.target.name]: e.target.value }));
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.nickname.trim()) { setError('닉네임을 입력해주세요.'); return; }
    if (form.password && !form.currentPassword) {
      setError('현재 비밀번호를 입력해주세요.');
      return;
    }
    setSubmitting(true);
    try {
      await updateProfile({
        nickname: form.nickname,
        currentPassword: form.currentPassword || null,
        password: form.password || null,
      });
      onSaved();
    } catch (err) {
      setError(err.response?.data?.status?.message || '수정에 실패했습니다.');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className={styles.overlay} onClick={onClose}>
      <div className={styles.sheet} onClick={(e) => e.stopPropagation()}>
        <div className={styles.sheetHandle} />
        <div className={styles.sheetHeader}>
          <h2 className={styles.sheetTitle}>프로필 수정</h2>
          <button className={styles.closeBtn} onClick={onClose}>✕</button>
        </div>
        <form onSubmit={handleSubmit} className={styles.form}>
          <div className={styles.field}>
            <label className={styles.label}>닉네임</label>
            <input
              name="nickname"
              className={styles.input}
              value={form.nickname}
              onChange={handleChange}
              placeholder="닉네임 입력"
            />
          </div>
          <div className={styles.divider} />
          <p className={styles.sectionLabel}>비밀번호 변경 (선택)</p>
          <div className={styles.field}>
            <label className={styles.label}>현재 비밀번호</label>
            <input
              name="currentPassword"
              type="password"
              className={styles.input}
              value={form.currentPassword}
              onChange={handleChange}
              placeholder="현재 비밀번호"
              autoComplete="current-password"
            />
          </div>
          <div className={styles.field}>
            <label className={styles.label}>새 비밀번호</label>
            <input
              name="password"
              type="password"
              className={styles.input}
              value={form.password}
              onChange={handleChange}
              placeholder="새 비밀번호 (8자 이상)"
              autoComplete="new-password"
            />
          </div>
          {error && <p className={styles.errorText}>{error}</p>}
          <button type="submit" className={styles.saveBtn} disabled={submitting}>
            {submitting ? '저장 중...' : '저장하기'}
          </button>
        </form>
      </div>
    </div>
  );
}

// ──────────────────────────────────────────
// 메인 페이지
// ──────────────────────────────────────────
export default function MyPage() {
  const navigate = useNavigate();
  const clearAuth = useAuthStore((s) => s.clearAuth);

  const [profile, setProfile] = useState(null);
  const [levels, setLevels] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showEdit, setShowEdit] = useState(false);

  const loadData = useCallback(() => {
    Promise.all([getProfile(), getLevels()])
      .then(([profileRes, levelsRes]) => {
        setProfile(profileRes.data.body);
        setLevels(levelsRes.data.body ?? []);
      })
      .catch(() => {})
      .finally(() => setLoading(false));
  }, []);

  useEffect(() => { loadData(); }, [loadData]);

  const handleLogout = () => {
    clearAuth();
    navigate(ROUTES.LOGIN, { replace: true });
  };

  const handleSaved = () => {
    setShowEdit(false);
    loadData();
  };

  if (loading) return <p className={styles.center}>불러오는 중...</p>;

  return (
    <div className={styles.page}>

      {/* 프로필 카드 - 중앙 정렬 */}
      <div className={styles.profileCard}>
        <div className={styles.avatarWrap}>
          {profile?.profileImageUrl
            ? <img src={profile.profileImageUrl} alt="프로필" className={styles.avatar} />
            : <span className={styles.avatarDefault}>👤</span>
          }
        </div>
        <h1 className={styles.nickname}>{profile?.nickname ?? '-'}</h1>
        <p className={styles.email}>{profile?.email ?? '-'}</p>
      </div>

      {/* 포인트 / EXP */}
      <div className={styles.statsRow}>
        <div className={styles.statBox}>
          <span className={`${styles.statValue} ${styles.statBlue}`}>
            {(profile?.points ?? 0).toLocaleString()}
          </span>
          <span className={styles.statLabel}>포인트</span>
        </div>
        <div className={styles.statDivider} />
        <div className={styles.statBox}>
          <span className={`${styles.statValue} ${styles.statGreen}`}>
            {profile?.exp ?? 0}
          </span>
          <span className={styles.statLabel}>EXP</span>
        </div>
      </div>

      {/* 퀴즈 레벨 */}
      {levels.length > 0 && (
        <section className={styles.section}>
          <h2 className={styles.sectionTitle}>퀴즈 레벨</h2>
          <div className={styles.levelGrid}>
            {levels.map((lv) => {
              const color = LEVEL_COLOR[lv.level] ?? LEVEL_COLOR[1];
              return (
                <div key={lv.categoryId} className={styles.levelCard}>
                  <span
                    className={styles.levelBadge}
                    style={{ background: color.bg, color: color.color }}
                  >
                    LV.{lv.level}
                  </span>
                  <span className={styles.levelCategory}>{lv.categoryName}</span>
                </div>
              );
            })}
          </div>
        </section>
      )}

      {/* 설정 메뉴 */}
      <section className={styles.section}>
        <h2 className={styles.sectionTitle}>설정</h2>
        <div className={styles.menuList}>
          <button className={styles.menuItem} onClick={() => setShowEdit(true)}>
            <span className={`${styles.menuIconWrap} ${styles.iconBlue}`}>✏️</span>
            <span className={styles.menuLabel}>프로필 수정</span>
            <span className={styles.menuArrow}>›</span>
          </button>
          <button className={styles.menuItem} onClick={() => navigate(ROUTES.SHOP)}>
            <span className={`${styles.menuIconWrap} ${styles.iconPurple}`}>🎁</span>
            <span className={styles.menuLabel}>리워드 샵</span>
            <span className={styles.menuArrow}>›</span>
          </button>
          <button
            className={`${styles.menuItem} ${styles.menuItemDanger}`}
            onClick={handleLogout}
          >
            <span className={`${styles.menuIconWrap} ${styles.iconRed}`}>🚪</span>
            <span className={styles.menuLabel}>로그아웃</span>
            <span className={styles.menuArrow}>›</span>
          </button>
        </div>
      </section>

      {showEdit && profile && (
        <EditProfileSheet
          profile={profile}
          onClose={() => setShowEdit(false)}
          onSaved={handleSaved}
        />
      )}
    </div>
  );
}