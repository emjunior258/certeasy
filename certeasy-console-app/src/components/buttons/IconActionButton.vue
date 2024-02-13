<template>
  <button
    class="rounded-lg py-3 px-6 font-normal leading-4 text-base"
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
    <component
      :is="buttonProps.icon"
      class="inline mr-2 w-6 h-auto"
    />
    {{ buttonProps.text }}
  </button>
</template>

<script setup>
defineProps(['buttonProps'])
</script>
