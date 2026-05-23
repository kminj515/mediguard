import { useState, useEffect } from 'react';
import { searchMedicines, getAllMedicines } from '../../shared/api/medicine';
import styles from './MedicinePage.module.css';

function MedicineDetailSheet({ medicine, onClose }) {
  return (
    <div className={styles.overlay} onClick={onClose}>
      <div className={styles.sheet} onClick={(e) => e.stopPropagation()}>
        <div className={styles.sheetHandle} />

        <div className={styles.sheetTop}>
          <div className={styles.sheetIconWrap}>
            <span>💊</span>
          </div>
          <div className={styles.sheetTopInfo}>
            <p className={styles.sheetCategory}>{medicine.category ?? '일반약'}</p>
            <h2 className={styles.sheetName}>{medicine.name}</h2>
          </div>
          <button className={styles.closeBtn} onClick={onClose}>✕</button>
        </div>

        <div className={styles.tagRow}>
          <span className={`${styles.tag} ${medicine.emptyStomachSafe ? styles.tagGreen : styles.tagRed}`}>
            {medicine.emptyStomachSafe ? '✓ 공복 복용 가능' : '✗ 공복 복용 주의'}
          </span>
          {medicine.drowsiness && (
            <span className={`${styles.tag} ${styles.tagOrange}`}>⚠ 졸음 유발</span>
          )}
        </div>

        <div className={styles.detailBody}>
          {[
            { label: '효능', value: medicine.efficacy },
            { label: '주의사항', value: medicine.precautions },
            { label: '부작용', value: medicine.sideEffects },
            { label: '주요 성분', value: medicine.activeIngredient },
          ].map(({ label, value }) =>
            value ? (
              <div key={label} className={styles.detailRow}>
                <span className={styles.detailLabel}>{label}</span>
                <p className={styles.detailValue}>{value}</p>
              </div>
            ) : null
          )}
        </div>
      </div>
    </div>
  );
}

function MedicineCard({ medicine, onClick }) {
  return (
    <button className={styles.card} onClick={onClick}>
      <div className={styles.cardIcon}>💊</div>
      <div className={styles.cardMain}>
        <span className={styles.medicineName}>{medicine.name}</span>
        <span className={styles.medicineCategory}>{medicine.category}</span>
        <div className={styles.cardTags}>
          {medicine.emptyStomachSafe && (
            <span className={`${styles.tagSmall} ${styles.tagGreen}`}>공복 가능</span>
          )}
          {medicine.drowsiness && (
            <span className={`${styles.tagSmall} ${styles.tagOrange}`}>졸음</span>
          )}
        </div>
      </div>
      <span className={styles.cardArrow}>›</span>
    </button>
  );
}

export default function MedicinePage() {
  const [keyword, setKeyword] = useState('');
  const [medicines, setMedicines] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selected, setSelected] = useState(null);

  useEffect(() => {
    getAllMedicines()
      .then(({ data }) => setMedicines(data.body ?? []))
      .catch(() => {})
      .finally(() => setLoading(false));
  }, []);

  useEffect(() => {
    if (!keyword.trim()) {
      getAllMedicines().then(({ data }) => setMedicines(data.body ?? [])).catch(() => {});
      return;
    }
    const t = setTimeout(() => {
      searchMedicines(keyword)
        .then(({ data }) => setMedicines(data.body ?? []))
        .catch(() => {});
    }, 300);
    return () => clearTimeout(t);
  }, [keyword]);

  return (
    <div className={styles.page}>
      <header className={styles.header}>
        <h1 className={styles.title}>약 검색</h1>
        <p className={styles.sub}>약 이름으로 복용 정보를 확인하세요</p>
      </header>

      <div className={styles.searchBox}>
        <span className={styles.searchIcon}>🔍</span>
        <input
          className={styles.searchInput}
          placeholder="약 이름을 검색하세요"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
        />
        {keyword && (
          <button className={styles.clearBtn} onClick={() => setKeyword('')}>✕</button>
        )}
      </div>

      {loading ? (
        <p className={styles.center}>불러오는 중...</p>
      ) : medicines.length === 0 ? (
        <div className={styles.emptyBox}>
          <span className={styles.emptyIcon}>🔍</span>
          <p className={styles.emptyText}>검색 결과가 없습니다</p>
          <p className={styles.emptySub}>다른 키워드로 검색해보세요</p>
        </div>
      ) : (
        <>
          <p className={styles.count}>총 {medicines.length}개</p>
          <div className={styles.list}>
            {medicines.map((m) => (
              <MedicineCard key={m.medicineId} medicine={m} onClick={() => setSelected(m)} />
            ))}
          </div>
        </>
      )}

      {selected && (
        <MedicineDetailSheet medicine={selected} onClose={() => setSelected(null)} />
      )}
    </div>
  );
}