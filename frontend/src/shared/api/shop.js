import instance from './instance';

// 💡 기존 size: 10을 size: 50으로 수정해서 첫 페이지에 다 땡겨오기!
export const getProducts = (page = 0) =>
    instance.get('/api/v1/shop/products', { params: { page, size: 50 } });

export const exchangeProduct = (productId) =>
    instance.post(`/api/v1/shop/exchange/${productId}`);

export const getExchangeHistory = () => instance.get('/api/v1/shop/history');