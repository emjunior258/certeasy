<template>
  <ul class="root">
    <TreeNode :item="treeData" />
  </ul>
</template>

<script setup>
import TreeNode from "@/components/TreeNode.vue";

defineProps(["treeData"]);
</script>

<style>
ul:not(:first-child) {
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
  top: -8px;
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
  top: 0;
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
  top: 12px;
  bottom: 0;
  z-index: -1;
}
</style>
