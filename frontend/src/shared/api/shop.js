import instance from './instance';

export const getProducts = (page = 0) =>
  instance.get('/api/v1/shop/products', { params: { page, size: 10 } });
export const exchangeProduct = (productId) =>
  instance.post(`/api/v1/shop/exchange/${productId}`);
export const getExchangeHistory = () => instance.get('/api/v1/shop/history');