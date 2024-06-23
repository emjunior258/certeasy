<template>
  <ul>
    <IssuerCard
      v-for="issuer in issuersList"
      :key="issuer.id"
      :issuer="issuer"
      data-test="issuer"
      :selectNode="handleSelectNode"
      :deleteIssuer="handleDeleteIssuer"
    />
  </ul>
</template>
<script setup>
import IssuerCard from '@/components/IssuerCard.vue'
const { selectNode, deleteIssuer } = defineProps([
  'issuersList',
  'selectNode',
  'deleteIssuer',
])

const handleDeleteIssuer = (id) => {
  deleteIssuer(id)
}

const handleSelectNode = (id) => {
  selectNode(id)
}
</script>
