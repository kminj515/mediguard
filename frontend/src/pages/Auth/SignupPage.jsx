import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { signup } from '../../shared/api/auth';
import ROUTES from '../../shared/constants/routes';
import styles from './Auth.module.css';

export default function SignupPage() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    email: '',
    password: '',
    confirmPassword: '',
    nickname: '',
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
    setError('');
  };

  const validate = () => {
    if (!form.email || !form.password || !form.confirmPassword || !form.nickname) {
      return '모든 항목을 입력해주세요.';
    }
    if (form.password !== form.confirmPassword) {
      return '비밀번호가 일치하지 않습니다.';
    }
    if (form.password.length < 8) {
      return '비밀번호는 8자 이상이어야 합니다.';
    }
    return null;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const validationError = validate();
    if (validationError) {
      setError(validationError);
      return;
    }
    setLoading(true);
    try {
      await signup(form.email, form.password, form.confirmPassword, form.nickname);
      navigate(ROUTES.LOGIN, { replace: true, state: { signedUp: true } });
    } catch (err) {
      setError(
        err.response?.data?.status?.message || '회원가입에 실패했습니다. 다시 시도해주세요.'
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <h1 className={styles.logo}>MediGuard</h1>
        <p className={styles.subtitle}>회원가입</p>
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
          <label className={styles.label} htmlFor="nickname">닉네임</label>
          <input
            id="nickname"
            name="nickname"
            type="text"
            className={styles.input}
            placeholder="닉네임을 입력하세요"
            value={form.nickname}
            onChange={handleChange}
          />
        </div>

        <div className={styles.field}>
          <label className={styles.label} htmlFor="password">비밀번호</label>
          <input
            id="password"
            name="password"
            type="password"
            className={styles.input}
            placeholder="8자 이상 입력하세요"
            value={form.password}
            onChange={handleChange}
            autoComplete="new-password"
          />
        </div>

        <div className={styles.field}>
          <label className={styles.label} htmlFor="confirmPassword">비밀번호 확인</label>
          <input
            id="confirmPassword"
            name="confirmPassword"
            type="password"
            className={styles.input}
            placeholder="비밀번호를 다시 입력하세요"
            value={form.confirmPassword}
            onChange={handleChange}
            autoComplete="new-password"
          />
        </div>

        {error && <p className={styles.error}>{error}</p>}

        <button
          type="submit"
          className={styles.submitBtn}
          disabled={loading}
        >
          {loading ? '처리 중...' : '가입하기'}
        </button>
      </form>

      <p className={styles.footer}>
        이미 계정이 있으신가요?{' '}
        <Link to={ROUTES.LOGIN} className={styles.link}>로그인</Link>
      </p>
    </div>
  );
}