import { describe, it, expect, beforeEach, vi } from 'vitest';
import axios from 'axios';
import client from './client';

vi.mock('axios');

describe('axios client', () => {
    beforeEach(() => {
        localStorage.clear();
    });

    it('attaches token to requests', async () => {
        localStorage.setItem('token', 'test-token');
        axios.create.mockReturnThis();
        const mockRequest = vi.fn().mockResolvedValue({});
        axios.interceptors.request.use.mockImplementation(cb => {
            return mockRequest.mockImplementation(cb);
        });

        await client.get('/test');
        expect(mockRequest).toHaveBeenCalledWith(expect.objectContaining({
            headers: { Authorization: 'Bearer test-token' }
        }));
    });

    it('clears token on 401 response', async () => {
        const mockError = {
            response: { status: 401 },
            config: { url: '/api/test' }
        };
        axios.create.mockReturnThis();
        const mockUse = vi.fn().mockImplementation(cb => cb(mockError));
        axios.interceptors.response.use.mockImplementation((success, error) => {
            return mockUse(error);
        });

        await expect(client.get('/test')).rejects.toThrow();
        expect(localStorage.getItem('token')).toBeNull();
    });
});