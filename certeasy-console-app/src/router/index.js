import { createRouter, createWebHistory } from "vue-router";
import ListOfIssuers from "../views/ListOfIssuers.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "issuers.list",
      component: ListOfIssuers,
    },
  ],
});

export default router;
