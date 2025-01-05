<template>
  <div class="container mt-4">
    <AtomHeading text="Manage Flights" />

    <!-- Add Flight Button -->
    <div class="text-center mb-4">
      <AtomButton
          label="Add Flight"
          @click="showAddFlightModal"
          class="btn-success add-flight-button"
      />
    </div>

    <!-- Flight List Table -->
    <MoleculeFlightTable
        v-if="adminUserStore.flights.length > 0"
        :key="tableKey"
        :flights="adminUserStore.flights"
        @edit="onEditFlight"
        @delete="onDeleteFlight"
    />

    <!-- Error Message -->
    <div v-if="fetchFlightError" class="alert alert-danger mt-3" role="alert">
      {{ fetchFlightError }}
    </div>

    <!-- Add Flight Modal -->
    <OrganismAddFlightModal ref="addFlightModal" @flightAdded="refreshFlights" />

    <!-- Edit Flight Modal -->
    <OrganismEditFlightModal ref="editFlightModal" :flight="currentFlight" @flightUpdated="refreshFlights" />
  </div>
</template>

<script setup>
import {ref, onMounted, provide} from "vue";
import {useAdminUserStore} from "@/store/adminUserStore";
import MoleculeFlightTable from "@/components/molecules/MoleculeFlightTable.vue";
import AtomHeading from "@/components/atoms/AtomHeading.vue";
import AtomButton from "@/components/atoms/AtomButton.vue";
import OrganismAddFlightModal from "@/components/organisms/OrganismAddFlightModal.vue";
import OrganismEditFlightModal from "@/components/organisms/OrganismEditFlightModal.vue";

const adminUserStore = useAdminUserStore();
const fetchFlightError = ref(""); // State for flight fetch errors
const tableKey = ref(0); // Key to force re-render of the table

// Add and edit flight modal references
const addFlightModal = ref(null);
const editFlightModal = ref(null);
const currentFlight = ref({}); // Holds the flight being edited

const hideEditFlightModal = () => {
  editFlightModal.value.hideModal();
};
// Provide this method to children components
provide('hideEditFlightModal', hideEditFlightModal);

const onEditFlight = (flight) => {
  currentFlight.value = flight; // Set the current flight data
  editFlightModal.value.showModal(); // Open the modal
};

const onDeleteFlight = async (flightId) => {
  const result = await adminUserStore.deleteFlight(flightId);
  if (!result.success) {
    fetchFlightError.value = result.message;
  } else {
    refreshFlights();
  }
};

// Refresh flights and handle errors
const refreshFlights = async () => {
  try {
    const result = await adminUserStore.fetchFlights();
    if (!result.success) {
      fetchFlightError.value = result.message || "Error fetching flights.";
    } else {
      tableKey.value++; // Increment key to force table re-render
      fetchFlightError.value = ""; // Clear previous errors
    }
  } catch (error) {
    fetchFlightError.value = error.message || "An unexpected error occurred while fetching flights.";
  }
};

// Fetch flights when the component is mounted
onMounted(() => {
  refreshFlights();
});

// Show the Add Flight Modal
const showAddFlightModal = () => {
  addFlightModal.value.showModal();
};
</script>

<style scoped>
.add-flight-button {
  margin-top: 1rem;
}
</style>
