<template>
  <tr>
    <td>{{ index + 1 }}</td>
    <td>{{ user.firstName }} {{ user.lastName }}</td>
    <td>{{ user.email }}</td>
    <td>
      <MoleculeUserStatusBadge :status="user.status" />
    </td>
    <td>
      <MoleculeUserActionButtons
          :onToggle="() => onToggle(user)"
          :onDelete="() => onDelete(user)"
      />
    </td>
  </tr>
</template>

<script setup>
import MoleculeUserStatusBadge from "@/components/molecules/MoleculeUserStatusBadge.vue";
import MoleculeUserActionButtons from "@/components/molecules/MoleculeUserActionButtons.vue";
import { defineProps } from "vue";

defineProps({
  user: {
    type: Object,
    required: true,
  },
  index: {
    type: Number,
    required: true,
  },
  onToggle: Function,
  onDelete: Function,
});
</script>
