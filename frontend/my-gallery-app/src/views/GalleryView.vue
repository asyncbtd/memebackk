<template>
  <div class="home">
    <HeroSection />
    <div class="content">
      <h2>Popular Images</h2>
      
    </div>
  </div>
</template>

<script setup>
import HeroSection from '@/components/HeroSection.vue'
</script>

<style scoped>
.home {
  padding-top: 68px;
}

.content {
  padding: 2rem 4%;
}

.content h2 {
  margin-bottom: 1rem;
  font-size: 1.5rem;
}
</style>