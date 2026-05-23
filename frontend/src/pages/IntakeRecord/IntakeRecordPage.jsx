import { useState, useEffect, useCallback, useRef } from 'react';
import { getIntakeRecords, createIntakeRecord, scanMedicineImage } from '../../shared/api/intakeRecord';
import { searchMedicines } from '../../shared/api/medicine';
import styles from './IntakeRecordPage.module.css';

const formatDate = (iso) => {
  const d = new Date(iso);
  return `${d.getFullYear()}. ${d.getMonth() + 1}. ${d.getDate()}`;
};
const formatTime = (iso) => {
  const d = new Date(iso);
  const h = d.getHours(), m = d.getMinutes();
  return `${h < 12 ? '오전' : '오후'} ${h % 12 || 12}:${String(m).padStart(2, '0')}`;
};
const today = () => new Date().toISOString().slice(0, 16);

const groupByDate = (records) => {
  const map = {};
  records.forEach((r) => {
    const key = formatDate(r.intakeTime);
    if (!map[key]) map[key] = [];
    map[key].push(r);
  });
  return Object.entries(map).sort((a, b) => new Date(b[0]) - new Date(a[0]));
};

const DAY_LABELS = ['일', '월', '화', '수', '목', '금', '토'];

function WeeklyChart({ records }) {
  const days = Array.from({ length: 7 }, (_, i) => {
    const d = new Date();
    d.setDate(d.getDate() - (6 - i));
    return d;
  });

  const counts = days.map((d) => {
    const key = formatDate(d.toISOString());
    return records.filter((r) => formatDate(r.intakeTime) === key).length;
  });

  const maxCount = Math.max(...counts, 1);
  const total = counts.reduce((a, b) => a + b, 0);

  return (
    <div className={styles.chartBox}>
      <div className={styles.chartHeader}>
        <span className={styles.chartTitle}>최근 7일 복약 현황</span>
        <span className={styles.chartTotal}>총 {total}회</span>
      </div>
      <div className={styles.chartBars}>
        {days.map((d, i) => (
          <div key={i} className={styles.chartCol}>
            <span className={styles.chartCount}>{counts[i] > 0 ? counts[i] : ''}</span>
            <div className={styles.chartBarWrap}>
              <div
                className={styles.chartBar}
                style={{ height: `${(counts[i] / maxCount) * 100}%` }}
              />
            </div>
            <span className={styles.chartLabel}>{DAY_LABELS[d.getDay()]}</span>
          </div>
        ))}
      </div>
    </div>
  );
}

function MedicineSearchInput({ onSelect, scanKeyword }) {
  const [keyword, setKeyword] = useState('');
  const [results, setResults] = useState([]);
  const [searching, setSearching] = useState(false);

  useEffect(() => {
    if (scanKeyword) {
      setKeyword(scanKeyword);
      setResults([]);
    }
  }, [scanKeyword]);

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    if (!keyword.trim()) { setResults([]); return; }
    const timer = setTimeout(async () => {
      setSearching(true);
      try {
        const { data } = await searchMedicines(keyword);
        setResults(data.body ?? []);
      } catch { setResults([]); }
      finally { setSearching(false); }
    }, 300);
    return () => clearTimeout(timer);
  }, [keyword]);

  return (
    <div className={styles.searchWrapper}>
      <input
        className={styles.searchInput}
        placeholder="약 이름 검색..."
        value={keyword}
        onChange={(e) => setKeyword(e.target.value)}
      />
      {searching && <p className={styles.searchHint}>검색 중...</p>}
      {results.length > 0 && (
        <ul className={styles.searchResults}>
          {results.map((m) => (
            <li key={m.medicineId}>
              <button
                className={styles.searchResultItem}
                onClick={() => { onSelect(m); setKeyword(m.name); setResults([]); }}
                type="button"
              >
                <span className={styles.medicineName}>{m.name}</span>
                <span className={styles.medicineCategory}>{m.category}</span>
              </button>
            </li>
          ))}
        </ul>
      )}
      {!searching && keyword.trim() && results.length === 0 && (
        <p className={styles.searchHint}>검색 결과가 없습니다.</p>
      )}
    </div>
  );
}

