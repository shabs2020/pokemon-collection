import { defineStore } from 'pinia';
import { ref } from 'vue';
import { list, add, remove } from '@/api/collection';
import { getPokemon } from '@/api/pokeapi';

export const useCollectionStore = defineStore('collection', () => {
    const items = ref([]);

    const fetch = async () => {
        const response = await list();
        items.value = response.data;
    };

    const addItem = async (pokemonId) => {
        const response = await add(pokemonId);
        items.value.push(response.data);
    };

    const removeItem = async (itemId) => {
        await remove(itemId);
        items.value = items.value.filter(item => item.id !== itemId);
    };

    const enrichItems = async (items) => {
        const ids = items.map(item => item.pokemonId);
        const uniqueIds = [...new Set(ids)];
        const responses = await Promise.all(
            uniqueIds.map(id => getPokemon(id).catch(() => ({ data: { id, name: 'Unknown', sprites: {} } })))
        );
        const pokemonMap = responses.reduce((map, response) => {
            map[response.data.id] = response.data;
            return map;
        }, {});
        return items.map(item => ({
            ...item,
            pokemonData: pokemonMap[item.pokemonId]
        }));
    };

    const enrichItem = async (item) => {
        if (!item.pokemonData) {
            const response = await getPokemon(item.pokemonId);
            item.pokemonData = response.data;
        }
        return item;
    };

    return { items, fetch, addItem, removeItem, enrichItem, enrichItems };
});