<template>
  <div class="flight-info-entry-card card shadow">
    <div class="row align-items-center">
      <!-- Departure Information -->
      <div class="col text-center">
        <font-awesome-icon icon="plane-departure" class="icon" />
        <h4 class="time departure-time">{{ formatTime(flightDepartureTime) }}</h4>
        <p class="airport">{{ flightDepartureInfo.name }}</p>
        <p class="code">({{ flightDepartureInfo.code }})</p>
      </div>

      <!-- Divider with Duration -->
      <div class="col-auto text-center">
        <div class="d-flex flex-column align-items-center duration-container">
          <font-awesome-icon icon="clock" class="duration-icon" />
          <p class="duration-text">Duration: {{ formatDuration(flightDuration) }}</p>
        </div>
      </div>

      <!-- Arrival Information -->
      <div class="col text-center">
        <font-awesome-icon icon="plane-arrival" class="icon" />
        <h4 class="time arrival-time">{{ formatTime(flightArrivalTime) }}</h4>
        <p class="airport">{{ flightArrivalInfo.name }}</p>
        <p class="code">({{ flightArrivalInfo.code }})</p>
      </div>
    </div>

    <!-- Airline Information -->
    <div class="row mt-3 airline-info">
      <div class="col text-center">
        <p class="airline-text">
          <font-awesome-icon icon="plane" class="airline-icon" />
          Operated by: {{ airline }}
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps } from 'vue';

// Props for flight information
defineProps({
  flightDepartureInfo: {
    type: Object,
    required: true,
  },
  flightArrivalInfo: {
    type: Object,
    required: true,
  },
  flightDepartureTime: {
    type: String, // ISO string
    required: true,
  },
  flightArrivalTime: {
    type: String, // ISO string
    required: true,
  },
  flightDuration: {
    type: Number, // Duration in minutes
    required: true,
  },
  airline: {
    type: String,
    required: true,
  },
});

// Format the flight duration
const formatDuration = (minutes) => {
  const hours = Math.floor(minutes / 60);
  const remainingMinutes = minutes % 60;
  return `${hours}h ${remainingMinutes}m`;
};

// Format the flight time (e.g., "14:00")
const formatTime = (isoString) => {
  const date = new Date(isoString);
  return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
};
</script>

<style scoped>
/* Card styling */
.flight-info-entry-card {
  margin: 20px auto;
  padding: 20px;
  max-width: 800px;
  background: #f9f9f9;
  border-radius: 15px;
  border: 1px solid #e0e0e0;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s, box-shadow 0.2s;
}

.flight-info-entry-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.time {
  font-size: 2rem;
  font-weight: bold;
}

.departure-time {
  color: #007bff;
}

.arrival-time {
  color: #28a745;
}

.icon {
  font-size: 1.5rem;
  color: #007bff;
  margin-bottom: 5px;
}

.duration-icon {
  font-size: 1.5rem;
  color: #ff9900;
}

.airport {
  font-size: 1rem;
  font-weight: 500;
  color: #666;
}

.code {
  font-size: 0.9rem;
  font-weight: 400;
  color: #999;
}

.duration-container {
  margin: 10px 0;
}

.duration-text {
  font-size: 1rem;
  font-weight: 500;
  color: #555;
  margin-top: 5px;
}

.airline-info {
  border-top: 1px solid #e0e0e0;
  margin-top: 20px;
  padding-top: 10px;
}

.airline-icon {
  color: #007bff;
  margin-right: 5px;
}

.airline-text {
  font-size: 1rem;
  font-weight: 500;
  color: #333;
}
</style>
