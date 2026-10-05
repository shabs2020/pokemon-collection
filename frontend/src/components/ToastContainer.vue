<script setup>
import { useToastStore } from '@/stores/toast';

const toast = useToastStore();
</script>

<template>
    <div class="toast-container">
        <div
            v-for="t in toast.toasts"
            :key="t.id"
            :class="['toast', `toast--${t.type}`]"
            role="alert"
        >
            <span class="toast__message">{{ t.message }}</span>
            <button class="toast__close" @click="toast.dismiss(t.id)" aria-label="Close">&times;</button>
        </div>
    </div>
</template>

<style scoped>
.toast-container {
    position: fixed;
    top: 20px;
    right: 20px;
    z-index: 1000;
    display: flex;
    flex-direction: column;
    gap: 10px;
    max-width: 360px;
}
.toast {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 12px;
    padding: 12px 16px;
    border-radius: 6px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
    color: #fff;
    font-size: 14px;
}
.toast--success { background: #2e7d32; }
.toast--error   { background: #c62828; }
.toast--info    { background: #1565c0; }
.toast__message { flex: 1; }
.toast__close {
    background: transparent;
    border: none;
    color: #fff;
    font-size: 18px;
    line-height: 1;
    cursor: pointer;
    padding: 0 4px;
}
.toast__close:hover { opacity: 0.8; }
</style>
