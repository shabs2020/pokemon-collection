import axios from 'axios';

const pokeApi = axios.create({
    baseURL: 'https://pokeapi.co/api/v2'
});

export const listPokemon = (offset = 0, limit = 20) => {
    return pokeApi.get('/pokemon', { params: { offset, limit } });
};

export const getPokemon = (id) => {
    return pokeApi.get(`/pokemon/${id}`);
};