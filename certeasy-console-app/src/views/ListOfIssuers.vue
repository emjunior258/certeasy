<template>
  <PlaceholderLoading v-if="loading" />
  <div v-else>
    <NavComponent
      :logo="logo"
      :navLinks="navLinks"
      class="sticky top-0 left-0"
    />

    <section class="mb-[112px]">
      <div
        class="flex justify-between px-16 mt-12 mb-6 items-end border-b pb-6"
      >
        <div class="font-light text-lg">
          <IconTextButton
            v-for="filterButton in filterButtons"
            :key="filterButton.id"
            :buttonProps="filterButton"
            @handleClick="navigateToRoute(filterButton.query)"
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
      <div>
        <div
          v-if="
            currentRoute === 'TREE' &&
            filteredIssuersList &&
            filteredIssuersList.length > 0
          "
          class="px-16"
        >
          <TheTree
            :treeData="treeData"
            :getChildren="getChildren"
            :selectNode="handleSelectNode"
            v-for="treeData in filteredIssuersList"
            :key="treeData.id"
          />
        </div>
        <IssuersList
          class="px-16"
          v-if="issuersList.length > 0 && currentRoute !== 'TREE'"
          :issuersList="filteredIssuersList || issuersList"
          :selectNode="handleSelectNode"
        />
        <IssuerCardNoContent v-if="issuersList.length === 0" />
      </div>
    </section>
    <section>
      <Transition name="slide">
        <TheSidebar
          :key="selectedTreeNode"
          :issuer="selectedTreeNode"
          @toggleSidebar="toggleSidebar"
          v-if="selectedTreeNode && isSidebarOpen"
        />
      </Transition>
    </section>
    <TheFooter />
  </div>
</template>

<script setup>
import { ref, onMounted, computed, shallowRef } from "vue";
import { useRouter, useRoute } from "vue-router";

import AddFileIcon from "@/assets/icons/AddFileIcon.vue";
import api from "@/config/config";
import apiIcon from "@/assets/icons/api.svg";
import cogIcon from "@/assets/icons/cog.svg";
import TheFooter from "@/components/TheFooter.vue";
import IconActionButton from "@/components/buttons/IconActionButton.vue";
import IconTextButton from "@/components/buttons/IconTextButton.vue";
import imgSrc from "@/assets/logo.svg";
import ImportFileIcon from "@/assets/icons/ImportFileIcon.vue";
import IssuerCardNoContent from "@/components/IssuerCardNoContent.vue";
import IssuersList from "@/components/IssuersList.vue";
import NavComponent from "@/components/NavComponent.vue";
import PlaceholderLoading from "@/components/loading/PlaceholderLoading.vue";
import StorageIcon from "@/assets/icons/StorageIcon.vue";
import SubStorageIcon from "@/assets/icons/SubStorageIcon.vue";
import TheSidebar from "@/components/TheSidebar.vue";
import TheTree from "@/components/TheTree.vue";
import TreeIcon from "@/assets/icons/TreeIcon.vue";

const addFileIcon = AddFileIcon;
const importFileIcon = ImportFileIcon;
const storageIcon = StorageIcon;
const subStorageIcon = SubStorageIcon;
const treeIcon = TreeIcon;

const router = useRouter();
const route = useRoute();
const currentRoute = computed(() => route.query.type);
const issuersList = ref([]);
const filteredIssuersList = ref(null);
const selectedTreeNode = ref(null);
const loading = ref(true);
const isSidebarOpen = ref(false);

const toggleSidebar = () => {
  isSidebarOpen.value = !isSidebarOpen.value;
  if (selectedTreeNode && !isSidebarOpen.value) {
    unselectTreeNode();
    selectedTreeNode.value = null;
  }
};

const fetchData = async (url) => {
  loading.value = true;
  try {
    const res = await api.get(url);
    const data = await res;
    issuersList.value = data.data;
    setCounters(issuersList);
    filterIssuersList(
      route.query.type
        ? route.query.type === "TREE"
          ? "ROOT"
          : route.query.type
        : ""
    );
  } catch (error) {
    console.error("Error fetching data", error);
  } finally {
    loading.value = false;
  }
};

