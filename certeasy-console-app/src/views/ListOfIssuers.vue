<template>
  <PlaceholderLoading v-if="loading" />
  <div
    v-else
    class=""
  >
    <NavComponent
      :logo="logo"
      :navLinks="navLinks"
      class="sticky top-0 left-0"
    />

    <section class="mb-[112px]">
      <div class="flex justify-between px-16 mt-12 mb-6 items-end">
        <div class="font-light text-lg margin-trim">
          <IconTextButton
            v-for="filterButton in filterButtons"
            :key="filterButton.id"
            :buttonProps="filterButton"
            class="mr-6"
          />
        </div>
        <div class="flex gap-11">
          <IconActionButton
            v-for="actionButton in actionButtons"
            :key="actionButton.id"
            :buttonProps="actionButton"
          />
        </div>
      </div>
      <ul
        class="px-16"
        v-if="issuersList.length > 0"
      >
        <IssuerCard
          v-for="issuer in issuersList"
          :key="issuer.id"
          :issuer="issuer"
          data-test="issuer"
        />
      </ul>
      <IssuerCardNoContent v-else />
    </section>
    <footer class="px-16 fixed bottom-0 w-full bg-white">
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
import NavComponent from "@/components/NavComponent.vue";
import IconTextButton from "@/components/buttons/IconTextButton.vue";
import IconActionButton from "@/components/buttons/IconActionButton.vue";
import IssuerCard from "@/components/IssuerCard.vue";
import PlaceholderLoading from "@/components/loading/PlaceholderLoading.vue";
import IssuerCardNoContent from "@/components/IssuerCardNoContent.vue";
import api from "@/config/config";
import { ref, onMounted, computed } from "vue";

const issuersList = ref([]);
const loading = ref(true);
const countAll = computed(() => issuersList.value.length);
const countRoot = computed(
  () => issuersList.value.filter((item) => item.type == "ROOT").length
);
const disabledFilter = countAll === 0;

const countSub = 0;
const countTree = 0;

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
    id: 1,
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
    amount: countAll,
    active: true,
    disabled: disabledFilter,
  },
  {
    id: 1,
    icon: "./src/assets/icons/storage.svg",
    iconAlt: "storage",
    text: "Root",
    amount: countRoot,
    disabled: disabledFilter,
  },
  {
    id: 2,
    icon: "./src/assets/icons/sub-storage.svg",
    iconAlt: "sub-storage",
    text: "Sub",
    amount: countSub,
    active: false,
    disabled: disabledFilter,
  },
  {
    id: 3,
    icon: "./src/assets/icons/tree.svg",
    iconAlt: "tree",
    text: "Tree",
    amount: countTree,
    active: false,
    disabled: disabledFilter,
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
