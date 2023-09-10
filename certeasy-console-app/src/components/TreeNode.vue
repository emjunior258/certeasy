<template>
  <li>
    <div
      @click="toggle(item)"
      class="flex items-center gap-[14px] font-light mb-1"
    >
      <span
        class="border border-primary text-primary font-semibold rounded w-4 h-4 flex items-center justify-center"
        v-if="isParent"
        >{{ isOpen ? "-" : "+" }}</span
      >
      <div
        class="flex items-center gap-2"
        :class="{
          'bg-primary-0.08': item.active,
          'border-primary-0.6': item.active,
          border: item.active,
          rounded: item.active,
          'px-1.5': item.active,
        }"
      >
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
        v-for="child in item.children"
        :key="child.id"
        :item="child"
        :getChildren="getChildren"
        :selectNode="selectNode"
      />
    </ul>
  </li>
</template>

<script setup>
import { ref } from "vue";

const { item, getChildren, selectNode } = defineProps([
  "item",
  "getChildren",
  "selectNode",
]);

const isOpen = ref(false);
const isParent = ref(false);

if (item.children_count && item.children_count > 0) {
  isParent.value = true;
}

const toggle = (node) => {
  if (isParent.value) {
    isOpen.value = !isOpen.value;
  }

  if (isParent.value && !node.children) {
    getChildren(node.id);
  }
  selectNode(node.id);
};
</script>
