<script setup>
import defineProps from "vue";
defineProps(["buttonProps"]);
</script>
<template>
  <button
    class="rounded py-1 px-4 min-h-[41px]"
    :class="{
      'bg-primary': buttonProps.active,
      'text-white': buttonProps.active,
    }"
    type="button"
  >
    <img
      v-if="buttonProps.icon"
      :src="buttonProps.icon"
      alt="icon"
      class="inline"
    />
    <span
      class="align-middle"
      :class="{ 'ml-2': buttonProps.icon }"
    >
      {{ buttonProps.text }} ({{ buttonProps.amount }})</span
    >
  </button>
</template>
