<template>
  <div class="container">
    <Form
        :validation-schema="addFlightFormSchema"
        @submit="onSubmit"
        v-model="formData">

      <!-- Flight Number Input -->
      <AtomInput label="Flight Number" name="flightNumber" id="flightNumber" v-model="formData.flightNumber" />

      <!-- Airline Dropdown -->
      <AtomFormSelect
          label="Airline"
          name="airlineId"
          id="airlineId"
          placeholder="Select an airline"
          :options="airlineOptions"
          v-model="formData.airlineId"
      />

      <!-- Origin Airport Dropdown -->
      <AtomFormSelect
          label="Origin Airport Code"
          name="origin"
          id="origin"
          placeholder="Select an origin airport"
          :options="cityOptions"
          v-model="formData.origin"
      />

      <!-- Destination Airport Dropdown -->
      <AtomFormSelect
          label="Destination Airport Code"
          name="destination"
          id="destination"
          placeholder="Select a destination airport"
          :options="cityOptions"
          v-model="formData.destination"
      />

      <!-- Aircraft Dropdown -->
      <AtomFormSelect
          label="Aircraft Serial Number"
          name="aircraft"
          id="aircraft"
          placeholder="Select an aircraft serial number"
          :options="filteredAircraftOptions"
          v-model="formData.aircraft"
      />

      <!-- Departure Time -->
      <AtomInput
          type="datetime-local"
          label="Departure Time"
          name="departureTime"
          id="departureTime"
          v-model="formData.departureTime"
      />

      <!-- Arrival Time -->
      <AtomInput
          type="datetime-local"
          label="Arrival Time"
          name="arrivalTime"
          id="arrivalTime"
          v-model="formData.arrivalTime"
      />

      <!-- Price -->
      <AtomInput type="number" label="Price" name="price" id="price" v-model="formData.price" />

      <!-- Submit Button -->
      <AtomButton type="submit" label="Add Flight" class="mb-3" :disabled="isSubmitting" />

      <!-- Success Messages -->
      <div v-if="addFlightSuccess" class="alert alert-info mt-3" role="alert">
        {{ addFlightSuccess }}
      </div>
      <!-- Error Messages -->
      <div v-if="addFlightError" class="alert alert-danger mt-3" role="alert">
        {{ addFlightError }}
      </div>
      <div v-if="fetchCityError || fetchAirlineError || fetchAircraftError" class="alert alert-danger mt-3" role="alert">
        {{ fetchCityError || fetchAirlineError || fetchAircraftError }}
      </div>
    </Form>
  </div>
</template>

<script setup>
import {ref, onMounted, inject, computed} from "vue";
import { Form } from "vee-validate";
import { object, string, number } from "yup";
import AtomInput from "@/components/atoms/AtomInput.vue";
import AtomFormSelect from "@/components/atoms/AtomFormSelect.vue";
import AtomButton from "@/components/atoms/AtomButton.vue";
import axios from "axios";
import apiClient from "@/utils/axiosClient";
import { formatISO } from "date-fns";
import {useAdminUserStore} from "@/store/adminUserStore";

// Reactive state
const formData = ref({
  flightNumber: "",
  airlineId: "",
  origin: "",
  destination: "",
  aircraft: "",
  departureTime: "",
  arrivalTime: "",
  price: null,
});

// Options for dropdowns
const airlineOptions = ref([]);
const cityOptions = ref([]);
const aircraftOptions = ref([]);
const isSubmitting = ref(false);
const addFlightError = ref("");
const fetchCityError = ref("");
const fetchAirlineError = ref("");
const fetchAircraftError = ref("");
const addFlightSuccess = ref("");
const adminUserStore = useAdminUserStore();
const hideAddFlightModal = inject('hideAddFlightModal');

