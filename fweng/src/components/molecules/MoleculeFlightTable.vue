<template>
  <table class="table table-striped text-center">
    <thead>
    <tr>
      <th>#</th>
      <th>Flight number</th>
      <th>Airline</th>
      <th>Origin</th>
      <th>Destination</th>
      <th>Departure Time</th>
      <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="(flight, index) in flights" :key="flight.id">
      <td>{{ index + 1 }}</td>
      <td>{{ flight.flightNumber }}</td>
      <td>{{ flight.aircraft.airline.name }}</td>
      <td>{{ flight.flightOrigin.code }}</td>
      <td>{{ flight.flightDestination.code }}</td>
      <td>{{ formatDepartureTime(flight.departureTime) }}</td>
      <td class="actions-cell">
        <div class="button-group">
          <AtomButton
              label="Edit"
              :onClick="() => onEdit(flight)"
              class="btn-primary"
          />
          <AtomButton
              label="Delete"
              :onClick="() => onDelete(flight)"
              class="btn-danger"
          />
        </div>
      </td>
    </tr>
    </tbody>
  </table>
</template>

<script setup>
import { defineProps } from "vue";
import AtomButton from '@/components/atoms/AtomButton.vue';

defineProps({
  flights: {
    type: Array,
    required: true,
  }
});

function onEdit(flight){
  console.log("Flight to edit: "+flight.flightNumber);
}

function onDelete(flight){
  console.log("Flight to delete: "+flight.flightNumber);
}

function formatDepartureTime(dateTimeString) {
  const date = new Date(dateTimeString);

  // Extract components
  const day = String(date.getDate()).padStart(2, "0"); // Ensure 2-digit day
  const month = String(date.getMonth() + 1).padStart(2, "0"); // Ensure 2-digit month (months are 0-indexed)
  const year = date.getFullYear();
  const hours = String(date.getHours()).padStart(2, "0"); // Ensure 2-digit hours
  const minutes = String(date.getMinutes()).padStart(2, "0"); // Ensure 2-digit minutes

  // Combine into desired format
  return `${day}.${month}.${year} ${hours}:${minutes}`;
}



</script>

<style>
.actions-cell {
  vertical-align: middle;
}

.button-group {
  display: flex;
  gap: 10px;
  justify-content: center;
  align-items: center;
}
</style>
