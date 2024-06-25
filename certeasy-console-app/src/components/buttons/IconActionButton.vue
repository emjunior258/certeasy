<template>
  <button
    class="rounded-lg py-4 px-4 font-normal leading-4 text-base"
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
      :class="{
        'w-[14px]': buttonProps.iconSize,
        'h-[14px]': buttonProps.iconSize,
      }"
      class="inline mr-2 h-auto w-6"
    />
    <span class="align-middle">{{ buttonProps.text }}</span>
  </button>
</template>

<script setup>
defineProps(['buttonProps'])
</script>
