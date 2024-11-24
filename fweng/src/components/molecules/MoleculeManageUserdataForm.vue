<template>
  <div class="container">
    <Form
        :validation-schema="userdataChangeFormSchema"
        v-model="formData"
        @submit="onSubmit"
        :initial-values="formData"
    >

      <AtomFormSelect
          label="Salutation"
          name="salutation"
          id="salutation"
          placeholder="Select salutation"
          v-model="formData.gender"
          :options="[
          { value: 'male', text: 'Male' },
          { value: 'female', text: 'Female' },
          { value: 'other', text: 'Other' },
        ]"
      />

      <AtomInput
          v-if="formData.gender === 'other'"
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
          v-model="formData.street"
      />
      <AtomInput
          type="number"
          label="Address Line 2"
          name="address.number"
          id="streetNumber"
          placeholder="Building number"
          v-model="formData.number"
      />
      <AtomInput
          label="City"
          name="address.city"
          id="city"
          v-model="formData.city"
      />
      <AtomFormSelect
          label="Country"
          name="address.country"
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
            options: otherCountriesOptions,
          },
        ]"
      />

      <AtomInput
          type="number"
          label="Zip Code"
          name="address.zip"
          id="zip"
          v-model="formData.zip"
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

      <AtomButton type="submit" :disabled="!isFormChanged || isSubmitting" label="Change Userdata" />

    </Form>
  </div>
  <div class="container mt-3">
    <AtomButton type="button" @click="openChangePasswordModal" label="Change Password" />
  </div>
  <div class="container">
    <div v-if="changeError" class="alert alert-danger mt-3" role="alert">
      {{ changeError }}
    </div>
    <div v-if="changeSuccess" class="alert alert-success mt-3" role="alert">
      {{ changeSuccess }}
    </div>
  </div>
  <OrganismChangePasswordModal ref="changePasswordModal" />
</template>

<script setup>
import { Form } from "vee-validate";
import {number, object, string} from "yup";
import {computed, ref as vueRef} from "vue";
import AtomInput from "@/components/atoms/AtomInput.vue";
import AtomButton from "@/components/atoms/AtomButton.vue";
import AtomFormSelect from "@/components/atoms/AtomFormSelect.vue";
import { useUserStore } from "@/store/user";
import OrganismChangePasswordModal from "@/components/organisms/OrganismChangePasswordModal.vue";
import apiClient from "@/utils/axiosClient";

const changeError = vueRef("");
const changeSuccess = vueRef("");
const isSubmitting = vueRef(false);
const changePasswordModal = vueRef(null);

// Pinia store instance
const userStore = useUserStore();

// Validation schema
const userdataChangeFormSchema = object({
  gender: string().required("Salutation is required"),
  otherSalutation: string().when("gender", {
    is: (value) => value === "other",
    then: (schema) =>
        schema
            .required("Please fill in your salutation")
            .max(30, "Salutation must be at most 30 characters"),
    otherwise: (schema) => schema.notRequired(),
  }),
  firstName: string()
      .required("First name is required")
      .matches(/^[a-zA-Z'-\s]*$/, "Invalid name"),
  lastName: string()
      .required("Last name is required")
      .matches(/^[a-zA-Z'-\s]*$/, "Invalid name"),
  dateOfBirth: string()
      .required("Date of birth is required")
      .matches(/^\d{4}-\d{2}-\d{2}$/, "Date of birth must be YYYY-MM-DD"),
  email: string().email("Invalid email address").required("Email is required"),
  username: string().required("Username is required"),
  street: string().required("Street address is required"),
  number: number().nullable().required("Building number is required"),
  zip: number().nullable().required("ZIP code is required"),
  city: string().required("City is required"),
  country: string().required("Country is required"),
});

// Bind form data to userStore state
const formData = vueRef({ ...userStore.$state });
console.log(userStore.$state);

// Check for form changes
const isFormChanged = computed(() => {
  return JSON.stringify(formData.value) !== JSON.stringify(userStore.$state);
});

// Collect only changed data
function getChangedData() {
  const changedData = {};
  Object.keys(formData.value).forEach((key) => {
    if (JSON.stringify(formData.value[key]) !== JSON.stringify(userStore.$state[key])) {
      changedData[key] = formData.value[key];
    }
  });
  return changedData;
}

// Submit updated user data
async function onSubmit() {
  const changedData = getChangedData();
  if (Object.keys(changedData).length === 0) {
    changeError.value = "No changes detected.";
    return;
  }
  try {
    isSubmitting.value = true;
    const response = await apiClient.patch(`http://localhost:3000/users/${userStore.id}`, changedData);
    if (response.status >= 200 && response.status < 300) {
      changeSuccess.value = "User data updated successfully.";
      Object.assign(userStore.$state, formData.value);
    }
  } catch (error) {
    changeError.value = error.response?.data?.message || "Failed to update user data.";
  } finally {
    isSubmitting.value = false;
  }
}

// Open password modal
function openChangePasswordModal() {
  if (changePasswordModal.value) {
    changePasswordModal.value.showModal();
  }
}

/*const errors = vueRef({
  firstName: "",
  lastName: "",
  email: "",
  username: "",
});*/

// Watch each form field for changes and validate individually
/*watch(formData, async (newValues, oldValues) => {
  for (const field in newValues) {
    if (newValues[field] !== oldValues[field]) {
      try {
        await userdataChangeFormSchema.validateAt(field, newValues);
        errors.value[field] = ""; // Clear error if validation succeeds
      } catch (validationError) {
        errors.value[field] = validationError.message; // Set error message if validation fails
      }
    }
  }
}, { deep: true });*/

/*const isFormChanged = computed(() => {
  return (
      formData.value.firstName !== user.value.firstname ||
      formData.value.lastName !== user.value.lastname ||
      formData.value.email !== user.value.email ||
      formData.value.username !== user.value.username
  );
});*/

// Helper function to get only changed fields
/*function getChangedData() {
  const changedData = {};

  if (formData.value.firstName !== user.value.firstname) {
    changedData.firstName = formData.value.firstName;
  }
  if (formData.value.lastName !== user.value.lastname) {
    changedData.lastName = formData.value.lastName;
  }
  if (formData.value.email !== user.value.email) {
    changedData.email = formData.value.email;
  }
  if (formData.value.username !== user.value.username) {
    changedData.username = formData.value.username;
  }

  return changedData;
}*/



/*async function onSubmit() {
  try {

    const changedData = getChangedData();
    console.log(changedData);
    const response = await axios.patch(`http://localhost:3000/users/${user.value.id}`, changedData);
    console.log(changedData);

    if (response.data && response.data.message && response.data.message === 'Change successful') {
      changeSuccess.value = response.data.message;
    }

    return response.data;
  } catch (error) {
    if (error.response && error.response.data && error.response.data.message) {
      changeError.value = error.response.data.message;
    }
  } finally {
    isSubmitting.value = false;
  }
}

function openChangePasswordModal() {
  if (changePasswordModal.value) {
    changePasswordModal.value.showModal();
  }
}*/


</script>

<style scoped>
.container {
  text-align: start;
}
</style>
