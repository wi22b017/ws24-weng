<template>
  <h1>New Booking</h1>
  <h2>Flight Details</h2>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-8 text-start">
        <FlightListTemplate
            :flights="flightStore.flightToBook"
            :show-booking-button="false"
        />
        <MoleculePassengerForm
            v-for="(passenger, index) in passengers"
            :key="index"
            :passenger="passenger"
            :index="index"
            @updatePassenger="(updatedPassenger) => updatePassenger(index, updatedPassenger)"
        />
        <div class="text-center mb-3">
          <AtomButton
              type="button"
              @click="addPassenger"
              label="Add Passenger"
              :full-width="false"
          />
        </div>

        <h2 class="text-center">Total Price: {{ totalPrice }}</h2>
        <h2 class="text-center">Payment</h2>
        <AtomFormSelect
            label="Payment Method"
            name="paymentMethod"
            id="paymentMethod"
            placeholder="Select a payment method"
            v-model="selectedPaymentMethod"
            :options="paymentMethodOptions"
        />
        <div class="text-center mb-3">
          <AtomButton
              type="button"
              @click="confirmBooking"
              label="Confirm Booking"
              :full-width="false"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import FlightListTemplate from "@/components/template/FlightListTemplate.vue";
import MoleculePassengerForm from "@/components/molecules/MoleculePassengerForm.vue";
import { useFlightStore } from '@/store/flight';
import { onMounted, defineProps, reactive, computed, ref as vueRef } from "vue";
import { useRoute } from "vue-router";
import AtomButton from "@/components/atoms/AtomButton.vue";
import apiClient from "@/utils/axiosClient";
import AtomFormSelect from "@/components/atoms/AtomFormSelect.vue";
import { useUserStore } from "@/store/user";

import { object, string } from "yup";

// Define the same validation schema here for final check before send
const passengerFinalSchema = object({
  firstName: string().required().matches(/^[a-zA-Z'\-\s]*$/),
  lastName: string().required().matches(/^[a-zA-Z'\-\s]*$/),
  dateOfBirth: string().required().matches(/^\d{4}-\d{2}-\d{2}$/),
  baggage: object({
    baggageTypeId: string().required(),
  }),
  seatNumber: string().nullable(true),
});

defineProps({
  flightId: {
    type: String,
    required: true,
  },
});

const userStore = useUserStore();
const route = useRoute();
const flightId = route.params.flightId;
const flightStore = useFlightStore();
const paymentMethodOptions = vueRef([]);
const selectedPaymentMethod = vueRef();

const passengers = reactive([
  {
    firstName: userStore.firstName,
    lastName: userStore.lastName,
    dateOfBirth: userStore.dateOfBirth,
    seatNumber: "",
    baggage: {
      baggageTypeId: "",
    },
  },
]);

const totalPrice = computed(() => {
  // Example price calculation
  return passengers.length * 100;
});

const addPassenger = () => {
  passengers.push({
    firstName: "",
    lastName: "",
    dateOfBirth: "",
    seatNumber: "",
    baggage: {
      baggageTypeId: "",
    },
  });
};

const updatePassenger = (index, updatedPassenger) => {
  passengers[index] = {...updatedPassenger};
};

const confirmBooking = async () => {
  // Validate all passengers before sending the request
  for (const passenger of passengers) {
    try {
      await passengerFinalSchema.validate(passenger, {abortEarly: false});
    } catch (err) {
      // If validation fails, show error and return early
      alert("Validation failed for one or more passengers. Please check the details again.");
      console.error("Validation errors:", err.errors);
      return;
    }
  }

  if (!selectedPaymentMethod.value) {
    alert("Please select a payment method.");
    return;
  }

  const bookingData = {
    status: "Confirmed",
    price: totalPrice.value,
    bookingDate: new Date().toISOString(),
    userId: userStore.id,
    paymentMethodId: selectedPaymentMethod.value,
    flightId: flightId,
    passengers: passengers,
  };

  console.log("Booking Data Sent to Backend:", bookingData);
};

async function getPaymentMethods() {
  try {
    const paymentMethodOptionsResponse = await apiClient.get(`/paymentMethods`);
    paymentMethodOptions.value = paymentMethodOptionsResponse.data.map((method) => ({
      value: method.name,
      text: method.name,
    }));
  } catch (error) {
    console.error("Failed to fetch payment methods:", error);
  }
}

onMounted(async () => {
  await flightStore.fetchFlight(flightId);
  await getPaymentMethods();
});
</script>

<style scoped>
.container {
  text-align: center;
}
.text-center {
  text-align: center;
}
</style>
