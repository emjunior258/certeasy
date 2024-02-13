<template>
  <div>
    <NavComponent
      :logo="logo"
      :navLinks="navLinks"
      class="sticky top-0 left-0"
    />
    <div class="px-16 full-page-nav pt-36">
      <div
        class="border-x border-t border-gray-14 rounded-tr-lg h-full relative flex-col"
      >
        <ul class="absolute -top-[60px] -left-[1px] flex">
          <li>
            <a
              href="#"
              class="pl-10 pr-20 py-3 inline-block border border-b-0 border-r-0 border-gray-14 rounded-tl-lg"
            >
              <Issuer class="inline mr-2" /> Issuers</a
            >
          </li>
          <li>
            <a
              href="#"
              class="px-10 pr-20 py-3 inline-block border border-b-0 border-gray-14"
            >
              <Certificate class="inline mr-2" />
              Certificates</a
            >
          </li>
        </ul>
        <div class="flex justify-between px-16 mt-12 mb-6 items-end pb-6">
          <div class="font-light text-lg">
            <IconTextButton
              :buttonProps="buttonProp"
              @handleClick="navigateToRoute('/')"
              class="mr-6 last:mr-0"
            />
            <IconTextButton
              :buttonProps="buttonProp3"
              @handleClick="navigateToRoute('/')"
              class="mr-6 last:mr-0"
            />
            <IconTextButton
              :buttonProps="buttonProp3"
              @handleClick="navigateToRoute('/')"
              class="mr-6 last:mr-0"
            />
            <IconTextButton
              :buttonProps="buttonProp3"
              @handleClick="navigateToRoute('/')"
              class="mr-6 last:mr-0"
            />
          </div>
          <div class="flex gap-11">
            <IconActionButton :buttonProps="buttonProp2" />
            <IconActionButton :buttonProps="buttonProp4" />
          </div>
        </div>
        <ul class="px-16">
          <IssuerCard />
        </ul>
      </div>
    </div>
  </div>
</template>
<script setup>
import AddFileIcon from "@/assets/icons/AddFileIcon.vue";
import NavComponent from "@/components/NavComponent.vue";
import ApiIcon from "@/assets/icons/ApiIcon.vue";
import CogIcon from "@/assets/icons/CogIcon.vue";
import imgSrc from "@/assets/logo.svg";
import Issuer from "@/assets/icons/Issuer.vue";
import Certificate from "../assets/icons/Certificate.vue";
import IconActionButton from "@/components/buttons/IconActionButton.vue";
import IconTextButton from "@/components/buttons/IconTextButton.vue";
import IssuerCard from "@/components/IssuerCard.vue";
import TreeIcon from "@/assets/icons/TreeIcon.vue";
const cogIcon = CogIcon;
const apiIcon = ApiIcon;
const addFileIcon = AddFileIcon;
const treeIcon = TreeIcon;
const logo = {
  imgSrc: imgSrc,
  alt: "Certeasy",
};

const buttonProp = {
  id: 0,
  text: "All",
  amount: 0,
  active: false,
  disabled: false,
  query: "",
};

const buttonProp2 = {
  id: 0,
  icon: addFileIcon,
  text: "New Root CA",
};

const buttonProp3 = {
  id: 3,
  icon: treeIcon,
  iconAlt: "tree",
  text: "Tree",
  active: false,
  disabled: false,
  query: "TREE",
};
const buttonProp4 = {
  id: 0,
  icon: addFileIcon,
  text: "New Root CA",
  outlined: true,
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
