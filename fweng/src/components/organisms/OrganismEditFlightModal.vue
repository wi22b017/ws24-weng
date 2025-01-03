<template>
  <AtomModal
      :visible="isVisible"
      id="editFlightModal"
      labelledby="editFlightModalLabel"
      @close="hideModal"
  >
    <template #header>
      <div class="modal-header">
        <h5 class="modal-title" id="editFlightModalLabel">Edit Flight</h5>
        <button type="button" class="btn-close" @click="hideModal" aria-label="Close"></button>
      </div>
    </template>
    <template #body>
      <MoleculeEditFlightForm
          :initial-values="flight"
          @submit="onSubmit"
      />
      <div v-if="errorMessage" class="alert alert-danger mt-3" role="alert">
        {{ errorMessage }}
      </div>
    </template>
  </AtomModal>
</template>

<script setup>
import { ref, watch, defineExpose, defineProps, defineEmits, nextTick } from "vue";
import AtomModal from "@/components/atoms/AtomModal.vue";
import MoleculeEditFlightForm from "@/components/molecules/MoleculeEditFlightForm.vue";

// Define props
const props = defineProps({
  flight: {
    type: Object,
    required: true,
    default: () => ({}), // Provide default value for safety
  },
});

// Modal state and error handling
const isVisible = ref(false);
const errorMessage = ref("");

// Define emit events
const emit = defineEmits(["flightUpdated"]);

// Watch for changes in the flight prop
watch(
    () => props.flight,
    (newFlight) => {
      console.log("Flight prop updated in modal:", newFlight); // Log the flight prop update
    },
    { immediate: true }
);

// Show modal with delay to ensure props.flight is ready
const showModal = async () => {
  console.log("Opening modal with flight data:", props.flight);

  if (!props.flight || Object.keys(props.flight).length === 0) {
    console.log("Waiting for flight data...");
    await nextTick();
  }

  if (!props.flight || Object.keys(props.flight).length === 0) {
    console.error("Flight data is still not ready. Modal will not be shown.");
    return;
  }

  console.log("Modal is ready to open with flight data:", props.flight);
  isVisible.value = true; // Open modal after flight data is ready
};


// Hide modal
const hideModal = () => {
  isVisible.value = false;
  errorMessage.value = "";
  console.log("Closing modal");
};

// Handle form submission
const onSubmit = async (formData) => {
  try {
    console.log("Form submitted with data:", formData);
    emit("flightUpdated", { id: props.flight.id, ...formData });
    hideModal();
  } catch (error) {
    console.error("Error during form submission:", error);
    errorMessage.value = error.message || "An error occurred while updating the flight.";
  }
};

// Expose methods to parent components
defineExpose({ showModal, hideModal });
</script>


<style scoped>
.alert {
  margin-top: 1rem;
}
</style>
