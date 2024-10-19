<template>
  <div class="container register-container">
    <div class="row justify-content-center align-items-center min-vh-100">
      <div class="col-md-8">
        <div class="card shadow">
          <div class="card-body">
            <h2 class="mb-4">Registration</h2>
            <form @submit.prevent="onSubmit">
              <!-- Salutation Field -->
              <div class="mb-3">
                <label for="salutation" class="form-label">Salutation</label>
                <select
                  v-model="formValues.salutation"
                  name="salutation"
                  class="form-select"
                  v-bind:class="{ 'is-invalid': errors.salutation }"
                  @change="validate('salutation')"
                >
                  <option value="">Select salutation</option>
                  <option value="male">Male</option>
                  <option value="female">Female</option>
                  <option value="other">Other</option>
                </select>
                <div v-if="errors.salutation" class="invalid-feedback">
                  {{ errors.salutation }}
                </div>
              </div>

              <!-- Other Salutation Field (conditional) -->
              <div v-if="formValues.salutation === 'other'" class="mb-3">
                <label for="otherSalutation" class="form-label"
                  >Other Salutation</label
                >
                <input
                  v-model="formValues.otherSalutation"
                  type="text"
                  name="otherSalutation"
                  class="form-control"
                  v-bind:class="{ 'is-invalid': errors.otherSalutation }"
                  @blur="validate('otherSalutation')"
                  @keypress="validate('otherSalutation')"
                />
                <div class="invalid-feedback">
                  {{ errors.otherSalutation }}
                </div>
              </div>

              <!-- Email Field -->
              <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input
                  v-model="formValues.email"
                  type="email"
                  name="email"
                  class="form-control"
                  v-bind:class="{ 'is-invalid': errors.email }"
                  @blur="validate('email')"
                  @keypress="validate('email')"
                />
                <div v-if="errors.email" class="invalid-feedback">
                  {{ errors.email }}
                </div>
              </div>

              <!-- Username Field -->
              <div class="mb-3">
                <label for="username" class="form-label">Username</label>
                <input
                  v-model="formValues.username"
                  type="text"
                  name="username"
                  class="form-control"
                  v-bind:class="{ 'is-invalid': errors.username }"
                  @blur="validate('username')"
                  @keypress="validate('username')"
                />
                <div v-if="errors.username" class="invalid-feedback">
                  {{ errors.username }}
                </div>
              </div>

              <!-- Password Field -->
              <div class="mb-3">
                <label for="password" class="form-label">Password</label>
                <input
                  v-model="formValues.password"
                  type="password"
                  name="password"
                  class="form-control"
                  v-bind:class="{ 'is-invalid': errors.password }"
                  @blur="validate('password')"
                  @keypress="validate('password')"
                />
                <div v-if="errors.password" class="invalid-feedback">
                  {{ errors.password }}
                </div>
              </div>

              <!-- Confirm Password Field -->
              <div class="mb-3">
                <label for="confirmPassword" class="form-label"
                  >Confirm Password</label
                >
                <input
                  v-model="formValues.confirmPassword"
                  type="password"
                  name="confirmPassword"
                  class="form-control"
                  v-bind:class="{ 'is-invalid': errors.confirmPassword }"
                  @blur="validate('confirmPassword')"
                  @keypress="validate('confirmPassword')"
                />
                <div v-if="errors.confirmPassword" class="invalid-feedback">
                  {{ errors.confirmPassword }}
                </div>
              </div>

              <!-- Country Field -->
              <div class="mb-3">
                <label for="country" class="form-label">Country</label>
                <select
                  v-model="formValues.country"
                  name="country"
                  class="form-select"
                  v-bind:class="{ 'is-invalid': errors.country }"
                  @change="validate('country')"
                >
                  <option value="">Select country</option>
                  <optgroup label="DACH Countries">
                    <option value="DE">Germany</option>
                    <option value="AT">Austria</option>
                    <option value="CH">Switzerland</option>
                  </optgroup>
                  <optgroup label="Other Countries">
                    <option
                      v-for="country in otherCountries"
                      :key="country.code"
                      :value="country.code"
                    >
                      {{ country.name }}
                    </option>
                  </optgroup>
                </select>
                <div v-if="errors.country" class="invalid-feedback">
                  {{ errors.country }}
                </div>
              </div>

              <button type="submit" class="btn btn-primary">Register</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { object, string, ref } from "yup";
import { ref as vueRef } from "vue";

// Define Yup validation schema
const RegisterFormSchema = object({
  salutation: string().required("Salutation is required"),
  otherSalutation: string().when("salutation", {
    is: (value) => value === "other",
    then: (schema) =>
      schema
        .required("Please fill in your salutation")
        .max(30, "Salutation must be at most 30 characters"),
    otherwise: (schema) => schema.notRequired(), // Ensure it's not required for other cases
  }),
  email: string().email("Invalid email address").required("Email is required"),
  username: string().required("Username is required"),
  password: string()
    .min(12, "Password must be at least 12 characters long")
    .matches(
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])/,
      "Password must include uppercase, lowercase letters, numbers, and symbols"
    )
    .required("Password is required"),
  confirmPassword: string()
    .required("Please repeat your password")
    .oneOf([ref("password")], "Passwords must match"),
  country: string().required("Country is required"),
});

export default {
  name: "RegisterForm",
  setup() {
    const otherCountries = vueRef([
      { code: "US", name: "United States" },
      { code: "GB", name: "United Kingdom" },
      { code: "FR", name: "France" },
      { code: "CA", name: "Canada" },
      { code: "AU", name: "Australia" },
    ]);

    return {otherCountries};
  },

  data() {
    return {
      formValues: {
        salutation: "",
        otherSalutation: "",
        email: "",
        username: "",
        password: "",
        confirmPassword: "",
        country: "",
      },
      errors: {
        salutation: "",
        otherSalutation: "",
        email: "",
        username: "",
        password: "",
        confirmPassword: "",
        country: "",
      },
    };
  },

  methods: {
    onSubmit() {
      RegisterFormSchema.validate(this.formValues, { abortEarly: false })
        .then(() => {
          console.log("Form submitted", this.formValues);
          alert("The form has been submitted succesfully");
        })
        .catch((err) => {
          err.inner.forEach((error) => {
            this.errors[error.path] = error.message;
            console.log(err.inner);
          });
        });
    },
    validate(field) {
      RegisterFormSchema.validateAt(field, this.formValues)
        .then(() => {
          this.errors[field] = "";
        })
        .catch((err) => {
          this.errors[field] = err.message;
        });
    },
  },
};
</script>

<style scoped>
.register-container {
  text-align: start;
}
</style>