function AddRecordForm({ onClose, onSaved }) {
  const [selectedMedicine, setSelectedMedicine] = useState(null);
  const [intakeTime, setIntakeTime] = useState(today());
  const [memo, setMemo] = useState('');
  const [isEmptyStomach, setIsEmptyStomach] = useState(false);
  const [alertNeeded, setAlertNeeded] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  const [scanning, setScanning] = useState(false);
  const [scanPreview, setScanPreview] = useState(null);
  const [scanKeyword, setScanKeyword] = useState('');
  const [scanInfo, setScanInfo] = useState(null);
  const fileInputRef = useRef(null);

  const handleScanFile = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    setScanPreview(URL.createObjectURL(file));
    setScanning(true);
    setError('');
    setScanInfo(null);

    const formData = new FormData();
    formData.append('image', file);

    try {
      const { data } = await scanMedicineImage(formData);
      const result = data.body;
      if (result.medicineName) {
        setScanKeyword(result.medicineName);
        setScanInfo(result);
        if (result.memo) setMemo(result.memo);
      } else {
        setError('약 정보를 인식하지 못했습니다. 사진을 다시 찍어보세요.');
      }
    } catch {
      setError('이미지 분석에 실패했습니다. 다시 시도해주세요.');
    } finally {
      setScanning(false);
      e.target.value = '';
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!selectedMedicine) { setError('약을 선택해주세요.'); return; }
    if (!intakeTime) { setError('복용 시간을 입력해주세요.'); return; }
    setSubmitting(true);
    try {
      await createIntakeRecord({
        medicineId: selectedMedicine.medicineId,
        intakeTime: new Date(intakeTime).toISOString(),
        memo: memo || null,
        isEmptyStomach,
        alertNeeded,
      });
      onSaved();
    } catch {
      setError('기록 저장에 실패했습니다. 다시 시도해주세요.');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className={styles.formOverlay}>
      <div className={styles.formSheet}>
        <div className={styles.formHeader}>
          <h2 className={styles.formTitle}>복약 기록 추가</h2>
          <button className={styles.closeBtn} onClick={onClose} type="button">✕</button>
        </div>

        <form onSubmit={handleSubmit} className={styles.form}>
          {/* 카메라 스캔 버튼 */}
          <div className={styles.scanSection}>
            <input
              ref={fileInputRef}
              type="file"
              accept="image/*"
              capture="environment"
              className={styles.hiddenInput}
              onChange={handleScanFile}
            />
            <button
              type="button"
              className={styles.scanBtn}
              onClick={() => fileInputRef.current?.click()}
              disabled={scanning}
            >
              {scanning ? (
                <>
                  <span className={styles.scanSpinner} />
                  AI 분석 중...
                </>
              ) : (
                <>
                  <span className={styles.scanIcon}>📷</span>
                  약 봉투 사진으로 자동 입력
                </>
              )}
            </button>

            {scanPreview && (
              <div className={styles.scanPreviewWrap}>
                <img src={scanPreview} alt="스캔한 약 사진" className={styles.scanPreview} />
                {scanning && (
                  <div className={styles.scanOverlay}>
                    <span className={styles.scanOverlayText}>AI 분석 중...</span>
                  </div>
                )}
              </div>
            )}

            {scanInfo && !scanning && (
              <div className={styles.scanResult}>
                <span className={styles.scanResultIcon}>✅</span>
                <div>
                  <p className={styles.scanResultTitle}>AI 인식 완료</p>
                  {scanInfo.dosage && (
                    <p className={styles.scanResultSub}>{scanInfo.dosage}</p>
                  )}
                  {!scanInfo.matched && (
                    <p className={styles.scanResultHint}>DB에서 약을 찾지 못했습니다. 아래에서 직접 선택해주세요.</p>
                  )}
                </div>
              </div>
            )}
          </div>

          <div className={styles.field}>
            <label className={styles.label}>약 선택 *</label>
            <MedicineSearchInput onSelect={setSelectedMedicine} scanKeyword={scanKeyword} />
            {selectedMedicine && (
              <div className={styles.selectedMedicine}>
                <span className={styles.selectedIcon}>💊</span>
                <div>
                  <p className={styles.selectedName}>{selectedMedicine.name}</p>
                  <p className={styles.selectedCategory}>{selectedMedicine.category}</p>
                </div>
              </div>
            )}
          </div>

          <div className={styles.field}>
            <label className={styles.label}>복용 시간 *</label>
            <input
              type="datetime-local"
              className={styles.input}
              value={intakeTime}
              onChange={(e) => setIntakeTime(e.target.value)}
            />
          </div>

          <div className={styles.field}>
            <label className={styles.label}>메모 (선택)</label>
            <textarea
              className={styles.textarea}
              placeholder="복용 관련 메모를 입력하세요"
              value={memo}
              onChange={(e) => setMemo(e.target.value)}
              rows={2}
            />
          </div>

          <div className={styles.toggleRow}>
            <span className={styles.toggleLabel}>공복 복용</span>
            <button
              type="button"
              className={`${styles.toggle} ${isEmptyStomach ? styles.toggleOn : ''}`}
              onClick={() => setIsEmptyStomach((v) => !v)}
            >
              <span className={styles.toggleThumb} />
            </button>
          </div>
          <div className={styles.toggleRow}>
            <span className={styles.toggleLabel}>다음 복용 알림</span>
            <button
              type="button"
              className={`${styles.toggle} ${alertNeeded ? styles.toggleOn : ''}`}
              onClick={() => setAlertNeeded((v) => !v)}
            >
              <span className={styles.toggleThumb} />
            </button>
          </div>

          {error && <p className={styles.errorText}>{error}</p>}

          <button type="submit" className={styles.saveBtn} disabled={submitting}>
            {submitting ? '저장 중...' : '기록 저장'}
          </button>
        </form>
      </div>
    </div>
  );
}

