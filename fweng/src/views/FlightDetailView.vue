<template>
  <h1>New Booking</h1>
  <h2>Flight Details</h2>
  <ErrorModal ref="errorModal" />
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-8 text-start">
        <FlightListTemplate
            :flights="flightStore.flightToBook"
            :show-booking-button="false"
        />
        <MoleculePassengerForm
            v-for="(passenger, index) in passengers"
            :key="index"
            :passenger="passenger"
            :index="index"
            @updatePassenger="(updatedPassenger) => updatePassenger(index, updatedPassenger)"
        />
        <div class="text-center mb-3">
          <AtomButton
              type="button"
              @click="addPassenger"
              label="Add Passenger"
              :full-width="false"
          />
        </div>

        <h2 class="text-center">Total Price: {{ totalPrice }} €</h2>
        <h2 class="text-center">Payment</h2>
        <Form :initial-values="{ paymentMethod: userStore.paymentMethodName }">
          <AtomFormSelect
              label="Payment Method"
              name="paymentMethod"
              id="paymentMethod"
              placeholder="Select a payment method"
              v-model="selectedPaymentMethod"
              :options="paymentMethodOptions"
          />
        </Form>
        <div class="text-center mb-3">
          <AtomButton
              type="button"
              @click="confirmBooking"
              label="Confirm Booking"
              :full-width="false"
          />
        </div>
        <div v-if="errorMessage" class="alert alert-danger mt-3" role="alert">
          {{ errorMessage }}
        </div>
        <div v-if="successMessage" class="alert alert-info mt-3" role="alert">
          {{ successMessage }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import FlightListTemplate from "@/components/template/FlightListTemplate.vue";
import MoleculePassengerForm from "@/components/molecules/MoleculePassengerForm.vue";
import { useFlightStore } from '@/store/flight';
import {onMounted, defineProps, reactive, computed, ref as vueRef, ref} from "vue";
import { useRoute } from "vue-router";
import AtomButton from "@/components/atoms/AtomButton.vue";
import apiClient from "@/utils/axiosClient";
import AtomFormSelect from "@/components/atoms/AtomFormSelect.vue";
import { useUserStore } from "@/store/user";
import { Form } from "vee-validate";
import { formatISO } from "date-fns";
import { object, string } from "yup";
import ErrorModal from "@/components/organisms/OrganismErrorModal.vue";

// Define the same validation schema here for final check before send
const passengerFinalSchema = object({
  firstName: string().required().matches(/^[a-zA-Z'\-\s]*$/),
  lastName: string().required().matches(/^[a-zA-Z'\-\s]*$/),
  dateOfBirth: string().required().matches(/^\d{4}-\d{2}-\d{2}$/),
  baggage: object({
    baggageTypeId: string().required(),
  }),
  seatNumber: string().nullable(true),
});

defineProps({
  flightId: {
    type: String,
    required: true,
  },
});

const userStore = useUserStore();
const route = useRoute();
const flightId = route.params.flightId;
const flightStore = useFlightStore();
const paymentMethodOptions = vueRef([]);
const selectedPaymentMethod = vueRef();
const errorModal = ref(null);
const successMessage = ref('');
const errorMessage = ref('');

const passengers = reactive([
  {
    firstName: userStore.firstName,
    lastName: userStore.lastName,
    dateOfBirth: userStore.dateOfBirth,
    seatNumber: "",
    baggage: {
      baggageTypeId: flightStore.defaultBaggageType,
    },
  },
]);

const totalPrice = computed(() => {
  if (!flightStore.flightToBook.length) return 0;

  const basePricePerPerson = flightStore.flightToBook[0].price || 0;

  return passengers.reduce((total, passenger) => {
    const baggageType = flightStore.baggageTypesOptions.find(
        (option) => option.value === passenger.baggage.baggageTypeId
    );

    const baggageFee = baggageType ? baggageType.fee : 0;

    return total + basePricePerPerson + baggageFee;
  }, 0);
});

const addPassenger = () => {
  passengers.push({
    firstName: "",
    lastName: "",
    dateOfBirth: "",
    seatNumber: "",
    baggage: {
      baggageTypeId: flightStore.defaultBaggageType,
    },
  });
};

const updatePassenger = (index, updatedPassenger) => {
  passengers[index] = {...updatedPassenger};
  console.log(passengers);
};

const confirmBooking = async () => {
  // Validate all passengers before sending the request
  for (const passenger of passengers) {
    try {
      await passengerFinalSchema.validate(passenger, {abortEarly: false});
    } catch (err) {
      // If validation fails, show error and return early
      console.log(passengers);
      errorModal.value.showModal("Please make sure that every passenger has a firstname, lastname, date of birth and a baggage type selected");
      return;
    }
  }

  if (!selectedPaymentMethod.value) {
    errorModal.value.showModal("Please select a payment method.");
    return;
  }

  const bookingData = {
    status: "Confirmed",
    price: totalPrice.value,
    bookingDate: formatISO(new Date().toISOString(), {representation: "complete"}),
    userId: userStore.id,
    paymentMethodId: selectedPaymentMethod.value,
    flightId: flightId,
    passengers: passengers,
  };

  const result = await userStore.createBooking(bookingData);

  if(result.success===true){
    successMessage.value = result.message;
  }else{
    errorMessage.value = result.message;
  }
};

async function getPaymentMethods() {
  try {
    const paymentMethodOptionsResponse = await apiClient.get(`/paymentMethods`);
    paymentMethodOptions.value = paymentMethodOptionsResponse.data.map((method) => ({
      value: method.id,
      text: method.name,
    }));

    // After fetching the payment methods, preselect based on userStore if available
/*    if (userStore.paymentMethodName) {
      // Check if the fetched payment methods contain this payment method
      const availableMethodId = paymentMethodOptions.value.find((option) => {
            if(option.text === userStore.paymentMethodName){
              return option.id;
            }
          }
      );

      if (availableMethodId) {
        selectedPaymentMethod.value = availableMethodId;
      }
    }*/
    // Preselect the payment method based on `userStore.paymentMethodName`
    if (userStore.paymentMethodName) {
      const availableMethod = paymentMethodOptions.value.find(
          (option) => option.text === userStore.paymentMethodName
      );
      if (availableMethod) {
        selectedPaymentMethod.value = availableMethod.value;
      }
    }
    console.log(selectedPaymentMethod.value);
  } catch (error) {
    console.error("Failed to fetch payment methods:", error);
  }
}

onMounted(async () => {
  await flightStore.fetchFlight(flightId);
  await flightStore.fetchBaggageTypes();
  await getPaymentMethods();
});
</script>

<style scoped>
.container {
  text-align: center;
}
.text-center {
  text-align: center;
}
</style>
