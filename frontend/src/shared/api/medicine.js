import instance from './instance';

export const getAllMedicines = () => instance.get('/api/v1/medicines');

export const searchMedicines = (keyword) =>
  instance.get('/api/v1/medicines/search', { params: { keyword } });

export const getMedicine = (id) => instance.get(`/api/v1/medicines/${id}`);