<template>
  <div class="container">
    <Form
        :validation-schema="userdataChangeFormSchema"
        v-model="formData"
        @submit="onSubmit"
        :initial-values="formData"
    >

      <div class="form-group mb-3">
        <label for="profilePicture" class="form-label">Profile Picture</label>
        <input
            id="profilePicture"
            type="file"
            class="form-control"
            ref="fileInput"
            accept="image/*"
            @change="handleFileChange"
        />
      </div>

      <div class="profile-picture-container">
        <div v-if="imagePreview || userStore.profilePictureUrl" class="image-preview mb-3">
          <img :src="imagePreview || userStore.profilePictureUrl" alt="Uploaded Image" />
        </div>
      </div>

      <AtomButton
          type="button"
          @click="uploadProfilePicture"
          :disabled="!selectedFile || isSubmitting"
          label="Save Profile Picture"
          class="mb-3"
      />

      <div v-if="profilePictureSuccess" class="alert alert-success mt-3" role="alert">
        {{ profilePictureSuccess }}
      </div>

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
          name="street"
          id="street"
          placeholder="Street address"
          v-model="formData.street"
      />
      <AtomInput
          type="number"
          label="Address Line 2"
          name="number"
          id="streetNumber"
          placeholder="Building number"
          v-model="formData.number"
      />
      <AtomInput
          type="number"
          label="Zip Code"
          name="zip"
          id="zip"
          v-model="formData.zip"
      />
      <AtomInput
          label="City"
          name="city"
          id="city"
          v-model="formData.city"
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
            options: otherCountriesOptions,
          },
        ]"
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
          name="paymentMethodName"
          id="paymentMethodName"
          placeholder="Select a payment method"
          v-model="formData.paymentMethodName"
          :options="paymentMethodOptions"
      />
      <AtomInput
          label="Username"
          name="username"
          id="username"
          v-model="formData.username"
      />

      <AtomButton type="submit" :disabled="!formChanged || isSubmitting" label="Save My Changes" />

    </Form>
  </div>
  <div class="container mt-3">
    <AtomButton type="button" @click="showChangePasswordModal" label="Change Password" />
  </div>
  <div class="container">
    <div v-if="changeError" class="alert alert-danger mt-3" role="alert">
      {{ changeError }}
    </div>
    <div v-if="changeSuccess" class="alert alert-success mt-3" role="alert">
      {{ changeSuccess }}
    </div>
  </div>


</template>

<script setup>
import { Form } from "vee-validate";
import {number, object, string} from "yup";
import {computed, inject, onMounted, ref as vueRef, watch} from "vue";
import AtomInput from "@/components/atoms/AtomInput.vue";
import AtomButton from "@/components/atoms/AtomButton.vue";
import AtomFormSelect from "@/components/atoms/AtomFormSelect.vue";
import { useUserStore } from "@/store/user";
import apiClient from "@/utils/axiosClient";

const selectedFile = vueRef(null);
const imagePreview = vueRef(null);
const fileInput = vueRef(null);

const changeError = vueRef("");
const changeSuccess = vueRef("");
const profilePictureSuccess = vueRef("");
const isSubmitting = vueRef(false);
const formChanged = vueRef(false);

// Pinia store instance
const userStore = useUserStore();

// Inject the method to control modals from parent
const showChangePasswordModal = inject('showChangePasswordModal');

// Validation schema
const userdataChangeFormSchema = object({
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
  street: string().required("Street address is required"),
  number: number().nullable().required("Building number is required"), //Allow null or empty inputs initially
  zip: number()
        .nullable()
        .required("ZIP code is required")
        .typeError("ZIP code must be numeric"), // Matches int type
  city: string().required("City is required"), // Matches String type
  country: string().required("Country is required"), // Matches String type
  paymentMethodName: string().required("Payment method is required")
});

const gender = userStore.gender;

// Bind form data to userStore state
const formData = vueRef({
  id: userStore.id,
  salutation: gender === "male" || gender === "female" ? gender : "other",
  otherSalutation: gender !== "male" && gender !== "female" ? gender : "",
  firstName: userStore.firstName,
  lastName: userStore.lastName,
  username: userStore.username,
  email: userStore.email,
  dateOfBirth: userStore.dateOfBirth,
  street: userStore.street,
  number: userStore.number,
  zip: userStore.zip,
  city: userStore.city,
  country: userStore.country,
  paymentMethodName: userStore.paymentMethodName,
});

