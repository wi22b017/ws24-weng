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
        <div>
          <p class="price-text">Price: {{ flightPrice }} €</p>
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

    <!-- Airline and Book Now Button -->
    <div class="row mt-3 align-items-center justify-content-center">
      <div class="col-12 text-center">
        <p class="airline-text">
          <i class="bi bi-plane text-secondary"></i>
          Operated by: {{ airline }}
        </p>
      </div>
    </div>
    <div v-if="!bookNowButtonInvisible" class="row mt-3 justify-content-center">
      <div class="col-auto">
        <AtomButton
            label="Book Now"
            :onClick="handleBookNow"
            class="btn-book"
        />
      </div>
    </div>

  </div>
</template>

<script setup>
import {defineProps, inject} from 'vue';
import AtomButton from "@/components/atoms/AtomButton.vue";
import { useUserStore } from '@/store/user';
import router from "@/router";

const userStore = useUserStore();
// Inject the method to control modals from parent
const showLoginModal = inject('showLoginModal');

// Props for flight information
const props = defineProps({
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
  flightId: {
    type: String,
    required: true,
  },
  flightPrice: {
    type: Number,
    required: true,
  },
  bookNowButtonInvisible: {
    type: Boolean,
    required: false,
    default: false,
  },
});

console.log("Prop value received:", props.bookNowButtonInvisible);
const isButtonInvisible = Boolean(props.bookNowButtonInvisible);
console.log("Boolean value:", isButtonInvisible);

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

// Placeholder function for the Book Now button
const handleBookNow = () => {
  if(!userStore.isLoggedIn){
    showLoginModal(true, props.flightId);
  }else{
    //router.push({name: 'flightDetail'});
    // eslint-disable-next-line
    router.push({ name: 'flightDetail', params: { flightId: props.flightId } });
  }
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

.price-text {
  font-size: 1.5rem;
  font-weight: 800;
  color: #555;
  margin-top: 5px;
}


.airline-text {
  font-size: 1rem;
  font-weight: 500;
  color: #333;
  text-align: center; /* Centers text horizontally */
  margin: 0 auto; /* Ensures it remains centered */
  width: 100%; /* Ensures the text spans the full width of the row */
}



.btn-book {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.btn-book:hover {
  background-color: #0056b3;
}


</style>
