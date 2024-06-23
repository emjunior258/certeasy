<template>
  <div class="fixed top-0 left-0 w-screen h-screen z-50">
    <div
      class="shadow-md w-[560px] h-fit max-h-[80%] max-w-[50%] fixed top-[50%] right-[50%] translate-x-[50%] translate-y-[-50%] bg-white z-50 overflow-y-auto rounded-lg"
    >
      <div class="p-8">
        <button
          @click="$emit('toggleSidebar')"
          class="cursor-pointer text-primary text-md block ml-auto mb-3"
        >
          <CloseIcon />
        </button>
        <div class="flex justify-between items-center">
          <span>
            <img
              src="@/assets/avatar-placeholder.svg"
              alt="avatar"
              class="w-[24px] h-[24px] rounded inline-block align-middle mr-2"
            />
            <h2
              class="text-base text-text font-semibold mb-1 inline align-middle"
            >
              {{ issuer && issuer.name }}
            </h2>
          </span>
        </div>
        <div class="flex items-center gap-3 mt-2 mb-4">
          <TheBadge
            :text="issuer && issuer.type === 'SUB_CA' ? 'SUB' : issuer.type"
          />
          <p class="text-[8px] font-normal text-black-0.4 flex items-center ga">
            <CopyIcon class="inline text-primary w-4 h-4" />
            {{ issuer && issuer.id }}
          </p>
          <DownIcon class="inline mr-2 text-primary" /><TheSquaredBadge
            class="mr-2"
            :text="issuer && issuer.children_count"
          />
        </div>

        <IssuerDetails
          class="mb-4"
          heading="Distinguished Name"
          :details="issuer && childDN"
        />

        <IssuerDetails
          class="mb-4"
          heading="Parent"
          :details="details"
          v-if="issuer.type !== 'ROOT'"
        />

        <div class="flex flex-wrap gap-2 mb-8">
          <IconActionButton
            v-for="actionButton in actionButtons"
            :key="actionButton.id"
            :buttonProps="actionButton"
            class="text-[9px] px-[8px] py-[6px]"
          />
        </div>
        <IconActionButton
          :buttonProps="deleteButton"
          @click="$emit('deleteIssuer')"
          class="text-[9px] px-[8px] py-[6px] bg-red"
        />
      </div>
    </div>
  </div>
</template>
<script setup>
import AddFileIcon from '@/assets/icons/AddFileIcon.vue'
import CopyIcon from '@/assets/icons/CopyIcon.vue'
import TrashIcon from '@/assets/icons/TrashIcon.vue'
import DownIcon from '@/assets/icons/DownIcon.vue'
import IconActionButton from '@/components/buttons/IconActionButton.vue'
import ImportFileIcon from '@/assets/icons/ImportFileIcon.vue'
import IssuerDetails from '@/components/IssuerDetails.vue'
import TheBadge from '@/components/text-containers/TheBadge.vue'
import TheSquaredBadge from '@/components/text-containers/TheSquaredBadge.vue'
import ViewIcon from '@/assets/icons/ViewIcon.vue'
import CloseIcon from '@/assets/icons/CloseIcon.vue'
const { issuer } = defineProps(['issuer'])

const addFileIcon = AddFileIcon
const importFileIcon = ImportFileIcon
const trashIcon = TrashIcon
const viewIcon = ViewIcon

const splitDN = (dn) => {
  const re = /[^\\], /
  return dn.split(re).map((item) => {
    const detail = item.split('=')
    if (detail[1].includes('\\')) {
      detail[1] = detail[1].split('\\').join('')
    }
    return { key: detail[0], value: detail[1] }
  })
}
let childDN
if (issuer) {
  childDN = splitDN(issuer && issuer.dn)
}

const details = [
  { key: 'CN', value: 'John Doe' },
  { key: 'OU', value: 'Sales' },
  { key: 'O', value: 'Company' },
  { key: 'C', value: 'US' },
  { key: 'L', value: 'New York' },
  { key: 'ST', value: 'New York' },
]

const actionButtons = [
  {
    id: 0,
    icon: addFileIcon,
    iconSize: 14,
    text: 'Create Sub CA',
  },
  {
    id: 1,
    icon: importFileIcon,
    iconSize: 14,
    text: 'Export PEM',
    outlined: true,
  },
  {
    id: 2,
    icon: addFileIcon,
    iconSize: 14,
    text: 'Issue New Certificate',
    outlined: true,
  },
  {
    id: 3,
    icon: viewIcon,
    iconSize: 14,
    text: 'View Certificates',
    outlined: true,
  },
]

const deleteButton = {
  icon: trashIcon,
  text: 'Delete',
}
</script>
