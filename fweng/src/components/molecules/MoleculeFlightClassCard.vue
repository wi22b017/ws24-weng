<template>
  <div class="flight-class-card" :class="flightClassColor">
    <div class="flight-class-name">
      <span>{{ flightClassName }}</span>
    </div>
    <div class="flight-price">
      <p>from</p>
      <p>{{ currency }}</p>
      <p class="fw-bold flight-price-number">{{ formattedPrice }}</p>
    </div>
    <p>per adult</p>
    <font-awesome-icon :icon="faChevronDown" class="mb-2" />
  </div>
</template>

<script setup>
import { faChevronDown } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import { defineProps, computed } from "vue";

const props = defineProps({
  flightClassName: {
    type: String,
    required: true,
    validator: (value) => flightClassNames.includes(value),
  },
  price: {
    type: Number,
    required: true,
  },
  currency: {
    type: String,
    default: "EUR",
  },
});

// Format the price using Intl.NumberFormat
const formattedPrice = computed(() => {
  return new Intl.NumberFormat("en-US", {
    style: "decimal",
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  }).format(props.price);
});

const flightClassColor = computed(() => {
  switch (props.flightClassName) {
    case "Economy":
      return "economy-card";
    case "Business":
      return "business-card";
    case "First":
      return "first-card";
    default:
      return "economy-card";
  }
});
</script>

<script>
const flightClassNames = ["Economy", "Business", "First"];
</script>

<style scoped>
.flight-class-card {
  display: flex;
  flex-direction: column;
  justify-content: start;
  border: 1px solid;
  min-width: 0;
  height: 100%;
}

.flight-class-name {
  font-weight: bold;
  font-size: 18px;
  margin-top: 5px;
}

.flight-price {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 3px;
  margin-top: 5px;
  margin-bottom: 5px;
}

.flight-price p {
  margin: 0;
}

.flight-price-number {
  font-size: 20px;
}
.economy-card {
  color: green;
}
.business-card {
  color: #014f30;
}
.first-card {
  color: darkblue;
}
</style>
