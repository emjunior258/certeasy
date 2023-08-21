<template>
  <div class="">
    <NavComponent
      :logo="logo"
      :navLinks="navLinks"
    />

    <section>
      <div class="flex justify-between px-16 mt-12 mb-6 items-end">
        <div class="font-light text-lg margin-trim">
          <IconTextButton
            v-for="filterButton in filterButtons"
            :key="filterButton.id"
            :buttonProps="filterButton"
            class="mr-6"
          />
        </div>
        <div class="flex gap-11 font-medium text-lg">
          <ActionButton
            v-for="actionButton in actionButtons"
            :key="actionButton.id"
            :buttonProps="actionButton"
          />
        </div>
      </div>
      <ul class="px-16">
        <IssuerCard
          v-for="issuer in issuersList"
          :key="issuer.id"
          :issuer="issuer"
        />
      </ul>
    </section>
    <footer class="px-16 fixed bottom-0 w-full">
      <div class="border-t text-right pt-8 pb-11">
        <a href="#"
          ><img
            src="../assets/icons/github.svg"
            alt="api"
            class="inline"
          />
          Read Documentation</a
        >
        <a href="#"
          ><img
            src="../assets/icons/warning.svg"
            alt="api"
            class="inline ml-6"
          />
          Report Issue</a
        >
      </div>
    </footer>
  </div>
</template>

<script setup>
import NavComponent from "../components/NavComponent.vue";
import IconTextButton from "../components/buttons/IconTextButton.vue";
import ActionButton from "../components/buttons/ActionButton.vue";
import IssuerCard from "../components/IssuerCard.vue";
import api from "@/config/config";
import { ref, onMounted } from "vue";

const issuersList = ref([]);
const loading = ref(false);

const fetchData = async () => {
  loading.value = true;
  try {
    const res = await api.get("/issuers");
    const data = await res;
    issuersList.value = data.data;
  } catch (error) {
    console.error("Error fetching data", error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchData();
});

const logo = {
  imgSrc: "./src/assets/logo.svg",
  alt: "Certeasy",
};

const navLinks = [
  {
    id: 0,
    icon: "./src/assets/icons/api.svg",
    altIcon: "API",
    text: "Open Api",
    linkHref: "#",
  },
  {
    id: 0,
    icon: "./src/assets/icons/cog.svg",
    altIcon: "settings",
    text: "Settings",
    linkHref: "#",
  },
];

const filterButtons = [
  {
    id: 0,
    text: "All",
    amount: 16,
    active: true,
  },
  {
    id: 1,
    icon: "./src/assets/icons/storage.svg",
    text: "Root",
    amount: 9,
  },
  {
    id: 2,
    icon: "./src/assets/icons/sub-storage.svg",
    text: "Sub",
    amount: 16,
    active: false,
  },
  {
    id: 3,
    icon: "./src/assets/icons/tree.svg",
    text: "Tree",
    amount: 16,
    active: false,
  },
];

const actionButtons = [
  {
    id: 0,
    icon: "./src/assets/icons/add-file.svg",
    text: "New Root CA",
  },
  {
    id: 1,
    icon: "./src/assets/icons/import-file.svg",
    text: "Import CA",
    outlined: true,
  },
];
</script>

<style>
.margin-trim > :last-child {
  margin-right: 0;
}
</style>
