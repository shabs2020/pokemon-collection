import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

const routes = [
    { path: '/login', component: () => import('@/views/LoginView.vue') },
    { path: '/browse', component: () => import('@/views/BrowseView.vue'), meta: { requiresAuth: true } },
    { path: '/collection', component: () => import('@/views/CollectionView.vue'), meta: { requiresAuth: true } },
    { path: '/', redirect: '/browse' }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

router.beforeEach(async (to) => {
    const auth = useAuthStore();
    if (to.meta.requiresAuth && !await auth.checkAuth()) {
        return '/login';
    }
});

export default router;