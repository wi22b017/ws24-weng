<template>
  <div v-if="visible" class="modal fade show" :id="id" tabindex="-1" role="dialog" :aria-labelledby="labelledby" :aria-hidden="visible ? 'false' : 'true'" style="display: block;">
    <div class="modal-dialog">
      <div class="modal-content">
        <slot name="header" />
        <slot name="body" />
        <slot name="footer" />
      </div>
    </div>
  </div>
  <!-- Move backdrop outside the modal content -->
  <div v-if="visible" class="modal-backdrop fade show" @click="closeModal"></div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';

defineProps({
  id: String,
  labelledby: String,
  visible: Boolean,
});

const emit = defineEmits(['close']);

const closeModal = () => {
  emit('close');
};
</script>

<style scoped>
.modal {
  display: none;
}

.modal.show {
  display: block;
}

.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1040; /* Ensure it overlays content */
}
</style>
