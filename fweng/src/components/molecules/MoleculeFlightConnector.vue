<template>
  <div class="flight-connector">
    <div class="stop-info">
      <span>{{ stopText }}</span>
    </div>
  </div>
</template>

<script setup>
import { defineProps, computed } from "vue";

const props = defineProps({
  stops: {
    type: Number,
    required: true,
    default: 0,
  },
});

const stopText = computed(() => {
  if (props.stops === 0) {
    return "Direct";
  } else if (props.stops === 1) {
    return "1 stop";
  } else {
    return `${props.stops} stops`;
  }
});
</script>

<style scoped>
.flight-connector {
  border-top: 3px solid #bbb;
  width: 100%;
  height: 0;
  position: relative;
  top: -22px;
}

.stop-info {
  position: absolute;
  top: -18px; /* Position the box above the line */
  left: 50%; /* Move to the horizontal center */
  transform: translateX(-50%); /* Center the box */
  background-color: #fff; /* White background for the box */
  border-radius: 50px; /* Rounded box */
  padding: 4px 10px; /* Padding inside the box */
  border: 1px solid #bbb; /* Optional: border around the box */
  font-size: 14px; /* Text size */
  z-index: 2; /* Ensures the box appears above the line */
}
</style>
