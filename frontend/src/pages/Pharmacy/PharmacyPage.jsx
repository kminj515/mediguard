import { useState, useCallback, useEffect, useRef } from 'react';
import { getNearbyPharmacies } from '../../shared/api/pharmacy';
import styles from './PharmacyPage.module.css';

const RADIUS_OPTIONS = [
  { label: '500m', value: 500 },
  { label: '1km',  value: 1000 },
  { label: '3km',  value: 3000 },
  { label: '5km',  value: 5000 },
];

const KAKAO_KEY = import.meta.env.VITE_KAKAO_MAP_KEY;

function loadKakaoScript() {
  return new Promise((resolve, reject) => {
    if (window.kakao?.maps) { resolve(); return; }
    const script = document.createElement('script');
    script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=${KAKAO_KEY}&autoload=false`;
    script.onload = () => window.kakao.maps.load(resolve);
    script.onerror = reject;
    document.head.appendChild(script);
  });
}

export default function PharmacyPage() {
  const mapContainerRef = useRef(null);
  const mapRef = useRef(null);
  const markersRef = useRef([]);

  const [pharmacies, setPharmacies] = useState([]);
  const [status, setStatus] = useState('idle'); // idle | locating | loading | done | error
  const [errorMsg, setErrorMsg] = useState('');
  const [radius, setRadius] = useState(3000);
  const [coords, setCoords] = useState(null);
  const [selected, setSelected] = useState(null);
  const [mapReady, setMapReady] = useState(false);

  // 카카오맵 SDK 로드 및 지도 초기화
  useEffect(() => {
    if (!KAKAO_KEY) {
      setErrorMsg('카카오맵 API 키(VITE_KAKAO_MAP_KEY)가 설정되지 않았습니다.');
      setStatus('error');
      return;
    }
    loadKakaoScript()
      .then(() => {
        const { kakao } = window;
        const options = {
          center: new kakao.maps.LatLng(37.5665, 126.9780),
          level: 7,
        };
        mapRef.current = new kakao.maps.Map(mapContainerRef.current, options);
        setMapReady(true);
      })
      .catch(() => {
        setErrorMsg('카카오맵을 불러오지 못했습니다.');
        setStatus('error');
      });
  }, []);

  // 약국 목록이 바뀔 때 마커 갱신
  useEffect(() => {
    if (!mapReady || !window.kakao?.maps) return;
    const { kakao } = window;

    markersRef.current.forEach((m) => m.setMap(null));
    markersRef.current = [];

    if (pharmacies.length === 0) return;

    const bounds = new kakao.maps.LatLngBounds();
    if (coords) bounds.extend(new kakao.maps.LatLng(coords.lat, coords.lon));

    pharmacies.forEach((p) => {
      if (p.latitude == null || p.longitude == null) return;
      const position = new kakao.maps.LatLng(p.latitude, p.longitude);
      bounds.extend(position);

      const marker = new kakao.maps.Marker({ position, map: mapRef.current });
      kakao.maps.event.addListener(marker, 'click', () => setSelected(p));
      markersRef.current.push(marker);
    });

    mapRef.current.setBounds(bounds);
  }, [pharmacies, mapReady, coords]);

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
      ({ coords: c }) => {
        const { latitude, longitude } = c;
        setCoords({ lat: latitude, lon: longitude });
        if (mapRef.current && window.kakao?.maps) {
          mapRef.current.setCenter(new window.kakao.maps.LatLng(latitude, longitude));
        }
        fetchPharmacies(latitude, longitude, radius);
      },
      (err) => {
        setErrorMsg(
          err.code === err.PERMISSION_DENIED
            ? '위치 권한이 필요합니다. 브라우저 설정에서 허용해주세요.'
            : '위치를 가져오지 못했습니다. 다시 시도해주세요.'
        );
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
      {/* 카카오맵 컨테이너 */}
      <div className={styles.mapContainer} ref={mapContainerRef} />

      {/* 상단 오버레이 컨트롤 */}
      <div className={styles.topOverlay}>
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
        <button
          className={styles.locateBtn}
          onClick={handleLocate}
          disabled={isLoading}
        >
          {status === 'locating' && '📍 위치 확인 중...'}
          {status === 'loading'  && '🔍 검색 중...'}
          {(status === 'idle' || status === 'error') && '📍 내 위치로 검색'}
          {status === 'done'    && '🔄 재검색'}
        </button>
      </div>

      {/* 에러 토스트 */}
      {status === 'error' && (
        <div className={styles.errorToast}>{errorMsg}</div>
      )}

      {/* 결과 개수 배지 */}
      {status === 'done' && pharmacies.length > 0 && !selected && (
        <div className={styles.countBadge}>주변 {pharmacies.length}개 약국</div>
      )}

      {/* 초기 안내 */}
      {status === 'idle' && (
        <div className={styles.guide}>
          <span>💊</span>
          <p>버튼을 눌러 내 위치 기반으로<br />주변 약국을 찾아보세요</p>
        </div>
      )}

      {/* 선택한 약국 바텀 시트 */}
      {selected && (
        <div className={styles.bottomSheet}>
          <div className={styles.sheetHandle} />
          <div className={styles.sheetContent}>
            <button className={styles.sheetClose} onClick={() => setSelected(null)}>✕</button>
            <div className={styles.sheetHeader}>
              <span className={styles.sheetIcon}>🏥</span>
              <div className={styles.sheetInfo}>
                <h3 className={styles.sheetName}>{selected.name}</h3>
                <p className={styles.sheetAddress}>{selected.address}</p>
              </div>
            </div>
            <div className={styles.sheetMeta}>
              {selected.distance != null && (
                <span className={styles.distanceBadge}>
                  {selected.distance < 1000
                    ? `${Math.round(selected.distance)}m`
                    : `${(selected.distance / 1000).toFixed(1)}km`}
                </span>
              )}
              {selected.nightPharmacy && <span className={styles.tag}>심야약국</span>}
              {selected.twentyFourHours && <span className={styles.tag}>24시간</span>}
            </div>
            {selected.operatingHours && (
              <p className={styles.sheetHours}>🕐 {selected.operatingHours}</p>
            )}
            {selected.contact ? (
              <a href={`tel:${selected.contact}`} className={styles.phoneBtn}>
                📞 {selected.contact}
              </a>
            ) : (
              <span className={styles.noPhone}>전화번호 없음</span>
            )}
          </div>
        </div>
      )}
    </div>
  );
}