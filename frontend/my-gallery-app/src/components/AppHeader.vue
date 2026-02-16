<template>
  <header class="search-header">
    <div class="search-container">
      <div class="logo">
        <h1>LD</h1>
      </div>
      <div class="search-bar">
        <SearchIcon class="search-icon" />
        <input 
          type="text" 
          class="search-input" 
          placeholder="Search images..." 
          v-model="searchQuery"
          @keyup.enter="performSearch"
          @input="handleInput"
        />
        <button class="search-btn" @click="performSearch">
          Search
        </button>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref } from 'vue'
import SearchIcon from './icons/SearchIcon.vue'

const searchQuery = ref('')
const emit = defineEmits(['search'])

let searchTimeout = null

const handleInput = () => {
  clearTimeout(searchTimeout)
  searchTimeout = setTimeout(() => {
    performSearch()
  }, 300)
}

const performSearch = () => {
  console.log('Searching for:', searchQuery.value)
  emit('search', searchQuery.value)
}

// Экспортируем методы для использования в родительском компоненте
defineExpose({
  clearSearch: () => {
    searchQuery.value = ''
    emit('search', '')
  }
})
</script>

<style scoped>
.search-header {
  background-color: #141414;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  padding: 1rem 4%;
  border-bottom: 1px solid #333;
  backdrop-filter: blur(10px);
}

.search-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1920px;
  margin: 0 auto;
  gap: 2rem;
}

.logo h1 {
  color: #fff;
  font-size: 1.5rem;
  font-weight: 600;
  margin: 0;
  letter-spacing: 1px;
}

.search-bar {
  display: flex;
  align-items: center;
  flex: 1;
  max-width: 600px;
  background: #1a1a1a;
  border-radius: 8px;
  padding: 0.5rem;
  gap: 0.5rem;
  border: 1px solid #333;
  transition: border-color 0.3s;
}

.search-bar:focus-within {
  border-color: #555;
}

.search-icon {
  width: 20px;
  height: 20px;
  color: #666;
  margin-left: 0.5rem;
}

.search-input {
  flex: 1;
  background: transparent;
  border: none;
  color: #fff;
  font-size: 1rem;
  padding: 0.5rem;
  outline: none;
}

.search-input::placeholder {
  color: #666;
}

.search-btn {
  background: #333;
  color: #fff;
  border: none;
  padding: 0.5rem 1.5rem;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.3s;
}

.search-btn:hover {
  background: #444;
}

@media (max-width: 768px) {
  .search-container {
    flex-direction: column;
    gap: 1rem;
  }
  
  .search-bar {
    width: 100%;
    max-width: none;
  }
  
  .search-header {
    padding: 0.8rem 4%;
  }
  
  .logo h1 {
    font-size: 1.3rem;
    text-align: center;
  }
}

@media (max-width: 480px) {
  .search-container {
    gap: 0.8rem;
  }
  
  .logo h1 {
    font-size: 1.2rem;
  }
  
  .search-bar {
    flex-direction: column;
    gap: 0.8rem;
  }
  
  .search-input {
    width: 100%;
    text-align: center;
  }
  
  .search-btn {
    width: 100%;
  }
}
</style>