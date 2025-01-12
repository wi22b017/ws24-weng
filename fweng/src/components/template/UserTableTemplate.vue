<template>
  <div class="table-responsive">
    <table class="table table-striped text-center">
      <thead>
      <tr>
        <th>#</th>
        <th>Full Name</th>
        <th>Email</th>
        <th>Status</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(user, index) in users" :key="user.id">
        <td>{{ index + 1 }}</td>
        <td>{{ user.firstName }} {{ user.lastName }}</td>
        <td>{{ user.email }}</td>
        <td>
          <MoleculeUserStatusBadge :status="user.status" />
        </td>
        <td class="actions-cell">
          <MoleculeUserActionButtons
              :onToggle="() => onToggle(user)"
              :onDelete="() => onDelete(user)"
          />
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import MoleculeUserStatusBadge from "@/components/molecules/MoleculeUserStatusBadge.vue";
import MoleculeUserActionButtons from "@/components/molecules/MoleculeUserActionButtons.vue";
import { defineProps } from "vue";

defineProps({
  users: {
    type: Array,
    required: true,
  },
  onToggle: {
    type: Function,
    required: true,
  },
  onDelete: {
    type: Function,
    required: true,
  },
});
</script>

<style>
.actions-cell {
  vertical-align: middle;
}
</style>
