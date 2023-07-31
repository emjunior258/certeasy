import { createRouter, createWebHistory } from "vue-router";
import ListOfUsers from "../views/ListOfUsers.vue";

const routes = [
  {
    path: "/",
    name: "list of users",
    component: ListOfUsers,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
