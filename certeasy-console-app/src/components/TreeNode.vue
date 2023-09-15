<template>
  <li
    class="node-tab"
    :class="isParent && 'node-parent-tab'"
  >
    <div
      class="flex items-center gap-[14px] font-light mb-1"
      :class="{ 'ml-[30px]': !isParent }"
    >
      <span
        @click="toggle(item)"
        class="border border-primary text-primary font-semibold rounded w-4 h-4 flex items-center justify-center"
        v-if="isParent"
        >{{ item.isOpen ? "-" : "+" }}</span
      >
      <div
        @click="handleSelectNode(item)"
        class="flex items-center gap-2 text-sm py-0.5"
        :class="{
          'bg-primary-0.08': item.active,
          'border-primary-0.6': item.active,
          border: item.active,
          rounded: item.active,
          'px-1.5': item.active,
          'font-medium': item.active,
          'text-primary': item.active,
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
      v-show="item.isOpen"
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

const isParent = ref(false);

if (item.children_count && item.children_count > 0) {
  isParent.value = true;
}

const toggle = (node) => {
  if (isParent.value) {
    if (!node.isOpen) {
      node.isOpen = true;
    } else {
      node.isOpen = !node.isOpen;
    }
  }

  if (isParent.value && !node.children) {
    getChildren(node.id);
  }
};

const handleSelectNode = (node) => {
  selectNode(node.id);
};
</script>

<style scoped>
.root ul {
  padding-left: 30px;
}

.root > li:first-child > div::before {
  display: none;
}
.root ul {
  position: relative;
}
.root ul::before {
  content: "";
  position: absolute;
  left: 8px;
  top: -9px;
  width: 0;
  height: 100%;
  border-left: 1px solid #c0c0c0;
  z-index: -2;
}

.root ul li {
  position: relative;
}

.node-tab::before {
  content: "";
  position: absolute;
  left: -22px;
  top: -4px;
  width: 52px;
  height: 16px;
  border-left: 1px solid #c0c0c0;
  border-bottom: 1px solid #c0c0c0;
  border-radius: 0 0 0 4px;
}

.node-parent-tab::before {
  width: 22px;
}

.root ul li:last-child:after {
  content: "";
  position: absolute;
  width: 1px;
  left: -22px;
  background: #fff;
  top: 9px;
  bottom: 0;
  z-index: -1;
}

li > div > div,
li > div > span {
  cursor: pointer;
}
</style>
