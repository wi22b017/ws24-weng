<template>
  <div class="container mt-4">
    <div class="row justify-content-center">
      <div class="col-6">
    <AtomHeading text="Edit User Profile"/>
    <MoleculeEditUserProfileForm v-if="Object.keys(currentUser).length > 0" :initial-values="currentUser" @userDataUpdated="handleUserDataUpdated" />
    <div v-else>Loading user data...</div>
      </div>
    </div>
  </div>

</template>
<script setup>
import {useRoute} from "vue-router";
import MoleculeEditUserProfileForm from "@/components/molecules/MoleculeEditUserProfileForm.vue";
import {useAdminUserStore} from "@/store/adminUserStore";
import {onMounted, ref} from "vue";
import AtomHeading from "@/components/atoms/AtomHeading.vue";

const route = useRoute();
const userId = route.params.userId;
const currentUser = ref({})

const adminUserStore = useAdminUserStore();

onMounted(async() => {
  try {
   currentUser.value =  await adminUserStore.fetchUserById(userId);
   console.log("currentUser", currentUser.value)
  }catch (error) {
    console.error("Error fetching user:", error);
  }

})

async function handleUserDataUpdated() {
  currentUser.value =  await adminUserStore.fetchUserById(userId);
}
</script>