<template>
  <div class="">
    <NavbarComponent :logo="logo" :navLinks="navLinks" />
    <section>
      <div class="flex justify-between px-16 mt-12 mb-6 items-end">
        <div class="font-light text-lg margin-trim">
          <IconTextButton v-for="filterButton in filterButtons" :key="filterButton.id" :buttonProps="filterButton"
            class="mr-6" />
        </div>
        <div class="flex gap-11 font-medium text-lg">
          <ActionButton v-for="actionButton in actionButtons" :key="actionButton.id" :buttonProps="actionButton" />
        </div>
      </div>
      <ul class="px-16">
        <IssuerCard v-for="issuer in issuersList" :key="issuer.id" :issuer="issuer" />
      </ul>
    </section>
    <footer class="px-16 fixed bottom-0 w-full">
      <div class="border-t text-right pt-8 pb-11">
        <a href="#"><img src="../assets/icons/github.svg" alt="api" class="inline" />
          Read Documentation</a>
        <a href="#"><img src="../assets/icons/warning.svg" alt="api" class="inline ml-6" />
          Report Issue</a>
      </div>
    </footer>
  </div>
</template>

<script>
import NavbarComponent from "@/components/NavbarComponent.vue";
import IconTextButton from "@/components/buttons/IconTextButton.vue";
import ActionButton from "@/components/buttons/ActionButton.vue";
import IssuerCard from "@/components/IssuerCard.vue";
import axios from 'axios'
import { BaseUrl } from '@/config/config'
export default {
  components:{
    NavbarComponent,
    IconTextButton,
    ActionButton,
    IssuerCard
  },
  data() {
    return {
      logo : {
        imgSrc: require("@/assets/logo.svg"),
        alt: "Certeasy",
      },
      navLinks : [
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
      ],

      filterButtons : [
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
      ],
      actionButtons: [
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
      ],

      issuersList : [
      ],
    }
  },
  created(){
    this.getAllIssuers()
  },
  methods:{
    getAllIssuers(){
      axios.get(`${BaseUrl.url}/issuers`).then(res => {
        this.issuersList = res.data
      })
    }
  }

}
</script>

<style>
.margin-trim> :last-child {
  margin-right: 0;
}
</style>
