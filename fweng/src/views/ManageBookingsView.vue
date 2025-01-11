<template>
  <div class="container mt-4">
    <AtomHeading text="Manage Bookings"/>
    <ManageUserBookingsTemplate
    :bookings="adminUserStore.bookings"
    @change-status="handleChangeStatus"
    @delete-booking="handleDeleteBooking"
    />
  </div>
</template>

<script setup>
import AtomHeading from "@/components/atoms/AtomHeading.vue";
import {useAdminUserStore} from "@/store/adminUserStore";
import {onMounted} from "vue";
import ManageUserBookingsTemplate from "@/components/template/ManageUserBookingsTemplate.vue";

const adminUserStore = useAdminUserStore();

onMounted(async () => {
  await adminUserStore.fetchBookings();

});


async function handleChangeStatus({ bookingId, newStatus }) {
  try {
    const result = await adminUserStore.updateBookingStatus(bookingId, newStatus);
    if (result.success) {
      alert("Booking status updated successfully.");
    } else {
      alert(`Error: ${result.message}`);
    }
  } catch (error) {
    console.error("Error updating booking status:", error);
  }
}

async function handleDeleteBooking(bookingId) {
  try {
    if (confirm("Are you sure you want to delete this booking?")) {
      const result = await adminUserStore.deleteBooking(bookingId);
      if (result.success) {
        alert(`${result.message}`);
      } else {
        alert(`Error: ${result.message}`);
      }
    }
  }catch (error) {
    console.error("Error deleting the booking:", error);
  }

}


</script>

<style scoped>

</style>