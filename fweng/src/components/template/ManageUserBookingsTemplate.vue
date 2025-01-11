<template>
  <table class="table table-striped text-center table-hover">
    <thead class="thead-dark">
    <tr>
      <th>Booking ID</th>
      <th>Travel Details</th>
      <th>Travel Date</th>
      <th>Booked by</th>
      <th>Status</th>
      <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="booking in bookings" :key="booking.id">
      <td>{{ booking.id }}</td>
      <td>{{ booking.flight.flightOrigin.name }} -> {{ booking.flight.flightDestination.name }}</td>
      <td>{{ formatDateTime(booking.flight.departureTime) }}</td>
      <td>
        {{booking.user.username}}
      </td>
      <td class="status-cell">
        <select
            v-model="booking.status"
            class="form-select"
            @change="changeStatus(booking.id, booking.status)"
        >
          <option value="Confirmed">Confirmed</option>
          <option value="Cancelled">Cancelled</option>
        </select>
      </td>
      <td class="actions-cell">
        <button class="btn btn-danger" @click="deleteBooking(booking.id)">Delete</button>
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

const emit = defineEmits(["change-status", "delete-booking"]);

function changeStatus(bookingId, newStatus) {
  emit("change-status", { bookingId, newStatus });
}

function deleteBooking(bookingId) {
  emit("delete-booking", bookingId);
}

function formatDateTime(dateString) {
  const date = new Date(dateString);

  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = date.getFullYear();

  let hours = date.getHours();
  const minutes = String(date.getMinutes()).padStart(2, "0");

  return `${day}.${month}.${year}, ${hours}:${minutes}`;
}
</script>

<style>
.table-hover tbody tr:hover {
  background-color: #f9f9f9;
}

.status-cell, .actions-cell {
  vertical-align: middle;
}

.btn {
  margin: 0 5px;
}
</style>