// Validation schema
const addFlightFormSchema = object({
  flightNumber: string().required("Flight number is required"),
  airlineId: string().required("Please select an airline"),
  origin: string().required("Origin airport code is required"),
  destination: string().required("Destination airport code is required"),
  aircraft: string().required("Please select an aircraft serial number"),
  departureTime: string()
      .required("Departure time is required")
      .matches(
          /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}$/,
          "Departure time must be in the format YYYY-MM-DDTHH:mm"
      ),
  arrivalTime: string()
      .required("Arrival time is required")
      .matches(
          /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}$/,
          "Arrival time must be in the format YYYY-MM-DDTHH:mm"
      ),
  price: number()
      .required("Price is required")
      .min(1, "Price must be at least 1"),
});

// Filter the available aircrafts for the dropdown according to the selected airline
const filteredAircraftOptions = computed(() => {
  if (!formData.value.airlineId) {
    return [];
  }
  return aircraftOptions.value.filter(
      (aircraft) => aircraft.fullData.airline.id === formData.value.airlineId
  ).map((aircraft) => ({
    value: aircraft.value,
    text: aircraft.text,
    fullData: aircraft.fullData
  }));
});

// Submit form data
async function onSubmit(values) {
  console.log(values);
  isSubmitting.value = true; // Disable the form while submitting
  addFlightError.value = null; // Reset error message
  try {

    const selectedAircraft = aircraftOptions.value.find(
        (aircraft) => aircraft.value === values.aircraft
    )?.fullData;

    const selectedOrigin = cityOptions.value.find(
        (cityOption) => cityOption.value === values.origin
    )?.fullData;

    const selectedDestination = cityOptions.value.find(
        (cityOption) => cityOption.value === values.destination
    )?.fullData;


    const payload = {
      flightNumber: values.flightNumber,
      airlineId: values.airlineId,
      flightOrigin: { airportCode: selectedOrigin.code, airportText: selectedOrigin.name },
      flightDestination: { airportCode: selectedDestination.code, airportText: selectedDestination.name },
      aircraft: {
        serialNumber: values.aircraft,
        manufacturer: selectedAircraft.manufacturer,
        model: selectedAircraft.model,
        capacity: selectedAircraft.capacity,
        airline: { name: selectedAircraft.airline.name }
      },
      departureTime: formatISO(new Date(values.departureTime)),
      arrivalTime: formatISO(new Date(values.arrivalTime)),
      price: values.price
    };

    // Send the payload to the backend
    const response = await apiClient.post("http://localhost:3000/flights", payload);

    addFlightSuccess.value = response.data.message;

    await adminUserStore.fetchFlights();

    setTimeout(() => {
      hideAddFlightModal();
    }, 1000);

  } catch (error) {
    console.error("Error adding flight:", error.response?.data || error.message);
    addFlightError.value = error.response?.data?.error || "An error occurred while adding the flight.";
  } finally {
    isSubmitting.value = false;
  }
}

// Fetch dropdown options
const fetchCityOptions = async () => {
  try {
    const response = await axios.get("http://localhost:3000/airports");
    cityOptions.value = response.data.map((airport) => ({
      value: airport.code,
      text: airport.name,
      fullData: airport,
    }));
  } catch (error) {
    fetchCityError.value = "Error fetching city options.";
    console.error(error);
  }
};

const fetchAirlineOptions = async () => {
  try {
    const response = await axios.get("http://localhost:3000/airlines");
    airlineOptions.value = response.data.map((airline) => ({
      value: airline.id,
      text: airline.name,
    }));
  } catch (error) {
    fetchAirlineError.value = "Error fetching airline options.";
    console.error(error);
  }
};

const fetchAircraftOptions = async () => {
  try {
    const response = await axios.get("http://localhost:3000/aircrafts");
    aircraftOptions.value = response.data.map((aircraft) => ({
      value: aircraft.serialNumber,
      text: aircraft.serialNumber,
      fullData: aircraft // Include full aircraft data for submission
    }));
  } catch (error) {
    fetchAircraftError.value = "Error fetching aircraft options.";
    console.error(error);
  }
};

// Fetch data when component is mounted
onMounted(() => {
  fetchCityOptions();
  fetchAirlineOptions();
  fetchAircraftOptions();
});
</script>

<style scoped>
.container {
  text-align: start;
}
</style>
