<template>
<h1>Flight Overview</h1>
  <div class="flight-list-container">
    <FlightListTemplate :flights="flightStore.flightToBook" />
  </div>
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