import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue')
  },
  {
    path: '/attractions',
    name: 'Attractions',
    component: () => import('@/views/Attractions.vue')
  },
  {
    path: '/attractions/detail/:id',
    name: 'AttractionDetail',
    component: () => import('@/views/AttractionDetail.vue')
  },
  {
    path: '/hotels',
    name: 'Hotels',
    component: () => import('@/views/Hotels.vue')
  },
  {
    path: '/hotels/detail/:id',
    name: 'HotelDetail',
    component: () => import('@/views/HotelDetail.vue')
  },
  {
    path: '/route',
    name: 'Route',
    component: () => import('@/views/Route.vue')
  },
  {
    path: '/route/list/:city',
    name: 'RouteList',
    component: () => import('@/views/RouteList.vue')
  },
  {
    path: '/route/detail/:id',
    name: 'RouteDetail',
    component: () => import('@/views/RouteDetail.vue')
  },
  {
    path: '/favorites',
    name: 'Favorites',
    component: () => import('@/views/Favorites.vue')
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/Profile.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  const publicPages = ['/login', '/register', '/']
  
  if (!publicPages.includes(to.path) && !userStore.token) {
    next('/login')
  } else {
    next()
  }
})

export default router
