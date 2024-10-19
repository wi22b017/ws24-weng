<template>
  <div class="faq-list">
    <h1>Frequently Asked Questions</h1>
    <div class="container">
      <div v-for="(item, index) in faqItems" :key="index">
        <FaqItem :question="item.question" :answer="item.answer" />
      </div>
    </div>
  </div>
</template>

<script setup>
import FaqItem from '@/components/molecules/MoleculeFaqItem.vue';

const faqItems = [
  { question: "How can I book a flight?", answer: "You can book a flight by..." },
  { question: "What are the cancellation policies?", answer: "The cancellation policies..." },
  { question: "How do I change my booking?", answer: "To change your booking, you can..." },
  { question: "What payment methods are accepted?", answer: "We accept credit cards, debit cards, and PayPal." }
];
</script>

<style scoped>
.faq-list {
  padding: 1rem;
}
.container {
  text-align: start;
}
</style>
