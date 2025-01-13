<template>
  <div>
      <div class="mb-4 d-flex justify-content-end">
        <div class="col-12 col-sm-6 col-md-4 col-lg-3">
          <select v-model="localSortCriteria" id="sort" class="form-select form-select-sm" @change="updateSort">
            <option value="default" selected>Sort By:</option>
            <option value="createdAtNewToOld">Creation Time (new to old)</option>
            <option value="createdAtOldToNew">Creation Time (old to new)</option>
            <option value="lastUpdatedAtNewToOld">Last Update Time (new to old)</option>
            <option value="lastUpdatedAtOldToNew">Last Update Time (old to new)</option>
            <option value="statusAToZ">Status (A to Z)</option>
            <option value="statusZToA">Status (Z to A)</option>
        </select>
        </div>
      </div>
    <div class="table-responsive">
  <table class="table table-striped text-center table-hover">
    <thead class="thead-dark">
    <tr>
      <th>Booking ID</th>
      <th>Travel Details</th>
      <th>Travel Date</th>
      <th>User</th>
      <th>Created On</th>
      <th>Updated On</th>
      <th style="min-width: 150px;">Status</th>
      <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="booking in bookings" :key="booking.id">
      <td>{{ booking.id }}</td>
      <td>{{ booking.flight.flightOrigin.name }} -> {{ booking.flight.flightDestination.name }}</td>
      <td>{{ formatDateTime(booking.flight.departureTime) }}</td>
      <td>{{booking.user.username}}
      </td>
      <td>
        {{formatDateTime(booking.createdOn)}}
      </td>
      <td>
        {{formatDateTime(booking.lastUpdatedOn)}}
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
        <button class="btn btn-danger btn-sm" @click="deleteBooking(booking.id)">Delete</button>
      </td>
    </tr>
    </tbody>
  </table>
      </div>
  </div>
</template>

<script setup>
import {defineProps, defineEmits, ref,watch} from "vue";

const props = defineProps({
  bookings: {
    type: Array,
    required: true,
  },
  sortCriteria: {
    type: String,
    required: true,
  },
});

const emit = defineEmits(["change-status", "delete-booking", "update-sort"]);

const localSortCriteria = ref("default");

// Watch for external sort criteria updates
watch(() => props.sortCriteria, (newValue) => {
  localSortCriteria.value = newValue;
});

// Emit sort criteria change
function updateSort() {
  emit("update-sort", localSortCriteria.value);
}

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