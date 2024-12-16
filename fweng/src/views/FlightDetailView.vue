<template>
<h1>New Booking</h1>
  <h2>Flight Details</h2>
  <div class="flight-list-container">
    <FlightListTemplate
        :flights="flightStore.flightToBook"
        :show-booking-button="false"
    />
  </div>
  <h2>Passengers</h2>
    <MoleculePassengerForm
        v-for="(passenger, index) in passengers"
        :key="index"
        :passenger="passenger"
        :index="index"
        :baggageTypes="baggageTypes"
    />
    <button @click="addPassenger">Add Passenger</button>
  <h2>Payment</h2>
  <div>
    <label for="paymentMethod">Payment Method</label>
    <select v-model="selectedPaymentMethod" id="paymentMethod">
      <option v-for="method in paymentMethods" :key="method.id" :value="method.id">
        {{ method.label }}
      </option>
    </select>
  </div>
  <h2>Total Price: {{ totalPrice }}</h2>
  <button @click="confirmBooking">Confirm Booking</button>
</template>

<script setup>
import FlightListTemplate from "@/components/template/FlightListTemplate.vue";
import MoleculePassengerForm from "@/components/molecules/MoleculePassengerForm.vue";
import { useFlightStore } from '@/store/flight';
import {onMounted, defineProps, reactive, computed} from "vue";
import {useRoute} from "vue-router";

defineProps({
  flightId: {
    type: String,
    required: true,
  },
});


const route = useRoute();
const flightId = route.params.flightId;
const flightStore = useFlightStore();

const passengers = reactive([
  {
    firstName: "LoggedUserFirstName",
    lastName: "LoggedUserLastName",
    birthday: "1990-01-01",
    seatNumber: "",
    baggage: {
      baggageTypeId: "",
    },
  },
]);

const baggageTypes = [
  { id: "c164e92b-aa35-11ef-a7d0-0242ac120002", label: "Carry-On" },
  { id: "c164e92c-aa35-11ef-a7d0-0242ac120002", label: "Checked" },
];

const paymentMethods = [
  { id: "c167f3d0-aa35-11ef-a7d0-0242ac120002", label: "Credit Card" },
  { id: "c167f3d1-aa35-11ef-a7d0-0242ac120002", label: "PayPal" },
];

const selectedPaymentMethod = reactive("");

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
    userId: "686e877d-503d-477b-b51b-3c731ba4a12d",
    paymentMethodId: selectedPaymentMethod,
    flightId: flightId,
    passengers: passengers,
  };

  console.log("Booking Data Sent to Backend:", bookingData);
  // Replace with actual API call
  // await flightStore.createBooking(bookingData);
};



onMounted(async () => {
  await flightStore.fetchFlight(flightId);
});

</script>

<style scoped>

</style>