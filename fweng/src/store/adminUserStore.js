import { defineStore } from "pinia";
import apiClient from "@/utils/axiosClient";

export const useAdminUserStore = defineStore("adminUserStore", {
    state: () => ({
        users: [],
        flights: [],
        isLoading: false,
        bookings: [],
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
        async fetchUserById(userId) {
            try {
                const response = await apiClient.get(`/users/${userId}`);
                return response.data; // Return the user data
            } catch (error) {
                console.error(`Error fetching user with ID ${userId}:`, error);
                throw new Error(
                    error.response?.data?.error || `An error occurred while fetching the user.`
                );
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
        async fetchBookings(){
            try {
                const response = await apiClient.get("/bookings");
                this.bookings = response.data;
                } catch(error) {
                return error.response.data.message;
                }
        },
        async deleteBooking(bookingId){
            try {
                await apiClient.delete(`/bookings/${bookingId}`);
                this.bookings = this.bookings.filter((booking) => booking.id !== bookingId);
                return {
                    success: true,
                    message: `Booking with ID ${bookingId} has been deleted successfully`,
                }
            }catch (error) {
                console.error('Error deleting booking:', error);
                return {
                    success: false,
                    message: error.response?.data?.error || 'An error occurred while deleting the booking',
                };
            }
        },
        async updateBookingStatus(bookingId, newStatus) {
            try {
                const response = await apiClient.patch(`/bookings/${bookingId}/status`, { status: newStatus });
                return {
                    success: true,
                    message: "Booking status updated successfully",
                    data: response.data,
                };
            } catch (error) {
                console.error("Error updating booking status:", error);
                return {
                    success: false,
                    message: error.response?.data?.error || "Error occurred while updating booking status",
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
