import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";

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
        import(
            /* webpackChunkName: "login" */ "../components/organisms/OrganismLoginModal.vue"
            ),
  },
  {
    path: "/userdata",
    name: "userdata",
    component: () =>
        import(/* webpackChunkName: "ManageUserdataView" */ "../views/ManageUserdataView.vue"),
  },
  {
    path: "/register",
    name: "register",
    component: () =>
        import(
            /* webpackChunkName: "register" */ "../components/organisms/OrganismRegisterModal.vue"
            ),
  },

  {
    path: "/imprint",
    name: "imprint",
    component: () =>
        import(/* webpackChunkName: "imprint" */ "../views/ImprintView.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
