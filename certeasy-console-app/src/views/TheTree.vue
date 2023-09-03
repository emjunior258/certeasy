<template>
  <ul class="root">
    <li class="cursor-pointer">
      <div
        :class="{ bold: isFolder }"
        @click="toggle"
        @dblclick="createFolder"
        class="flex items-center gap-2 font-light"
      >
        <span
          class="border border-primary text-primary font-semibold rounded w-4 h-4 flex items-center justify-center"
          v-if="isFolder"
          >{{ false ? "-" : "+" }}</span
        >
        <div
          active="true"
          class="flex items-center gap-2 px-1.5 py-1"
        >
          <img
            src="@/assets/avatar-placeholder.svg"
            alt="issuer"
            class="h-4 w-4 rounded-[2px] border-primary-0.6"
          />
          My Issuer Name
        </div>
      </div>
      <ul>
        <li>
          <div
            :class="{ bold: isFolder }"
            @click="toggle"
            @dblclick="createFolder"
            class="flex items-center gap-2 font-light"
          >
            <span
              class="border border-primary text-primary font-semibold rounded w-4 h-4 flex items-center justify-center"
              >-</span
            >
            <div
              active="true"
              class="flex items-center gap-2 px-1.5 py-1"
            >
              <img
                src="@/assets/avatar-placeholder.svg"
                alt="issuer"
                class="h-4 w-4 rounded-[2px] border-primary-0.6"
              />
              My Issuer Name
            </div>
          </div>
          <ul>
            <li>
              <div
                :class="{ bold: isFolder }"
                @click="toggle"
                @dblclick="createFolder"
                class="flex items-center gap-2 font-light"
              >
                <span
                  class="border border-primary text-primary font-semibold rounded w-4 h-4 flex items-center justify-center"
                  >-</span
                >
                <div class="flex items-center px-1.5 gap-2 py-1">
                  <img
                    src="@/assets/avatar-placeholder.svg"
                    alt="issuer"
                    class="h-4 w-4 rounded-[2px] border-primary-0.6"
                  />
                  My Issuer Name
                </div>
              </div>
              <ul>
                <li>
                  <div
                    :class="{ bold: isFolder }"
                    @click="toggle"
                    @dblclick="createFolder"
                    class="flex items-center gap-2 font-light"
                  >
                    <div class="flex items-center gap-2 py-1">
                      <img
                        src="@/assets/avatar-placeholder.svg"
                        alt="issuer"
                        class="h-4 w-4 rounded-[2px] border-primary-0.6"
                      />
                      My Issuer Name
                    </div>
                  </div>
                </li>
              </ul>
            </li>
          </ul>
        </li>
        <li>
          <div
            :class="{ bold: isFolder }"
            @click="toggle"
            @dblclick="createFolder"
            class="flex items-center gap-2 font-light"
          >
            <span
              class="border border-primary text-primary font-semibold rounded w-4 h-4 flex items-center justify-center"
              >-</span
            >
            <div
              active="true"
              class="flex items-center gap-2 px-1.5 py-1"
            >
              <img
                src="@/assets/avatar-placeholder.svg"
                alt="issuer"
                class="h-4 w-4 rounded-[2px] border-primary-0.6"
              />
              My Issuer Name
            </div>
          </div>
          <ul>
            <li>
              <div
                :class="{ bold: isFolder }"
                @click="toggle"
                @dblclick="createFolder"
                class="flex items-center gap-2 font-light"
              >
                <div class="flex items-center gap-2 py-1">
                  <img
                    src="@/assets/avatar-placeholder.svg"
                    alt="issuer"
                    class="h-4 w-4 rounded-[2px] border-primary-0.6"
                  />
                  My Issuer Name
                </div>
              </div>
            </li>
          </ul>
        </li>
        <li>
          <div
            :class="{ bold: isFolder }"
            @click="toggle"
            @dblclick="createFolder"
            class="flex items-center gap-2 font-light"
          >
            <div class="flex items-center gap-2 py-1">
              <img
                src="@/assets/avatar-placeholder.svg"
                alt="issuer"
                class="h-4 w-4 rounded-[2px] border-primary-0.6"
              />
              My Issuer Name
            </div>
          </div>
        </li>
      </ul>
    </li>
  </ul>
</template>
<script setup>
import { ref } from "vue";

const treeData = ref({
  name: "My Tree",
  children: [
    { name: "hello" },
    { name: "wat" },
    {
      name: "child folder",
      children: [
        {
          name: "child folder",
          children: [{ name: "hello" }, { name: "wat" }],
        },
        { name: "hello" },
        { name: "wat" },
        {
          name: "child folder",
          children: [{ name: "hello" }, { name: "wat" }],
        },
      ],
    },
  ],
});

const makeFolder = (item) => {
  item.children = [];
  addItem(item);
};

const addItem = (item) => {
  item.children.push({
    name: "new stuff",
  });
};

const isOpen = ref(false);

const isFolder = ref(false);
if (treeData.value.children && treeData.value.children.length) {
  isFolder.value = true;
}

const toggle = () => {
  if (isFolder.value) {
    isOpen.value = !isOpen.value;
  }
};

const createFolder = () => {
  if (!isFolder.value) {
    makeFolder(treeData.value);
    isOpen.value = true;
  }
};
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
  border-left: 1px solid blue;
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
  border-left: 1px solid blue;
  border-bottom: 1px solid blue;
  border-radius: 0 0 0 4px;
  z-index: 2;
}

.root ul li:last-child:after {
  content: "";
  position: absolute;
  width: 1px;
  left: -22px;
  background: #fff;
  top: 12px;
  bottom: 0;
}
</style>
