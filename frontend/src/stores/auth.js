import { defineStore } from 'pinia';
import { ref } from 'vue';
import { login, me } from '@/api/auth';
import router from '@/router';

export const useAuthStore = defineStore('auth', () => {
    const token = ref(localStorage.getItem('token') || '');
    const username = ref(localStorage.getItem('username') || '');

    const isAuthenticated = () => !!token.value;

    const loginUser = async (usernameInput, password) => {
        const response = await login(usernameInput, password);
        token.value = response.data.token;
        username.value = response.data.username;
        localStorage.setItem('token', token.value);
        localStorage.setItem('username', username.value);
        await router.push('/browse');
    };

    const logout = () => {
        token.value = '';
        username.value = '';
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        router.push('/login');
    };

    const checkAuth = async () => {
        if (!token.value) return false;
        try {
            await me();
            return true;
        } catch {
            logout();
            return false;
        }
    };

    return { token, username, isAuthenticated, loginUser, logout, checkAuth };
});