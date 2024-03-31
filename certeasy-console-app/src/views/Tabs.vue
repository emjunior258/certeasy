wwwwwwww
<template>
  <div>
    <NavComponent
      :logo="logo"
      :navLinks="navLinks"
      class="sticky top-0 left-0"
    />
    <div class="px-16 full-page-nav pt-[104px]">
      <div
        class="border-x border-t border-gray-14 rounded-tr-lg h-full relative flex-col"
      >
        <ul class="absolute -top-[60px] -left-[1px] flex">
          <li @click="activeTab = 'issuers'">
            <a
              href="#"
              class="pl-10 pr-20 py-3 inline-block border border-b-0 border-r-0 border-gray-14 rounded-tl-lg"
              :class="{
                'bg-lightBlue': activeTab === 'issuers',
              }"
            >
              <Issuer class="inline mr-2" /> Issuers</a
            >
          </li>
          <li @click="activeTab = 'certificates'">
            <a
              href="#"
              class="px-10 pr-20 py-3 inline-block border border-b-0 border-gray-14"
              :class="{
                'bg-lightBlue': activeTab === 'certificates',
              }"
            >
              <Certificate class="inline mr-2" />
              Certificates</a
            >
          </li>
        </ul>
        <template v-if="activeTab === 'issuers'">
          <div class="px-16 flex justify-between mt-12 mb-6 items-end pb-6">
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
          <div
            class="pl-16 mr-4 pr-8 overflow-y-auto max-h-[calc(100%-220px)] gutter-stable"
          >
            <IssuersList
              v-if="issuersList.length > 0"
              :issuersList="issuersList"
            />
            <IssuerCardNoContent v-if="issuersList.length === 0" />
          </div>
        </template>
        <template v-if="activeTab === 'certificates'">
          <div class="px-16 flex justify-between mt-12 mb-6 items-end">
            <div
              class="w-full border border-primary px-5 py-3 rounded-lg bg-lightBlue flex justify-between"
            >
              <div class="flex items-center gap-1">
                <IssuerIcon class="w-6 h-6" />
                <span class="text-primary font-normal"> All Issuers </span>
              </div>
              <ChevronDown />
            </div>
          </div>
          <div class="px-16 flex justify-between mt-8 mb-5 items-end">
            <div class="flex gap-5">
              <div
                class="border border-primary px-4 py-2 rounded-lg bg-lightBlue flex justify-between gap-2 items-center relative hover:cursor-pointer"
                @click="filterCertsIsOpen = !filterCertsIsOpen"
              >
                <span class="font-normal"> All </span>
                <ChevronDown :class="{ 'rotate-180': filterCertsIsOpen }" />
                <ul
                  class="absolute top-12 left-0 bg-white rounded-lg shadow-xl p-5 min-w-[264px]"
                  v-if="filterCertsIsOpen"
                >
                  <li
                    class="px-5 py-2 rounded-lg hover:bg-lightBlue hover:cursor-pointer"
                  >
                    All
                  </li>
                  <li
                    class="px-5 py-2 rounded-lg hover:bg-lightBlue hover:cursor-pointer"
                  >
                    <ShieldIcon class="inline align-top mr-1" />
                    CA
                  </li>
                  <li
                    class="px-5 py-2 rounded-lg hover:bg-lightBlue hover:cursor-pointer"
                  >
                    <PadlockerIcon class="inline align-top mr-1" />
                    TLS Server
                  </li>
                  <li
                    class="px-5 py-2 rounded-lg hover:bg-lightBlue hover:cursor-pointer"
                  >
                    <PersonIcon class="inline align-top mr-1" />
                    Personal
                  </li>
                  <li
                    class="px-5 py-2 rounded-lg hover:bg-lightBlue hover:cursor-pointer"
                  >
                    <BriefcaseIcon class="inline align-top mr-1" />
                    Employee
                  </li>
                  <li
                    class="px-5 py-2 rounded-lg hover:bg-lightBlue hover:cursor-pointer"
                  >
                    <EditIcon class="inline align-top mr-1" />
                    Custom
                  </li>
                </ul>
              </div>
              <form class="flex">
                <input
                  type="text"
                  class="h-full border border-primary rounded-l-lg pl-4 w-[350px]"
                  placeholder="Search Certicate"
                />
                <button class="h-full bg-primary rounded-r-lg p-2">
                  <Search />
                </button>
              </form>
            </div>
            <IconActionButton :buttonProps="buttonProp2" />
          </div>
          <div
            class="pl-16 mr-4 pr-8 overflow-y-auto max-h-[calc(100%-268px)] gutter-stable"
          >
            <IssuersList
              v-if="certificatesList.length > 0"
              :issuersList="certificatesList"
            />
            <IssuerCardNoContent v-if="certificatesList.length === 0" />
          </div>
        </template>
        <CertificatesTab />
        <TheFooter class="absolute bottom-0 left-0 px-16" />
      </div>
    </div>
  </div>
