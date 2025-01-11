<template>
  <AtomModal :visible="isVisible" id="addFlightModal" labelledby="addFlightModalLabel" @close="isVisible = false">
    <template #header>
      <div class="modal-header">
        <h5 class="modal-title" id="addFlightModalLabel">Add Flight</h5>
        <button type="button" class="btn-close" @click="isVisible = false" aria-label="Close"></button>
      </div>
    </template>
    <template #body>
      <div class="modal-body">
        <MoleculeAddFlightForm />
      </div>
    </template>
  </AtomModal>
</template>

<script setup>
import { ref, defineExpose } from "vue";
import AtomModal from "@/components/atoms/AtomModal.vue";
import MoleculeAddFlightForm from "@/components/molecules/MoleculeAddFlightForm.vue";

const isVisible = ref(false);

const showModal = () => {
  isVisible.value = true;
};

const hideModal = () => {
  isVisible.value = false;
};

defineExpose({
  showModal,
  hideModal,
});
</script>
