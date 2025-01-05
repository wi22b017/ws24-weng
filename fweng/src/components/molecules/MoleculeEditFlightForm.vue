<template>
  <div>
    <Form
        :validation-schema="editFlightFormSchema"
        @submit="onSubmit"
        v-model="formData"
        :initial-values="formData"
      >
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
          :options="aircraftOptions"
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
      <AtomButton type="submit" label="Save Changes" :disabled="isSubmitting" />

      <!-- Error Messages -->
      <div v-if="editFlightError" class="alert alert-danger mt-3" role="alert">
        {{ editFlightError }}
      </div>
      <div v-if="fetchCityError || fetchAirlineError || fetchAircraftError" class="alert alert-danger mt-3" role="alert">
        {{ fetchCityError || fetchAirlineError || fetchAircraftError }}
      </div>
    </Form>
  </div>
</template>

<script setup>
import { ref, onMounted, defineProps} from "vue";
import { Form } from "vee-validate";
import { object, string, number } from "yup";
import AtomInput from "@/components/atoms/AtomInput.vue";
import AtomFormSelect from "@/components/atoms/AtomFormSelect.vue";
import AtomButton from "@/components/atoms/AtomButton.vue";
import axios from "axios";
import apiClient from "@/utils/axiosClient";
import { formatISO } from "date-fns";

// Validation schema
const editFlightFormSchema = object({
  flightNumber: string().required("Flight number is required"),
  airlineId: string().required("Please select an airline"),
  origin: object({
    code: string().required("Origin airport code is required"),
    text: string().required("Origin airport name is required"),
  }).default(() => ({ code: "", text: "" })),
  destination: object({
    code: string().required("Destination airport code is required"),
    text: string().required("Destination airport name is required"),
  }).default(() => ({ code: "", text: "" })),
  aircraftSerialNumber: string().required("Please select an aircraft serial number"),
  departureTime: string().required("Departure time is required"),
  arrivalTime: string().required("Arrival time is required"),
  price: number().required("Price is required").min(1, "Price must be at least 1"),
});

const airlineOptions = ref([]);
const cityOptions = ref([]);
const aircraftOptions = ref([]);
const isSubmitting = ref(false);
const editFlightError = ref("");
const fetchCityError = ref("");
const fetchAirlineError = ref("");
const fetchAircraftError = ref("");

// Props to receive initial data
const props = defineProps({
  initialValues: {
    type: Object,
    default: () => ({}),
  },
});

// Reactive state
const formData = ref({
  flightNumber: props.initialValues.flightNumber,
  airlineId: props.initialValues.aircraft.airline.id,
  origin: props.initialValues.flightOrigin.code,
  destination: props.initialValues.flightDestination.code,
  aircraft: props.initialValues.aircraft.serialNumber,
  departureTime: props.initialValues.departureTime.substring(0, 16),
  arrivalTime: props.initialValues.arrivalTime.substring(0, 16),
  price: props.initialValues.price,
});

console.log("HEEEEEEEEELLLLLLOOOOOO", formData);

// Watch for changes in initialValues and update formData
/*watch(
    () => props.initialValues, // Watch for changes in initialValues
    (newValues) => {
      console.log("Initial values updated in form:", newValues); // Log initialValues
      if (!newValues || Object.keys(newValues).length === 0) {
        console.warn("No initial values provided to form.");
        return;
      }

      // Safely update formData
      formData.value = {
        flightNumber: newValues.flightNumber || "",
        airlineId: newValues.aircraft?.airline?.name || "",
        origin: {
          code: newValues.flightOrigin?.airportCode || "",
          text: newValues.flightOrigin?.airportText || "",
        },
        destination: {
          code: newValues.flightDestination?.airportCode || "",
          text: newValues.flightDestination?.airportText || "",
        },
        aircraftSerialNumber: newValues.aircraft?.serialNumber || "",
        departureTime: newValues.departureTime || "",
        arrivalTime: newValues.arrivalTime || "",
        price: newValues.price || null,
      };
      console.log("Updated formData in form:", formData.value); // Log updated formData
    },
    { immediate: true }
);*/


// Fetch dropdown options
const fetchCityOptions = async () => {
  try {
    const response = await axios.get("http://localhost:3000/airports");
    cityOptions.value = response.data.map((airport) => ({
      value: airport.code,
      text: airport.name,
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


onMounted(() => {
  fetchCityOptions();
  fetchAirlineOptions();
  fetchAircraftOptions();
});

// Handle form submission

const onSubmit = async (values) => {
  try {

    const selectedAircraft = aircraftOptions.value.find(
        (aircraft) => aircraft.value === values.aircraftSerialNumber
    )?.fullData;

    if (!selectedAircraft) {
      throw new Error("Invalid aircraft selection.");
    }

    const payload = {
      flightNumber: values.flightNumber,
      airlineId: values.airlineId,
      flightOrigin: { airportCode: values.origin.code, airportText: values.origin.text },
      flightDestination: { airportCode: values.destination.code, airportText: values.destination.text },
      aircraft: {
        serialNumber: values.aircraftSerialNumber,
        manufacturer: selectedAircraft.manufacturer, // Add missing fields
        model: selectedAircraft.model,
        capacity: selectedAircraft.capacity,
        airline: { name: selectedAircraft.airline.name }
      },
      departureTime: formatISO(new Date(values.departureTime)),
      arrivalTime: formatISO(new Date(values.arrivalTime)),
      price: values.price
    };


    console.log("Submitting payload to API:", payload); // Log payload

    const response = await apiClient.put(`/flights/${props.initialValues.id}`, payload);
    console.log("API response:", response.data); // Log success response
  } catch (error) {
    console.error("Error during API request:", error); // Log full error
    console.error("API Error Response:", error.response?.data || error.message); // Log API response details
    throw error; // Re-throw error if needed
  }
};

</script>
