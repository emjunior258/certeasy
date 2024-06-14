<template>
  <div>
    <h3 class="text-[11px] font-semibold mb-2">{{ heading }}</h3>
    <div class="flex flex-wrap gap-2">
      <span
        class="border rounded border-primary-0.6 text-xs"
        v-for="detail in details"
        :key="detail.key"
      >
        <h4
          class="inline bg-lightBlue relative z-[-1] text-[8px] text-primary font-semibold py-1 px-[6px] border-primary-0.6 border-r"
        >
          {{ detail.key }}
        </h4>
        <p class="inline px-[6px] text-[8px] py-1 font-normal">
          {{ detail.value }}
        </p>
      </span>
    </div>
  </div>
</template>
<script setup>
defineProps(['heading', 'details'])
</script>
