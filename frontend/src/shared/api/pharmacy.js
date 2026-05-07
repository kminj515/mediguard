import instance from './instance';

export const getNearbyPharmacies = (latitude, longitude, radius = 5000) =>
  instance.post('/api/v1/pharmacies/search', { latitude, longitude, radius });