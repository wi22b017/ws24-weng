<template>
  <div class="container mt-4">
    <h1 class="mt-5 mb-3">My Bookings</h1>
    <MyBookingsTemplate
        v-if="bookings.length > 0"
        :bookings="bookings"
        @cancel-booking="handleCancelBooking"
    />
    <div v-if="bookings.length === 0">No bookings have been made.</div>
  </div>
</template>

<script setup>
import {useUserStore} from "@/store/user";
import MyBookingsTemplate from "@/components/template/MyBookingsTemplate.vue";

const userStore = useUserStore();

const bookings = userStore.bookings

async function handleCancelBooking(bookingId) {
  const result = await userStore.cancelBooking(bookingId);

  if (result.success) {
    alert("Booking cancelled successfully.");
  } else {
    alert(`Error: ${result.message}`);
  }

}

</script>

<style scoped>

</style>