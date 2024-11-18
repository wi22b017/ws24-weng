<template>
  <div class="userdata-change">
    <h1 class="mt-5">Change Userdata</h1>
    <h6 class="mt-2">Just copied from our SEPJ, this is not working right now. You have to add also all fields from the backend users and address table.
      Maybe there is also an easier way to implement this (maybe with watchers so just the field that has actually changed gets sent with the patch request.</h6>
    <div class="container">
      <MoleculeManageUserdataForm/>
    </div>
  </div>
</template>

<script setup>

import MoleculeManageUserdataForm from "@/components/molecules/MoleculeManageUserdataForm.vue";
</script>

<style scoped>

</style>