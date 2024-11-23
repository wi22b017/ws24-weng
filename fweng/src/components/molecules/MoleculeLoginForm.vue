<template>
  <div class="container">
    <Form :validation-schema="validationSchema" @submit="onSubmit">
      <AtomInput
          label="Please enter your username or email"
          name="usernameOrEmail"
          id="usernameOrEmail"
          placeholder="username or email"
      />
      <AtomInput
          label="Please enter your password"
          name="password"
          id="password"
          type="password"
          placeholder="password"
      />
      <AtomButton type="submit" :disabled="isSubmitting" label="Log In"/>
      <div v-if="loginError" class="alert alert-danger mt-3" role="alert">
        {{ loginError }}
      </div>
      <div v-if="loginSuccess" class="alert alert-info mt-3" role="alert">
        {{ loginSuccess }}
      </div>
      <a href="#" @click.prevent="switchToRegister">No Account? Register Here!</a>
    </Form>
  </div>

</template>

<script setup>
import { Form } from 'vee-validate';
import * as yup from 'yup';
import {inject, ref} from 'vue';
import AtomInput from '@/components/atoms/AtomInput.vue';
import AtomButton from '@/components/atoms/AtomButton.vue';
import { useUserStore } from '@/store/user';

const validationSchema = yup.object({
  usernameOrEmail: yup
      .string()
      .test(
          'is-email-or-username',
          'Must be a valid email or username',
          (value) =>
              yup.string().email().isValidSync(value) ||
              yup.string().min(3).isValidSync(value)
      )
      .required('Username or email is required'),
  password: yup
      .string()
      .min(8, 'Password must be at least 8 characters')
      .required('Password is required'),
});

const isSubmitting = ref(false);
const loginError = ref('');
const loginSuccess = ref('');
const userStore = useUserStore();

// Inject the method to control modals from parent
const switchToRegisterModal = inject('switchToRegisterModal');
const hideLoginModal = inject('hideLoginModal');

// Method to handle the switch from login to register modal
const switchToRegister = () => {
  switchToRegisterModal(); // Call the injected method to switch modals
};

const onSubmit = async (values) => {
  /*isSubmitting.value = true;
  await new Promise((resolve) => setTimeout(resolve, 1000));
  alert(JSON.stringify(values, null, 2));
  // Simulate a login error
  loginError.value = '!!Simulated error!! Invalid credentials. Please try again.';
  isSubmitting.value = false;*/

  isSubmitting.value = true;
  loginError.value = null;
  loginSuccess.value = null;

  const result = await userStore.login(values.usernameOrEmail, values.password);
  console.log(result);

  if(result.success===true){
    loginSuccess.value = result.message;
  }else{
    loginError.value = result.message;
  }

  setTimeout(() => {
    hideLoginModal();
  }, 1000);

  isSubmitting.value = false;

  /*
  try {
    const response = await axios.post('http://localhost:3000/auth/login', {
          usernameOrEmail: values.usernameOrEmail,
          password: values.password
        }
    );

    // Check if the response contains userId and username
    if (response.data && response.data.message && response.data.message==='Login successful') {

      loginSuccess.value = response.data.message;

      await checkLoginStatus(); // Ensure navbar updates after login

      setTimeout(() => {
        hideLoginModal();
      }, 1000);

    }
    return response.data;
  } catch (error) {
    if (error.response && error.response.data && error.response.data.message) {
      loginError.value = error.response.data.message;
    }
  } finally {
    isSubmitting.value = false;
  }*/




};
</script>

<style scoped>
.container {
  text-align: start;
}
a {
  display: block;
  margin-top: 0.5em;
}
</style>/