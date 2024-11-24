<template>
  <span :class="badgeClass">{{ status }}</span>
</template>

<script setup>
import { defineProps, computed } from "vue";

defineProps({
  status: {
    type: String,
    required: true,
  },
});

const badgeClass = computed(() =>
    status === "ACTIVE" ? "badge bg-success" : "badge bg-danger"
);
</script>

<style>
.badge {
  padding: 0.5rem 1rem;
  font-size: 0.9rem;
  border-radius: 0.5rem;
  color: white;
}
</style>
