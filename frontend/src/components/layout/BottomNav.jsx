import { NavLink } from 'react-router-dom';
import { Home, MessageCircle, FileEdit, PlusSquare, User } from 'lucide-react'; // 💡 아이콘 라이브러리 추가
import ROUTES from '../../shared/constants/routes';
import styles from './BottomNav.module.css';

// 💡 icon 부분을 이모지 대신 컴포넌트로 교체
const NAV_ITEMS = [
  { to: ROUTES.HOME,     label: '홈',    icon: <Home size={24} /> },
  { to: ROUTES.CHATBOT,  label: '상담',   icon: <MessageCircle size={24} /> },
  { to: ROUTES.QUIZ,     label: '퀴즈',   icon: <FileEdit size={24} /> },
  { to: ROUTES.PHARMACY, label: '약국',   icon: <PlusSquare size={24} /> },
  { to: ROUTES.MY_PAGE,  label: '마이',   icon: <User size={24} /> },
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
              {/* JSX 컴포넌트가 그대로 렌더링됩니다 */}
              <span className={styles.icon}>{icon}</span>
              <span className={styles.label}>{label}</span>
            </NavLink>
        ))}
      </nav>
  );
}