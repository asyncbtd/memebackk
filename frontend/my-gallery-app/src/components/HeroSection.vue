<template>
	<section class="hero-section">
		<div class="hero-content">
			<h1 class="hero-title">Image Gallery</h1>
			<p class="hero-description">
				Discover and organize your favorite images. Upload, search, and manage your visual collection with ease.
			</p>
			<div class="hero-actions">
				<button class="play-btn download-btn" @click="handleDownload">
					<UploadIcon />
					Upload Image
				</button>
				<button class="more-info-btn" @click="handleMorePictures">
					<GalleryIcon />
					View Gallery
				</button>
			</div>
		</div>
		<div class="hero-overlay"></div>
	</section>
</template>

<script setup>
import UploadIcon from './icons/UploadIcon.vue'
import GalleryIcon from './icons/GalleryIcon.vue'

const emit = defineEmits(['download', 'more-pictures'])

const handleDownload = () => {
  emit('download')
}

const handleMorePictures = () => {
  const gallerySection = document.querySelector('.content')
  if (gallerySection) {
    gallerySection.scrollIntoView({ behavior: 'smooth' })
  }
  emit('more-pictures')
}
</script>

<style scoped>
.hero-section {
	position: relative;
	height: 70vh;
	min-height: 500px;
	background: linear-gradient(rgba(0, 0, 0, 0.6), rgba(0, 0, 0, 0.6)),
		url('/images/hero-bg.jpg')
			center/cover;
	display: flex;
	align-items: center;
	padding: 0 4%;
	margin-bottom: 2rem;
}

.hero-content {
	max-width: 600px;
	z-index: 2;
	position: relative;
}

.hero-title {
	color: #fff;
	font-size: 3rem;
	font-weight: 300;
	margin-bottom: 1rem;
	text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
	letter-spacing: 3px;
}

.hero-description {
	color: #ccc;
	font-size: 1.1rem;
	line-height: 1.6;
	margin-bottom: 2rem;
	text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
	max-width: 500px;
}

.hero-actions {
	display: flex;
	gap: 1rem;
}

.download-btn,
.more-info-btn {
	display: flex;
	align-items: center;
	gap: 0.5rem;
	padding: 0.8rem 2rem;
	border: none;
	border-radius: 8px;
	font-size: 1rem;
	font-weight: 500;
	cursor: pointer;
	transition: all 0.3s;
}

.download-btn {
	background-color: #fff;
	color: #000;
}

.download-btn:hover {
	background-color: rgba(255, 255, 255, 0.9);
	transform: translateY(-2px);
	box-shadow: 0 10px 20px rgba(0, 0, 0, 0.3);
}

.more-info-btn {
	background-color: rgba(255, 255, 255, 0.1);
	color: #fff;
	border: 1px solid rgba(255, 255, 255, 0.3);
	backdrop-filter: blur(10px);
}

.more-info-btn:hover {
	background-color: rgba(255, 255, 255, 0.2);
	transform: translateY(-2px);
	box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
}

.hero-overlay {
	position: absolute;
	bottom: 0;
	left: 0;
	right: 0;
	height: 7.4rem;
	background-image: linear-gradient(
		180deg,
		transparent,
		rgba(10, 10, 10, 0.8),
		#0a0a0a
	);
}

@media (max-width: 768px) {
	.hero-title {
		font-size: 2rem;
	}

	.hero-description {
		font-size: 1rem;
	}

	.hero-actions {
		flex-direction: column;
		align-items: flex-start;
	}
	
	.download-btn,
	.more-info-btn {
		width: 100%;
		justify-content: center;
	}
}
</style>