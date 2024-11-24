<template>
  <form @submit.prevent="onSubmit">
    <div class="row g-3 align-items-center">


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

  cityOptions: {
    type: Array,
    required: true,
  },

});

const emit = defineEmits(['submit']);


const departureCity = ref('');
const arrivalCity = ref('');
const departureDate = ref('');





// Handle form submission
const onSubmit = () => {
  emit('submit', {
    departureCity: departureCity.value,
    arrivalCity: arrivalCity.value,
    departureDate: departureDate.value,
  });
};
</script>
