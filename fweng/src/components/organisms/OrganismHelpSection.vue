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
  const email = 'support@airscape.com';
  const subject = 'Contact Request';
  const body = 'Hello, I would like to get in touch regarding...';

  // Create the mailto link
  const mailtoLink = `mailto:${email}?subject=${encodeURIComponent(subject)}&body=${encodeURIComponent(body)}`;

  // Redirect the user to their email client
  window.location.href = mailtoLink;
};

</script>

<style scoped>
.help-section {
  text-align: center;
  margin-top: 2rem;
}
</style>
