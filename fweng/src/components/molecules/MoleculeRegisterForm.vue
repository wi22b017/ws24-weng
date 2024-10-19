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
        label="Email"
        name="email"
        id="email"
        type="email"
        v-model="formData.email"
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

      <AtomFormSelect
        label="Country"
        name="country"
        id="country"
        placeholder="Select country"
        v-model="formData.country"
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
            options: otherCountries.map((country) => ({
              value: country.code,
              text: country.name,
            })),
          },
        ]"
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
import { object, string, ref } from "yup";
import { ref as vueRef } from "vue";
import AtomInput from "@/components/atoms/AtomInput.vue";
import AtomButton from "@/components/atoms/AtomButton.vue";
import AtomFormSelect from "@/components/atoms/AtomFormSelect.vue";

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
    .matches(/^[a-zA-Z'-\s]*$/, "Invalid name"),
  lastName: string()
    .required("Last name is required")
    .matches(/^[a-zA-Z'-\s]*$/, "Invalid name"),
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
    .oneOf([ref("password")], "Passwords must match"),
  country: string().required("Country is required"),
});

const otherCountries = vueRef([
  { code: "US", name: "United States" },
  { code: "GB", name: "United Kingdom" },
  { code: "FR", name: "France" },
  { code: "CA", name: "Canada" },
  { code: "AU", name: "Australia" },
]);

const formData = vueRef({
  salutation: "",
  otherSalutation: "",
  firstName: "",
  lastName: "",
  email: "",
  username: "",
  password: "",
  confirmPassword: "",
  country: "",
});

const registerError = vueRef("");
const isSubmitting = vueRef(false);


/*function onSubmit(values) {
  alert(JSON.stringify(values, null, 2));
}*/

// Form submit handler
/*function onSubmit(values) {
  try {
    // Form submission logic
    alert(JSON.stringify(values, null, 2));
    registerError.value = null; // Clear any previous errors
  } catch (error) {
    registerError.value = "An error occurred during registration"; // Set the error message
  }
}*/
// Form submit handler
async function onSubmit(values) {
  isSubmitting.value = true;
  registerError.value = null;

  try {
    await new Promise((resolve) => setTimeout(resolve, 2000));

    // Handle successful submission
    alert(JSON.stringify(values, null, 2));
  } catch (error) {
    // Handle any errors during submission
    registerError.value = "An error occurred during registration";
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
