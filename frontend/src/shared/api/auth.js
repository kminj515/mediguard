import instance from './instance';

export const login = (email, password) =>
  instance.post('/api/v1/auth/login', { email, password });

export const signup = (email, password, confirmPassword, nickname) =>
  instance.post('/api/v1/auth/signup', { email, password, confirmPassword, nickname });