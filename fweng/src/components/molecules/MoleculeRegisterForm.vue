<template>
  <div class="container">
    <Form
      :validation-schema="registerFormSchema"
      v-model="formData"
      @submit="onSubmit"
      :initial-values="formData"
    >
      <AtomFormSelect
        label="Salutation"
        name="salutation"
        id="salutation"
        placeholder="Select salutation"
        v-model="formData.salutation"
        :options="[
          { value: 'male', text: 'Male' },
          { value: 'female', text: 'Female' },
          { value: 'other', text: 'Other' },
        ]"
      />
      <AtomInput
        v-if="formData.salutation === 'other'"
        label="Other Salutation"
        name="otherSalutation"
        id="otherSalutation"
        v-model="formData.otherSalutation"
      />
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
      <AtomInput
          label="Address Line 1"
          name="address.street"
          id="street"
          placeholder="Street address"
          v-model="formData.address.street"
      />
      <AtomInput
          type="number"
          label="Address Line 2"
          name="address.number"
          id="streetNumber"
          placeholder="Building number"
          v-model="formData.address.number"
      />
      <AtomInput
          label="City"
          name="address.city"
          id="city"
          v-model="formData.address.city"
      />
      <AtomFormSelect
          label="Country"
          name="address.country"
          id="country"
          placeholder="Select country"
          v-model="formData.address.country"
          :optGroups="[
          {
            label: 'DACH Countries',
            options: [
              { value: 'DE', text: 'Germany' },
              { value: 'AT', text: 'Austria' },
              { value: 'CH', text: 'Switzerland' },
            ],
          },
          {
            label: 'Other Countries',
            options: otherCountriesOptions,
          },
        ]"
      />
      <AtomInput
          type="number"
          label="Zip Code"
          name="address.zip"
          id="zip"
          v-model="formData.address.zip"
      />
      <AtomInput
        label="Email"
        name="email"
        id="email"
        type="email"
        v-model="formData.email"
      />
      <AtomFormSelect
          label="Payment Method"
          name="paymentMethod"
          id="paymentMethod"
          placeholder="Select a payment method"
          v-model="formData.paymentMethod"
          :options="paymentMethodOptions"
      />
      <AtomInput
        label="Username"
        name="username"
        id="username"
        v-model="formData.username"
      />
      <AtomInput
        label="Password"
        name="password"
        id="password"
        type="password"
        v-model="formData.password"
      />
      <AtomInput
        label="Confirm Password"
        name="confirmPassword"
        id="confirmPassword"
        type="password"
        v-model="formData.confirmPassword"
      />
      <Input
          type="hidden"
          name="role"
          v-model="formData.role"
      />
      <Input
          type="hidden"
          name="status"
          v-model="formData.status"
      />
      <AtomButton type="submit" :disabled="isSubmitting" label="Register" />
      <div v-if="registerError" class="alert alert-danger mt-3" role="alert">
        {{ registerError }}
      </div>
    </Form>
  </div>
</template>

<script setup>
import { Form } from "vee-validate";
import {object, string, ref as yupRef, number} from "yup";
import {computed, onMounted, ref as vueRef, defineEmits} from "vue";
import AtomInput from "@/components/atoms/AtomInput.vue";
import AtomButton from "@/components/atoms/AtomButton.vue";
import AtomFormSelect from "@/components/atoms/AtomFormSelect.vue";
import {useUserStore} from "@/store/user";
import apiClient from "@/utils/axiosClient";

const registerFormSchema = object({
  salutation: string().required("Salutation is required"),
  otherSalutation: string().when("salutation", {
    is: (value) => value === "other",
    then: (schema) =>
      schema
        .required("Please fill in your salutation")
        .max(30, "Salutation must be at most 30 characters"),
    otherwise: (schema) => schema.notRequired(), // Ensure it's not required for other cases
  }),
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
  email: string().email("Invalid email address").required("Email is required"),
  username: string().required("Username is required"),
  password: string()
    .required("Password is required")
    .min(12, "Password must be at least 12 characters long")
    .matches(
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])/,
      "Password must include uppercase, lowercase letters, numbers, and symbols"
    ),
  confirmPassword: string()
    .required("Please repeat your password")
    .oneOf([yupRef("password")], "Passwords must match"),
  address: object({
    street: string().required("Street address is required"),
    number: number().nullable().required("Building number is required"), //Allow null or empty inputs initially
    zip: number()
        .nullable()
        .required("ZIP code is required")
        .typeError("ZIP code must be numeric"), // Matches int type
    city: string().required("City is required"), // Matches String type
    country: string().required("Country is required"), // Matches String type
  }),
  paymentMethod: string().required("Payment method is required"),
});

const otherCountries = vueRef([
  { code: "US", name: "United States" },
  { code: "GB", name: "United Kingdom" },
  { code: "FR", name: "France" },
  { code: "CA", name: "Canada" },
  { code: "AU", name: "Australia" },
]);

const otherCountriesOptions = computed(() =>
    otherCountries.value.map((country) => ({
      value: country.code,
      text: country.name,
    }))
);

const paymentMethodOptions = vueRef([]);
// Fetch payment methods on component mount
onMounted(async () => {
  try {
    const paymentMethodOptionsResponse = await apiClient.get(`/paymentMethods`);
    // Map API response to the structure required by AtomFormSelect
    paymentMethodOptions.value = paymentMethodOptionsResponse.data.map((method) => ({
      value: method.name,
      text: method.name,
    }));
  } catch (error) {
    console.error("Failed to fetch payment methods:", error);
  }
});

const formData = vueRef({
  salutation: "",
  otherSalutation: "",
  firstName: "",
  lastName: "",
  dateOfBirth:"",
  address: {
    street: "",
    number: null,
    city: "",
    country: "",
    zip: null
  },
  email: "",
  paymentMethod: "",
  username: "",
  password: "",
  confirmPassword: "",
  role: "USER",
  status: "ACTIVE"
});


const registerError = vueRef("");
const isSubmitting = vueRef(false);
const emit = defineEmits(['registration-success']);

async function onSubmit(values) {
  console.log(values)
  isSubmitting.value = true;
  registerError.value = null;
  try {
    const gender = values.salutation === "other"
        ? values.otherSalutation
        : values.salutation;

    const payload = {
      gender: gender,
      firstName: values.firstName,
      lastName: values.lastName,
      username: values.username,
      password: values.password,
      email: values.email,
      dateOfBirth:values.dateOfBirth,
      role: values.role,
      status: values.status,
      address: {
        street: values.address.street,
        number: values.address.number,
        zip: values.address.zip,
        city: values.address.city,
        country: values.address.country,
      },
      paymentMethod: {
        name: values.paymentMethod}
    }
    console.log("Payload:", payload);
    // Send the payload to the backend
    const response = await apiClient.post("http://localhost:3000/users", payload,{
      headers: {
        "Content-Type": "application/json",
      },
    });
    // Handle success
    if (response.status >= 200 && response.status < 300) {
      console.log("Registration successful:", response.data);
      emit('registration-success')
      await useUserStore().login(values.username, values.password);
    } else {
      throw new Error(`Unexpected response status: ${response.status}`);
    }

  } catch (error) {
    // console.error("Error:", error);
    console.error("Error Response:", error.response.data); // Log server response
    console.error("Status Code:", error.response.status); // Log status code
    registerError.value = error.response?.data?.message || "An error occurred.";
  } finally {
    isSubmitting.value = false; // Enable form submission
  }
}
</script>

<style scoped>
.container {
  text-align: start;
}
</style>
