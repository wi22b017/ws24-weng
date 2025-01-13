<template>
  <div class="container mt-4">
    <AtomHeading text="Manage Bookings"/>
    <ManageUserBookingsTemplate
        :bookings="sortedBookings"
        :sort-criteria="sortCriteria"
        @change-status="handleChangeStatus"
        @delete-booking="handleDeleteBooking"
        @update-sort="updateSortCriteria"
        class="mt-5"
    />
  </div>
</template>

<script setup>
import AtomHeading from "@/components/atoms/AtomHeading.vue";
import {useAdminUserStore} from "@/store/adminUserStore";
import {onMounted, ref, computed} from "vue";
import ManageUserBookingsTemplate from "@/components/template/ManageUserBookingsTemplate.vue";

const adminUserStore = useAdminUserStore();
const sortCriteria = ref("default");

onMounted(async () => {
  await adminUserStore.fetchBookings();

});

const sortedBookings = computed(() => {
  if (sortCriteria.value === "default") {
    return adminUserStore.bookings; // Default: no sorting
  }

  return [...adminUserStore.bookings].sort((a, b) => {
    if (sortCriteria.value.includes("createdAt") || sortCriteria.value.includes("lastUpdatedAt")) {
      const dateA = new Date(
          sortCriteria.value.includes("createdAt") ? a.createdOn : a.lastUpdatedOn
      );
      const dateB = new Date(
          sortCriteria.value.includes("createdAt") ? b.createdOn : b.lastUpdatedOn
      );

      return sortCriteria.value.endsWith("NewToOld") ? dateB - dateA : dateA - dateB;
    }

    if (sortCriteria.value.includes("status")) {
      if (sortCriteria.value === "statusAToZ") {
        return a.status === "Cancelled" ? -1 : 1;
      }
      if (sortCriteria.value === "statusZToA") {
        return a.status === "Cancelled" ? 1 : -1;
      }
    }

    return 0;
  });
});

async function handleChangeStatus({ bookingId, newStatus }) {
  try {
    const result = await adminUserStore.updateBookingStatus(bookingId, newStatus);
    if (result.success) {
      alert("Booking status updated successfully.");
      await adminUserStore.fetchBookings();
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

// Update Sort Criteria
function updateSortCriteria(criteria) {
  sortCriteria.value = criteria;
}


</script>

<style scoped>

</style>