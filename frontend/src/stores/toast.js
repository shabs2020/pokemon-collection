import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useToastStore = defineStore('toast', () => {
    const toasts = ref([]);
    let nextId = 0;

    const push = (message, type = 'info', timeout = 4000) => {
        const id = ++nextId;
        toasts.value.push({ id, message, type });
        if (timeout > 0) {
            setTimeout(() => dismiss(id), timeout);
        }
        return id;
    };

    const dismiss = (id) => {
        const idx = toasts.value.findIndex(t => t.id === id);
        if (idx !== -1) toasts.value.splice(idx, 1);
    };

    const success = (message, timeout) => push(message, 'success', timeout);
    const error = (message, timeout) => push(message, 'error', timeout);
    const info = (message, timeout) => push(message, 'info', timeout);

    return { toasts, push, dismiss, success, error, info };
});
