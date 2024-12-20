import {defineStore} from 'pinia'
import apiClient from '../utils/axiosClient';

export const useFlightStore = defineStore('flight', {
    state: () => ({
        flights: [],
        flightToBook: [],
        baggageTypesOptions: [],
        defaultBaggageType: String
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
        async fetchBaggageTypes() {
            try {
                const baggageTypesOptionsResponse = await apiClient.get(`/baggageTypes`);
                this.baggageTypesOptions = baggageTypesOptionsResponse.data.map((baggageType) => ({
                    value: baggageType.id,
                    text: `${baggageType.name} (${baggageType.fee.toFixed(2)} €)`,
                    originalName: baggageType.name,
                    fee: baggageType.fee,
                }));

                // Set default baggage type to "Hand Luggage" if available
                const handLuggageOption = this.baggageTypesOptions.find(
                    option => option.originalName.toLowerCase().includes("hand luggage")
                );
                if (handLuggageOption) {
                   this.defaultBaggageType = handLuggageOption.value;
                }
            } catch (error) {
                console.error("Failed to fetch baggage types:", error);
            }
        }
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