<template>
  <div>
    <div v-for="flight in flights" :key="flight.id" class="flight-item">
      <OrganismFlightInfoEntry
          :flight-departure-info="flight.flightOrigin"
          :flight-arrival-info="flight.flightDestination"
          :flight-departure-time="flight.departureTime"
          :flight-arrival-time="flight.arrivalTime"
          :flight-duration="calculateDurationInMinutes(flight.departureTime, flight.arrivalTime)"
          :airline="flight.aircraft.airline.name"
          :flight-id="flight.id"
          :flight-price="flight.price"

      />
    </div>
  </div>
</template>

<script setup>
import OrganismFlightInfoEntry from "@/components/organisms/OrganismFlightInfoEntry.vue";
import { defineProps } from "vue";

const calculateDurationInMinutes = (departureTime, arrivalTime) => {
  const departure = new Date(departureTime);
  const arrival = new Date(arrivalTime);
  return Math.floor((arrival - departure) / (1000 * 60)); // Duration in minutes
};

defineProps({
  flights: {
    type: Array,
    required: true,
  },
});
</script>
