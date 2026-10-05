import client from './client';

export const list = () => {
    return client.get('/collection');
};

export const add = (pokemonId) => {
    return client.post(`/collection/${pokemonId}`);
};

export const remove = (itemId) => {
    return client.delete(`/collection/${itemId}`);
};