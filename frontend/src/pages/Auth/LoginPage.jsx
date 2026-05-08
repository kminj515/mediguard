import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { login } from '../../shared/api/auth';
import useAuthStore from '../../store/authStore';
import ROUTES from '../../shared/constants/routes';
import styles from './Auth.module.css';

export default function LoginPage() {
  const navigate = useNavigate();
  const setAuth = useAuthStore((s) => s.setAuth);

  const [form, setForm] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.email || !form.password) {
      setError('이메일과 비밀번호를 입력해주세요.');
      return;
    }
    setLoading(true);
    try {
      const { data } = await login(form.email, form.password);
      setAuth({ accessToken: data.body.accessToken });
      navigate(ROUTES.HOME, { replace: true });
    } catch (err) {
      setError(
        err.response?.data?.status?.message || '로그인에 실패했습니다. 다시 시도해주세요.'
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <h1 className={styles.logo}>MediGuard</h1>
        <p className={styles.subtitle}>복약 안전 도우미</p>
      </div>

      <form className={styles.form} onSubmit={handleSubmit}>
        <div className={styles.field}>
          <label className={styles.label} htmlFor="email">이메일</label>
          <input
            id="email"
            name="email"
            type="email"
            className={styles.input}
            placeholder="example@email.com"
            value={form.email}
            onChange={handleChange}
            autoComplete="email"
          />
        </div>

        <div className={styles.field}>
          <label className={styles.label} htmlFor="password">비밀번호</label>
          <input
            id="password"
            name="password"
            type="password"
            className={styles.input}
            placeholder="비밀번호를 입력하세요"
            value={form.password}
            onChange={handleChange}
            autoComplete="current-password"
          />
        </div>

        {error && <p className={styles.error}>{error}</p>}

        <button
          type="submit"
          className={styles.submitBtn}
          disabled={loading}
        >
          {loading ? '로그인 중...' : '로그인'}
        </button>
      </form>

      <p className={styles.footer}>
        계정이 없으신가요?{' '}
        <Link to={ROUTES.SIGNUP} className={styles.link}>회원가입</Link>
      </p>
    </div>
  );
}