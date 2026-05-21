import { useState, useEffect } from 'react';
import {
  getCategories,
  getQuizzesByCategory,
  getQuizDetail,
  submitAnswer,
} from '../../shared/api/quiz';
import styles from './QuizPage.module.css';

const CATEGORY_STYLES = [
  { icon: '💊', bg: '#eff6ff', accent: '#2563eb' },
  { icon: '⚠️', bg: '#fff7ed', accent: '#d97706' },
  { icon: '✅', bg: '#f0fdf4', accent: '#16a34a' },
  { icon: '🔄', bg: '#f5f3ff', accent: '#7c3aed' },
];

const LEVEL_LABEL = { 1: '초급', 2: '중급', 3: '고급' };
const LEVEL_COLOR = { 1: '#f0fdf4', 2: '#fff7ed', 3: '#fef2f2' };
const LEVEL_TEXT  = { 1: '#16a34a', 2: '#d97706', 3: '#dc2626' };

// ──────────────────────────────────────────
// 카테고리 목록
// ──────────────────────────────────────────
function CategoryScreen({ onSelect }) {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getCategories()
      .then(({ data }) => setCategories(data.body ?? []))
      .catch(() => {})
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p className={styles.center}>불러오는 중...</p>;

  return (
    <div className={styles.screen}>
      <header className={styles.pageHeader}>
        <h1 className={styles.pageTitle}>복약 퀴즈</h1>
        <p className={styles.pageSub}>카테고리를 선택하세요</p>
      </header>
      <div className={styles.categoryGrid}>
        {categories.map((cat, i) => {
          const s = CATEGORY_STYLES[i] ?? CATEGORY_STYLES[0];
          return (
            <button
              key={cat.categoryId}
              className={styles.categoryCard}
              style={{ background: s.bg }}
              onClick={() => onSelect(cat)}
            >
              <span className={styles.categoryIcon}>{s.icon}</span>
              <span className={styles.categoryName} style={{ color: s.accent }}>
                {cat.name}
              </span>
            </button>
          );
        })}
      </div>
    </div>
  );
}

// ──────────────────────────────────────────
// 퀴즈 목록
// ──────────────────────────────────────────
function QuizListScreen({ category, onBack, onSelect }) {
  const [quizzes, setQuizzes] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getQuizzesByCategory(category.categoryId)
      .then(({ data }) => setQuizzes(data.body?.quizzes ?? []))
      .catch(() => {})
      .finally(() => setLoading(false));
  }, [category.categoryId]);

  if (loading) return <p className={styles.center}>불러오는 중...</p>;

  return (
    <div className={styles.screen}>
      <header className={styles.pageHeader}>
        <button className={styles.backBtn} onClick={onBack}>← 뒤로</button>
        <h1 className={styles.pageTitle}>{category.name}</h1>
        <p className={styles.pageSub}>총 {quizzes.length}문제</p>
      </header>
      <div className={styles.quizList}>
        {quizzes.map((quiz, idx) => (
          <button
            key={quiz.quizId}
            className={styles.quizItem}
            onClick={() => onSelect(quiz)}
          >
            <div className={styles.quizTop}>
              <span className={styles.quizIndex}>Q{idx + 1}</span>
              <div className={styles.quizMeta}>
                <span
                  className={styles.levelBadge}
                  style={{ background: LEVEL_COLOR[quiz.level], color: LEVEL_TEXT[quiz.level] }}
                >
                  {LEVEL_LABEL[quiz.level] ?? `Lv${quiz.level}`}
                </span>
                <span className={styles.pointBadge}>+{quiz.point}P</span>
              </div>
            </div>
            <span className={styles.quizQuestion}>{quiz.question}</span>
          </button>
        ))}
      </div>
    </div>
  );
}

