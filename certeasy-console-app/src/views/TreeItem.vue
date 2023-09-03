<template>
  <li>
    <div
      :class="{ bold: isFolder }"
      @click="toggle"
      @dblclick="createFolder"
    >
      {{ item.name }}
      <span v-if="isFolder">[{{ isOpen ? "-" : "+" }}]</span>
    </div>
    <ul
      v-show="isOpen"
      v-if="isFolder"
    >
      <tree-item
        class="item"
        v-for="(child, index) in item.children"
        :key="index"
        :item="child"
        v-bind:make-folder="makeFolder"
        v-bind:add-item="addItem"
      ></tree-item>
      <li
        class="add"
        @click="addItem(item)"
      >
        +
      </li>
    </ul>
  </li>
</template>
<script setup>
defineProps(["item", "makeFolder", "addItem"]);
</script>
<style>
.item {
  cursor: pointer;
}
.bold {
  font-weight: bold;
}
ul {
  padding-left: 1em;
  line-height: 1.5em;
  list-style-type: dot;
}
</style>