watch(
    () => formData.value,
    (newValues) => {
      const derivedGender =
          newValues.salutation === "other"
              ? newValues.otherSalutation
              : newValues.salutation;
      formChanged.value =
          derivedGender !== userStore.gender ||
          newValues.firstName !== userStore.firstName ||
          newValues.lastName !== userStore.lastName ||
          newValues.username !== userStore.username ||
          newValues.email !== userStore.email ||
          newValues.dateOfBirth !== userStore.dateOfBirth ||
          newValues.street !== userStore.street ||
          newValues.number !== userStore.number ||
          newValues.zip !== userStore.zip ||
          newValues.city !== userStore.city ||
          newValues.country !== userStore.country ||
          newValues.paymentMethodName !== userStore.paymentMethodName;
    },
    { deep: true }
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

// Collect only changed data
function getChangedData() {
  const changedData = {};

  const addressFields = ["street", "number", "zip", "city", "country"];
  const hasAddressChanged = addressFields.some((field) =>
      JSON.stringify(formData.value[field]) !== JSON.stringify(userStore.$state[field])
  );

  if (hasAddressChanged) {
    console.log("address changed")
    changedData.address = {
      street: formData.value.street,
      number: formData.value.number,
      zip: formData.value.zip,
      city: formData.value.city,
      country: formData.value.country,
    };
  }

  Object.keys(formData.value).forEach((key) => {
    if (!addressFields.includes(key)) {
      if (key === "salutation" || key === "otherSalutation") {
        const derivedGender =
            formData.value.salutation === "other"
                ? formData.value.otherSalutation
                : formData.value.salutation;

        // Compare derived gender with userStore gender
        if (JSON.stringify(derivedGender) !== JSON.stringify(userStore.$state.gender)) {
          changedData.gender = derivedGender;
        }
      } else {
        // Compare other fields as usual
        if (JSON.stringify(formData.value[key]) !== JSON.stringify(userStore.$state[key])) {
          changedData[key] = formData.value[key];
        }
      }
    }
  });

  return changedData;
}

// Submit updated user data
async function onSubmit() {
  const changedData = getChangedData();
  console.log(" changedData",  changedData)

  if (Object.keys(changedData).length === 0) {
    changeError.value = "No changes detected.";
    return;
  }
  try {
    isSubmitting.value = true;
    const response = await apiClient.patch(`http://localhost:3000/users/${userStore.id}`, changedData);
    if (response.status >= 200 && response.status < 300) {
      changeSuccess.value = "User data updated successfully.";
      await userStore.fetchUserData(userStore.id);
    }
  } catch (error) {
    changeError.value = error.response?.data?.error || "Failed to update user data.";
  } finally {
    isSubmitting.value = false;
  }
}

const otherCountries = vueRef([
  { code: "US", name: "United States" },
  { code: "GB", name: "United Kingdom" },
  { code: "FR", name: "France" },
  { code: "CA", name: "Canada" },
  { code: "AU", name: "Australia" },
]);

// Ensure it is not undefined
const otherCountriesOptions = computed(() =>
    otherCountries.value.map((country) => ({
      value: country.code,
      text: country.name,
    }))
);

// Method to handle file selection
const handleFileChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    if (!file.type.startsWith('image/')) {
      alert('Please select a valid image file.');
      return;
    }
    selectedFile.value = file;
    imagePreview.value = URL.createObjectURL(file);
  }
};

async function uploadProfilePicture() {
  if (!selectedFile.value) return;

  try {
    isSubmitting.value = true;
    const formData = new FormData();
    formData.append("file", selectedFile.value);

    const response = await apiClient.post(`/users/${userStore.id}/picture`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });

    if (response.status >= 200 && response.status < 300) {
      profilePictureSuccess.value = "Profile picture updated successfully.";
      await userStore.fetchUserData(userStore.id); // Refresh user data
    }
  } catch (error) {
    changeError.value = error.response?.data?.error || "Failed to update profile picture.";
  } finally {
    isSubmitting.value = false;
  }
}

</script>

<style scoped>
.container {
  text-align: start;
}

.profile-picture-container {
  display: flex;
  align-items: center;
  flex-direction: column;
  align-items: center;
}

.image-preview img {
  max-width: 100%;
  height: auto;
  border: 1px solid #ccc;
  border-radius: 5px;
}

</style>
