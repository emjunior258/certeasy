<template>
  <button
    class="rounded-lg py-2 px-4 min-h-[41px] border border-primary"
    :class="{
      'bg-primary': buttonProps.active && !buttonProps.disabled,
      'text-white': buttonProps.active && !buttonProps.disabled,
      'text-black-0.6': buttonProps.disabled,
    }"
    type="button"
    :disabled="buttonProps.disabled"
    @click="$emit('handleClick')"
  >
    <component
      :is="buttonProps.icon"
      class="inline w-6 h-auto"
      :class="{ 'text-primary': !buttonProps.active }"
    ></component>
    <span
      class="align-middle text-text font-normal leading-4 text-base"
      :class="{ 'ml-2': buttonProps.icon }"
    >
      {{ buttonProps.text }}
      {{ buttonProps.amount >= 0 && `(${buttonProps.amount})` }}
    </span>
  </button>
</template>

<script setup>
defineProps(['buttonProps'])
</script>
