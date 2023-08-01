<template>
  <NavbarComponent
    :logo="logo"
    :navLinks="navLinks"
  />
  <section>
    <div class="flex justify-between px-16 mt-12 mb-6 items-end">
      <div class="font-light text-lg margin-trim">
        <IconButton
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
      <li class="border border-gray-200 rounded shadow px-11 py-6 mb-2">
        <div class="flex justify-between">
          <div class="flex gap-6">
            <img
              src="https://placehold.co/52x52"
              alt="Issuer photo"
              class="h-13 w-13 border border-primary rounded"
            />
            <div class="flex flex-col justify-between items-start">
              <h3 class="text-xl font-medium">My Issuer Name</h3>
              <span
                class="rounded-full py-0.5 px-2.5 bg-purple font-light text-xs"
                >Root</span
              >
            </div>
          </div>
          <div class="flex items-center gap-4">
            <button class="rounded border border-primary-0.6 p-2 bg-blue-light">
              <img
                src="../assets/icons/download.svg"
                alt="download"
              />
            </button>
            <button class="rounded border border-red-0.6 p-2 bg-red-light">
              <img
                src="../assets/icons/trash.svg"
                alt="delete"
              />
            </button>
          </div>
        </div>
      </li>
      <li class="border border-gray-200 rounded shadow px-11 py-6 mb-2">
        <div class="flex justify-between">
          <div class="flex gap-6">
            <img
              src="https://placehold.co/52x52"
              alt="Issuer photo"
              class="h-13 w-13 border border-primary rounded"
            />
            <div class="flex flex-col justify-between items-start">
              <h3 class="text-xl font-medium">My Issuer Name</h3>
              <span
                class="rounded-full py-0.5 px-2.5 bg-purple font-light text-xs"
                >Root</span
              >
            </div>
          </div>
          <div class="flex items-center gap-4">
            <button class="rounded border border-primary-0.6 p-2 bg-blue-light">
              <img
                src="../assets/icons/download.svg"
                alt="download"
              />
            </button>
            <button class="rounded border border-red-0.6 p-2 bg-red-light">
              <img
                src="../assets/icons/trash.svg"
                alt="delete"
              />
            </button>
          </div>
        </div>
      </li>
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
</template>

<script setup>
// @ is an alias to /src
import NavbarComponent from "@/components/NavbarComponent.vue";
import IconButton from "@/components/buttons/IconButton.vue";
import ActionButton from "@/components/buttons/ActionButton.vue";

const logo = {
  imgSrc: require("@/assets/logo.svg"),
  alt: "Certeasy",
};
const navLinks = [
  {
    id: 0,
    icon: require("@/assets/icons/api.svg"),
    altIcon: "API",
    text: "Open Api",
    linkHref: "#",
  },
  {
    id: 0,
    icon: require("@/assets/icons/cog.svg"),
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
    icon: require("@/assets/icons/storage.svg"),
    text: "Root",
    amount: 9,
  },
  {
    id: 2,
    icon: require("@/assets/icons/sub-storage.svg"),
    text: "Sub",
    amount: 16,
    active: false,
  },
  {
    id: 3,
    icon: require("@/assets/icons/tree.svg"),
    text: "Tree",
    amount: 16,
    active: false,
  },
];
const actionButtons = [
  {
    id: 0,
    icon: require("@/assets/icons/add-file.svg"),
    text: "New Root CA",
  },
  {
    id: 1,
    icon: require("@/assets/icons/import-file.svg"),
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
