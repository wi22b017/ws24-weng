<template>
  <div class="icon-text">
    <FontAwesomeIcon :icon="icon" />
    <span>{{ text }}</span>
  </div>
</template>

<script setup>
import { defineProps } from "vue";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

defineProps({
  icon: {
    type: Object,
    required: true,
  },
  text: {
    type: String,
    required: true,
  },
});
</script>

<style scoped>
.icon-text {
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>
