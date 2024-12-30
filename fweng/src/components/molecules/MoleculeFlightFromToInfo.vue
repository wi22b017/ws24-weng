<template>
  <div
    class="flight-time-airport"
    :class="[flightInfo.type === 'departure' ? 'flight-departure' : 'flight-arrival',
    ]"
  >
    <div class="flight-time">
      <time>{{ flightInfo.time }}</time>
    </div>
    <div class="flight-airport-code">
      <span>{{ flightInfo.airportCode }}</span>
    </div>
    <div v-if="flightInfo.terminal" class="flight-terminal">
      <span>Terminal {{ flightInfo.terminal }}</span>
    </div>
  </div>
</template>

<script setup>
import { defineProps } from "vue";

defineProps({
  flightInfo: {
    type: Object,
    required: true,
  }
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
