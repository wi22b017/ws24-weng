<template>
  <!-- Reuse AtomModal for the error modal -->
  <AtomModal
      :visible="isVisible"
      id="errorModal"
      labelledby="errorModalLabel"
      @close="isVisible = false"
  >
    <!-- Header slot -->
    <template #header>
      <div class="modal-header bg-danger text-white">
        <!-- Warning icon -->
        <i class="bi bi-exclamation-triangle-fill me-2"></i>
        <h5 class="modal-title" id="errorModalLabel">Error</h5>
        <button type="button" class="btn-close" @click="isVisible = false" aria-label="Close"></button>
      </div>
    </template>

    <!-- Body slot for displaying the error message -->
    <template #body>
      <div class="modal-body">
        <p>{{ errorMessage }}</p>
      </div>
    </template>

    <!-- Footer slot (optional) -->
    <template #footer>
      <div class="modal-footer">
        <button type="button" class="btn btn-danger" @click="isVisible = false">
          Close
        </button>
      </div>
    </template>
  </AtomModal>
</template>

<script setup>
import { ref, defineExpose } from 'vue';
import AtomModal from '@/components/atoms/AtomModal.vue';

// Error message to display
const errorMessage = ref('');

// Visibility control for the modal
const isVisible = ref(false);

// Function to show the modal and pass the error message
const showModal = (message) => {
  errorMessage.value = message; // Set the error message
  isVisible.value = true; // Show the modal
};

// Function to hide the modal
const hideModal = () => {
  isVisible.value = false;
};

// Expose methods to parent component
defineExpose({
  showModal,
  hideModal,
});
</script>

<style scoped>
</style>/
