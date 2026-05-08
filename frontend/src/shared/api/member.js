import instance from './instance';

export const getProfile = () => instance.get('/api/v1/member/profile');
export const getGrade = () => instance.get('/api/v1/member/me/grade');
export const updateProfile = (data) => instance.put('/api/v1/member/profile', data);