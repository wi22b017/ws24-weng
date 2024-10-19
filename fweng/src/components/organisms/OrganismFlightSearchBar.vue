<template>
  <div>
    <MoleculeFlightSearchForm
        v-bind:flightTypes="flightTypes"
        v-bind:cityOptions="cityOptions"
        v-bind:flightClassOptions="flightClassOptions"
        @submit="onSubmit"
    />
  </div>
</template>

<script setup>
import { defineEmits } from 'vue';
import MoleculeFlightSearchForm from "@/components/molecules/MoleculeFlightSearchForm.vue";

// City options
const cityOptions = [
  { value: 'VIE', text: 'Vienna International Airport' },
  { value: 'NYC', text: 'New York' },
  { value: 'LAX', text: 'Los Angeles' },
  { value: 'LON', text: 'London' },
  { value: 'PAR', text: 'Paris' },
];

// Flight types
const flightTypes = [
  { value: 'roundTrip', text: 'Round Trip' },
  { value: 'oneWay', text: 'One Way' },
];

// Flight Class options
const flightClassOptions = [
  { value: 'economy', text: 'Economy' },
  { value: 'business', text: 'Business' },
  { value: 'first', text: 'First' }
];

const emit = defineEmits(['submit']);

const onSubmit = (formData) => {
  emit('submit', formData);
};
</script>
