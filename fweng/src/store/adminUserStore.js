import { defineStore } from "pinia";
import apiClient from "@/utils/axiosClient";

export const useAdminUserStore = defineStore("adminUserStore", {
    state: () => ({
        users: [],
        isLoading: false,
    }),
    actions: {
        async fetchUsers() {
            try {
                this.isLoading = true;
                console.log("Fetching users from API...");
                const response = await apiClient.get("/users");
                console.log("API Response:", response.data);
                this.users = response.data;
                console.log("Users stored in state:", this.users);
            } catch (error) {
                console.error("Error fetching users:", error);
                throw error;
            } finally {
                this.isLoading = false;
                console.log("Finished fetching users. Loading state:", this.isLoading);
            }
        },
        async updateUserStatus(userId, newStatus) {
            console.log(`Updating user status: User ID: ${userId}, New Status: ${newStatus}`);
            await apiClient.patch(`/users/${userId}`, { status: newStatus }); // Send PATCH request
            const user = this.users.find((u) => u.id === userId); // Find the user in the store
            if (user) {
                user.status = newStatus; // Update the user's status locally
                console.log(`User status updated locally: ${JSON.stringify(user)}`);
            }
        }
        ,
        async deleteUser(userId) {
            try {
                console.log(`Deleting user: User ID: ${userId}`);
                await apiClient.delete(`/users/${userId}`);
                this.users = this.users.filter((user) => user.id !== userId);
                console.log("User deleted. Remaining users in state:", this.users);
            } catch (error) {
                console.error("Error deleting user:", error);
                throw error;
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
