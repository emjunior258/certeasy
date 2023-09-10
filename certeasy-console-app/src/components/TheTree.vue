<template>
  <ul class="root">
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

<style>
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
  top: -10px;
  width: 0;
  height: 100%;
  border-left: 1px solid #c0c0c0;
  z-index: -2;
}

.root ul li {
  position: relative;
}

.root ul li::before {
  content: "";
  position: absolute;
  left: -22px;
  top: -3px;
  width: 22px;
  height: 16px;
  border-left: 1px solid #c0c0c0;
  border-bottom: 1px solid #c0c0c0;
  border-radius: 0 0 0 4px;
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

li > div {
  cursor: pointer;
}
</style>
