import { NavLink } from 'react-router-dom';
import ROUTES from '../../shared/constants/routes';
import styles from './BottomNav.module.css';

const NAV_ITEMS = [
  { to: ROUTES.HOME,     label: '홈',    icon: '🏠' },
  { to: ROUTES.CHATBOT,  label: '상담',   icon: '💬' },
  { to: ROUTES.QUIZ,     label: '퀴즈',   icon: '📝' },
  { to: ROUTES.PHARMACY, label: '약국',   icon: '🏥' },
  { to: ROUTES.MY_PAGE,  label: '마이',   icon: '👤' },
];

export default function BottomNav() {
  return (
    <nav className={styles.nav}>
      {NAV_ITEMS.map(({ to, label, icon }) => (
        <NavLink
          key={to}
          to={to}
          end={to === ROUTES.HOME}
          className={({ isActive }) =>
            `${styles.item} ${isActive ? styles.active : ''}`
          }
        >
          <span className={styles.icon}>{icon}</span>
          <span className={styles.label}>{label}</span>
        </NavLink>
      ))}
    </nav>
  );
}