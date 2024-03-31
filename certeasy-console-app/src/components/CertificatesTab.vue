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
        class="border border-primary px-4 py-2 rounded-lg bg-lightBlue flex justify-between gap-2 items-center"
      >
        <span class="font-normal"> All </span>
        <ChevronDown />
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
  <div class="pl-16 mr-4 pr-8 overflow-y-auto max-h-[calc(100%-268px)]">
    <IssuersList
      v-if="issuersList.length > 0"
      :issuersList="issuersList"
    />
    <IssuerCardNoContent v-if="issuersList.length === 0" />
  </div>
</template>
<script setup>
import IssuerCardNoContent from '@/components/IssuerCardNoContent'
</script>
