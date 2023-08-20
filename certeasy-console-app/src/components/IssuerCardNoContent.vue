<template>
    <div class="flex flex-col items-center justify-center h-screen space-y-4" style=" margin: -150px;">
      <img src="@/assets/nocontents.png" width="200" height="200" alt="">
      <h1 class="text-2xl font-bold text-primary">No certificate issuers created yet!</h1>
      <p class="text-gray-500">You can start by creating your first certificate issuer.</p>
      <button class="flex items-center rounded py-1 px-4 min-h-[41px] btn-import">
        <span class="align-middle">New Issuer</span>
        <img src="@/assets/icons/import-file.svg" class="ml-2" alt="">
      </button>
    </div>
  </template>
<style>
.btn-import {
    border: 1px solid primary;
    margin-left: 10px;
}
</style>