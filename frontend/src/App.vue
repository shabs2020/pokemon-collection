<script setup>
import { useAuthStore } from '@/stores/auth';
import ToastContainer from '@/components/ToastContainer.vue';
import { RouterView } from 'vue-router';

const auth = useAuthStore();
</script>

<template>
  <header v-if="auth.isAuthenticated()">
    <nav>
      <RouterLink to="/browse">Browse</RouterLink>
      <RouterLink to="/collection">Collection</RouterLink>
      <button @click="auth.logout">Logout</button>
    </nav>
  </header>
  <RouterView />
  <ToastContainer />
</template>

<style scoped>
nav {
  display: flex;
  gap: 20px;
  padding: 20px;
  background: #f0f0f0;
}
button {
  margin-left: auto;
}
</style>