<template>
  <AppNavbar @show-login="showLoginModal" @show-register="showRegisterModal" />
  <router-view />
  <OrganismLoginModal
      ref="loginModalRef"
      :shouldForwardToFlightDetail="shouldForwardToFlightDetail"
      :shouldForwardToFlightId="shouldForwardToFlightId"
  />
  <OrganismRegisterModal
      ref="registerModalRef"
      :shouldForwardToFlightDetail="shouldForwardToFlightDetail"
      :shouldForwardToFlightId="shouldForwardToFlightId"
  />
  <OrganismChangePasswordModal ref="changePasswordModalRef"/>
  <OrganismAdminChangePasswordModal ref="adminChangePasswordModalRef" :currentUser="currentUser"/>
</template>

<script setup>
import {provide, ref} from "vue";
import AppNavbar from "@/components/organisms/OrganismNavbar.vue";
import OrganismLoginModal from "@/components/organisms/OrganismLoginModal.vue";
import OrganismRegisterModal from "@/components/organisms/OrganismRegisterModal.vue";
import OrganismChangePasswordModal from "@/components/organisms/OrganismChangePasswordModal.vue";
import OrganismAdminChangePasswordModal from "@/components/organisms/OrganismAdminChangePasswordModal.vue";

// Create refs to control the modals
const loginModalRef = ref(null);
const registerModalRef = ref(null);
const changePasswordModalRef = ref(null);
const shouldForwardToFlightDetail = ref(false);
const shouldForwardToFlightId = ref(null);
const adminChangePasswordModalRef = ref(null);

const currentUser = ref(null);

const showLoginModal = (forward = false, flightId  = null) => {
  // if the loginModal get's called from a Flight Booking card, this value is set to true
  // after the login or registration process the user gets redirected to the flights detail page
  shouldForwardToFlightDetail.value = forward;
  shouldForwardToFlightId.value = flightId;
  loginModalRef.value.showModal();
};

const hideLoginModal = () => {
  loginModalRef.value.hideModal();
};

const showRegisterModal = () => {
  registerModalRef.value.showModal();
};

const hideRegisterModal = () => {
  registerModalRef.value.hideModal();
};

const switchToRegisterModal = () => {
    loginModalRef.value.hideModalWithoutRedirection();
    registerModalRef.value.showModal();
};

const hideChangePasswordModal = () => {
  changePasswordModalRef.value.hideModal();
};

const showChangePasswordModal = () => {
  changePasswordModalRef.value.showModal();
};

const hideAdminChangePasswordModal = () => {
  adminChangePasswordModalRef.value.hideModal();
}

const showAdminChangePasswordModal = (user) => {
    currentUser.value = user;
    adminChangePasswordModalRef.value.showModal();
}

// Provide this method to children components
provide('switchToRegisterModal', switchToRegisterModal);
provide('showChangePasswordModal', showChangePasswordModal);
provide('hideChangePasswordModal', hideChangePasswordModal);
provide('showLoginModal', showLoginModal);
provide('hideLoginModal', hideLoginModal);
provide('showRegisterModal', showRegisterModal);
provide('hideRegisterModal', hideRegisterModal);
provide('showAdminChangePasswordModal', showAdminChangePasswordModal);
provide('hideAdminChangePasswordModal', hideAdminChangePasswordModal);

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
