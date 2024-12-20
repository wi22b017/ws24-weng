<template>
  <h3 class="text-center">Passenger {{ index + 1 }}</h3>
  <Form
      :validation-schema="passengerFormSchema"
      v-model="formData"
      :initial-values="formData"
  >
    <AtomInput
        label="First Name"
        name="firstName"
        id="firstName"
        v-model="formData.firstName"
    />
    <AtomInput
        label="Last Name"
        name="lastName"
        id="lastName"
        v-model="formData.lastName"
    />
    <AtomInput
        type="date"
        label="Date of Birth"
        name="dateOfBirth"
        id="dateOfBirth"
        v-model="formData.dateOfBirth"
    />
    <AtomFormSelect
        label="Baggage Type"
        name="baggageType"
        id="baggageType"
        placeholder="Select a baggage type"
        v-model="formData.baggage.baggageTypeId"
        :options="flightStore.baggageTypesOptions"
    />
    <AtomInput
        label="Preferred Seat Number"
        name="seatNumber"
        id="seatNumber"
        v-model="formData.seatNumber"
    />
  </Form>
</template>

<script setup>
/* eslint-disable */
import {watch, defineProps, defineEmits, reactive, ref, onMounted} from "vue";
import { Form } from "vee-validate";
import AtomInput from "@/components/atoms/AtomInput.vue";
import AtomFormSelect from "@/components/atoms/AtomFormSelect.vue";
import {object, string} from "yup";
import { useFlightStore } from '@/store/flight';

const passengerFormSchema = object({
  firstName: string()
      .required("First name is required")
      .matches(/^[a-zA-Z'\-\s]*$/, "Invalid name"),
  lastName: string()
      .required("Last name is required")
      .matches(/^[a-zA-Z'\-\s]*$/, "Invalid name"),
  dateOfBirth: string()
      .required("Date of birth is required")
      .matches(/^\d{4}-\d{2}-\d{2}$/, "Date of birth must be in the format YYYY-MM-DD"),
  baggageType: string().required("Baggage type is required"),
});

const props = defineProps({
  passenger: {
    type: Object,
    required: true,
  },
  index: {
    type: Number,
    required: true,
  },
});

const emit = defineEmits(["updatePassenger"]);
const flightStore = useFlightStore();

const formData = reactive({
  firstName: props.passenger.firstName || "",
  lastName: props.passenger.lastName || "",
  dateOfBirth: props.passenger.dateOfBirth || "",
  baggage: {
    baggageTypeId: props.passenger.baggage.baggageTypeId || "",
  },
  seatNumber: props.passenger.seatNumber || "",
});

formData.baggageType = flightStore.defaultBaggageType;

const errors = ref({
  firstName: "",
  lastName: "",
  dateOfBirth: "",
  baggageType: "",
  seatNumber: "",
});

watch(
    () => formData,
    async (newValues) => {
      try {
        // Validate the entire form
        await passengerFormSchema.validate(newValues, { abortEarly: false });

        // Clear all errors if validation succeeds
        Object.keys(errors.value).forEach((field) => {
          errors.value[field] = "";
        });

        // Emit the updated passenger data to the parent
        emit("updatePassenger", { ...formData });
      } catch (validationError) {
        // Populate errors for all invalid fields
        validationError.inner.forEach((err) => {
          errors.value[err.path] = err.message;
        });
      }
    },
    { deep: true }
);


onMounted(async () => {
  await flightStore.fetchBaggageTypes();
});
</script>

<style scoped>
.text-center{
  text-align: center;
}
</style>
