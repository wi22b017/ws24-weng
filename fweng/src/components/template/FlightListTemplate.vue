<template>
  <div>
    <div v-for="flight in flightData.flights" :key="flight.id">
      <OrganismFlightInfoEntry
          :flight-departure-info="flight.flightDepartureInfo"
          :flight-arrival-info="flight.flightArrivalInfo"
          :stops="flight.stops"
          :flight-duration="flight.flightDuration"
          :airline="flight.airline"
          :economy-class-card="flight.economyClassCard"
          :business-class-card="flight.businessClassCard"
          :first-class-card="flight.firstClassCard"
      />
    </div>
  </div>
</template>

<script setup>
import OrganismFlightInfoEntry from "@/components/organisms/OrganismFlightInfoEntry.vue";
import flightData from "/mock/flightData.json";
</script>
