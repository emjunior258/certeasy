<template>
  <div
    class="animate-pulse"
    data-test="loading"
  >
    <div
      class="px-16 py-4 bg-gradient-to-r from-gray-20 via-gray-10 via-70% to-via-gray-10"
    >
      <div class="flex justify-between items-center">
        <div
          class="h-9 w-[130px] bg-gradient-to-r from-gray-40 via-gray-30 via-70% to-via-gray-30 rounded"
        />
        <div
          class="h-9 w-[194px] bg-gradient-to-r from-gray-40 via-gray-30 via-70% to-via-gray-30 rounded"
        />
      </div>
    </div>
    <div class="flex justify-between items-end px-16 mt-12 mb-6 rounded">
      <div
        class="h-[41px] w-[596px] bg-gradient-to-r from-gray-40 via-gray-30 via-70% to-via-gray-30 rounded"
      />
      <div
        class="h-[54px] w-[425px] bg-gradient-to-r from-gray-40 via-gray-30 via-70% to-via-gray-30 rounded"
      />
    </div>
    <div class="overflow-y-hidden section-y-container">
      <div
        class="px-16"
        v-for="n in 10"
        :key="n"
      >
        <div
          class="bg-gradient-to-r from-gray-20 via-gray-10 via-70% to-via-gray-10 rounded px-11 py-6 mb-2 flex justify-between"
        >
          <div class="flex gap-6">
            <div
              class="h-[54px] w-[54px] bg-gradient-to-r from-gray-40 via-gray-30 via-70% to-via-gray-30 rounded"
            />
            <div class="flex flex-col justify-between items-start">
              <div
                class="h-7 w-[161px] bg-gradient-to-r from-gray-40 via-gray-30 via-70% to-via-gray-30 rounded"
              />
              <div
                class="h-5 w-[52px] bg-gradient-to-r from-gray-40 via-gray-30 via-70% to-via-gray-30 rounded"
              />
            </div>
          </div>
          <div class="flex items-center gap-4">
            <div
              class="h-[34px] w-[34px] bg-gradient-to-r from-gray-40 via-gray-30 via-70% to-via-gray-30 rounded"
            />
            <div
              class="h-[34px] w-[34px] bg-gradient-to-r from-gray-40 via-gray-30 via-70% to-via-gray-30 rounded"
            />
          </div>
        </div>
      </div>
    </div>
    <div class="px-16 fixed bottom-0 left-0 w-full bg-white">
      <div
        class="flex border-t from-gray-20 via-gray-10 via-70% to-via-gray-10 w-full h-[104px] items-center"
      >
        <div
          class="h-[26px] w-[327px] bg-gradient-to-r from-gray-40 via-gray-30 via-70% to-via-gray-30 rounded ml-auto"
        />
      </div>
    </div>
  </div>
</template>
<style>
.section-y-container {
  max-height: calc(100vh - 285px);
}
</style>
<script setup></script>
