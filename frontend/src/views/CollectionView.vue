<script setup>
import { ref, onMounted } from 'vue';
import { useCollectionStore } from '@/stores/collection';

const collection = useCollectionStore();
const enrichedItems = ref([]);
const loading = ref(false);

    const fetchCollection = async () => {
        loading.value = true;
        await collection.fetch();
        enrichedItems.value = await collection.enrichItems(collection.items);
        loading.value = false;
    };

const removeFromCollection = async (itemId) => {
    await collection.removeItem(itemId);
    await fetchCollection();
};

onMounted(fetchCollection);
</script>

<template>
    <div class="collection">
        <h1>My Collection</h1>
        <div v-if="loading">Loading...</div>
        <div v-else>
            <div v-if="enrichedItems.length === 0">No items in collection.</div>
            <div v-else class="collection-grid">
                <div v-for="item in enrichedItems" :key="item.id" class="collection-card">
                    <img :src="item.pokemonData.sprites.front_default" :alt="item.pokemonData.name" />
                    <h3>{{ item.pokemonData.name }}</h3>
                    <p>Added: {{ new Date(item.addedAt).toLocaleString() }}</p>
                    <button @click="removeFromCollection(item.id)">Remove</button>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.collection-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 20px;
}
.collection-card {
    border: 1px solid #ccc;
    padding: 10px;
    text-align: center;
}
</style>