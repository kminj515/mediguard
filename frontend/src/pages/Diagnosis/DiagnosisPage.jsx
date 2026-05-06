import { useState, useEffect } from 'react';
import { getDiagnosisQuestions, submitDiagnosis } from '../../shared/api/diagnosis';
import styles from './DiagnosisPage.module.css';

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
        <p className={styles.resultMessage}>{result.message}</p>
      </div>
      <button className={styles.retryBtn} onClick={onRetry}>다시 진단하기</button>
    </div>
  );
}

export default function DiagnosisPage() {
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
      .catch(() => {})
      .finally(() => setLoading(false));
  };

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
    } catch {
      setError('제출에 실패했습니다. 다시 시도해주세요.');
    } finally {
      setSubmitting(false);
    }
  };

  if (result) return <ResultScreen result={result} onRetry={loadQuestions} />;

  if (loading) return <p className={styles.center}>불러오는 중...</p>;

  return (
    <div className={styles.page}>
      <header className={styles.header}>
        <h1 className={styles.title}>복약 역량 진단</h1>
        <p className={styles.sub}>총 {totalCount}문항 · {answeredCount}개 완료</p>
      </header>

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
    </div>
  );
}