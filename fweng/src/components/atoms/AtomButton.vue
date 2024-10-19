<template>
  <button
      :type="type"
      :disabled="disabled"
      @click="onClick"
      :class="computedClasses"
      :style="attrs.style"
  >{{ label }}
    <slot />
  </button>
</template>

<script setup>
import { defineProps, computed, useAttrs } from 'vue';

const props = defineProps({
  label: {
    type: String,
    required: true,
  },
  type: {
    type: String,
    default: 'button',
  },
  disabled: Boolean,
  onClick: Function,
  fullWidth: {
    type: Boolean,
    default: true,
  },
});

const attrs = useAttrs();

const computedClasses = computed(() => {
  const classes = ['btn', 'btn-primary', 'app-button'];
  if (props.fullWidth) {
    classes.push('w-100');
  }
  if (attrs.class) {
    classes.push(attrs.class);
  }
  return classes;
});
</script>

<style>
.app-button {
  padding: 0.5rem 1rem;
  border: none;
  cursor: pointer;
  border-radius: 4px;
}
</style>
