<template>
  <li class="border border-gray-200 rounded shadow px-11 py-6 mb-2">
    <div class="flex justify-between">
      <div class="flex gap-6">
        <img
          src="../assets/avatar-placeholder.svg"
          :alt="issuer.altText"
          class="h-[54px] w-[54px]"
        />
        <div class="flex flex-col justify-between items-start">
          <h3 class="text-xl font-medium">{{ issuer.name }}</h3>
          <span
            class="rounded-full py-0.5 px-2.5 bg-purple font-light text-xs"
            >{{ issuer.type === "SUB_CA" ? "SUB" : issuer.type }}</span
          >
        </div>
      </div>
      <div class="flex items-center gap-4">
        <button class="rounded border border-primary-0.6 p-2 bg-primary-0.08">
          <img
            src="../assets/icons/download.svg"
            alt="download"
          />
        </button>
        <button class="rounded border border-red-0.6 p-2 bg-red-light">
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
defineProps(["issuer"]);
</script>
