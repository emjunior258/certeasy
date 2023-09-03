<template>
  <ul>
    <IssuerCard
      v-for="issuer in issuersList"
      :key="issuer.id"
      :issuer="issuer"
      data-test="issuer"
    />
  </ul>
</template>
<script setup>
import IssuerCard from "@/components/IssuerCard.vue";
defineProps(["issuersList"]);
</script>
