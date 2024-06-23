<template>
  <li
    class="border border-gray-200 rounded-lg shadow-xs px-5 py-3 mb-2 hover:bg-lightBlue60 cursor-pointer"
    :class="true && 'shadow-xs'"
    @click="handleSelectNode(issuer)"
  >
    <div class="flex justify-between">
      <div
        class="flex gap-6"
        :class="true && 'text-primary'"
      >
        <img
          src="../assets/avatar-placeholder.svg"
          :alt="'alt'"
          class="pt-1"
        />
        <div class="flex flex-col justify-between items-stretch gap-2">
          <h3 class="text-base text-text capitalize font-semibold leading-4">
            {{ issuer.name }}
          </h3>
          <span
            class="rounded text-[0.5rem] font-medium py-1 px-2 bg-lightBlue capitalize w-fit leading-3"
            >{{ issuer.type }}</span
          >
        </div>
      </div>
      <div class="flex items-center gap-4">
        <button class="rounded border-primary-0.6 bg-blue-pale">
          <img
            src="@/assets/icons/download.svg"
            alt="download"
          />
        </button>
        <button
          class="rounded border-red-0.6 bg-red-light"
          @click.stop="handleDeleteIssuer(issuer)"
        >
          <img
            src="../assets/icons/trash.svg"
            alt="delete"
          />
        </button>
      </div>
    </div>
  </li>
</template>

<script setup>
const { selectNode, issuer, deleteIssuer } = defineProps([
  'selectNode',
  'issuer',
  'deleteIssuer',
])
const handleDeleteIssuer = (issuer) => {
  deleteIssuer(issuer.id)
}

const handleSelectNode = (node) => {
  selectNode(node.id)
}
</script>
