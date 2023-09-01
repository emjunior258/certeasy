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
        <div class="font-light text-lg">
          <IconTextButton
            v-for="filterButton in filterButtons"
            :key="filterButton.id"
            :buttonProps="filterButton"
            @handleClick="navigateToRoute(filterButton.route)"
            class="mr-6 last:mr-0"
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
    <TheFooter />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useRouter, useRoute } from "vue-router";

import addFileIcon from "@/assets/icons/add-file.svg";
import api from "@/config/config";
import apiIcon from "@/assets/icons/api.svg";
import cogIcon from "@/assets/icons/cog.svg";
import TheFooter from "@/components/TheFooter.vue";
import IconActionButton from "@/components/buttons/IconActionButton.vue";
import IconTextButton from "@/components/buttons/IconTextButton.vue";
import imgSrc from "@/assets/logo.svg";
import importFileIcon from "@/assets/icons/import-file.svg";
import IssuerCard from "@/components/IssuerCard.vue";
import IssuerCardNoContent from "@/components/IssuerCardNoContent.vue";
import NavComponent from "@/components/NavComponent.vue";
import PlaceholderLoading from "@/components/loading/PlaceholderLoading.vue";
import storageIcon from "@/assets/icons/storage.svg";
import subStorageIcon from "@/assets/icons/sub-storage.svg";
import treeIcon from "@/assets/icons/tree.svg";

const router = useRouter();
const route = useRoute();
const issuersList = ref([]);
const loading = ref(true);
const countAll = computed(() => issuersList.value.length);
const countRoot = computed(
  () => issuersList.value.filter((item) => item.type == "ROOT").length
);
const disabledFilter = countAll === 0;

const countSub = 0;
const countTree = 0;

const fetchData = async (url = "/issuers") => {
  loading.value = true;
  try {
    const res = await api.get(url);
    const data = await res;
    issuersList.value = data.data;
  } catch (error) {
    console.error("Error fetching data", error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchData(`/issuers${route.query.type ? `?type=${route.query.type}` : ""}`);
});

const logo = {
  imgSrc: imgSrc,
  alt: "Certeasy",
};

const navLinks = [
  {
    id: 0,
    icon: apiIcon,
    altIcon: "API",
    text: "Open Api",
    linkHref: "#",
  },
  {
    id: 1,
    icon: cogIcon,
    altIcon: "settings",
    text: "Settings",
    linkHref: "#",
  },
];

const navigateToRoute = (routeURL) => {
  router.push(routeURL);
  fetchData("/issuers" + routeURL);
};

const filterButtons = ref([
  {
    id: 0,
    text: "All",
    amount: countAll,
    active: true,
    disabled: disabledFilter,
    route: "",
  },
  {
    id: 1,
    icon: storageIcon,
    iconAlt: "storage",
    text: "Root",
    amount: countRoot,
    disabled: disabledFilter,
    route: "/?type=ROOT",
  },
  {
    id: 2,
    icon: subStorageIcon,
    iconAlt: "sub-storage",
    text: "Sub",
    amount: countSub,
    active: false,
    disabled: disabledFilter,
    route: "/?type=SUB_CA",
  },
  {
    id: 3,
    icon: treeIcon,
    iconAlt: "tree",
    text: "Tree",
    amount: countTree,
    active: false,
    disabled: disabledFilter,
    route: "/",
  },
]);

const actionButtons = [
  {
    id: 0,
    icon: addFileIcon,
    text: "New Root CA",
  },
  {
    id: 1,
    icon: importFileIcon,
    text: "Import CA",
    outlined: true,
  },
];
</script>
