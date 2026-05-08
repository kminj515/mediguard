import instance from './instance';

export const getDiagnosisQuestions = () => instance.get('/api/v1/diagnoses/questions');
export const submitDiagnosis = (answers) => instance.post('/api/v1/diagnoses/submit', { answers });