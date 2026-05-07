import instance from './instance';

export const getAllMedicines = () => instance.get('/api/medications');

export const searchMedicines = (keyword) =>
  instance.get('/api/medications/search', { params: { name: keyword } });

export const getMedicine = (id) => instance.get(`/api/medications/${id}`);