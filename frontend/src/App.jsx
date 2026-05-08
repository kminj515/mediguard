import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import ROUTES from './shared/constants/routes';
import Layout from './components/layout/Layout';
import useAuthStore from './store/authStore';

import HomePage from './pages/Home/HomePage';
import LoginPage from './pages/Auth/LoginPage';
import SignupPage from './pages/Auth/SignupPage';
import ChatbotPage from './pages/Chatbot/ChatbotPage';
import QuizPage from './pages/Quiz/QuizPage';
import PharmacyPage from './pages/Pharmacy/PharmacyPage';
import MedicinePage from './pages/Medicine/MedicinePage';
import IntakeRecordPage from './pages/IntakeRecord/IntakeRecordPage';
import DiagnosisPage from './pages/Diagnosis/DiagnosisPage';
import VideoPage from './pages/Video/VideoPage';
import ShopPage from './pages/Shop/ShopPage';
import MyPage from './pages/MyPage/MyPage';

function PrivateRoute({ children }) {
  const accessToken = useAuthStore((s) => s.accessToken);
  return accessToken ? children : <Navigate to={ROUTES.LOGIN} replace />;
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* 인증 불필요 */}
        <Route path={ROUTES.LOGIN} element={<LoginPage />} />
        <Route path={ROUTES.SIGNUP} element={<SignupPage />} />

        {/* 챗봇 - 전체화면 단독 레이아웃 */}
        <Route path={ROUTES.CHATBOT} element={<PrivateRoute><ChatbotPage /></PrivateRoute>} />

        {/* 인증 필요 - 레이아웃 적용 */}
        <Route element={<PrivateRoute><Layout /></PrivateRoute>}>
          <Route path={ROUTES.HOME} element={<HomePage />} />
          <Route path={ROUTES.QUIZ} element={<QuizPage />} />
          <Route path={ROUTES.PHARMACY} element={<PharmacyPage />} />
          <Route path={ROUTES.MEDICINE} element={<MedicinePage />} />
          <Route path={ROUTES.INTAKE_RECORD} element={<IntakeRecordPage />} />
          <Route path={ROUTES.DIAGNOSIS} element={<DiagnosisPage />} />
          <Route path={ROUTES.VIDEO} element={<VideoPage />} />
          <Route path={ROUTES.SHOP} element={<ShopPage />} />
          <Route path={ROUTES.MY_PAGE} element={<MyPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}