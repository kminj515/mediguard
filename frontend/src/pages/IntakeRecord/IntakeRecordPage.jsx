import { useState, useEffect, useCallback } from 'react';
import { getIntakeRecords, createIntakeRecord } from '../../shared/api/intakeRecord';
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

function MedicineSearchInput({ onSelect }) {
  const [keyword, setKeyword] = useState('');
  const [results, setResults] = useState([]);
  const [searching, setSearching] = useState(false);

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
          <div className={styles.field}>
            <label className={styles.label}>약 선택 *</label>
            <MedicineSearchInput onSelect={setSelectedMedicine} />
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