<template>
  <p class="app-text">{{ content }}</p>
</template>

<script setup>
import { defineProps } from 'vue';

defineProps({
  content: {
    type: String,
    required: true,
  },
});
</script>
