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
</template>

<script setup>
import FlightListTemplate from "@/components/template/FlightListTemplate.vue";
import { useFlightStore } from '@/store/flight';
import {onMounted, defineProps} from "vue";
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

onMounted(async () => {
  await flightStore.fetchFlight(flightId);
});

</script>

<style scoped>

</style>