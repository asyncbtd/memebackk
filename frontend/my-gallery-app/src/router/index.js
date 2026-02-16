import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'

const routes = [
	{
		path: '/',
		name: 'home',
		component: HomeView,
	},
	// Добавляем маршрут для /login чтобы избежать ошибки
	{
		path: '/login',
		name: 'login',
		redirect: '/', // временно перенаправляем на главную
	},
]

const router = createRouter({
	history: createWebHistory(import.meta.env.BASE_URL),
	routes,
})

export default router