function RecordCard({ record }) {
  return (
    <div className={styles.recordCard}>
      <div className={styles.recordLeft}>
        <span className={styles.recordTime}>{formatTime(record.intakeTime)}</span>
        {record.isEmptyStomach && <span className={styles.tag}>공복</span>}
      </div>
      <div className={styles.recordRight}>
        <p className={styles.recordMedicine}>💊 {record.medicineName}</p>
        {record.memo && <p className={styles.recordMemo}>{record.memo}</p>}
        {record.alertNeeded && <p className={styles.recordAlert}>🔔 알림 설정됨</p>}
      </div>
    </div>
  );
}

export default function IntakeRecordPage() {
  const [records, setRecords] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);

  const loadRecords = useCallback(() => {
    setLoading(true);
    getIntakeRecords()
      .then(({ data }) => setRecords(data.body ?? []))
      .catch(() => {})
      .finally(() => setLoading(false));
  }, []);

  // eslint-disable-next-line react-hooks/set-state-in-effect
  useEffect(() => { loadRecords(); }, [loadRecords]);

  const handleSaved = () => {
    setShowForm(false);
    loadRecords();
  };

  const grouped = groupByDate(records);

  return (
    <div className={styles.page}>
      <header className={styles.header}>
        <h1 className={styles.title}>복약 기록</h1>
        <p className={styles.sub}>복용한 약을 기록하세요</p>
      </header>

      {!loading && records.length > 0 && <WeeklyChart records={records} />}

      {loading ? (
        <p className={styles.center}>불러오는 중...</p>
      ) : records.length === 0 ? (
        <div className={styles.emptyBox}>
          <span className={styles.emptyIcon}>💊</span>
          <p className={styles.emptyText}>아직 복약 기록이 없어요.</p>
          <p className={styles.emptySub}>아래 버튼으로 첫 기록을 추가해보세요!</p>
        </div>
      ) : (
        <div className={styles.list}>
          {grouped.map(([date, items]) => (
            <div key={date} className={styles.group}>
              <p className={styles.groupDate}>{date}</p>
              {items.map((r) => (
                <RecordCard key={r.intakeRecordId} record={r} />
              ))}
            </div>
          ))}
        </div>
      )}

      <button className={styles.fab} onClick={() => setShowForm(true)} aria-label="복약 기록 추가">
        +
      </button>

      {showForm && (
        <AddRecordForm
          onClose={() => setShowForm(false)}
          onSaved={handleSaved}
        />
      )}
    </div>
  );
}