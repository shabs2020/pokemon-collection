<script setup>
import { ref } from 'vue';
import { useAuthStore } from '@/stores/auth';

const username = ref('');
const password = ref('');
const error = ref('');
const auth = useAuthStore();

const handleLogin = async () => {
    try {
        await auth.loginUser(username.value, password.value);
    } catch (e) {
        error.value = 'Invalid credentials';
    }
};
</script>

<template>
    <div class="login">
        <h1>Login</h1>
        <form @submit.prevent="handleLogin">
            <input v-model="username" placeholder="Username" required />
            <input v-model="password" type="password" placeholder="Password" required />
            <button type="submit">Login</button>
        </form>
        <p v-if="error" class="error">{{ error }}</p>
    </div>
</template>

<style scoped>
.login {
    max-width: 400px;
    margin: 0 auto;
    padding: 20px;
}
.error {
    color: red;
}
</style>