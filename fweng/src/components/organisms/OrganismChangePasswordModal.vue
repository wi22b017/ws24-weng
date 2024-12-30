<template>
  <AtomModal :visible="isVisible" id="changePasswordModal" labelledby="changePasswordModalLabel" @close="isVisible = false">
    <template #header>
      <div class="modal-header">
        <h5 class="modal-title" id="changePasswordModalLabel">Change Password</h5>
        <button type="button" class="btn-close" @click="isVisible = false" aria-label="Close"></button>
      </div>
    </template>
    <template #body>
      <div class="modal-body">
        <MoleculeChangePasswordForm/>
      </div>
    </template>
  </AtomModal>
</template>

<script setup>

import AtomModal from "@/components/atoms/AtomModal.vue";
import MoleculeChangePasswordForm from "@/components/molecules/MoleculeChangePasswordForm.vue";
import {defineExpose, ref} from "vue";

const isVisible = ref(false);

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


<style scoped>

</style>