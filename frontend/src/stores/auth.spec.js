import { describe, it, expect, beforeEach, vi } from 'vitest';
import { setActivePinia, createPinia } from 'pinia';
import { useAuthStore } from './auth';
import { login, me } from '@/api/auth';

vi.mock('@/api/auth');
vi.mock('vue-router', () => ({
    useRouter: () => ({ push: vi.fn() })
}));

describe('authStore', () => {
    beforeEach(() => {
        setActivePinia(createPinia());
        localStorage.clear();
    });

    it('loginUser sets token and username', async () => {
        login.mockResolvedValue({ data: { token: 'test-token', username: 'ash' } });
        const auth = useAuthStore();
        await auth.loginUser('ash', 'pikachu123');
        expect(auth.token).toBe('test-token');
        expect(auth.username).toBe('ash');
        expect(localStorage.getItem('token')).toBe('test-token');
    });

    it('logout clears token and username', () => {
        const auth = useAuthStore();
        auth.token = 'test-token';
        auth.username = 'ash';
        auth.logout();
        expect(auth.token).toBe('');
        expect(auth.username).toBe('');
        expect(localStorage.getItem('token')).toBeNull();
    });

    it('checkAuth returns false for invalid token', async () => {
        me.mockRejectedValue(new Error('Unauthorized'));
        const auth = useAuthStore();
        auth.token = 'invalid-token';
        const result = await auth.checkAuth();
        expect(result).toBe(false);
    });
});