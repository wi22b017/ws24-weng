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
        async updateFlight(flightId, payload) {
            try {
                const response = await apiClient.put(`/flights/${flightId}`, payload);
                if (response.status >= 200 && response.status < 300) {
                    // Update the local state with the updated flight
                    const index = this.flights.findIndex((flight) => flight.id === flightId);
                    if (index !== -1) {
                        this.flights[index] = response.data;
                    }
                    return {
                        success: true,
                        message: "Flight updated successfully",
                    };
                }
            } catch (error) {
                console.error("Error updating flight:", error.response?.data || error.message);
                return {
                    success: false,
                    message: error.response?.data?.error || "Failed to update flight",
                };
            }
        },

        async deleteFlight(flightId) {
            try {
                await apiClient.delete(`/flights/${flightId}`);
                this.flights = this.flights.filter((flight) => flight.id !== flightId);
                return {
                    success: true,
                    message: "Flight deleted successfully",
                };
            } catch (error) {
                console.error("Error deleting flight:", error.response?.data || error.message);
                return {
                    success: false,
                    message: error.response?.data?.error || "Failed to delete flight",
                };
            }
        },




        async addFlight(payload) {
            try {
                const response = await apiClient.post("/flights", payload);

                if (response.status >= 200 && response.status < 300) {
                    console.log("Flight added successfully:", response.data);
                    this.flights.push(response.data); // Add the new flight to the local state
                    return {
                        success: true,
                        message: "Flight added successfully",
                    };
                }
            } catch (error) {
                console.error("Error adding flight:", error.response?.data || error.message);
                return {
                    success: false,
                    message: error.response?.data?.error || "Failed to add flight",
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
