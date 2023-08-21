<template>
  <div class="flex items-center justify-center center-div">
    <div class="flex flex-col items-center justify-center">
      <img
        src="@/assets/nocontents.png"
        class="mb-[26px] w-[158px] h-[158px]"
        alt=""
      />
      <h1 class="text-28 font-bold-400 text-28 text-primary mb-0.5">
        No certificate issuers created yet!
      </h1>
      <p class="text-gray-500 mb-[26px] text-base">
        You can start by creating your first certificate issuer.
      </p>
      <button
        class="flex items-center rounded py-1 px-4 min-h-[41px] btn-import mb-1"
      >
        <span class="align-middle">New Issuer</span>
        <img
          src="@/assets/icons/import-file.svg"
          class="ml-2"
          alt=""
        />
      </button>
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
