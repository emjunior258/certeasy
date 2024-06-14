<template>
  <div
    class="shadow-md w-[300px] h-fit max-h-[350px] max-w-[400px] fixed top-[50%] right-[50%] translate-x-[50%] translate-y-[-50%] bg-white z-50 overflow-y-auto rounded-lg"
  >
    <div class="p-3">
      <button
        @click="$emit('toggleSidebar')"
        class="cursor-pointer text-primary text-md block ml-auto mb-3"
      >
        <CloseIcon />
      </button>
      <div class="p-10 text-center">
        <DangerIcon />
        <p class="font-semibold text-[14px]">
          Are you sure want to delete this certificate?
        </p>
        <p class="font-semibold text-[14px]">
          This will delete this certificate permanently. You cannot undo this
          action.
        </p>
        <button
          class="border border-primary text-primary text center py-2 px-4 rounded mr-4 text-[8px] font-normal"
        >
          Cancel
        </button>
        <button
          class="border border-red text-white text center py-2 px-4 rounded text-[8px] font-normal"
          @click="handleDelete()"
        >
          Delete
        </button>
      </div>
    </div>
  </div>
</template>
<script setup>
import DangerIcon from '@/assets/icons/DangerIcon.vue'
import CloseIcon from '@/assets/icons/CloseIcon.vue'
const { certId, deleteCert } = defineProps(['deleteCert', 'certId'])

const handleDelete = () => {
  deleteCert(certId)
}
</script>
