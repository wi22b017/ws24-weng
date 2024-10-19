<template>
  <h2 class="app-heading">{{ text }}</h2>
</template>

<script setup>
import { defineProps } from 'vue';

defineProps({
  text: {
    type: String,
    required: true,
  },
});
</script>

<style scoped>
.app-heading {
  font-size: 1.5rem;
  margin-bottom: 1rem;
}
</style>
