import { useState, useEffect } from 'react';
import { getProducts, exchangeProduct, getExchangeHistory } from '../../shared/api/shop';
import styles from './ShopPage.module.css';

const TABS = [
  { key: 'shop',    label: '상품' },
  { key: 'history', label: '교환 내역' },
];

function ExchangeResultSheet({ result, onClose }) {
  return (
    <div className={styles.overlay}>
      <div className={styles.sheet}>
        <div className={styles.sheetHeader}>
          <h2 className={styles.sheetTitle}>교환 완료!</h2>
          <button className={styles.closeBtn} onClick={onClose}>✕</button>
        </div>
        <div className={styles.resultBody}>
          {result.thumbnail && (
            <img src={result.thumbnail} alt={result.name} className={styles.resultThumb} />
          )}
          <p className={styles.resultBrand}>{result.brand}</p>
          <p className={styles.resultName}>{result.name}</p>
          <div className={styles.couponBox}>
            <p className={styles.couponLabel}>쿠폰 코드</p>
            <p className={styles.couponCode}>{result.couponCode}</p>
          </div>
          {result.validUntil && (
            <p className={styles.validUntil}>유효기간 · {result.validUntil}</p>
          )}
        </div>
      </div>
    </div>
  );
}

function ProductCard({ product, onExchange }) {
  return (
    <div className={styles.productCard}>
      <div className={styles.productThumb}>
        {product.thumbnail
          ? <img src={product.thumbnail} alt={product.name} className={styles.productImg} />
          : <span className={styles.productEmoji}>🎁</span>
        }
      </div>
      <div className={styles.productInfo}>
        <p className={styles.productBrand}>{product.brand}</p>
        <p className={styles.productName}>{product.name}</p>
        <p className={styles.productPoint}>{product.pricePoint?.toLocaleString()}P</p>
      </div>
      <button className={styles.exchangeBtn} onClick={() => onExchange(product)}>
        교환
      </button>
    </div>
  );
}

export default function ShopPage() {
  const [tab, setTab] = useState('shop');
  const [products, setProducts] = useState([]);
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [exchangeResult, setExchangeResult] = useState(null);
  const [exchanging, setExchanging] = useState(false);

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    setLoading(true);
    if (tab === 'shop') {
      getProducts()
        .then(({ data }) => setProducts(data.body?.content ?? []))
        .catch(() => {})
        .finally(() => setLoading(false));
    } else {
      getExchangeHistory()
        .then(({ data }) => setHistory(data.body ?? []))
        .catch(() => {})
        .finally(() => setLoading(false));
    }
  }, [tab]);

  const handleExchange = async (product) => {
    if (!window.confirm(`${product.name}을(를) ${product.pricePoint}P로 교환하시겠어요?`)) return;
    setExchanging(true);
    try {
      const { data } = await exchangeProduct(product.id);
      setExchangeResult(data.body);
    } catch (err) {
      alert(err.response?.data?.status?.message || '교환에 실패했습니다. 포인트를 확인해주세요.');
    } finally {
      setExchanging(false);
    }
  };

  return (
    <div className={styles.page}>
      <header className={styles.header}>
        <h1 className={styles.title}>리워드 샵</h1>
      </header>

      <div className={styles.tabs}>
        {TABS.map((t) => (
          <button
            key={t.key}
            className={`${styles.tab} ${tab === t.key ? styles.tabActive : ''}`}
            onClick={() => setTab(t.key)}
          >
            {t.label}
          </button>
        ))}
      </div>

      {loading ? (
        <p className={styles.center}>불러오는 중...</p>
      ) : tab === 'shop' ? (
        products.length === 0 ? (
          <div className={styles.emptyBox}>
            <span>🎁</span><p>상품이 없습니다.</p>
          </div>
        ) : (
          <div className={styles.productList}>
            {products.map((p) => (
              <ProductCard key={p.id} product={p} onExchange={handleExchange} />
            ))}
          </div>
        )
      ) : (
        history.length === 0 ? (
          <div className={styles.emptyBox}>
            <span>📋</span><p>교환 내역이 없습니다.</p>
          </div>
        ) : (
          <div className={styles.historyList}>
            {history.map((h) => (
              <div key={h.exchangeId} className={styles.historyCard}>
                <div className={styles.historyThumb}>
                  {h.thumbnail
                    ? <img src={h.thumbnail} alt={h.productName} className={styles.historyImg} />
                    : <span>🎁</span>
                  }
                </div>
                <div className={styles.historyInfo}>
                  <p className={styles.historyBrand}>{h.brandName}</p>
                  <p className={styles.historyName}>{h.productName}</p>
                  <p className={styles.historyPoint}>-{h.pointsUsed?.toLocaleString()}P</p>
                  <p className={styles.historyDate}>{h.exchangedAt}</p>
                </div>
                <span className={`${styles.statusBadge} ${h.status === 'COMPLETED' ? styles.statusDone : ''}`}>
                  {h.status === 'COMPLETED' ? '완료' : h.status}
                </span>
              </div>
            ))}
          </div>
        )
      )}

      {exchanging && (
        <div className={styles.loadingOverlay}>
          <p className={styles.loadingText}>교환 처리 중...</p>
        </div>
      )}

      {exchangeResult && (
        <ExchangeResultSheet
          result={exchangeResult}
          onClose={() => { setExchangeResult(null); setTab('history'); }}
        />
      )}
    </div>
  );
}