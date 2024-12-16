<template>
  <div class="container">
    <h3>Passenger {{ index + 1 }}</h3>
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
  </div>
</template>

<script setup>
import {watch, defineProps, defineEmits, onMounted, reactive, ref} from "vue";
import {Form} from "vee-validate";
import AtomInput from "@/components/atoms/AtomInput.vue";

import apiClient from "@/utils/axiosClient";
import AtomFormSelect from "@/components/atoms/AtomFormSelect.vue";
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
      .matches(
          /^\d{4}-\d{2}-\d{2}$/,
          "Date of birth must be in the format YYYY-MM-DD"
      ),
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



/*const localPassenger = reactive({ ...props.passenger });

const formData = vueRef({
  firstName: localPassenger.firstName,
  lastName: localPassenger.lastName,
  dateOfBirth: localPassenger.dateOfBirth,
  baggageType:"",
  seatNumber: "",
});*/

// CHANGE: Create a local formData reactive object based on the passenger prop
//const formData = reactive({ ...props.passenger });

const formData = reactive({
  firstName: props.passenger.firstName || "",
  lastName: props.passenger.lastName || "",
  dateOfBirth: props.passenger.dateOfBirth || "",
  baggage: {
    baggageTypeId: props.passenger.baggage?.baggageTypeId || "",
  },
  seatNumber: props.passenger.seatNumber || "",
});


const emit = defineEmits(["updatePassenger"]);

watch(formData, (newValue) => {
  emit("updatePassenger", newValue);
}, {deep: true});

async function getBaggageTypes(){
  try {
    const baggageTypesOptionsResponse = await apiClient.get(`/baggageTypes`);
    // Map API response to the structure required by AtomFormSelect
    baggageTypesOptions.value = baggageTypesOptionsResponse.data.map((baggageType) => ({
      value: baggageType.id,
      text: `${baggageType.name} (${baggageType.fee.toFixed(2)} €)`,
    }));
  } catch (error) {
    console.error("Failed to fetch baggage types:", error);
  }
}

onMounted(async () => {
  await getBaggageTypes();
});

</script>

<style scoped>
</style>
