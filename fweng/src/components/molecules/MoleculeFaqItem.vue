<template>
  <div class="faq-item">
    <div class="faq-question" @click="toggleAnswer">
      <AppHeading :text="question" />
      <span class="toggle-icon">{{ isAnswerVisible ? '-' : '+' }}</span>
    </div>
    <transition name="fade">
      <div v-if="isAnswerVisible" class="faq-answer">
        <AppText :content="answer"/>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import AppHeading from '@/components/atoms/AtomHeading.vue';
import AppText from '@/components/atoms/AtomText.vue';
import { defineProps } from 'vue';

defineProps({
  question: {
    type: String,
    required: true,
  },
  answer: {
    type: String,
    required: true,
  },
});

// Reactive state to control the visibility of the answer
const isAnswerVisible = ref(false);

// Method to toggle the answer's visibility
function toggleAnswer() {
  isAnswerVisible.value = !isAnswerVisible.value;
}
</script>

<style scoped>
.faq-item {
  border-bottom: 1px solid #ccc;
  padding: 1rem 0;
}

.faq-question {
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
}

.toggle-icon {
  font-size: 1.5rem;
  line-height: 1;
}

.faq-answer {
  margin-top: 0.5rem;
}

.fade-enter-active,
.fade-leave-active {
  transition: all 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  max-height: 0;
  overflow: hidden;
}

.fade-enter-to,
.fade-leave-from {
  opacity: 1;
  max-height: 1000px;
}

</style>
