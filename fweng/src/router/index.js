import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import { useUserStore } from "@/store/user";

const routes = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/help",
    name: "help",
    component: () =>
        import(/* webpackChunkName: "help" */ "../views/HelpView.vue"),
  },
  {
    path: "/login",
    name: "login",
    component: () =>
        import(/* webpackChunkName: "login" */ "../components/organisms/OrganismLoginModal.vue"),
  },
  {
    path: "/userdata",
    name: "userdata",
    component: () =>
        import(/* webpackChunkName: "ManageUserdataView" */ "../views/ManageUserdataView.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/register",
    name: "register",
    component: () =>
        import(/* webpackChunkName: "register" */ "../components/organisms/OrganismRegisterModal.vue"),
  },
  {
    path: "/imprint",
    name: "imprint",
    component: () =>
        import(/* webpackChunkName: "imprint" */ "../views/ImprintView.vue"),
  },
  {
    path: "/myBookings",
    name: "myBookings",
    component: () =>
        import(/* webpackChunkName: "myBookings" */ "../views/MyBookingsView.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/manageBookings",
    name: "manageBookings",
    component: () =>
        import(/* webpackChunkName: "manageBookings" */ "../views/ManageBookingsView.vue"),
    meta: { requiresAdmin: true },
  },
  {
    path: "/manageUsers",
    name: "manageUsers",
    component: () =>
        import(/* webpackChunkName: "manageUsers" */ "../views/ManageUsersView.vue"),
    meta: { requiresAdmin: true },
  },
  {
    path: "/manageFlights",
    name: "manageFlights",
    component: () =>
        import(/* webpackChunkName: "manageFlights" */ "../views/ManageFlightsView.vue"),
    meta: { requiresAdmin: true },
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

// Navigation guard
router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore(); // Access the user store
  const isLoggedIn = userStore.isLoggedIn || false; // Check if the user is logged in
  const userRole = userStore.role || "GUEST"; // Retrieve the user's role from the store
  const requiresAuth = to.matched.some((record) => record.meta.requiresAuth);
  const requiresAdmin = to.matched.some((record) => record.meta.requiresAdmin);


  // Protect routes requiring proper authentication
  if (requiresAdmin && userRole !== "ADMIN" || requiresAuth && !isLoggedIn) {
    return router.replace({ name: "home" }); // Redirect to the home page
  }

  next(); // Allow navigation otherwise
});

export default router;
