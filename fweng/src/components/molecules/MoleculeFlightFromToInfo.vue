<template>
  <div
    class="flight-time-airport"
    :class="[
      flightInfoType,
      type == 'departure' ? 'flight-departure' : 'flight-arrival',
    ]"
  >
    <div class="flight-time">
      <time>{{ time }}</time>
    </div>
    <div class="flight-airport-code">
      <span>{{ airportCode }}</span>
    </div>
    <div v-if="terminal" class="flight-terminal">
      <span>Terminal {{ terminal }}</span>
    </div>
  </div>
</template>

<script setup>
import { defineProps } from "vue";

defineProps({
  time: {
    type: String,
    required: true,
  },
  airportCode: {
    type: String,
    required: true,
  },
  terminal: {
    type: String,
    required: false,
  },
  type: {
    type: String,
    required: true,
    validator: (value) => ["departure", "arrival"].includes(value), // Validate type
  },
});
</script>

<style scoped>
.flight-time-airport {
  display: flex;
  flex-direction: column;
  align-items: start;
  gap: 2px;
}

.flight-time {
  font-weight: bold;
  font-size: 20px;
  color: black;
}

.flight-airport-code {
  font-size: 16px;
  color: black;
}
.flight-terminal {
  font-size: 14px;
  color: black;
  text-wrap: nowrap;
}
</style>
