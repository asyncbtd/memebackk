<template>
	<div class="home">
		<HeroSection
			@download="handleDownloadFromHero"
			@more-pictures="scrollToGallery"
		/>
		<AppHeader @search="handleSearch" />
		<div class="content" ref="gallerySection">
			<h2>Gallery</h2>

			<!-- Статистика и поиск -->
			<div class="search-info" v-if="searchQuery">
				<div class="search-results">
					<span>Search results for: "{{ searchQuery }}"</span>
					<span class="results-count">({{ images.length }} images)</span>
				</div>
				<button @click="clearSearch" class="clear-search-btn">
					Clear search
				</button>
			</div>

			<div class="stats" v-else-if="images.length > 0">
				{{ images.length }} images in gallery
			</div>

			<!-- Галерея изображений -->
			<div class="images-grid">
				<div class="image-card" v-for="image in images" :key="image.fileName">
					<div class="image-container">
						<img
							:src="getImageUrl(image.fileName)"
							:alt="image.title"
							@error="handleImageError"
							loading="lazy"
							class="image"
						/>
						<div class="image-overlay">
							<button
								@click="deleteImage(image)"
								class="delete-btn"
								title="Delete"
							>
								🗑️
							</button>
						</div>
					</div>
					<div class="image-info">
						<h3 class="image-title">{{ image.title }}</h3>
						<p class="image-description">{{ image.description }}</p>
					</div>
				</div>
			</div>

			<!-- Сообщение при отсутствии изображений -->
			<div
				v-if="images.length === 0 && !loading && !searchQuery"
				class="no-images"
			>
				<p>📷 No images uploaded yet</p>
				<p class="hint">
					Click "Upload Image" button above to add your first photo
				</p>
			</div>

			<!-- Сообщение при отсутствии результатов поиска -->
			<div
				v-if="images.length === 0 && searchQuery && !loading"
				class="no-results"
			>
				<p>🔍 No images found for "{{ searchQuery }}"</p>
				<button @click="clearSearch" class="clear-search-btn">
					Show all images
				</button>
			</div>

			<!-- Индикатор загрузки -->
			<div v-if="loading" class="loading">
				<p>⏳ Loading images...</p>
			</div>
		</div>
	</div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import HeroSection from '@/components/HeroSection.vue'
import AppHeader from '@/components/AppHeader.vue'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api/images'

const images = ref([])
const loading = ref(false)
const searchQuery = ref('')
const gallerySection = ref(null)

function getImageUrl(fileName) {
	return `${API_BASE_URL}/${fileName}`
}

function handleImageError(event) {
	event.target.src = 'https://picsum.photos/300/200?random=999'
	event.target.alt = 'Image not found'
}

// Обработка скачивания/загрузки из HeroSection
function handleDownloadFromHero() {
	triggerFileUpload()
}

function scrollToGallery() {
	if (gallerySection.value) {
		gallerySection.value.scrollIntoView({ behavior: 'smooth' })
	}
}

// Скрытый input для загрузки файлов
function triggerFileUpload() {
	const input = document.createElement('input')
	input.type = 'file'
	input.accept = 'image/*'
	input.style.display = 'none'

	input.onchange = async (event) => {
		const file = event.target.files[0]
		if (file) {
			await handleFileUpload(file)
		}
		document.body.removeChild(input)
	}

	document.body.appendChild(input)
	input.click()
}

async function handleFileUpload(file) {
	const title = prompt('Enter image title:', file.name.replace(/\.[^/.]+$/, ''))
	if (title === null) return

	const description = prompt('Enter image description:', `Image ${title}`)
	if (description === null) return

	const formData = new FormData()
	formData.append('file', file)
	formData.append('title', title)
	formData.append('description', description)

	try {
		loading.value = true

		const response = await fetch(`${API_BASE_URL}/upload`, {
			method: 'POST',
			body: formData,
		})

		if (response.ok) {
			await loadImages()
			alert('✅ Image uploaded successfully!')
		} else {
			const errorText = await response.text()
			throw new Error(errorText)
		}
	} catch (error) {
		console.error('Upload error:', error)
		alert('❌ Error uploading image: ' + error.message)
	} finally {
		loading.value = false
	}
}

// Обработка поиска из Header
async function handleSearch(query) {
	searchQuery.value = query
	await performSearch(query)
}

async function performSearch(query) {
	try {
		loading.value = true
		console.log('Performing search for:', query)

		let url
		if (query && query.trim() !== '') {
			url = `${API_BASE_URL}/search?q=${encodeURIComponent(query)}`
		} else {
			url = `${API_BASE_URL}/all`
		}

		console.log('Fetching from:', url)

		const response = await fetch(url)

		if (response.ok) {
			const imageList = await response.json()
			console.log('Search results:', imageList)
			images.value = imageList
		} else {
			throw new Error(`HTTP error! status: ${response.status}`)
		}
	} catch (error) {
		console.error('Search error:', error)
		images.value = []
	} finally {
		loading.value = false
	}
}

function clearSearch() {
	searchQuery.value = ''
	loadImages()
}

async function loadImages() {
	await performSearch('')
}

