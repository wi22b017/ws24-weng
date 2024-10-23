<template>
  <form @submit.prevent="onSubmit">
    <div class="row g-3 align-items-center">
      <!-- Flight Type (no use of AtomInput, because of input type radio) -->
      <div class="col-md-12 d-flex align-items-center">
        <div
            class="form-check me-3"
            v-for="type in flightTypes"
            :key="type.value"
        >
          <input
              class="form-check-input"
              type="radio"
              v-model="flightType"
              :value="type.value"
              :id="type.value"
          />
          <label class="form-check-label" :for="type.value">
            {{ type.text }}
          </label>
        </div>
      </div>

      <!-- Departure City -->
      <div class="col-md-4">
        <AtomFormSelect
            v-model="departureCity"
            id="departureCity"
            :options="cityOptions"
            label="From"
            name="departureCity"
            placeholder="Select city"
        />
      </div>

      <!-- Arrival City -->
      <div class="col-md-4">
        <AtomFormSelect
            v-model="arrivalCity"
            id="arrivalCity"
            :options="cityOptions"
            label="To"
            name="arrivalCity"
            placeholder="Select city"
        />
      </div>

      <!-- Departure Date -->
      <div class="col-md-4">
      <AtomInput
          v-model="departureDate"
          id="departureDate"
          type="date"
          label="Departure Date"
          name="departureDate"
      />
      </div>

      <!-- Return Date -->
      <div class="col-md-4" v-if="flightType === 'roundTrip'">
        <AtomInput
            v-model="returnDate"
            id="returnDate"
            type="date"
            label="Return Date"
            name="returnDate"
        />
      </div>

      <!-- Passengers -->
      <div class="col-md-4">
        <AtomInput
            v-model="passengers"
            id="passengers"
            type="number"
            label="Passengers"
            name="passengers"
            min="1"
            placeholder="1"
        />
      </div>

      <!-- Class -->
      <div class="col-md-4">
        <AtomFormSelect
            v-model="flightClass"
            id="flightClass"
            :options="flightClassOptions"
            label="Flight Class"
            name="flightClass"
            placeholder="Select a class"
        />
      </div>

      <!-- Direct Flights Checkbox (no use of AtomInput, because of input type checkbox) -->
      <div class="col-md-4 d-flex align-items-center">
        <input
            type="checkbox"
            v-model="directFlights"
            id="directFlights"
            class="form-check-input me-2"
        />
        <label for="directFlights" class="form-check-label">Direct flights only</label>
      </div>

      <div class="col-md text-end">
      <AtomButton label="Search Flights" type="submit" :fullWidth="false" />
      </div>

    </div>

  </form>
</template>

<script setup>
import { ref } from 'vue';
import { defineProps, defineEmits } from 'vue';
import AtomFormSelect from "@/components/atoms/AtomFormSelect.vue";
import AtomButton from "@/components/atoms/AtomButton.vue";
import AtomInput from "@/components/atoms/AtomInput.vue";

defineProps({
  flightTypes: {
    type: Array,
    required: true,
  },
  cityOptions: {
    type: Array,
    required: true,
  },
  flightClassOptions: {
    type: Array,
    required: true,
  },
});

const emit = defineEmits(['submit']);

const flightType = ref('roundTrip');
const departureCity = ref('');
const arrivalCity = ref('');
const departureDate = ref('');
const returnDate = ref('');
const passengers = ref(1);
const flightClass = ref('');
const directFlights = ref(false);


// Handle form submission
const onSubmit = () => {
  emit('submit', {
    departureCity: departureCity.value,
    arrivalCity: arrivalCity.value,
    flightType: flightType.value,
    departureDate: departureDate.value,
    returnDate: returnDate.value,
    passengers: passengers.value,
    flightClass: flightClass.value,
    directFlights: directFlights.value,
  });
};
</script>
