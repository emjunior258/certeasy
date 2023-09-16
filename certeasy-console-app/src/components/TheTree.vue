<template>
  <ul class="root mb-6">
    <TreeNode
      :item="treeData"
      :getChildren="getChild"
      :selectNode="handleSelectNode"
    />
  </ul>
</template>

<script setup>
import TreeNode from "@/components/TreeNode.vue";

const { getChildren, selectNode } = defineProps([
  "treeData",
  "getChildren",
  "selectNode",
]);

const getChild = (id) => {
  getChildren(id);
};

const handleSelectNode = (id) => {
  selectNode(id);
};
</script>
