<template>
  <NavComponent
    :logo="logo"
    :navLinks="navLinks"
    class="sticky top-0 left-0"
  />
  <div class="px-16 full-page-nav pt-36">
    <div
      class="border-x border-t border-gray-14 rounded-tr-lg h-full relative flex-col"
    >
      <ul class="absolute -top-[50px] -left-[1px] flex">
        <li>
          <a
            href="#"
            class="pl-10 pr-20 py-3 inline-block border border-b-0 border-gray-14 rounded-tl-lg"
            >Issuers</a
          >
        </li>
        <li>
          <a
            href="#"
            class="px-10 pr-20 py-3 inline-block border border-b-0 border-gray-14"
            >Certificates</a
          >
        </li>
      </ul>
    </div>
  </div>
</template>
<script setup>
import NavComponent from "@/components/NavComponent.vue";
import ApiIcon from "@/assets/icons/ApiIcon.vue";
import CogIcon from "@/assets/icons/CogIcon.vue";
import imgSrc from "@/assets/logo.svg";

const cogIcon = CogIcon;
const apiIcon = ApiIcon;

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
</script>

<style scoped>
.full-page-nav {
  height: calc(100vh - 68px);
}
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
