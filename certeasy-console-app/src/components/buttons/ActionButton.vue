<script setup>
import defineProps from "vue";
defineProps(["buttonProps"]);
</script>
<template>
  <button
    class="rounded py-3 px-6"
    :class="{
      'text-white': !buttonProps.outlined,
      'text-primary': buttonProps.outlined,
      'bg-primary': !buttonProps.outlined,
      'bg-white': buttonProps.outlined,
      border: buttonProps.outlined,
      'border-primary': buttonProps.outlined,
    }"
    type="button"
  >
    {{ buttonProps.text }}
    <img
      :src="buttonProps.icon"
      alt="add"
      class="inline ml-2"
    />
  </button>
</template>
