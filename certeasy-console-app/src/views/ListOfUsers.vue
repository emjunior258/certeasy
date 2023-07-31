<template>
  <NavbarComponent
    :logo="logo"
    :navLinks="navLinks"
  />
  <section>
    <div class="flex justify-between px-16 mt-12 mb-3">
      <div class="font-light text-lg">
        <button
          class="rounded py-1 px-4 bg-primary text-white"
          type="button"
        >
          All (16)</button
        ><button
          class="rounded py-1 px-4"
          type="button"
        >
          <img
            src="../assets/icons/storage.svg"
            alt="api"
            class="inline"
          />
          Root (9)</button
        ><button
          class="rounded py-1 px-4"
          type="button"
        >
          <img
            src="../assets/icons/sub-storage.svg"
            alt="api"
            class="inline"
          />
          Sub (7)</button
        ><button
          class="rounded"
          type="button"
        >
          <img
            src="../assets/icons/tree.svg"
            alt="tree"
            class="inline"
          />
          Tree
        </button>
      </div>
      <div class="flex gap-11 font-medium text-lg">
        <button
          class="rounded py-3 px-6 bg-primary text-white"
          type="button"
        >
          New Root CA
          <img
            src="../assets/icons/add-file.svg"
            alt="add"
            class="inline ml-2"
          />
        </button>
        <button
          class="rounded py-3 px-6 border border-primary text-primary"
          type="button"
        >
          Import CA
          <img
            src="../assets/icons/import-file.svg"
            alt="import"
            class="inline ml-2"
          />
        </button>
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
</script>
