<template>
  <div class="container mt-5 p-4 border rounded">
    <h2 class="mb-4">Book flights with ease</h2>

    <!-- Error Modal Component -->
    <ErrorModal ref="errorModal" />

    <!-- OrganismFlightSearchBar Component -->
    <OrganismFlightSearchBar @submit="onSubmit"/>
  </div>
  <FlightListTemplate class="d-none" id="flightList" />
  <div class="mt-5">

  </div>
</template>

<script setup>
import { ref } from 'vue';
import ErrorModal from '@/components/organisms/OrganismErrorModal.vue';
import OrganismFlightSearchBar from "@/components/organisms/OrganismFlightSearchBar.vue";
import FlightListTemplate from "@/components/template/FlightListTemplate.vue";


// Reference to the error modal
const errorModal = ref(null);

// Error checking on form submission
const onSubmit = (formData) => {
  if (!formData.departureCity || !formData.arrivalCity) {
    errorModal.value.showModal('Please select both departure and arrival cities.');
    return;
  }


  if (!formData.departureDate) {
    errorModal.value.showModal('Please select a departure date.');
    return;
  }

  if (formData.flightType!=="oneWay" && !formData.returnDate) {
    errorModal.value.showModal('Please select a return date.');
    return;
  }

  if (!formData.flightClass) {
    errorModal.value.showModal('Please select a flight class');
    return;
  }



  // If no errors, continue the form submission
  alert(JSON.stringify(formData, null, 2));
  document.querySelector('#flightList').classList.remove('d-none')
};
</script>

<style scoped>
.container {
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
</style>
