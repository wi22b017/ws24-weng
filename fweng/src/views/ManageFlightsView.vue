<template>
  <h1>Manage Flights</h1>
  <h2>ToDo for Admin</h2>

  <!-- Flight List Template -->
  <div v-if="adminUserStore.flights.length>0" class="flight-list-container">
    <FlightListTemplate :flights="adminUserStore.flights" />
  </div>

  <div v-if="fetchFlightError" class="alert alert-danger mt-3" role="alert">
    {{ fetchFlightError }}
  </div>
  <div v-if="fetchFlightSuccess" class="alert alert-info mt-3" role="alert">
    {{ fetchFlightSuccess }}
  </div>

</template>

<script setup>
import FlightListTemplate from "@/components/template/FlightListTemplate.vue";
import {onMounted, ref} from "vue";
import {useAdminUserStore} from "@/store/adminUserStore";


const adminUserStore = useAdminUserStore();
const fetchFlightError = ref('');
const fetchFlightSuccess = ref('');

onMounted(async () => {

  const result = await adminUserStore.fetchFlights();

  if(result.success===true){
    fetchFlightSuccess.value = result.message;
  }else{
    fetchFlightError.value = result.message;
  }

});

</script>

<style scoped>

</style>