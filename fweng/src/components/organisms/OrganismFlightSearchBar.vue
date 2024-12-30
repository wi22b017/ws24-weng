<template>
  <div>
    <MoleculeFlightSearchForm

        v-bind:cityOptions="cityOptions"
        @submit="onSubmit"
    />
  </div>
</template>

<script setup>
import {defineEmits, onMounted, ref} from 'vue';
import MoleculeFlightSearchForm from "@/components/molecules/MoleculeFlightSearchForm.vue";
import axios from "axios";

// Reactive cityOptions variable
const cityOptions = ref([]);




const emit = defineEmits(['submit']);

const onSubmit = (formData) => {
  emit('submit', formData);
};

// Fetch city options from the backend on component mount
const fetchCityOptions = async () => {
  try {
    const response = await axios.get('http://localhost:3000/airports');
    cityOptions.value = response.data.map(airport => ({
      value: airport.code,
      text: airport.name,
    }));
  } catch (error) {
    console.error('Failed to fetch city options:', error);
  }
};

// Use onMounted to trigger fetch when component is mounted
onMounted(fetchCityOptions);

</script>
