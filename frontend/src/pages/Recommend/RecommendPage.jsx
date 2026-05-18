import { useState, useCallback } from 'react';
import { recommendWithPharmacy } from '../../shared/api/chatbot';
import styles from './RecommendPage.module.css';

const QUICK_SYMPTOMS = ['두통', '발열', '기침', '복통', '소화불량', '근육통', '콧물', '목통증'];

export default function RecommendPage() {
  const [symptom, setSymptom] = useState('');
  const [coords, setCoords] = useState(null);
  const [useLocation, setUseLocation] = useState(false);
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);
  const [error, setError] = useState('');

  const handleLocate = useCallback(() => {
    if (!navigator.geolocation) {
      setError('위치 서비스를 지원하지 않는 기기입니다.');
      return;
    }
    navigator.geolocation.getCurrentPosition(
      ({ coords: c }) => {
        setCoords({ lat: c.latitude, lon: c.longitude });
        setUseLocation(true);
        setError('');
      },
      () => {
        setError('위치를 가져오지 못했습니다. 브라우저 위치 권한을 확인해주세요.');
        setUseLocation(false);
      },
      { timeout: 10000 }
    );
  }, []);

  const handleSubmit = async () => {
    if (!symptom.trim()) { setError('증상을 입력해주세요.'); return; }
    setError('');
    setLoading(true);
    setResult(null);
    try {
      const payload = {
        prompt: symptom,
        ...(useLocation && coords
          ? { latitude: coords.lat, longitude: coords.lon, radius: 3000 }
          : {}),
      };
      const { data } = await recommendWithPharmacy(payload);
      setResult(data.body);
    } catch (err) {
      setError(err.response?.data?.status?.message || '오류가 발생했습니다. 다시 시도해주세요.');
    } finally {
      setLoading(false);
    }
  };

  const handleReset = () => {
    setResult(null);
    setSymptom('');
    setUseLocation(false);
    setCoords(null);
    setError('');
  };

  return (
    <div className={styles.page}>
      <header className={styles.header}>
        <h1 className={styles.title}>증상별 약 추천</h1>
        <p className={styles.sub}>증상을 입력하면 AI가 약을 추천하고 주변 약국을 찾아드려요</p>
      </header>

      {!result ? (
        <div className={styles.inputSection}>
          <div className={styles.quickTags}>
            {QUICK_SYMPTOMS.map((s) => (
              <button
                key={s}
                className={`${styles.quickTag} ${symptom === s ? styles.quickTagActive : ''}`}
                onClick={() => setSymptom(s)}
              >
                {s}
              </button>
            ))}
          </div>

          <textarea
            className={styles.textarea}
            placeholder="증상을 자세히 입력하세요&#10;예) 어제부터 두통이 심하고 미열이 있어요"
            value={symptom}
            onChange={(e) => { setSymptom(e.target.value); setError(''); }}
            rows={4}
          />

          <button
            className={`${styles.locationBtn} ${useLocation ? styles.locationActive : ''}`}
            onClick={handleLocate}
            type="button"
          >
            {useLocation ? '위치 연동됨 - 주변 약국을 함께 찾아드려요' : '내 위치로 주변 약국도 함께 찾기 (선택)'}
          </button>

          {error && <p className={styles.error}>{error}</p>}

          <button
            className={styles.submitBtn}
            onClick={handleSubmit}
            disabled={loading || !symptom.trim()}
          >
            {loading ? 'AI 분석 중...' : '약 추천받기'}
          </button>

          {loading && (
            <div className={styles.loadingBox}>
              <div className={styles.dots}>
                <span /><span /><span />
              </div>
              <p className={styles.loadingText}>AI가 증상을 분석하고 있어요...</p>
            </div>
          )}
        </div>
      ) : (
        <div className={styles.result}>
          <section className={styles.section}>
            <h2 className={styles.sectionTitle}>AI 약 추천</h2>
            <div className={styles.aiAnswer}>
              {result.aiAnswer.split('\n').map((line, i) => (
                <span key={i}>
                  {line}
                  {i < result.aiAnswer.split('\n').length - 1 && <br />}
                </span>
              ))}
            </div>
          </section>

          {result.nearbyPharmacies?.length > 0 && (
            <section className={styles.section}>
              <h2 className={styles.sectionTitle}>
                주변 약국 ({result.nearbyPharmacies.length}개)
              </h2>
              <div className={styles.pharmacyList}>
                {result.nearbyPharmacies.map((p) => (
                  <div key={p.pharmacyId} className={styles.pharmacyCard}>
                    <div className={styles.pharmacyInfo}>
                      <p className={styles.pharmacyName}>{p.name}</p>
                      <p className={styles.pharmacyAddress}>{p.address}</p>
                      <div className={styles.pharmacyMeta}>
                        {p.distance != null && (
                          <span className={styles.distanceBadge}>
                            {p.distance < 1000
                              ? `${Math.round(p.distance)}m`
                              : `${(p.distance / 1000).toFixed(1)}km`}
                          </span>
                        )}
                        {p.nightPharmacy && <span className={styles.badge}>심야</span>}
                        {p.twentyFourHours && <span className={styles.badge}>24h</span>}
                      </div>
                    </div>
                    {p.contact && (
                      <a href={`tel:${p.contact}`} className={styles.callBtn}>전화</a>
                    )}
                  </div>
                ))}
              </div>
            </section>
          )}

          {result.nearbyPharmacies?.length === 0 && (
            <p className={styles.noPharmacy}>위치를 제공하지 않아 주변 약국을 찾지 않았어요.</p>
          )}

          <button className={styles.resetBtn} onClick={handleReset}>
            다시 검색하기
          </button>
        </div>
      )}
    </div>
  );
}