<script setup>
import { defineProps } from "vue";
defineProps(["logo", "navLinks"]);
</script>

<template>
  <header class="bg-primary px-16 py-4 text-white">
    <div class="flex justify-between">
      <img
        :src="logo.imgSrc"
        alt="Certeasy"
        class="h-9"
      />
      <nav class="flex items-center">
        <ul class="flex items-center gap-4 font-light text-sm">
          <li
            v-for="navLink in navLinks"
            :key="navLink.id"
          >
            <a :href="navLink.linkHref">
              <img
                :src="navLink.icon"
                :alt="navLink.altIcon"
                class="inline"
              />
              {{ navLink.text }}
            </a>
          </li>
        </ul>
      </nav>
    </div>
  </header>
</template>
