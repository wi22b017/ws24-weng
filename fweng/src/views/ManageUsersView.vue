<template>
  <div class="container mt-4">
    <AtomHeading text="Manage Users" />

    <!-- Search Field -->
    <div class="mb-3">
      <input
          type="text"
          v-model="searchTerm"
          class="form-control"
          placeholder="Search users by any field..."
      />
    </div>

    <!-- User Table -->
    <UserTableTemplate
        v-if="!adminUserStore.isLoading && filteredUsers.length > 0"
        :users="filteredUsers"
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
    <div v-if="!adminUserStore.isLoading && filteredUsers.length === 0" class="text-center mt-5">
      <AtomText content="No users found." />
    </div>
  </div>
</template>

<script setup>
import AtomHeading from "@/components/atoms/AtomHeading.vue";
import AtomText from "@/components/atoms/AtomText.vue";
import UserTableTemplate from "@/components/template/UserTableTemplate.vue";
import {useAdminUserStore} from "@/store/adminUserStore";
import {computed, onMounted, ref, watch} from "vue";

const adminUserStore = useAdminUserStore();
const searchTerm = ref("");

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

// Computed property to filter users based on the search term
const filteredUsers = computed(() => {
  if (!searchTerm.value.trim()) {
    return adminUserStore.users; // No search term, return all users
  }

  const searchLower = searchTerm.value.toLowerCase();

  return adminUserStore.users.filter((user) => {
    return (
        user.firstName.toLowerCase().includes(searchLower) ||
        user.lastName.toLowerCase().includes(searchLower) ||
        user.username.toLowerCase().includes(searchLower) ||
        user.email.toLowerCase().includes(searchLower) ||
        user.role.toLowerCase().includes(searchLower) ||
        user.status.toLowerCase().includes(searchLower) ||
        user.gender.toLowerCase().includes(searchLower)
    );
  });
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
