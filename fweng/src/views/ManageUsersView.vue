<template>
  <div class="container mt-4">
    <AtomHeading text="Manage Users" />

    <!-- User Table -->
    <UserTableTemplate
        v-if="!isLoading && users.length > 0"
        :users="adminUserStore.users"
        :onToggle="toggleStatus"
        :onDelete="confirmDelete"
    />

    <!-- Loading State -->
    <div v-if="isLoading" class="text-center mt-5">
      <div class="spinner-border" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
      <AtomText content="Loading users..." />
    </div>

    <!-- No Users Found -->
    <div v-if="!isLoading && users.length === 0" class="text-center mt-5">
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
const {users, isLoading} = adminUserStore;

// Watch users and log updates
watch(
    () => users,
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
    console.log("Toggling user status:", user);
    const newStatus = user.status === "ACTIVE" ? "INACTIVE" : "ACTIVE";
    await adminUserStore.updateUserStatus(user.id, newStatus);
    console.log(`Status updated to ${newStatus} for user ${user.id}`);
  } catch (error) {
    console.error("Error toggling user status:", error);
    alert("Failed to update user status. Please try again.");
  }
};

// Confirm and delete user
const confirmDelete = async (user) => {
  try {
    console.log("Attempting to delete user:", user);
    if (confirm(`Are you sure you want to delete ${user.firstName} ${user.lastName}?`)) {
      await adminUserStore.deleteUser(user.id);
      console.log(`User deleted: ${user.id}`);
    }
  } catch (error) {
    console.error("Error deleting user:", error);
    alert("Failed to delete user. Please try again.");
  }
};
</script>
