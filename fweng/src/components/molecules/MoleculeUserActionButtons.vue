<template>
  <div class="d-flex justify-content-center gap-2">
    <button class="btn btn-primary btn-sm" @click="onToggle">Toggle Status</button>
    <button class="btn btn-danger btn-sm" @click="onDelete">Delete</button>
  </div>
</template>

<script setup>
import { defineProps } from "vue";

defineProps({
  onToggle: {
    type: Function,
    required: true,
  },
  onDelete: {
    type: Function,
    required: true,
  },
});
</script>

<style>
.d-flex {
  display: flex;
  gap: 0.5rem;
}
</style>
