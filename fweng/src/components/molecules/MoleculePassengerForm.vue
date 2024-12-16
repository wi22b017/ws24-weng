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
        :options="baggageTypesOptions"
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
import {watch, defineProps, defineEmits, reactive, ref, onMounted} from "vue";
import { Form } from "vee-validate";
import AtomInput from "@/components/atoms/AtomInput.vue";
import AtomFormSelect from "@/components/atoms/AtomFormSelect.vue";
import apiClient from "@/utils/axiosClient";
import {object, string} from "yup";

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

const baggageTypesOptions = ref([]);

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

const formData = reactive({...props.passenger});

watch(formData, (newValue) => {
  emit("updatePassenger", newValue);
}, {deep: true});

async function getBaggageTypes() {
  try {
    const baggageTypesOptionsResponse = await apiClient.get(`/baggageTypes`);
    baggageTypesOptions.value = baggageTypesOptionsResponse.data.map((baggageType) => ({
      value: baggageType.id,
      text: `${baggageType.name} (${baggageType.fee.toFixed(2)} €)`,
      originalName: baggageType.name, // Keep the original name to detect hand luggage
    }));

    // Set default baggage type to "Hand Luggage" if available
    const handLuggageOption = baggageTypesOptions.value.find(
        option => option.originalName.toLowerCase().includes("hand luggage")
    );
    if (handLuggageOption && !formData.baggage.baggageTypeId) {
      formData.baggage.baggageTypeId = handLuggageOption.value;
    }
  } catch (error) {
    console.error("Failed to fetch baggage types:", error);
  }
}

onMounted(async () => {
  await getBaggageTypes();
});
</script>

<style scoped>
.text-center{
  text-align: center;
}
</style>
