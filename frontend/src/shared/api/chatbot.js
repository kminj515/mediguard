import instance from './instance';

export const sendMessage = (prompt) =>
  instance.post('/api/v1/chat/chat', { prompt });

export const recommendWithPharmacy = (data) =>
  instance.post('/api/v1/chat/recommend-with-pharmacy', data);