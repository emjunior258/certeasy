<template>
  <div>
    <h3 class="text-base font-medium mb-5">{{ heading }}</h3>
    <div class="flex flex-wrap gap-4">
      <span
        class="border rounded border-primary-0.6 text-xs py-1"
        v-for="detail in details"
        :key="detail.key"
      >
        <h4
          class="inline bg-primary-0.08 text-primary font-medium line-h py-1 px-3 border-primary-0.6 border-r"
        >
          {{ detail.key }}
        </h4>
        <p class="inline px-3 py-1 font-light">{{ detail.value }}</p>
      </span>
    </div>
  </div>
</template>
<script setup>
defineProps(["heading", "details"]);
</script>
