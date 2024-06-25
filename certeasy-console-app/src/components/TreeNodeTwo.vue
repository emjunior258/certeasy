<template>
  <li :class="{}">
    <div
      class="flex items-center gap-2 font-light mb-1"
      :class="{ 'ml-8': !isParent }"
    >
      <span
        @click="toggle(item)"
        class="border border-primary text-primary rounded w-6 h-6 flex items-center justify-center text-3xl font-light"
        v-if="isParent"
        >{{ item.isOpen ? '-' : '+' }}</span
      >
      <div
        @click="handleSelectNode(item)"
        class="flex items-center gap-2 text-sm py-0.5 px-1.5 text-text font-normal hover:bg-primary-0.08 hover:rounded"
        :class="{
          'bg-primary-0.08': item.active,
          rounded: item.active,

          'font-medium': item.active,
          'text-primary': item.active,
        }"
      >
        <img
          src="@/assets/avatar-placeholder.svg"
          alt="issuer"
          class="h-6 w-6 rounded-[2px] border-primary-0.6"
        />
        {{ item.name }}
      </div>
    </div>
    <ul
      v-show="item.isOpen"
      v-if="isParent"
    >
      <TreeNodeTwo
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
import { ref } from 'vue'

const { item, getChildren, selectNode } = defineProps([
  'item',
  'getChildren',
  'selectNode',
])

const isParent = ref(false)

if (item.children_count && item.children_count > 0) {
  isParent.value = true
}

const toggle = (node) => {
  if (isParent.value) {
    if (!node.isOpen) {
      node.isOpen = true
    } else {
      node.isOpen = !node.isOpen
    }
  }

  if (isParent.value && !node.children) {
    getChildren(node.id)
  }
}

const handleSelectNode = (node) => {
  selectNode(node.id)
}
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

.root ul li {
  position: relative;
}

li > div > div,
li > div > span {
  cursor: pointer;
}
</style>
