import { createRouter, createWebHistory } from "vue-router";
import ListOfIssuers from "../views/ListOfIssuers.vue";
import TreeView from "../views/TreeView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "issuers",
      component: ListOfIssuers,
    },
    {
      path: "/tree",
      name: "tree",
      component: TreeView,
    },
  ],
});

export default router;
