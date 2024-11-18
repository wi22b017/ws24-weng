<template>
  <div class="container">
    <Form
        :validation-schema="userdataChangeFormSchema"
        v-model="formData"
        @submit="onSubmit"
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
import { object, string} from "yup";
import {computed, ref as vueRef, watch} from "vue";
import AtomInput from "@/components/atoms/AtomInput.vue";
import AtomButton from "@/components/atoms/AtomButton.vue";
import axios from 'axios';
import OrganismChangePasswordModal from "@/components/organisms/OrganismChangePasswordModal.vue";

const changeError = vueRef("");
const changeSuccess = vueRef("");
const isSubmitting = vueRef(false);

const changePasswordModal = vueRef(null);

const userdataChangeFormSchema = object({
  firstName: string()
      .required("First name is required")
      .matches(/^[a-zA-Z'-\s]*$/, "Invalid name"),
  lastName: string()
      .required("Last name is required")
      .matches(/^[a-zA-Z'-\s]*$/, "Invalid name"),
  email: string().email("Invalid email address").required("Email is required"),
  username: string().required("Username is required")
});

// This should come from Pinia later on
const user = vueRef({
  id: 5,
  firstname: "Peter",
  lastname: "Schuster",
  email: "peter.schuster@gmail.com",
  username: "peterschuster99",
});


const formData = vueRef({
  firstName: user.value.firstname,
  lastName: user.value.lastname,
  email: user.value.email,
  username: user.value.username,
});

const errors = vueRef({
  firstName: "",
  lastName: "",
  email: "",
  username: "",
});

// Watch each form field for changes and validate individually
watch(formData, async (newValues, oldValues) => {
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
}, { deep: true });

const isFormChanged = computed(() => {
  return (
      formData.value.firstName !== user.value.firstname ||
      formData.value.lastName !== user.value.lastname ||
      formData.value.email !== user.value.email ||
      formData.value.username !== user.value.username
  );
});

// Helper function to get only changed fields
function getChangedData() {
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
}

async function onSubmit() {
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
}

</script>

<style scoped>
.container {
  text-align: start;
}
</style>
