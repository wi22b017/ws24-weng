<template>
  <table class="table table-striped text-center table-hover">
    <thead class="thead-dark">
    <tr>
      <th>Booking ID</th>
      <th>Travel Details</th>
      <th>Travel Date</th>
      <th>Passengers</th>
      <th>Status</th>
      <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="booking in bookings" :key="booking.id">
      <td>{{ booking.id}}</td>
      <td>{{ booking.flight.flightOrigin.name}} -> {{  booking.flight.flightDestination.name }}</td>
      <td>{{ formatDateTime(booking.flight.departureTime) }}</td>
      <td>
        <div v-for="passenger in booking.passengers" :key="passenger.id">
          {{ passenger.firstName }} {{ passenger.lastName }}
        </div>
      </td>
      <td class="status-cell">
        <span :class="['badge', booking.status === 'Cancelled' ? 'bg-danger' : 'bg-success']">
            {{ booking.status }}
          </span>
      </td>
      <td class="actions-cell">
        <button
            class="btn btn-danger"
            :disabled="booking.status === 'Cancelled'"
            @click="cancelBooking(booking.id)"
        >
          Cancel Booking
        </button>
      </td>
    </tr>
    </tbody>
  </table>
</template>

<script setup>
import { defineProps, defineEmits } from "vue";

defineProps({
  bookings: {
    type: Array,
    required: true,
  },
});

const emit = defineEmits(["cancel-booking"]);

// Method to handle cancel booking
function cancelBooking(bookingId) {
  emit("cancel-booking", bookingId);
}
function formatDateTime(dateString) {
  const date = new Date(dateString);

  // Extract date components
  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0"); // Months are 0-based
  const year = date.getFullYear();

  // Extract time components
  let hours = date.getHours();
  const minutes = String(date.getMinutes()).padStart(2, "0");
  const ampm = hours >= 12 ? "PM" : "AM";
  hours = hours % 12 || 12; // Convert 24-hour time to 12-hour time

  // Combine date and time
  return `${day}.${month}.${year}, ${hours}:${minutes}${ampm}`;
}
</script>

<style>
.table-hover tbody tr:hover {
  background-color: #f9f9f9;
}

.status-cell, .actions-cell {
  vertical-align: middle;
}

.badge {
  padding: 0.5em 1em;
  font-size: 0.9em;
  border-radius: 0.25em;
}

.bg-success {
  background-color: #28a745;
  color: #fff;
}

.bg-danger {
  background-color: #dc3545;
  color: #fff;
}
</style>
