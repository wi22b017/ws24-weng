import {defineStore} from 'pinia'
import apiClient from '../utils/axiosClient';

export const useFlightStore = defineStore('flight', {
    state: () => ({
        flights: [],
        flightToBook: []
    }),
    actions: {
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
        async fetchFlight(flightId) {
            try {
                const response = await apiClient.get(`/flights/${flightId}`);
                this.flightToBook = [response.data];
                return {
                    success: true,
                    message: 'Fetch Flight Successful'
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
                key: 'flight',
                storage: localStorage,
            },
        ],
    },
})