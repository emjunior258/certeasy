<template>
  <ul class="root mb-6">
    <TreeNodeTwo
      :item="treeData"
      :getChildren="getChild"
      :selectNode="handleSelectNode"
      :noLines="noLines"
    />
  </ul>
</template>

<script setup>
import TreeNodeTwo from '@/components/TreeNodeTwo.vue'

const { getChildren, selectNode, noLines } = defineProps([
  'treeData',
  'getChildren',
  'selectNode',
  'noLines',
])

const getChild = (id) => {
  getChildren(id)
}

const handleSelectNode = (id) => {
  selectNode(id)
}
</script>
