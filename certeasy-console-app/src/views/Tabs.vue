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
                v-for="filterButton in filterButtons"
                :key="filterButton.id"
                :buttonProps="filterButton"
                @handleClick="navigateToRoute(filterButton.query)"
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
            <div
              v-if="
                currentRoute === 'TREE' && issuersList && issuersList.length > 0
              "
              class="px-16"
            >
              <TheTree
                :treeData="treeData"
                :getChildren="getChildren"
                :selectNode="handleSelectNode"
                v-for="treeData in issuersList"
                :key="treeData.id"
              />
            </div>
            <IssuersList
              v-if="issuersList.length > 0 && currentRoute !== 'TREE'"
              :issuersList="issuersList"
            />
            <IssuerCardNoContent
              v-if="!isLoading && issuersList.length === 0"
            />
          </div>
        </template>
        <template v-if="activeTab === 'certificates'">
          <TheModal
            :key="selectedTreeNode"
            :issuer="selectedTreeNode"
            @toggleSidebar="toggleSidebar"
            v-if="selectedTreeNode && isSidebarOpen"
          />
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
            <IssuerCardNoContent
              v-if="!isLoading && certificatesList.length === 0"
            />
          </div>
        </template>
        <TheFooter class="absolute bottom-0 left-0 px-16" />
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted, computed, shallowRef } from 'vue'
import { useRouter, useRoute } from 'vue-router'
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
import TheTree from '@/components/TheTree.vue'
import IssuersList from '@/components/IssuersList.vue'
import IssuerCardNoContent from '@/components/IssuerCardNoContent.vue'
import ImportFileIcon from '@/assets/icons/ImportFileIcon.vue'
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
import TheModal from '@/components/TheModal.vue'
import StorageIcon from '@/assets/icons/StorageIcon.vue'
import SubStorageIcon from '@/assets/icons/SubStorageIcon.vue'

const cogIcon = CogIcon
const apiIcon = ApiIcon
const importFileIcon = ImportFileIcon
const addFileIcon = AddFileIcon
const treeIcon = TreeIcon
const storageIcon = StorageIcon
const subStorageIcon = SubStorageIcon
const logo = {
  imgSrc: imgSrc,
  alt: 'Certeasy',
}
const issuersList = ref([])
const certificatesList = ref([])
const isLoading = ref(true)
const activeTab = ref('issuers')
const filterCertsIsOpen = ref(false)
const router = useRouter()
const route = useRoute()

const setActiveButton = (type) => {
  filterButtons.value = filterButtons.value.map((item) => {
    return { ...item, active: type === item.query ? true : false }
  })
}

const currentRoute = computed(() => route.query.type)

const filterButtons = shallowRef([
  {
    id: 0,
    text: 'All',

    active: false,
    disabled: issuersList.value.length,
    query: '',
  },
  {
    id: 1,
    icon: storageIcon,
    iconAlt: 'storage',
    text: 'Root',
    active: false,
    disabled: false,
    query: 'ROOT',
  },
  {
    id: 2,
    icon: subStorageIcon,
    iconAlt: 'sub-storage',
    text: 'Sub',
    active: false,
    disabled: false,
    query: 'SUB_CA',
  },
  {
    id: 3,
    icon: treeIcon,
    iconAlt: 'tree',
    text: 'Tree',
    active: false,
    disabled: false,
    query: 'TREE',
  },
])

const buttonProp2 = {
  id: 0,
  icon: addFileIcon,
  text: 'New Root CA',
}

const buttonProp4 = {
  id: 1,
  icon: importFileIcon,
  text: 'Import CA',
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

const fetchChildren = async (node) => {
  try {
    const res = await api.get(`issuers/${node.id}/children`)
    const data = await res
    node.children = data.data
  } catch (error) {
    console.error('Error fetching data', error)
  }
}

function findNodeInTrees(nodes, targetId) {
  for (const node of nodes) {
    if (node.id === targetId) {
      return node
    } else if (node.children) {
      const foundInChildren = findNodeInTrees(node.children, targetId)
      if (foundInChildren) {
        return foundInChildren
      }
    }
  }
  return null
}

const getChildren = (id) => {
  const node = findNodeInTrees(issuersList.value, id)
  fetchChildren(node)
}

const handleSelectNode = (id) => {
  if (
    selectedTreeNode.value &&
    selectedTreeNode.value.id &&
    selectedTreeNode.value.id !== id
  ) {
    unselectTreeNode()
  }

  if (
    (currentRoute.value === 'TREE' && !selectedTreeNode.value) ||
    (currentRoute.value === 'TREE' &&
      selectedTreeNode.value &&
      selectedTreeNode.value.id &&
      selectedTreeNode.value.id !== id)
  ) {
    const node = findNodeInTrees(issuersList.value, id)
    selectedTreeNode.value = node
    node.active = true
  }

  if (!currentRoute.value) {
    const node = issuersList.value.find((item) => item.id === id)
    selectedTreeNode.value = node
    node.active = true
  } else if (currentRoute.value !== 'TREE') {
    const node = issuersList.value.find((item) => item.id === id)
    selectedTreeNode.value = node
    node.active = true
  }

  if (!isSidebarOpen.value) {
    toggleSidebar()
  }
}

const navigateToRoute = async (query) => {
  router.push({ name: 'issuers', query: query ? { type: query } : '' })
  fetchIssuer(
    `/issuers${
      query ? (query === 'TREE' ? '?type=ROOT' : '?type=' + query) : ''
    }`
  )

  setActiveButton(query)
}

const fetchIssuer = async (url) => {
  isLoading.value = true
  try {
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
  fetchIssuer(
    `/issuers${
      currentRoute.value
        ? currentRoute.value === 'TREE'
          ? '?type=ROOT'
          : '?type=' + currentRoute.value
        : ''
    }`
  )
  fetchCertificates()
  setActiveButton(route.query.type ? route.query.type : '')
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
