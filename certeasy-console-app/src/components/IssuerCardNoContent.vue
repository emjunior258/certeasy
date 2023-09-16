<template>
  <div
    class="flex items-center justify-center center-div"
    data-test="no-issuers"
  >
    <div class="flex flex-col items-center justify-center">
      <img
        src="@/assets/nocontents.png"
        class="mb-[26px] w-[158px] h-[158px]"
        alt=""
      />
      <h1 class="text-28 font-bold-400 text-primary mb-0.5">
        No certificate issuers created yet!
      </h1>
      <p class="text-gray-500 mb-[26px] text-base">
        You can start by creating your first certificate issuer.
      </p>
      <IconActionButton :buttonProps="actionButton" />
    </div>
  </div>
</template>
<style>
.btn-import {
  border: 1px solid primary;
  margin-left: 10px;
}

.center-div {
  height: calc(100vh - 285px);
}
</style>
<script setup>
import IconActionButton from "./buttons/IconActionButton.vue";
import plusIcon from "@/assets/icons/PlusIcon.vue";

const actionButton = {
  icon: plusIcon,
  text: "New Issuer",
  outlined: true,
};
</script>
