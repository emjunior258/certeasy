<template>
  <li
    class="border border-gray-200 rounded-lg shadow px-5 py-3 mb-2 hover:text-primary"
    :class="true && 'shadow shadow-primary-0.6'"
  >
    <div class="flex justify-between">
      <div
        class="flex gap-6 cursor-pointer"
        :class="true && 'text-primary'"
        @click="handleSelectNode(issuer)"
      >
        <img
          src="../assets/avatar-placeholder.svg"
          :alt="'alt'"
          class="pt-1"
        />
        <div class="flex flex-col justify-between items-stretch">
          <h3 class="text-base font-medium text-text capitalize">issuer</h3>
          <span
            class="rounded text-[0.5rem] py-0.5 px-2.5 bg-purple font-light capitalize"
            >Sub</span
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
        <button class="rounded border-red-0.6 bg-red-light">
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
// const { selectNode } = defineProps(["selectNode", "issuer"]);

// const handleSelectNode = (node) => {
//   selectNode(node.id);
// };
</script>
