<template>
  <div
    class="shadow-md w-[500px] h-screen fixed top-0 right-0 bg-white z-50 overflow-y-scroll"
  >
    <div class="p-8 mb-[104px]">
      <div class="flex justify-between items-center">
        <span
          @click="$emit('toggleSidebar')"
          class="cursor-pointer p-4"
          >x</span
        >
        <img
          src="@/assets/avatar-placeholder.svg"
          alt="avatar"
          class="w-[52px] h-[52px] rounded ml-auto"
        />
      </div>
      <div class="py-6">
        <h2 class="text-xl text-primary font-medium mb-1">
          {{ issuer && issuer.name }}
        </h2>
        <p class="text-sm font-light text-black-0.4 mb-2">
          {{ issuer && issuer.id }}
          <CopyIcon class="inline" />
        </p>
        <DownIcon class="inline mr-2 text-primary" /><TheSquaredBadge
          class="mr-2"
          :text="issuer && issuer.children_count"
        /><TheBadge
          :text="issuer && issuer.type === 'SUB_CA' ? 'SUB' : issuer.type"
        />
      </div>
      <div class="py-6 border-t">
        <IssuerDetails
          heading="Distinguished Name"
          :details="issuer && childDN"
        />
      </div>
      <div class="py-6 border-t">
        <IssuerDetails
          heading="Parent"
          :details="details"
        />
      </div>
      <div class="flex flex-wrap gap-4 py-6 border-t">
        <IconActionButton
          v-for="actionButton in actionButtons"
          :key="actionButton.id"
          :buttonProps="actionButton"
          class="text-sm px-[16px] py-[8px]"
        />
      </div>
    </div>
    <div class="fixed w-[500px] bottom-0 right-0 px-8 bg-white">
      <div class="border-t text-left pt-8 pb-8">
        <IconActionButton
          :buttonProps="deleteButton"
          class="text-sm px-[16px] py-[8px] bg-red"
        />
      </div>
    </div>
  </div>
</template>
<script setup>
import AddFileIcon from "@/assets/icons/AddFileIcon.vue";
import CopyIcon from "@/assets/icons/CopyIcon.vue";
import TrashIcon from "@/assets/icons/TrashIcon.vue";
import DownIcon from "@/assets/icons/DownIcon.vue";
import IconActionButton from "@/components/buttons/IconActionButton.vue";
import ImportFileIcon from "@/assets/icons/ImportFileIcon.vue";
import IssuerDetails from "@/components/IssuerDetails.vue";
import TheBadge from "@/components/text-containers/TheBadge.vue";
import TheSquaredBadge from "@/components/text-containers/TheSquaredBadge.vue";
import ViewIcon from "@/assets/icons/ViewIcon.vue";

const { issuer } = defineProps(["issuer"]);

const addFileIcon = AddFileIcon;
const importFileIcon = ImportFileIcon;
const trashIcon = TrashIcon;
const viewIcon = ViewIcon;

const splitDN = (dn) => {
  return dn.split(", ").map((item) => {
    const detail = item.split("=");
    return { key: detail[0], value: detail[1] };
  });
};
let childDN;
if (issuer) {
  childDN = splitDN(issuer && issuer.dn);
}

const details = [
  { key: "CN", value: "John Doe" },
  { key: "OU", value: "Sales" },
  { key: "O", value: "Company" },
  { key: "C", value: "US" },
  { key: "L", value: "New York" },
  { key: "ST", value: "New York" },
];

const actionButtons = [
  {
    id: 0,
    icon: addFileIcon,
    text: "Create Sub CA",
  },
  {
    id: 1,
    icon: importFileIcon,
    text: "Export PEM",
    outlined: true,
  },
  {
    id: 2,
    icon: addFileIcon,
    text: "Issue New Certificate",
    outlined: true,
  },
  {
    id: 3,
    icon: viewIcon,
    text: "View Certificates",
    outlined: true,
  },
];

const deleteButton = {
  icon: trashIcon,
  text: "Delete",
};
</script>
