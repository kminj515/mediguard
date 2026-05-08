import instance from './instance';

export const sendMessage = (prompt) =>
  instance.post('/api/v1/chat/chat', { prompt });