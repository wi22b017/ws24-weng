<template>
  <AppNavbar @show-login="showLoginModal" @show-register="showRegisterModal" />
  <router-view />
  <OrganismLoginModal ref="loginModalRef" />
  <OrganismRegisterModal ref="registerModalRef" />
</template>

<script setup>
import {provide, ref} from "vue";
import AppNavbar from "@/components/organisms/OrganismNavbar.vue";
import OrganismLoginModal from "@/components/organisms/OrganismLoginModal.vue";
import OrganismRegisterModal from "@/components/organisms/OrganismRegisterModal.vue";

// Create refs to control the modals
const loginModalRef = ref(null);
const registerModalRef = ref(null);

// Methods to show modals
const showLoginModal = () => {
  loginModalRef.value.showModal();
};

const showRegisterModal = () => {
  registerModalRef.value.showModal();
};

const switchToRegisterModal = () => {
  loginModalRef.value.hideModal();
  registerModalRef.value.showModal();
};

// Provide this method to children components
provide('switchToRegisterModal', switchToRegisterModal);

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
