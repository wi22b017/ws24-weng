<template>
  <div class="faq-template">
    <FaqList />
    <HelpSection />
  </div>
</template>

<script setup>
import FaqList from '@/components/organisms/OrganismFaqList.vue';
import HelpSection from '@/components/organisms/OrganismHelpSection.vue';
</script>

<style scoped>
.faq-template {
  max-width: 900px;
  margin: 0 auto;
  padding: 2rem;
}
</style>