</template>
<script setup>
import AddFileIcon from '@/assets/icons/AddFileIcon.vue'
import NavComponent from '@/components/NavComponent.vue'
import api from '@/config/config'
import ApiIcon from '@/assets/icons/ApiIcon.vue'
import CogIcon from '@/assets/icons/CogIcon.vue'
import imgSrc from '@/assets/logo.svg'
import Issuer from '@/assets/icons/Issuer.vue'
import Certificate from '../assets/icons/Certificate.vue'
import IconActionButton from '@/components/buttons/IconActionButton.vue'
import IconTextButton from '@/components/buttons/IconTextButton.vue'
import IssuersList from '@/components/IssuersList.vue'
import IssuerCardNoContent from '@/components/IssuerCardNoContent.vue'
import TreeIcon from '@/assets/icons/TreeIcon.vue'
import ChevronDown from '@/assets/icons/ChevronDown.vue'
import Search from '@/assets/icons/Search.vue'
import TheFooter from '@/components/TheFooter.vue'
import ShieldIcon from '@/assets/icons/ShieldIcon.vue'
import PadlockerIcon from '@/assets/icons/PadlockerIcon.vue'
import PersonIcon from '@/assets/icons/PersonIcon.vue'
import BriefcaseIcon from '@/assets/icons/BriefcaseIcon.vue'
import EditIcon from '@/assets/icons/EditIcon.vue'
import IssuerIcon from '@/assets/IssuerIcon.vue'
import { onMounted, ref } from 'vue'
const cogIcon = CogIcon
const apiIcon = ApiIcon
const addFileIcon = AddFileIcon
const treeIcon = TreeIcon
const logo = {
  imgSrc: imgSrc,
  alt: 'Certeasy',
}
const issuersList = ref([])
const certificatesList = ref([])
const isLoading = ref(true)
const activeTab = ref('issuers')
const filterCertsIsOpen = ref(false)

const buttonProp = {
  id: 0,
  text: 'All',
  amount: 0,
  active: false,
  disabled: false,
  query: '',
}

const buttonProp2 = {
  id: 0,
  icon: addFileIcon,
  text: 'New Root CA',
}

const buttonProp3 = {
  id: 3,
  icon: treeIcon,
  iconAlt: 'tree',
  text: 'Tree',
  active: false,
  disabled: false,
  query: 'TREE',
}
const buttonProp4 = {
  id: 0,
  icon: addFileIcon,
  text: 'New Root CA',
  outlined: true,
}
const navLinks = [
  {
    id: 0,
    icon: apiIcon,
    altIcon: 'API',
    text: 'Open Api',
    linkHref: '#',
  },
  {
    id: 1,
    icon: cogIcon,
    altIcon: 'settings',
    text: 'Settings',
    linkHref: '#',
  },
]

const fetchIssuer = async (url) => {
  try {
    isLoading.value = true
    const res = await api.get(url)
    const data = await res
    issuersList.value = data.data
  } catch (error) {
    console.error('Error fetching issuer', error)
  } finally {
    isLoading.value = false
  }
}

const fetchCertificates = async () => {
  try {
    isLoading.value = true
    const res = await api.get('certificates')
    const data = await res
    certificatesList.value = data.data
  } catch (error) {
    console.error('Error fetching certificates', error)
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  fetchIssuer('/issuers')
  fetchCertificates()
})
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

.gutter-stable {
  scrollbar-gutter: stable;
}
</style>
