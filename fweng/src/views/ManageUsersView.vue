<template>
  <div class="container mt-4">
    <AtomHeading text="Manage Users" />

    <!-- User Table -->
    <UserTableTemplate
        v-if="!adminUserStore.isLoading && adminUserStore.users.length > 0"
        :users="adminUserStore.users"
        :onToggle="toggleStatus"
        :onDelete="confirmDelete"
    />

    <!-- Loading State -->
    <div v-if="adminUserStore.isLoading" class="text-center mt-5">
      <div class="spinner-border" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
      <AtomText content="Loading users..." />
    </div>

    <!-- No Users Found -->
    <div v-if="!adminUserStore.isLoading && adminUserStore.users.length === 0" class="text-center mt-5">
      <AtomText content="No users found." />
    </div>
  </div>
</template>

<script setup>
import AtomHeading from "@/components/atoms/AtomHeading.vue";
import AtomText from "@/components/atoms/AtomText.vue";
import UserTableTemplate from "@/components/template/UserTableTemplate.vue";
import {useAdminUserStore} from "@/store/adminUserStore";
import {onMounted, watch} from "vue";

const adminUserStore = useAdminUserStore();

// Watch users and log updates
watch(
    () => adminUserStore.users,
    (newUsers) => {
      console.log("Users updated in ManageUsersView:", newUsers);
    },
    {deep: true}
);

onMounted(async () => {
  await adminUserStore.fetchUsers();
});

// Toggle user status
const toggleStatus = async (user) => {
  try {

    const newStatus = user.status === "ACTIVE" ? "INACTIVE" : "ACTIVE";
    await adminUserStore.updateUserStatus(user.id, newStatus);

  } catch (error) {
    console.error("Error toggling user status:", error);
    alert("Failed to update user status. Please try again.");
  }
};

// Confirm and delete user
const confirmDelete = async (user) => {
  try {
    if (confirm(`Are you sure you want to delete ${user.firstName} ${user.lastName}?`)) {
      await adminUserStore.deleteUser(user.id);
    }
  } catch (error) {
    console.error("Error deleting user:", error);
    alert("Failed to delete user. Please try again.");
  }
};
</script>
