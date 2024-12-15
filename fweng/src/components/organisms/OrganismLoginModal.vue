<template>
  <AtomModal :visible="isVisible" id="loginModal" labelledby="loginModalLabel" @close="isVisible = false">
    <template #header>
      <div class="modal-header">
        <h5 class="modal-title" id="loginModalLabel">Login</h5>
        <button type="button" class="btn-close" @click="isVisible = false" aria-label="Close"></button>
      </div>
    </template>
    <template #body>
      <div class="modal-body">
        <MoleculeLoginForm :shouldForwardToFlightDetail="shouldForwardToFlightDetail"/>
      </div>
    </template>
  </AtomModal>
</template>

<script setup>
import { ref, defineExpose, defineProps } from 'vue';
import AtomModal from '@/components/atoms/AtomModal.vue';
import MoleculeLoginForm from '@/components/molecules/MoleculeLoginForm.vue';
import router from "@/router";

const props = defineProps({
  shouldForwardToFlightDetail: {
    type: Boolean,
    required: false,
  },
});

const isVisible = ref(false);

// Define a method to show the modal and expose it to parent components
const showModal = () => {
  isVisible.value = true;
};

// Hide modal method
const hideModal = () => {
  isVisible.value = false;
  if(props.shouldForwardToFlightDetail===true){
    router.push({name: 'flightDetail'});
  }
};

// Hide modal method without possible redirection
const hideModalWithoutRedirection = () => {
  isVisible.value = false;
};

// Expose the showModal method
defineExpose({
  showModal,
  hideModal,
  hideModalWithoutRedirection,
});
</script>
