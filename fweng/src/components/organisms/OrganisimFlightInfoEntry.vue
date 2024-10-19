<template>
  <div class="flight-info-entry-card card shadow">
    <div class="row w-10">
      <div class="col-6 d-flex flex-column justify-content-between my-3">
        <div class="d-flex justify-content-center align-items-center w-100">
          <MoleculeFlightFromToInfo
            v-bind="flightDepartureInfo"
            class="flex-grow-1"
          />
          <MoleculeFlightConnector :stops="stops" />
          <MoleculeFlightFromToInfo
            v-bind="flightArrivalInfo"
            class="flex-grow-1 ms-2"
          />
        </div>
        <div class="d-flex flex-column align-items-start">
          <MoleculeIconText
            :icon="faClock"
            :text="'Duration ' + formatDuration(flightDuration)"
          />
          <MoleculeIconText :icon="faPlane" :text="'Operated by ' + airline" />
        </div>
      </div>
      <div class="col-6">
        <div class="flight-class-cards d-flex justify-content-between w-100">
          <MoleculeFlightClassCard
            v-bind="economyClassCard"
            class="flex-grow-1"
          />
          <MoleculeFlightClassCard
            v-bind="businessClassCard"
            class="flex-grow-1"
          />
          <MoleculeFlightClassCard
            v-bind="firstClassCard"
            class="flex-grow-1"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { faClock } from "@fortawesome/free-regular-svg-icons";
import MoleculeFlightClassCard from "../molecules/MoleculeFlightClassCard.vue";
import MoleculeFlightFromToInfo from "../molecules/MoleculeFlightFromToInfo.vue";
import MoleculeIconText from "../molecules/MoleculeIconText.vue";
import { defineProps } from "vue";
import { faPlane } from "@fortawesome/free-solid-svg-icons";
import MoleculeFlightConnector from "../molecules/MoleculeFlightConnector.vue";

defineProps({
  flightDepartureInfo: {
    type: Object,
    required: true,
    default: () => ({
      time: "",
      airportCode: "",
      terminal: "",
      type: "departure",
    }),
  },
  flightArrivalInfo: {
    type: Object,
    required: true,
    default: () => ({
      time: "",
      airportCode: "",
      terminal: "",
      type: "arrival",
    }),
  },
  stops: {
    type: Number,
    required: true,
    default: 0,
  },
  flightDuration: {
    type: Number,
    required: true,
  },
  airline: {
    type: String,
    required: true,
  },

  economyClassCard: {
    type: Object,
    required: true,
    default: () => ({
      flightClassName: "Economy",
      price: 0,
      currency: "EUR",
    }),
  },
  businessClassCard: {
    type: Object,
    required: true,
    default: () => ({
      flightClassName: "Business",
      price: 0,
      currency: "EUR",
    }),
  },
  firstClassCard: {
    type: Object,
    required: true,
    default: () => ({
      flightClassName: "First",
      price: 0,
      currency: "EUR",
    }),
  },
});

function formatDuration(minutes) {
  const hours = Math.floor(minutes / 60);
  const remainingMinutes = minutes % 60;
  return `${hours}h ${remainingMinutes}m`;
}
</script>

<style scoped>
.flight-info-entry-card {
  width: 70%;
  margin: 10px auto;
  padding: 20px;
  max-width: 1200px;
}
</style>
