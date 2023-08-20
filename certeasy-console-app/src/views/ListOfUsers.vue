<template>
  <div class="">
    <NavbarComponent />
    <section class="mb-[104px]">
      <div class="flex justify-between px-16 mt-12 mb-6 items-end">
        <div class="font-light text-lg margin-trim">
          <IconTextButton v-for="filterButton in filterButtons" :key="filterButton.id" :buttonProps="filterButton" />
        </div>
        <div class="flex gap-11 font-medium text-lg">
          <ActionButton />
        </div>
      </div>
      <ul class="px-16" v-if="issuersList.length > 0">
        <IssuerCard v-for="issuer in issuersList" :key="issuer.id" :issuer="issuer" />
      </ul>
      <ul class="px-16" v-else>
        <IssuerCardNoContent/>
      </ul>
    </section>
    <footer class="px-16 fixed bottom-0 w-full bg-white">
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
import NavbarComponent from '../components/NavbarComponent.vue';
import IssuerCard from '../components/IssuerCard.vue';
import IssuerCardNoContent from '../components/IssuerCardNoContent.vue';
import IconTextButton from '../components/buttons/IconTextButton.vue';
import ActionButton from '../components/buttons/ActionButton.vue';
import api from '../config/config'
export default {
  data() {
    return {
      issuersList: [],
      filterButtons: [
        {
          id: 0,
          text: "All",
          amount: 16,
          active: true,
        },
      ],

    }
  },
  components: {
    NavbarComponent,
    IssuerCard,
    IconTextButton,
    ActionButton,
    IssuerCardNoContent
  },
  created() {
    this.getAllIssuers()
  },
  methods: {
    getAllIssuers() {

      api.get(`/issuers`)
      .then(res => {
        this.issuersList = res.data
      })
      .catch(error => {

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
