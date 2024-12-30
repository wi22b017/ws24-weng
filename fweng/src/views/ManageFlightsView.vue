<template>
  <div class="container mt-4">
    <AtomHeading text="Manage Flights"/>

    <!-- Flight List Table-->
    <MoleculeFlightTable
        v-if="adminUserStore.flights.length>0"
        :flights="adminUserStore.flights"
    />

    <div v-if="fetchFlightError" class="alert alert-danger mt-3" role="alert">
      {{ fetchFlightError }}
    </div>
  </div>

</template>

<script setup>
import {onMounted, ref} from "vue";
import {useAdminUserStore} from "@/store/adminUserStore";
import MoleculeFlightTable from "@/components/molecules/MoleculeFlightTable.vue";
import AtomHeading from "@/components/atoms/AtomHeading.vue";


const adminUserStore = useAdminUserStore();
const fetchFlightError = ref('');

onMounted(async () => {

  const result = await adminUserStore.fetchFlights();

  if(result.success!==true){
    fetchFlightError.value = result.message;
  }

});

</script>

<style scoped>

</style>