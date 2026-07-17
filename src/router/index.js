import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/plan',
    name: 'Plan',
    component: () => import('@/views/Plan.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/route/:id',
    name: 'RouteDetail',
    component: () => import('@/views/RouteDetail.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/attractions',
    name: 'CityAttractions',
    component: () => import('@/views/CityAttractions.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/attraction/:id',
    name: 'AttractionDetail',
    component: () => import('@/views/AttractionDetail.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/hotels',
    name: 'CityHotels',
    component: () => import('@/views/CityHotels.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/hotel/:id',
    name: 'HotelDetail',
    component: () => import('@/views/HotelDetail.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/favorites',
    name: 'Favorites',
    component: () => import('@/views/Favorites.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/history',
    name: 'History',
    component: () => import('@/views/History.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/Profile.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    next('/login')
  } else {
    next()
  }
})

export default router
