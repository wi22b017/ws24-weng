<template>
  <AtomModal :visible="isVisible" id="registerModal" labelledby="registerModalLabel" @close="isVisible = false">
    <template #header>
      <div class="modal-header">
        <h5 class="modal-title" id="registerModalLabel">Registration</h5>
        <button type="button" class="btn-close" @click="isVisible = false" aria-label="Close"></button>
      </div>
    </template>
    <template #body>
    <div class="modal-body">
      <MoleculeRegisterForm @registration-success="handleRegistrationSuccess" />
    </div>
    </template>
  </AtomModal>
</template>

<script setup>
import { ref, defineExpose } from 'vue';
import AtomModal from '@/components/atoms/AtomModal.vue';
import MoleculeRegisterForm from '@/components/molecules/MoleculeRegisterForm.vue';

const isVisible = ref(false);

const handleRegistrationSuccess = () => {
  hideModal();
};

// Define a method to show the modal and expose it to parent components
const showModal = () => {
  isVisible.value = true;
};

// Hide modal method
const hideModal = () => {
  isVisible.value = false;
};

// Expose the showModal method
defineExpose({
  showModal,
  hideModal,
});
</script>
