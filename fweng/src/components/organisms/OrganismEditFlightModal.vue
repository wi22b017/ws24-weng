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
          :initial-values="props.flight"
      />
      <div v-if="errorMessage" class="alert alert-danger mt-3" role="alert">
        {{ errorMessage }}
      </div>
    </template>
  </AtomModal>
</template>

<script setup>
import { ref, defineExpose, defineProps } from "vue";
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

// Show modal
const showModal = () => {
  isVisible.value = true; // Open modal after flight data is ready
};

// Hide modal
const hideModal = () => {
  isVisible.value = false;
};

// Expose methods to parent components
defineExpose({ showModal, hideModal });
</script>

<style scoped>
.alert {
  margin-top: 1rem;
}
</style>
