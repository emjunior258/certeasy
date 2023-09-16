<template>
  <ul>
    <IssuerCard
      v-for="issuer in issuersList"
      :key="issuer.id"
      :issuer="issuer"
      data-test="issuer"
      :selectNode="handleSelectNode"
    />
  </ul>
</template>
<script setup>
import IssuerCard from "@/components/IssuerCard.vue";
const { selectNode } = defineProps(["issuersList", "selectNode"]);

const handleSelectNode = (id) => {
  selectNode(id);
};
</script>
