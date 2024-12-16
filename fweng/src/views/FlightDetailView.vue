<template>
<h1>New Booking</h1>
  <h2>Flight Details</h2>
  <div class="flight-list-container">
    <FlightListTemplate
        :flights="flightStore.flightToBook"
        :show-booking-button="false"
    />
  </div>
  <MoleculePassengerForm
      v-for="(passenger, index) in passengers"
      :key="index"
      :passenger="passenger"
      :index="index"
  />
  <AtomButton
      type="button"
      @click="addPassenger"
      label="Add Passenger"
      :full-width="false"
  />

  <h2>Total Price: {{ totalPrice }}</h2>
  <h2>Payment</h2>
  <AtomFormSelect
      label="Payment Method"
      name="paymentMethod"
      id="paymentMethod"
      placeholder="Select a payment method"
      v-model="selectedPaymentMethod"
      :options="paymentMethodOptions"
  />

  <AtomButton
      type="button"
      @click="confirmBooking"
      label="Confirm Booking"
      :full-width="false"
  />
</template>

<script setup>
import FlightListTemplate from "@/components/template/FlightListTemplate.vue";
import MoleculePassengerForm from "@/components/molecules/MoleculePassengerForm.vue";
import { useFlightStore } from '@/store/flight';
import {onMounted, defineProps, reactive, computed, ref as vueRef} from "vue";
import {useRoute} from "vue-router";
import AtomButton from "@/components/atoms/AtomButton.vue";
import apiClient from "@/utils/axiosClient";
import AtomFormSelect from "@/components/atoms/AtomFormSelect.vue";
import {useUserStore} from "@/store/user";

defineProps({
  flightId: {
    type: String,
    required: true,
  },
});

// Pinia store instance
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
  return passengers.length * 100; // Example price calculation
});

const addPassenger = () => {
  passengers.push({
    firstName: "",
    lastName: "",
    birthday: "",
    seatNumber: "",
    baggage: {
      baggageTypeId: "",
    },
  });
};

const confirmBooking = async () => {
  const bookingData = {
    status: "Confirmed",
    price: totalPrice.value,
    bookingDate: new Date().toISOString(),
    userId: userStore.id,
    paymentMethodId: selectedPaymentMethod,
    flightId: flightId,
    passengers: passengers,
  };

  console.log("Booking Data Sent to Backend:", bookingData);
  // Replace with actual API call
  // await flightStore.createBooking(bookingData);
};

async function getPaymentMethods(){
  try {
    const paymentMethodOptionsResponse = await apiClient.get(`/paymentMethods`);
    // Map API response to the structure required by AtomFormSelect
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

</style>