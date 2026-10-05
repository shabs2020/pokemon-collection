<script setup>
import { ref, onMounted } from 'vue';
import { listPokemon, getPokemon } from '@/api/pokeapi';
import { useCollectionStore } from '@/stores/collection';
import { useToastStore } from '@/stores/toast';

const pokemonList = ref([]);
const offset = ref(0);
const limit = ref(20);
const loading = ref(false);
const collection = useCollectionStore();
const toast = useToastStore();

const fetchPokemon = async () => {
    loading.value = true;
    const response = await listPokemon(offset.value, limit.value);
    const results = response.data.results;
    const ids = results.map(p => parseInt(p.url.split('/').slice(-2, -1)[0]));
    const details = await Promise.all(
        ids.map(id => getPokemon(id).catch(() => ({ data: { id, name: 'Unknown', sprites: {} } })))
    );
    const detailMap = details.reduce((map, res) => {
        map[res.data.id] = res.data;
        return map;
    }, {});
    pokemonList.value = results.map((p, i) => ({
        ...p,
        id: ids[i],
        sprite: detailMap[ids[i]]?.sprites?.front_default || ''
    }));
    loading.value = false;
};

const loadMore = () => {
    offset.value += limit.value;
    fetchPokemon();
};

const addToCollection = async (pokemon) => {
    const id = parseInt(pokemon.url.split('/').slice(-2, -1)[0]);
    try {
        await collection.addItem(id);
        toast.success(`${pokemon.name} added to collection`);
    } catch (err) {
        if (err.response?.status === 409) {
            toast.error(err.response.data?.detail || `${pokemon.name} is already in your collection`);
        } else {
            toast.error(err.response?.data?.detail || 'Failed to add Pokémon to collection');
        }
    }
};

onMounted(fetchPokemon);
</script>

<template>
    <div class="browse">
        <h1>Browse Pokémon</h1>
        <div v-if="loading">Loading...</div>
        <div v-else>
            <div class="pokemon-grid">
                <div v-for="pokemon in pokemonList" :key="pokemon.url" class="pokemon-card">
                    <img v-if="pokemon.sprite" :src="pokemon.sprite" :alt="pokemon.name" />
                    <h3>{{ pokemon.name }}</h3>
                    <button @click="addToCollection(pokemon)">Add to Collection</button>
                </div>
            </div>
            <button @click="loadMore">Load More</button>
        </div>
    </div>
</template>

<style scoped>
.pokemon-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 20px;
}
.pokemon-card {
    border: 1px solid #ccc;
    padding: 10px;
    text-align: center;
}
</style>