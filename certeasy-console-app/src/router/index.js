import { createRouter, createWebHistory } from "vue-router";
import ListOfIssuers from "../views/ListOfIssuers.vue";
import Tabs from "../views/Tabs.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "issuers",
      component: Tabs,
    },
  ],
});

export default router;
