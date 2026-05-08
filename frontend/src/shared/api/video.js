import instance from './instance';

export const getAllVideos = () => instance.get('/api/v1/videos/all');
export const getPopularVideos = () => instance.get('/api/v1/videos/popular');
export const getVideoCategories = () => instance.get('/api/v1/videos/categories');
export const incrementViewCount = (videoId) => instance.post(`/api/v1/videos/${videoId}/view`);