// ──────────────────────────────────────────
// 문제 풀기
// ──────────────────────────────────────────
function QuizQuestionScreen({ quiz, onBack, onResult }) {
  const [detail, setDetail] = useState(null);
  const [selected, setSelected] = useState(null);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    getQuizDetail(quiz.quizId)
      .then(({ data }) => setDetail(data.body))
      .catch(() => {})
      .finally(() => setLoading(false));
  }, [quiz.quizId]);

  const handleSubmit = async () => {
    if (selected === null || submitting) return;
    setSubmitting(true);
    try {
      const { data } = await submitAnswer(quiz.quizId, selected);
      onResult({ ...data.body, question: detail.question, point: detail.point });
    } catch {
      onResult({ isCorrect: false, gainExp: 0, message: '오류가 발생했습니다.', question: detail.question });
    } finally {
      setSubmitting(false);
    }
  };

  if (loading || !detail) return <p className={styles.center}>불러오는 중...</p>;

  return (
    <div className={styles.screen}>
      <div className={styles.questionHeader}>
        <button className={styles.backBtn} onClick={onBack}>← 뒤로</button>
        <span className={styles.pointTag}>+{detail.point}P</span>
      </div>

      <div className={styles.questionBox}>
        <p className={styles.questionLabel}>문제</p>
        <p className={styles.questionText}>{detail.question}</p>
      </div>

      <div className={styles.optionList}>
        {detail.options.map((opt, idx) => (
          <button
            key={opt.optionId}
            className={`${styles.optionBtn} ${selected === opt.optionId ? styles.optionSelected : ''}`}
            onClick={() => setSelected(opt.optionId)}
          >
            <span className={styles.optionNum}>{idx + 1}</span>
            <span className={styles.optionText}>{opt.optionText}</span>
          </button>
        ))}
      </div>

      <button
        className={styles.submitBtn}
        onClick={handleSubmit}
        disabled={selected === null || submitting}
      >
        {submitting ? '채점 중...' : '제출하기'}
      </button>
    </div>
  );
}

// ──────────────────────────────────────────
// 결과 화면
// ──────────────────────────────────────────
function ResultScreen({ result, onRetry, onHome }) {
  const isCorrect = result.isCorrect;
  return (
    <div className={styles.screen}>
      <div className={`${styles.resultCard} ${isCorrect ? styles.resultCorrect : styles.resultWrong}`}>
        <span className={styles.resultEmoji}>{isCorrect ? '🎉' : '😢'}</span>
        <h2 className={`${styles.resultTitle} ${isCorrect ? styles.titleCorrect : styles.titleWrong}`}>
          {isCorrect ? '정답이에요!' : '틀렸어요'}
        </h2>
        <p className={styles.resultMessage}>{result.message}</p>
        {isCorrect && result.gainExp > 0 && (
          <div className={styles.expBadge}>+{result.gainExp} EXP 획득!</div>
        )}
      </div>

      <div className={styles.resultActions}>
        <button className={styles.retryBtn} onClick={onRetry}>다른 문제 풀기</button>
        <button className={styles.homeBtn} onClick={onHome}>카테고리로 돌아가기</button>
      </div>
    </div>
  );
}

// ──────────────────────────────────────────
// 메인 페이지
// ──────────────────────────────────────────
export default function QuizPage() {
  const [view, setView] = useState('categories');
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [selectedQuiz, setSelectedQuiz] = useState(null);
  const [result, setResult] = useState(null);

  const goCategories = () => {
    setView('categories');
    setSelectedCategory(null);
    setSelectedQuiz(null);
    setResult(null);
  };

  const goList = () => {
    setView('list');
    setSelectedQuiz(null);
    setResult(null);
  };

  if (view === 'categories') return <CategoryScreen onSelect={(cat) => { setSelectedCategory(cat); setView('list'); }} />;
  if (view === 'list')       return <QuizListScreen category={selectedCategory} onBack={goCategories} onSelect={(quiz) => { setSelectedQuiz(quiz); setView('question'); }} />;
  if (view === 'question')   return <QuizQuestionScreen quiz={selectedQuiz} onBack={goList} onResult={(res) => { setResult(res); setView('result'); }} />;
  if (view === 'result')     return <ResultScreen result={result} onRetry={goList} onHome={goCategories} />;
}