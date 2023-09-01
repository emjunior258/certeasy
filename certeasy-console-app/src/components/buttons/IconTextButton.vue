<template>
  <button
    class="rounded py-1 px-4 min-h-[41px]"
    :class="{
      'bg-primary': buttonProps.active && !buttonProps.disabled,
      'text-white': buttonProps.active && !buttonProps.disabled,
      'text-black-0.6': buttonProps.disabled,
    }"
    type="button"
    :disabled="buttonProps.disabled"
    @click="$emit('handleClick')"
  >
    <img
      v-if="buttonProps.icon"
      :src="buttonProps.icon"
      :alt="buttonProps.iconAlt"
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

<script setup>
defineProps(["buttonProps"]);
</script>
