import { useState, useEffect } from 'react';
import { getDiagnosisQuestions, submitDiagnosis } from '../../shared/api/diagnosis';
import styles from './DiagnosisPage.module.css';

const HISTORY_KEY = 'diagnosis_history';

const loadHistory = () => {
  try { return JSON.parse(localStorage.getItem(HISTORY_KEY)) ?? []; }
  catch { return []; }
};

const saveHistory = (entry) => {
  const prev = loadHistory();
  localStorage.setItem(HISTORY_KEY, JSON.stringify([entry, ...prev].slice(0, 20)));
};

const GRADE_COLOR = {
  '우수': '#16a34a', '양호': '#2563eb', '보통': '#d97706', '미흡': '#dc2626',
};

function ResultScreen({ result, onRetry }) {
  return (
    <div className={styles.resultPage}>
      <div className={styles.resultCard}>
        <span className={styles.resultEmoji}>📋</span>
        <h2 className={styles.resultTitle}>진단 완료!</h2>
        <div className={styles.scoreBox}>
          <span className={styles.scoreValue}>{result.totalScore}</span>
          <span className={styles.scoreLabel}>점</span>
        </div>
        {result.grade && (
          <span
            className={styles.gradeBadge}
            style={{ background: `${GRADE_COLOR[result.grade] ?? '#6b7280'}20`, color: GRADE_COLOR[result.grade] ?? '#6b7280' }}
          >
            {result.grade}
          </span>
        )}
        <p className={styles.resultMessage}>{result.message}</p>
      </div>
      <button className={styles.retryBtn} onClick={onRetry}>다시 진단하기</button>
    </div>
  );
}

function HistoryScreen() {
  const history = loadHistory();
  if (history.length === 0) {
    return (
      <div className={styles.historyEmpty}>
        <span>📋</span>
        <p>아직 진단 기록이 없어요.</p>
        <p className={styles.historyEmptySub}>진단을 완료하면 결과가 여기에 쌓여요.</p>
      </div>
    );
  }
  const scores = history.map((h) => h.totalScore);
  const best = Math.max(...scores);
  return (
    <div className={styles.historyList}>
      <p className={styles.historyBest}>최고 점수 · <strong>{best}점</strong></p>
      {history.map((h, i) => (
        <div key={i} className={styles.historyCard}>
          <div className={styles.historyCardLeft}>
            <span
              className={styles.historyGrade}
              style={{ color: GRADE_COLOR[h.grade] ?? '#6b7280' }}
            >
              {h.grade ?? '-'}
            </span>
            <span className={styles.historyDate}>{h.date}</span>
          </div>
          <span className={styles.historyScore}>{h.totalScore}점</span>
        </div>
      ))}
    </div>
  );
}

export default function DiagnosisPage() {
  const [tab, setTab] = useState('diagnose');
  const [parts, setParts] = useState([]);
  const [answers, setAnswers] = useState({});
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');

  const loadQuestions = () => {
    setLoading(true);
    setResult(null);
    setAnswers({});
    getDiagnosisQuestions()
      .then(({ data }) => setParts(data.body ?? []))
      .catch(() => setError('문제를 불러오는데 실패했습니다. 다시 시도해주세요.'))
      .finally(() => setLoading(false));
  };

  // eslint-disable-next-line react-hooks/set-state-in-effect
  useEffect(() => { loadQuestions(); }, []);

  const allQuestions = parts.flatMap((p) => p.questions);
  const totalCount = allQuestions.length;
  const answeredCount = Object.keys(answers).length;
  const progress = totalCount > 0 ? Math.round((answeredCount / totalCount) * 100) : 0;

  const handleSelect = (questionId, optionId) => {
    setAnswers((prev) => ({ ...prev, [questionId]: optionId }));
  };

  const handleSubmit = async () => {
    if (answeredCount < totalCount) {
      setError(`아직 ${totalCount - answeredCount}개 문항이 남았습니다.`);
      return;
    }
    setError('');
    setSubmitting(true);
    const payload = Object.entries(answers).map(([qId, optId]) => ({
      quizID: Number(qId),
      selectedAnswer: optId,
    }));
    try {
      const { data } = await submitDiagnosis(payload);
      setResult(data.body);
      saveHistory({
        totalScore: data.body.totalScore,
        grade: data.body.grade,
        date: new Date().toLocaleDateString('ko-KR'),
      });
    } catch {
      setError('제출에 실패했습니다. 다시 시도해주세요.');
    } finally {
      setSubmitting(false);
    }
  };

  if (result) return <ResultScreen result={result} onRetry={loadQuestions} />;

  if (loading && tab === 'diagnose') return <p className={styles.center}>불러오는 중...</p>;

  if (error && parts.length === 0 && tab === 'diagnose') return (
    <div className={styles.page}>
      <p className={styles.errorText}>{error}</p>
      <button className={styles.retryBtn} onClick={loadQuestions}>다시 시도</button>
    </div>
  );

  return (
    <div className={styles.page}>
      <header className={styles.header}>
        <h1 className={styles.title}>복약 역량 진단</h1>
      </header>

      <div className={styles.tabs}>
        <button
          className={`${styles.tab} ${tab === 'diagnose' ? styles.tabActive : ''}`}
          onClick={() => setTab('diagnose')}
        >진단하기</button>
        <button
          className={`${styles.tab} ${tab === 'history' ? styles.tabActive : ''}`}
          onClick={() => setTab('history')}
        >진단 기록</button>
      </div>

      {tab === 'history' && <HistoryScreen />}

      {tab === 'diagnose' && <>
        <p className={styles.sub}>총 {totalCount}문항 · {answeredCount}개 완료</p>

      {/* 진행 바 */}
      <div className={styles.progressBar}>
        <div className={styles.progressFill} style={{ width: `${progress}%` }} />
      </div>

      {/* 파트별 문제 */}
      <div className={styles.partList}>
        {parts.map((part) => (
          <div key={part.partId} className={styles.part}>
            <h2 className={styles.partTitle}>{part.partName}</h2>
            {part.questions.map((q, idx) => (
              <div key={q.questionId} className={styles.questionBlock}>
                <p className={styles.questionText}>
                  <span className={styles.questionNum}>Q{idx + 1}.</span> {q.questionText}
                </p>
                <div className={styles.optionList}>
                  {q.options.map((opt) => {
                    const selected = answers[q.questionId] === opt.optionId;
                    return (
                      <button
                        key={opt.optionId}
                        className={`${styles.optionBtn} ${selected ? styles.optionSelected : ''}`}
                        onClick={() => handleSelect(q.questionId, opt.optionId)}
                      >
                        <span className={`${styles.optionDot} ${selected ? styles.optionDotOn : ''}`} />
                        <span className={styles.optionText}>{opt.text}</span>
                      </button>
                    );
                  })}
                </div>
              </div>
            ))}
          </div>
        ))}
      </div>

        {error && <p className={styles.errorText}>{error}</p>}

        <button
          className={styles.submitBtn}
          onClick={handleSubmit}
          disabled={submitting}
        >
          {submitting ? '채점 중...' : `제출하기 (${answeredCount}/${totalCount})`}
        </button>
      </>}
    </div>
  );
}