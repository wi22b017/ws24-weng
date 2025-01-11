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
  { question: "How can I book a flight?", answer: "You can book a flight by entering your travel details in the search bar on our homepage. Once you have found your flight, you have to login or register. As a last step you can enter the details of your fellow passengers and confirm the flight booking." },
  { question: "What are the cancellation policies?", answer: "Our cancellation policy allows for a full refund within 24 hours of booking. After that period, cancellations may be subject to fees or may result in a travel credit, depending on the fare type. Please refer to the specific terms and conditions of your booking for more details." },
  { question: "How do I change my booking?", answer: "You can cancel your booking in the MyBookings section." },
  { question: "What payment methods are accepted?", answer: "We accept credit cards, paypal, bank transfer and aaple pay." }
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
