import instance from './instance';

export const getCategories = () => instance.get('/api/v1/quiz/categories');
export const getProgress = () => instance.get('/api/v1/quiz/progress');
export const getQuizzesByCategory = (categoryId) => instance.get(`/api/v1/quiz/${categoryId}/list`);
export const getQuizDetail = (quizId) => instance.get(`/api/v1/quiz/${quizId}`);
export const submitAnswer = (quizId, selectedOptionId) => instance.post(`/api/v1/quiz/${quizId}/submit`, { selectedOptionId });
export const getQuizHistory = () => instance.get('/api/v1/quiz/history');
export const getLevels = () => instance.get('/api/v1/quiz/level');