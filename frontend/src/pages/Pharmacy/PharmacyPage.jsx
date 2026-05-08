import { useState, useCallback } from 'react';
import { getNearbyPharmacies } from '../../shared/api/pharmacy';
import styles from './PharmacyPage.module.css';

const RADIUS_OPTIONS = [
  { label: '500m',  value: 500 },
  { label: '1km',   value: 1000 },
  { label: '3km',   value: 3000 },
  { label: '5km',   value: 5000 },
];

function PharmacyCard({ pharmacy }) {
  return (
    <div className={styles.card}>
      <div className={styles.cardHeader}>
        <span className={styles.cardIcon}>🏥</span>
        <div className={styles.cardInfo}>
          <h3 className={styles.cardName}>{pharmacy.fullName}</h3>
          <p className={styles.cardAddress}>
            {pharmacy.roadAddress || pharmacy.address}
          </p>
        </div>
        {pharmacy.distance != null && (
          <span className={styles.distanceBadge}>
            {pharmacy.distance < 1000
              ? `${Math.round(pharmacy.distance)}m`
              : `${(pharmacy.distance / 1000).toFixed(1)}km`}
          </span>
        )}
      </div>

      <div className={styles.cardFooter}>
        {pharmacy.phoneNumber ? (
          <a
            href={`tel:${pharmacy.phoneNumber}`}
            className={styles.phoneBtn}
          >
            📞 {pharmacy.phoneNumber}
          </a>
        ) : (
          <span className={styles.noPhone}>전화번호 없음</span>
        )}
      </div>
    </div>
  );
}

export default function PharmacyPage() {
  const [pharmacies, setPharmacies] = useState([]);
  const [status, setStatus] = useState('idle'); // idle | locating | loading | done | error
  const [errorMsg, setErrorMsg] = useState('');
  const [radius, setRadius] = useState(3000);
  const [coords, setCoords] = useState(null);

  const fetchPharmacies = useCallback(async (lat, lon, r) => {
    setStatus('loading');
    try {
      const { data } = await getNearbyPharmacies(lat, lon, r);
      setPharmacies(data.body ?? []);
      setStatus('done');
    } catch {
      setErrorMsg('약국 정보를 불러오지 못했습니다.');
      setStatus('error');
    }
  }, []);

  const handleLocate = useCallback(() => {
    if (!navigator.geolocation) {
      setErrorMsg('이 기기에서는 위치 서비스를 지원하지 않습니다.');
      setStatus('error');
      return;
    }
    setStatus('locating');
    navigator.geolocation.getCurrentPosition(
      (pos) => {
        const { latitude, longitude } = pos.coords;
        setCoords({ lat: latitude, lon: longitude });
        fetchPharmacies(latitude, longitude, radius);
      },
      (err) => {
        if (err.code === err.PERMISSION_DENIED) {
          setErrorMsg('위치 권한이 필요합니다. 브라우저 설정에서 허용해주세요.');
        } else {
          setErrorMsg('위치를 가져오지 못했습니다. 다시 시도해주세요.');
        }
        setStatus('error');
      },
      { timeout: 10000 }
    );
  }, [radius, fetchPharmacies]);

  const handleRadiusChange = (val) => {
    setRadius(val);
    if (coords) fetchPharmacies(coords.lat, coords.lon, val);
  };

  const isLoading = status === 'locating' || status === 'loading';

  return (
    <div className={styles.page}>
      <header className={styles.header}>
        <h1 className={styles.title}>약국 찾기</h1>
        <p className={styles.sub}>내 주변 약국을 검색합니다</p>
      </header>

      {/* 반경 필터 */}
      <div className={styles.radiusRow}>
        {RADIUS_OPTIONS.map((opt) => (
          <button
            key={opt.value}
            className={`${styles.radiusBtn} ${radius === opt.value ? styles.radiusActive : ''}`}
            onClick={() => handleRadiusChange(opt.value)}
          >
            {opt.label}
          </button>
        ))}
      </div>

      {/* 위치 검색 버튼 */}
      <button
        className={styles.locateBtn}
        onClick={handleLocate}
        disabled={isLoading}
      >
        {status === 'locating' && '📍 위치 확인 중...'}
        {status === 'loading' && '🔍 약국 검색 중...'}
        {(status === 'idle' || status === 'error') && '📍 내 위치로 검색'}
        {status === 'done' && '🔄 다시 검색'}
      </button>

      {/* 에러 */}
      {status === 'error' && (
        <div className={styles.errorBox}>
          <p>{errorMsg}</p>
        </div>
      )}

      {/* 결과 없음 */}
      {status === 'done' && pharmacies.length === 0 && (
        <div className={styles.emptyBox}>
          <span className={styles.emptyIcon}>🏥</span>
          <p>주변 {radius >= 1000 ? `${radius / 1000}km` : `${radius}m`} 이내 약국이 없습니다.</p>
          <p className={styles.emptySub}>반경을 넓혀보세요.</p>
        </div>
      )}

      {/* 결과 목록 */}
      {status === 'done' && pharmacies.length > 0 && (
        <>
          <p className={styles.resultCount}>
            주변 {pharmacies.length}개 약국
          </p>
          <div className={styles.list}>
            {pharmacies.map((p) => (
              <PharmacyCard key={p.id} pharmacy={p} />
            ))}
          </div>
        </>
      )}

      {/* 초기 안내 */}
      {status === 'idle' && (
        <div className={styles.guideBox}>
          <span className={styles.guideIcon}>💊</span>
          <p className={styles.guideText}>
            버튼을 눌러 내 위치 기반으로<br />주변 약국을 찾아보세요
          </p>
        </div>
      )}
    </div>
  );
}