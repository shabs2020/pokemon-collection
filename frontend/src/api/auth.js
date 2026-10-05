import client from './client';

export const login = (username, password) => {
    return client.post('/auth/login', { username, password });
};

export const me = () => {
    return client.get('/auth/me');
};