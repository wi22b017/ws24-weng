<template>
  <div class="passenger-form">
    <h3>Passenger {{ index + 1 }}</h3>
    <div>
      <label for="firstName">First Name</label>
      <input type="text" v-model="localPassenger.firstName" id="firstName" />
    </div>
    <div>
      <label for="lastName">Last Name</label>
      <input type="text" v-model="localPassenger.lastName" id="lastName" />
    </div>
    <div>
      <label for="birthday">Birthday</label>
      <input type="date" v-model="localPassenger.birthday" id="birthday" />
    </div>
    <div>
      <label for="baggage">Baggage</label>
      <select v-model="localPassenger.baggage.baggageTypeId" id="baggage">
        <option v-for="baggage in baggageTypes" :key="baggage.id" :value="baggage.id">
          {{ baggage.label }}
        </option>
      </select>
    </div>
    <div>
      <label for="seatNumber">Seat Number</label>
      <input type="text" v-model="localPassenger.seatNumber" id="seatNumber" />
    </div>
  </div>
</template>

<script setup>
import { reactive, watch, defineProps, defineEmits } from "vue";

const props = defineProps({
  passenger: {
    type: Object,
    required: true,
  },
  index: {
    type: Number,
    required: true,
  },
  baggageTypes: {
    type: Array,
    required: true,
  },
});

const emit = defineEmits(["updatePassenger"]);

const localPassenger = reactive({...props.passenger});

watch(localPassenger, (newValue) => {
  emit("updatePassenger", newValue);
}, {deep: true});
</script>

<style scoped>
.passenger-form {
  margin-bottom: 1rem;
}
</style>
