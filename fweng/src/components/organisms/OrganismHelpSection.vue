<template>
  <div class="help-section">
    <AppHeading text="Need Help?" />
    <AppText content="If you need help or further assistance, please reach out to our support team." />
    <AppButton
        label="Contact Us"
        @click="handleContactClick"
        :fullWidth="false"
    />
  </div>
</template>

<script setup>
import AppHeading from '@/components/atoms/AtomHeading.vue';
import AppText from '@/components/atoms/AtomText.vue';
import AppButton from '@/components/atoms/AtomButton.vue';

const handleContactClick = () => {
  // Navigate to the contact page or trigger a modal
  console.log('Redirecting to contact page...');
};
</script>

<style scoped>
.help-section {
  text-align: center;
  margin-top: 2rem;
}
</style>
