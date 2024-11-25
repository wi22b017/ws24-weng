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
        async fetchFlights() {
            try {
                this.isLoading = true;
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
            } finally {
                this.isLoading = false;
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
