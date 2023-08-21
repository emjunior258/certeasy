<template>
  <button
    class="rounded py-3 px-6 font-medium text-lg"
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
      :alt="buttonProps.altIcon"
      class="inline ml-2"
    />
  </button>
</template>

<script setup>
defineProps(["buttonProps"]);
</script>
