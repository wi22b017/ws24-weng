import { defineStore } from "pinia";
import apiClient from "@/utils/axiosClient";

export const useAdminUserStore = defineStore("adminUserStore", {
    state: () => ({
        users: [],
        flights: [],
        isLoading: false,
    }),
    actions: {
        async fetchUsers() {
            try {
                this.isLoading = true;
                const response = await apiClient.get("/users");
                this.users = response.data;
            } catch (error) {
                console.error("Error fetching users:", error);
                throw error;
            } finally {
                this.isLoading = false;
            }
        },
        async updateUserStatus(userId, newStatus) {
            await apiClient.patch(`/users/${userId}`, { status: newStatus }); // Send PATCH request
            const user = this.users.find((u) => u.id === userId); // Find the user in the store
            if (user) {
                user.status = newStatus; // Update the user's status locally
            }
        }
        ,
        async deleteUser(userId) {
            try {
                await apiClient.delete(`/users/${userId}`);
                this.users = this.users.filter((user) => user.id !== userId);
            } catch (error) {
                console.error("Error deleting user:", error);
                throw error;
            }
        },
        async fetchFlights() {
            try {
                const response = await apiClient.get("/flights");
                this.flights = response.data;

                return {
                    success: true,
                    message: 'Fetch Flights Successful'
                };
            } catch (error) {
                return {
                    success: false,
                    message: error.response.data.error
                };
            }
        },
    },
    persist: {
        enabled: true,
        strategies: [
            {
                key: 'admin',
                storage: localStorage,
            },
        ],
    },
});
