<template>
  <AppNavbar @show-login="showLoginModal" @show-register="showRegisterModal" />
  <router-view />
  <OrganismLoginModal ref="loginModalRef" />
  <OrganismRegisterModal ref="registerModalRef" />
  <OrganismChangePasswordModal ref="changePasswordModalRef"/>
</template>

<script setup>
import {provide, ref} from "vue";
import AppNavbar from "@/components/organisms/OrganismNavbar.vue";
import OrganismLoginModal from "@/components/organisms/OrganismLoginModal.vue";
import OrganismRegisterModal from "@/components/organisms/OrganismRegisterModal.vue";
import OrganismChangePasswordModal from "@/components/organisms/OrganismChangePasswordModal.vue";

// Create refs to control the modals
const loginModalRef = ref(null);
const registerModalRef = ref(null);
const changePasswordModalRef = ref(null);

// Methods to show modals
const showLoginModal = () => {
  loginModalRef.value.showModal();
};

// Methods to hide modals
// eslint-disable-next-line no-unused-vars
const hideLoginModal = () => {
  loginModalRef.value.hideModal();
};

const showRegisterModal = () => {
  registerModalRef.value.showModal();
};

const switchToRegisterModal = () => {
  loginModalRef.value.hideModal();
  registerModalRef.value.showModal();
};

const hideChangePasswordModal = () => {
  changePasswordModalRef.value.hideModal();
};

const showChangePasswordModal = () => {
  changePasswordModalRef.value.showModal();
};



// Provide this method to children components
provide('switchToRegisterModal', switchToRegisterModal);
provide('showChangePasswordModal', showChangePasswordModal);
provide('hideChangePasswordModal', hideChangePasswordModal);
provide('showLoginModal', showLoginModal);
provide('hideLoginModal', hideLoginModal);

</script>


<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
