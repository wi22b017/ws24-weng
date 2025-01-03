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
        <MoleculeAddFlightForm @submit="onSubmit" />
        <div v-if="errorMessage" class="alert alert-danger mt-3" role="alert">
          {{ errorMessage }}
        </div>
      </div>
    </template>
  </AtomModal>
</template>

<script setup>
import { ref, defineExpose, defineEmits } from "vue";
import AtomModal from "@/components/atoms/AtomModal.vue";
import MoleculeAddFlightForm from "@/components/molecules/MoleculeAddFlightForm.vue";
import { useAdminUserStore } from "@/store/adminUserStore";

const isVisible = ref(false);
const errorMessage = ref("");
const adminUserStore = useAdminUserStore();
const emit = defineEmits(["flightAdded"]); // Emit event when flight is added

const showModal = () => {
  isVisible.value = true;
  errorMessage.value = ""; // Clear any previous error messages
};

const hideModal = () => {
  isVisible.value = false;
};

defineExpose({
  showModal,
  hideModal,
});

const onSubmit = async (formData) => {
  console.log("Add Flight Form Submitted:", formData);

  const payload = {
    flightNumber: formData.flightNumber,
    departureTime: formData.departureTime,
    arrivalTime: formData.arrivalTime,
    price: formData.price,
    aircraft: {
      id: formData.aircraftId,
    },
    flightOrigin: {
      code: formData.flightOrigin,
    },
    flightDestination: {
      code: formData.flightDestination,
    },
  };

  try {
    const result = await adminUserStore.addFlight(payload);
    if (result.success) {
      console.log(result.message);
      isVisible.value = false; // Close modal on success
      errorMessage.value = ""; // Clear error messages
      emit("flightAdded"); // Notify parent that flight was added
    } else {
      errorMessage.value = result.message;
    }
  } catch (error) {
    console.error("Error adding flight:", error);
    errorMessage.value = error.response?.data?.error || "An error occurred while adding the flight.";
  }
};
</script>
