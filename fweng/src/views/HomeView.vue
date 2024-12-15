<template>
  <div class="container mt-5 p-4 border rounded">
    <h2 class="mb-4">Book flights with ease</h2>

    <!-- Error Modal Component -->
    <ErrorModal ref="errorModal" />

    <!-- Search Bar Component -->
    <OrganismFlightSearchBar @submit="onSubmit" />
  </div>

  <!-- Flight List Template -->
  <div class="flight-list-container">
    <FlightListTemplate :flights="flights" />
  </div>
</template>

<script setup>
import {ref} from "vue";
import axios from "axios";
import ErrorModal from "@/components/organisms/OrganismErrorModal.vue";
import OrganismFlightSearchBar from "@/components/organisms/OrganismFlightSearchBar.vue";
import FlightListTemplate from "@/components/template/FlightListTemplate.vue";
import { useFlightStore } from '@/store/flight';

// References and state
const errorModal = ref(null);
const flights = ref([]); // List of flights
const isLoading = ref(false);
const flightStore = useFlightStore();

// On form submission
const onSubmit = async (formData) => {
  console.log("Form submitted:", formData);

  if (!formData.departureCity || !formData.arrivalCity) {
    errorModal.value.showModal("Please select both departure and arrival cities.");
    return;
  }

  if (!formData.departureDate) {
    errorModal.value.showModal("Please select a departure date.");
    return;
  }

  try {
    isLoading.value = true;

    // Fetch flights
    const response = await axios.get("http://localhost:3000/flights");
    await flightStore.fetchFlights();
    const userDate = new Date(formData.departureDate).toISOString().split("T")[0];

    flights.value = response.data.filter((flight) => {
      const flightDate = new Date(flight.departureTime).toISOString().split("T")[0];
      return (
          flight.flightOrigin.code === formData.departureCity &&
          flight.flightDestination.code === formData.arrivalCity &&
          flightDate === userDate
      );
    });

    console.log("Filtered flights:", flights.value);

    // If no flights are found
    if (flights.value.length === 0) {
      errorModal.value.showModal("No flights available for the selected route.");
    }
  } catch (error) {
    console.error("Error fetching flights:", error);
    errorModal.value.showModal("Failed to fetch flights. Please try again later.");
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
.container {
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.flight-list-container {
  margin-top: 20px; /* Add spacing below the search bar */
  padding: 20px;
}
</style>
