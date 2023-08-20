<template>
  <button class="rounded py-1 px-4 min-h-[41px]"
    :class="{
      'bg-primary': buttonProps.active,
      'text-white': buttonProps.active,
    }" type="button">
    <span
      class="align-middle"
    >
      {{ buttonProps.text }} ({{ buttonProps.amount }})</span
    >
  </button>
</template>
<script>
export default {
  props: {
    buttonProps: {
      required: true,
      type: Object,
    },
  },
}
</script>