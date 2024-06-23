<template>
  <div class="fixed top-0 left-0 w-screen h-screen z-50">
    <div
      class="shadow-md w-[350px] h-fit fixed top-[50%] right-[50%] translate-x-[50%] translate-y-[-50%] bg-white z-50 overflow-y-auto rounded-lg"
    >
      <div class="p-3">
        <button
          @click="$emit('toggleDialog')"
          class="cursor-pointer text-primary text-md block ml-auto mb-3"
        >
          <CloseIcon />
        </button>
        <div class="p-10 text-center">
          <DangerIcon class="m-auto mb-6" />
          <p class="font-semibold text-[14px] mb-2">
            Are you sure want to delete this certificate?
          </p>
          <p class="text-[10px] mb-4">
            This will delete this certificate permanently. You cannot undo this
            action.
          </p>
          <button
            @click="$emit('toggleDialog')"
            class="border border-primary text-primary text center py-2 px-4 rounded mr-4 text-[8px] font-normal"
          >
            Cancel
          </button>
          <button
            class="border border-red bg-red text-white text center py-2 px-4 rounded text-[8px] font-normal"
            @click="$emit('deleteCert')"
          >
            Delete
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import DangerIcon from '@/assets/icons/DangerIcon.vue'
import CloseIcon from '@/assets/icons/CloseIcon.vue'
const { certId } = defineProps(['certId'])
</script>