const fetchChildren = async (node) => {
  try {
    const res = await api.get(`issuers/${node.id}/children`);
    const data = await res;
    node.children = data.data;
  } catch (error) {
    console.error("Error fetching data", error);
  }
};

onMounted(() => {
  fetchData("/issuers");
  setActiveButton(route.query.type ? route.query.type : "");
});

const navigateToRoute = async (query) => {
  unselectTreeNode();
  selectedTreeNode.value = null;
  router.push({ name: "issuers", query: query ? { type: query } : "" });
  filterIssuersList(query === "TREE" ? "ROOT" : query);
  setActiveButton(query);
};

const setActiveButton = (type) => {
  filterButtons.value = filterButtons.value.map((item) => {
    return { ...item, active: type === item.query ? true : false };
  });
};

const setCounters = (list) => {
  filterButtons.value[0].amount = list.value.length;
  filterButtons.value[1].amount = list.value.filter((item) => {
    return item.type === "ROOT";
  }).length;
  filterButtons.value[2].amount = list.value.filter((item) => {
    return item.type === "SUB_CA";
  }).length;
};

const filterIssuersList = (type) => {
  if (type === "") {
    filteredIssuersList.value = null;
  } else {
    filteredIssuersList.value = issuersList.value.filter((item) => {
      return item.type === type;
    });
  }
};

function findNodeInTrees(nodes, targetId) {
  for (const node of nodes) {
    if (node.id === targetId) {
      return node;
    } else if (node.children) {
      const foundInChildren = findNodeInTrees(node.children, targetId);
      if (foundInChildren) {
        return foundInChildren;
      }
    }
  }
  return null;
}

const unselectTreeNode = () => {
  let node;
  if (selectedTreeNode.value)
    node = findNodeInTrees(issuersList.value, selectedTreeNode.value.id);
  if (node) delete node.active;
  issuersList.value.forEach((item) => {
    delete item.active;
  });
};

const handleSelectNode = (id) => {
  if (
    selectedTreeNode.value &&
    selectedTreeNode.value.id &&
    selectedTreeNode.value.id !== id
  ) {
    unselectTreeNode();
  }

  if (
    (currentRoute.value === "TREE" && !selectedTreeNode.value) ||
    (currentRoute.value === "TREE" &&
      selectedTreeNode.value &&
      selectedTreeNode.value.id &&
      selectedTreeNode.value.id !== id)
  ) {
    const node = findNodeInTrees(filteredIssuersList.value, id);
    selectedTreeNode.value = node;
    node.active = true;
  }

  if (!currentRoute.value) {
    const node = issuersList.value.find((item) => item.id === id);
    selectedTreeNode.value = node;
    node.active = true;
  } else if (currentRoute.value !== "TREE") {
    const node = filteredIssuersList.value.find((item) => item.id === id);
    selectedTreeNode.value = node;
    node.active = true;
  }

  if (!isSidebarOpen.value) {
    toggleSidebar();
  }
};

const getChildren = (id) => {
  const node = findNodeInTrees(filteredIssuersList.value, id);
  fetchChildren(node);
};

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

const filterButtons = shallowRef([
  {
    id: 0,
    text: "All",
    amount: 0,
    active: false,
    disabled: false,
    query: "",
  },
  {
    id: 1,
    icon: storageIcon,
    iconAlt: "storage",
    text: "Root",
    amount: 0,
    active: false,
    disabled: false,
    query: "ROOT",
  },
  {
    id: 2,
    icon: subStorageIcon,
    iconAlt: "sub-storage",
    text: "Sub",
    amount: 0,
    active: false,
    disabled: false,
    query: "SUB_CA",
  },
  {
    id: 3,
    icon: treeIcon,
    iconAlt: "tree",
    text: "Tree",
    active: false,
    disabled: false,
    query: "TREE",
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
<style scoped>
.slide-enter-active {
  transition: transform 0.8s ease-in-out;
}

.slide-leave-active {
  transition: transform 0.3s ease-in-out;
}

.slide-enter-from,
.slide-leave-to {
  transform: translate(500px);
}
</style>
