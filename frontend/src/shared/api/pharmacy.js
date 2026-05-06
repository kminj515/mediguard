import instance from './instance';

export const getNearbyPharmacies = (latitude, longitude, radius = 5000) =>
  instance.get('/api/v1/pharmacies/nearby', {
    params: { pharmacyName: '약국', latitude, longitude, radius },
  });