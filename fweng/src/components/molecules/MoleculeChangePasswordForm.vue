<template>
  <div class="container">
    <Form
        :validation-schema="changePasswordFormSchema"
        v-model="formData"
        @submit="onSubmit">
      <AtomInput
          label="Please enter your current password"
          name="currentPassword"
          id="currentPassword"
          type="password"
          placeholder="Enter current password"
      />
      <AtomInput
          label="Please enter your NEW password"
          name="newPassword"
          id="newPassword"
          type="password"
          placeholder="Enter new password"
      />
      <AtomInput
          label="Confirm your NEW password"
          name="confirmNewPassword"
          id="confirmNewPassword"
          type="password"
          placeholder="Confirm your new password"
      />
      <AtomButton type="submit" :disabled="isSubmitting" label="Change Password"/>
      <div v-if="changePasswordError" class="alert alert-danger mt-3" role="alert">
        {{ changePasswordError }}
      </div>
      <div v-if="changePasswordSuccess" class="alert alert-info mt-3" role="alert">
        {{ changePasswordSuccess }}
      </div>
    </Form>
  </div>
</template>

<script setup>

import {Form} from "vee-validate";
import AtomButton from "@/components/atoms/AtomButton.vue";
import AtomInput from "@/components/atoms/AtomInput.vue";
import {inject, ref as vueRef, ref} from "vue";
import {object, string, ref as yupRef} from "yup";
import apiClient from "@/utils/axiosClient";
import {useUserStore} from "@/store/user";

const changePasswordFormSchema = object({
  currentPassword: string()
      .required("Password is required")
      .min(3, "Password must be at least 3 characters long"),
      /*.matches(
          /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])/,
          "Password must include uppercase, lowercase letters, numbers, and symbols"
      ),*/
  newPassword: string()
      .required("Password is required")
      .min(3, "Password must be at least 3 characters long"),
      /*.matches(
          /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])/,
          "Password must include uppercase, lowercase letters, numbers, and symbols"
      ),*/
  confirmNewPassword: string()
      .required("Please repeat your password")
      .oneOf([yupRef("newPassword")], "Passwords must match"),
})

const formData = vueRef({
  currentPassword: "",
  newPassword: "",
  confirmPassword: ""
});

const isSubmitting = ref(false);
const changePasswordError = ref('');
const changePasswordSuccess = ref('');

// Pinia store instance
const userStore = useUserStore();

// Inject the method to control modals from parent
const hideChangePasswordModal = inject('hideChangePasswordModal');


async function onSubmit(values) {
  isSubmitting.value = true;
  changePasswordError.value = null;
  changePasswordSuccess.value = null;

  try {
    const response = await apiClient.patch(`http://localhost:3000/users/${userStore.id}/password`, {
    "currentPassword": values.currentPassword,
    "newPassword": values.newPassword
    });
    // console.log("response", response)
    changePasswordSuccess.value = response.data.message;
    setTimeout(() => {
        hideChangePasswordModal();
      }, 1000);
  }
   catch (error) {
    if (error.response && error.response.data) {
      changePasswordError.value = error.response.data.error;
    }
  } finally {
    isSubmitting.value = false;
  }
}

</script>

<style scoped>
.container {
  text-align: start;
}
</style>