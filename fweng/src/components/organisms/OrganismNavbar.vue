<template>
  <nav class="navbar navbar-expand-lg navbar-light">
    <router-link class="navbar-brand ms-3" :to="{ name: 'home' }"
      >AirScape</router-link
    >
    <button
      class="navbar-toggler"
      type="button"
      data-bs-toggle="collapse"
      data-bs-target="#navbarNav"
      aria-controls="navbarNav"
      aria-expanded="false"
      aria-label="Toggle navigation"
    >
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav">
        <li class="nav-item">
          <router-link class="nav-link" :to="{ name: 'help' }"
            >Help & FAQ</router-link
          >
        </li>
        <li class="nav-item">
          <router-link class="nav-link" :to="{ name: 'imprint' }"
            >Imprint</router-link
          >
        </li>
        <li class="nav-item" v-if="userStore.isLoggedIn && userStore.role === 'USER'">
          <router-link class="nav-link" :to="{ name: 'myBookings' }"
          >My Bookings</router-link
          >
        </li>
        <li class="nav-item" v-if="userStore.isLoggedIn && userStore.role === 'USER'">
          <router-link class="nav-link" :to="{ name: 'userdata' }"
          >My Profile</router-link
          >
        </li>
        <li class="nav-item" v-if="userStore.isLoggedIn && userStore.role === 'ADMIN'">
          <router-link class="nav-link" :to="{ name: 'manageBookings' }"
          >Manage Bookings</router-link
          >
        </li>
        <li class="nav-item" v-if="userStore.isLoggedIn && userStore.role === 'ADMIN'">
          <router-link class="nav-link" :to="{ name: 'manageUsers' }"
          >Manage Users</router-link
          >
        </li>
        <li class="nav-item" v-if="userStore.isLoggedIn && userStore.role === 'ADMIN'">
          <router-link class="nav-link" :to="{ name: 'manageFlights' }"
          >Manage Flights</router-link
          >
        </li>
        <li class="nav-item" v-if="!userStore.isLoggedIn">
          <button class="nav-link" @click="$emit('show-login')">Login</button>
        </li>
        <li class="nav-item" v-if="!userStore.isLoggedIn">
          <button class="nav-link" @click="$emit('show-register')">Registration</button>
        </li>
        <li class="nav-item" v-if="userStore.isLoggedIn">
          <button class="nav-link" @click="userStore.logout()">Logout</button>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script setup>
import { useUserStore } from '@/store/user';
const userStore = useUserStore();
</script>

<style scoped>
.navbar {
  background-color: #e3f2fd;
}
.nav-link {
  color: #007bff;
}

.navbar-brand {
  color: #007bff;
}

.router-link-exact-active {
  color: #0056b3 !important;
  font-weight: bold;
}
</style>
