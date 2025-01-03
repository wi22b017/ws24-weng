<template>
  <div>
    <Form :validation-schema="addFlightFormSchema" @submit="onSubmit" v-model="formData">
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
          name="origin.code"
          id="originCode"
          placeholder="Select an origin airport"
          :options="cityOptions"
          v-model="formData.origin.code"
      />

      <AtomInput
          label="Origin Airport Name"
          name="origin.text"
          id="originText"
          placeholder="Enter origin airport name"
          v-model="formData.origin.text"
      />

      <!-- Destination Airport Dropdown -->
      <AtomFormSelect
          label="Destination Airport Code"
          name="destination.code"
          id="destinationCode"
          placeholder="Select a destination airport"
          :options="cityOptions"
          v-model="formData.destination.code"
      />

      <AtomInput
          label="Destination Airport Name"
          name="destination.text"
          id="destinationText"
          placeholder="Enter destination airport name"
          v-model="formData.destination.text"
      />

      <!-- Aircraft Dropdown -->
      <AtomFormSelect
          label="Aircraft Serial Number"
          name="aircraftSerialNumber"
          id="aircraftSerialNumber"
          placeholder="Select an aircraft serial number"
          :options="aircraftOptions"
          v-model="formData.aircraftSerialNumber"
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
      <AtomButton type="submit" label="Add Flight" :disabled="isSubmitting" />

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
import { ref, onMounted } from "vue";
import { Form } from "vee-validate";
import { object, string, number } from "yup";
import AtomInput from "@/components/atoms/AtomInput.vue";
import AtomFormSelect from "@/components/atoms/AtomFormSelect.vue";
import AtomButton from "@/components/atoms/AtomButton.vue";
import axios from "axios";
import apiClient from "@/utils/axiosClient";
import { formatISO } from "date-fns";

// Validation schema
const addFlightFormSchema = object({
  flightNumber: string().required("Flight number is required"),
  airlineId: string().required("Please select an airline"),
  origin: object({
    code: string().required("Origin airport code is required"),
    text: string().required("Origin airport name is required"),
  }),
  destination: object({
    code: string().required("Destination airport code is required"),
    text: string().required("Destination airport name is required"),
  }),
  aircraftSerialNumber: string().required("Please select an aircraft serial number"),
  departureTime: string().required("Departure time is required"),
  arrivalTime: string().required("Arrival time is required"),
  price: number().required("Price is required").min(1, "Price must be at least 1"),
});

// Form data
const formData = ref({
  flightNumber: "",
  airlineId: "",
  origin: { code: "", text: "" },
  destination: { code: "", text: "" },
  aircraftSerialNumber: "",
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

// Fetch city options for airports
const fetchCityOptions = async () => {
  try {
    const response = await axios.get("http://localhost:3000/airports");
    cityOptions.value = response.data.map((airport) => ({
      value: airport.code,
      text: airport.name,
    }));
    fetchCityError.value = ""; // Clear errors if successful
  } catch (error) {
    console.error("Failed to fetch city options:", error);
    fetchCityError.value = "An error occurred while fetching city options.";
  }
};

// Fetch airline options
const fetchAirlineOptions = async () => {
  try {
    const response = await axios.get("http://localhost:3000/airlines");
    airlineOptions.value = response.data.map((airline) => ({
      value: airline.id,
      text: airline.name,
    }));
    fetchAirlineError.value = ""; // Clear errors if successful
  } catch (error) {
    console.error("Failed to fetch airline options:", error);
    fetchAirlineError.value = "An error occurred while fetching airline options.";
  }
};

// Fetch aircraft options
const fetchAircraftOptions = async () => {
  try {
    const response = await axios.get("http://localhost:3000/aircrafts");
    aircraftOptions.value = response.data.map((aircraft) => ({
      value: aircraft.serialNumber, // Bind serialNumber as the value
      text: aircraft.serialNumber, // Display serialNumber in the dropdown
      fullData: aircraft, // Include full aircraft data
    }));
    fetchAircraftError.value = ""; // Clear errors if successful
  } catch (error) {
    console.error("Failed to fetch aircraft options:", error);
    fetchAircraftError.value = "An error occurred while fetching aircraft options.";
  }
};

// Fetch data when component is mounted
onMounted(() => {
  fetchCityOptions();
  fetchAirlineOptions();
  fetchAircraftOptions();
});

// Submit form data
async function onSubmit(values) {
  console.log(values);
  isSubmitting.value = true; // Disable the form while submitting
  addFlightError.value = null; // Reset error message
  try {
    // Format departureTime and arrivalTime to match the backend's expected format
    const formattedDepartureTime = formatISO(new Date(values.departureTime));
    const formattedArrivalTime = formatISO(new Date(values.arrivalTime));

    // Find the full aircraft data based on the selected serial number
    const selectedAircraft = aircraftOptions.value.find(
        (aircraft) => aircraft.value === values.aircraftSerialNumber
    )?.fullData;

    if (!selectedAircraft) {
      throw new Error("Invalid aircraft selection");
    }

    // Construct the payload for adding a flight
    const payload = {
      flightNumber: values.flightNumber,
      airlineId: values.airlineId,
      flightOrigin: { airportCode: values.origin.code, airportText: values.origin.text },
      flightDestination: { airportCode: values.destination.code, airportText: values.destination.text },
      aircraft: {
        serialNumber: selectedAircraft.serialNumber,
        manufacturer: selectedAircraft.manufacturer,
        model: selectedAircraft.model,
        capacity: selectedAircraft.capacity,
        airline: { name: selectedAircraft.airline.name },
      },
      departureTime: formattedDepartureTime, // Use formatted datetime
      arrivalTime: formattedArrivalTime, // Use formatted datetime
      price: values.price,
    };

    console.log("Payload:", payload);

    // Send the payload to the backend
    const response = await apiClient.post("http://localhost:3000/flights", payload, {
      headers: {
        "Content-Type": "application/json",
      },
    });

    // Handle success
    if (response.status >= 200 && response.status < 300) {
      console.log("Flight added successfully:", response.data);
      addFlightSuccess.value = "Flight added successfully.";
      formData.value = {
        flightNumber: "",
        airlineId: "",
        origin: { code: "", text: "" },
        destination: { code: "", text: "" },
        aircraftSerialNumber: "",
        departureTime: "",
        arrivalTime: "",
        price: null,
      }; // Reset the form data
    } else {
      throw new Error(`Unexpected response status: ${response.status}`);
    }
  } catch (error) {
    console.error("Error adding flight:", error.response?.data || error.message);
    addFlightError.value = error.response?.data?.error || "An error occurred while adding the flight.";
  } finally {
    isSubmitting.value = false; // Enable the form
  }
}
</script>

<style scoped>
.alert {
  margin-top: 1rem;
  color: #721c24;
  background-color: #f8d7da;
  border-color: #f5c6cb;
  padding: 0.75rem;
  border-radius: 4px;
}
</style>
