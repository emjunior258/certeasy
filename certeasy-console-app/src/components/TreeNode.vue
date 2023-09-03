<template>
  <li>
    <div
      @click="toggle"
      class="flex items-center gap-[14px] font-light"
    >
      <span
        class="border border-primary text-primary font-semibold rounded w-4 h-4 flex items-center justify-center"
        v-if="isParent"
        >{{ isOpen ? "-" : "+" }}</span
      >
      <div class="flex items-center gap-2 py-1">
        <img
          src="@/assets/avatar-placeholder.svg"
          alt="issuer"
          class="h-4 w-4 rounded-[2px] border-primary-0.6"
        />
        {{ item.name }}
      </div>
    </div>
    <ul
      v-show="isOpen"
      v-if="isParent"
    >
      <TreeNode
        v-for="(child, index) in item.children"
        :key="index"
        :item="child"
      />
    </ul>
  </li>
</template>

<script setup>
import { ref } from "vue";

const { item } = defineProps(["item"]);

const isOpen = ref(false);
const isParent = ref(false);

if (item.children && item.children.length) {
  isParent.value = true;
}

const toggle = () => {
  if (isParent.value) {
    isOpen.value = !isOpen.value;
  }
};
</script>