async function deleteImage(image) {
	if (!confirm(`Delete image "${image.title}"?`)) {
		return
	}

	try {
		const response = await fetch(`${API_BASE_URL}/${image.fileName}`, {
			method: 'DELETE',
		})

		if (response.ok) {
			await loadImages()
		} else if (response.status === 404) {
			alert('Image not found')
		} else {
			throw new Error('Delete failed')
		}
	} catch (error) {
		console.error('Delete error:', error)
		alert('Error deleting image')
	}
}

onMounted(() => {
	loadImages()
})
</script>

<style scoped>
.home {
	min-height: 100vh;
	background: #0a0a0a;
}

.content {
	padding: 2rem 4%;
	max-width: 1400px;
	margin: 0 auto;
	margin-top: 100px; /* Уменьшили отступ так как хедер стал меньше */
}

.content h2 {
	margin-bottom: 1.5rem;
	font-size: 2rem;
	color: #fff;
	text-align: center;
	font-weight: 300;
	letter-spacing: 2px;
}

.search-info {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 2rem;
	padding: 1rem;
	background: #1a1a1a;
	border-radius: 8px;
	border: 1px solid #333;
}

.search-results {
	color: #fff;
	display: flex;
	flex-direction: column;
	gap: 0.25rem;
}

.results-count {
	color: #666;
	font-size: 0.9rem;
}

.stats {
	color: #666;
	margin-bottom: 2rem;
	font-size: 0.9rem;
	text-align: center;
	text-transform: uppercase;
	letter-spacing: 1px;
}

.clear-search-btn {
	background: #333;
	color: #fff;
	border: 1px solid #444;
	padding: 0.5rem 1rem;
	border-radius: 4px;
	cursor: pointer;
	transition: all 0.3s;
	white-space: nowrap;
}

.clear-search-btn:hover {
	background: #444;
	border-color: #555;
}

.no-results {
	text-align: center;
	padding: 3rem;
	color: #fff;
}

.no-results p {
	margin-bottom: 1rem;
	font-size: 1.1rem;
	color: #999;
}

.images-grid {
	display: grid;
	grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
	gap: 2rem;
	padding: 1rem 0;
}

.image-card {
	border-radius: 12px;
	overflow: hidden;
	transition: all 0.3s ease;
	background: #1a1a1a;
	border: 1px solid #2a2a2a;
	position: relative;
}

.image-card:hover {
	transform: translateY(-8px);
	box-shadow: 0 20px 40px rgba(0, 0, 0, 0.5);
	border-color: #444;
}

.image-container {
	position: relative;
	overflow: hidden;
	aspect-ratio: 4/3;
}

.image {
	width: 100%;
	height: 100%;
	object-fit: cover;
	transition: transform 0.3s ease;
}

.image-card:hover .image {
	transform: scale(1.05);
}

.image-overlay {
	position: absolute;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background: rgba(0, 0, 0, 0.7);
	display: flex;
	align-items: flex-start;
	justify-content: flex-end;
	opacity: 0;
	transition: opacity 0.3s ease;
	padding: 1rem;
}

.image-card:hover .image-overlay {
	opacity: 1;
}

.delete-btn {
	background: rgba(244, 67, 54, 0.9);
	border: none;
	border-radius: 50%;
	width: 40px;
	height: 40px;
	display: flex;
	align-items: center;
	justify-content: center;
	cursor: pointer;
	font-size: 1.1rem;
	transition: all 0.3s ease;
	backdrop-filter: blur(10px);
}

.delete-btn:hover {
	background: rgba(244, 67, 54, 1);
	transform: scale(1.1);
}

.image-info {
	padding: 1.2rem;
}

.image-title {
	color: #fff;
	margin-bottom: 0.5rem;
	font-weight: 500;
	font-size: 1.1rem;
	line-height: 1.3;
}

.image-description {
	color: #999;
	font-size: 0.9rem;
	line-height: 1.4;
	margin: 0;
	display: -webkit-box;
	-webkit-line-clamp: 2;
	-webkit-box-orient: vertical;
	overflow: hidden;
}

.no-images {
	text-align: center;
	padding: 4rem 2rem;
	color: #fff;
}

.no-images p {
	margin: 0.5rem 0;
	font-size: 1.1rem;
}

.hint {
	opacity: 0.6;
	font-size: 0.9rem !important;
}

.loading {
	text-align: center;
	padding: 3rem;
	color: #666;
	font-size: 1.1rem;
}

/* Анимации */
@keyframes fadeIn {
	from {
		opacity: 0;
		transform: translateY(20px);
	}
	to {
		opacity: 1;
		transform: translateY(0);
	}
}

.image-card {
	animation: fadeIn 0.6s ease forwards;
}

/* Адаптивность */
@media (max-width: 1200px) {
	.images-grid {
		grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
		gap: 1.5rem;
	}
}

@media (max-width: 768px) {
	.content {
		padding: 1rem;
		margin-top: 90px;
	}

	.search-info {
		flex-direction: column;
		gap: 1rem;
		text-align: center;
	}

	.images-grid {
		grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
		gap: 1rem;
	}

	.image-info {
		padding: 1rem;
	}

	.image-title {
		font-size: 1rem;
	}

	.image-description {
		font-size: 0.85rem;
	}
}

@media (max-width: 480px) {
	.images-grid {
		grid-template-columns: 1fr;
		gap: 1rem;
	}

	.content h2 {
		font-size: 1.5rem;
	}
}
</style>
