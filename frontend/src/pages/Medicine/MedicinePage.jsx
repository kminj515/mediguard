import { useState, useEffect } from 'react';
import { searchMedicines, getAllMedicines } from '../../shared/api/medicine';
import styles from './MedicinePage.module.css';

function MedicineDetailSheet({ medicine, onClose }) {
  return (
    <div className={styles.overlay}>
      <div className={styles.sheet}>
        <div className={styles.sheetHeader}>
          <h2 className={styles.sheetTitle}>{medicine.name}</h2>
          <button className={styles.closeBtn} onClick={onClose}>✕</button>
        </div>
        <div className={styles.detailBody}>
          <div className={styles.detailRow}>
            <span className={styles.detailLabel}>분류</span>
            <span className={styles.detailValue}>{medicine.category ?? '-'}</span>
          </div>
          <div className={styles.detailRow}>
            <span className={styles.detailLabel}>효능</span>
            <span className={styles.detailValue}>{medicine.efficacy ?? '-'}</span>
          </div>
          <div className={styles.detailRow}>
            <span className={styles.detailLabel}>주의사항</span>
            <span className={styles.detailValue}>{medicine.precautions ?? '-'}</span>
          </div>
          <div className={styles.detailRow}>
            <span className={styles.detailLabel}>부작용</span>
            <span className={styles.detailValue}>{medicine.sideEffects ?? '-'}</span>
          </div>
          <div className={styles.detailRow}>
            <span className={styles.detailLabel}>주요 성분</span>
            <span className={styles.detailValue}>{medicine.activeIngredient ?? '-'}</span>
          </div>
          <div className={styles.tagRow}>
            <span className={`${styles.tag} ${medicine.emptyStomachSafe ? styles.tagGreen : styles.tagRed}`}>
              {medicine.emptyStomachSafe ? '공복 복용 가능' : '공복 복용 주의'}
            </span>
            {medicine.drowsiness && (
              <span className={`${styles.tag} ${styles.tagOrange}`}>졸음 유발</span>
            )}
          </div>
        </div>
      </div>
    </div>
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
          <p>검색 결과가 없습니다.</p>
        </div>
      ) : (
        <>
          <p className={styles.count}>총 {medicines.length}개</p>
          <div className={styles.list}>
            {medicines.map((m) => (
              <button
                key={m.medicineId}
                className={styles.card}
                onClick={() => setSelected(m)}
              >
                <div className={styles.cardMain}>
                  <span className={styles.medicineName}>{m.name}</span>
                  <span className={styles.medicineCategory}>{m.category}</span>
                </div>
                <div className={styles.cardTags}>
                  {m.emptyStomachSafe && (
                    <span className={`${styles.tag} ${styles.tagGreen}`}>공복 가능</span>
                  )}
                  {m.drowsiness && (
                    <span className={`${styles.tag} ${styles.tagOrange}`}>졸음</span>
                  )}
                </div>
                <span className={styles.cardArrow}>›</span>
              </button>
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