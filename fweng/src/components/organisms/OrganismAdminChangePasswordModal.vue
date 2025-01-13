<template>
  <AtomModal :visible="isVisible" id="adminChangePasswordModal" labelledby="adminChangePasswordModalLabel" @close="isVisible = false">
    <template #header>
      <div class="modal-header">
        <h5 class="modal-title" id="adminChangePasswordModalLabel">Change Password</h5>
        <button type="button" class="btn-close" @click="isVisible = false" aria-label="Close"></button>
      </div>
    </template>
    <template #body>
      <div class="modal-body">
        <MoleculeAdminChangeUserPasswordForm :currentUser = "currentUser"/>
      </div>
    </template>
  </AtomModal>
</template>

<script setup>

import AtomModal from "@/components/atoms/AtomModal.vue";
import {defineExpose, ref, defineProps} from "vue";
import MoleculeAdminChangeUserPasswordForm from "@/components/molecules/MoleculeAdminChangeUserPasswordForm.vue";

const isVisible = ref(false);

defineProps({
  currentUser: {
    type: Object,
    required: true,
  },
})

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