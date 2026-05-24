import instance from './instance';

export const getIntakeRecords = () => instance.get('/api/v1/intake-records');

export const createIntakeRecord = (data) => instance.post('/api/v1/intake-records', data);

export const getIntakeRecord = (id) => instance.get(`/api/v1/intake-records/${id}`);

export const scanMedicineImage = (formData) =>
  instance.post('/api/v1/intake-records/scan', formData);