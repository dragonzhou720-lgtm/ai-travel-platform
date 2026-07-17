import axios from 'axios'
import { useUserStore } from '@/stores/user'

const request = axios.create({
  baseURL: '/api',
  timeout: 30000
})

request.interceptors.request.use((config) => {
  const userStore = useUserStore()
  if (userStore.token) {
    config.headers.Authorization = `Bearer ${userStore.token}`
  }
  return config
}, (error) => {
  return Promise.reject(error)
})

request.interceptors.response.use((response) => {
  const data = response.data
  if (data && data.value && Array.isArray(data.value)) {
    return data.value
  }
  return data
}, (error) => {
  if (error.response?.status === 401) {
    const userStore = useUserStore()
    userStore.logout()
    window.location.href = '/login'
  }
  return Promise.reject(error)
})

export const userApi = {
  login(data) {
    return request.post('/user/login', data)
  },
  register(data) {
    return request.post('/user/register', data)
  },
  getUserInfo() {
    return request.get('/user/info')
  },
  getUserByUsername(username) {
    return request.get(`/user/${username}`)
  }
}

export const attractionApi = {
  search(city, keyword) {
    return request.get('/attraction/search', { params: { city, keyword } })
  },
  getDetail(id) {
    return request.get('/attraction/detail', { params: { id } })
  },
  getAll() {
    return request.get('/attraction/all')
  }
}

export const hotelApi = {
  search(city, keyword) {
    return request.get('/hotel/search', { params: { city, keyword } })
  },
  getDetail(id) {
    return request.get('/hotel/detail', { params: { id } })
  }
}

export const routeApi = {
  generate(data) {
    return request.post('/route/generate', data)
  },
  getRoute(id) {
    return request.get(`/route/${id}`)
  },
  getAllRoutes() {
    return request.get('/route')
  },
  getRoutesByCity(city) {
    return request.get(`/route/city/${city}`)
  },
  getHotRoutes(limit = 10) {
    return request.get('/route/hot', { params: { limit } })
  },
  getHotRoutesByCity(limit = 10) {
    return request.get('/route/hot/cities', { params: { limit } })
  }
}

export const favoriteApi = {
  add(data) {
    return request.post('/favorites', data)
  },
  remove(userId, targetId, targetType) {
    return request.delete('/favorites', { params: { userId, targetId, targetType } })
  },
  getFavorites(userId) {
    return request.get(`/favorites/user/${userId}`)
  },
  getFavoritesByType(userId, targetType) {
    return request.get(`/favorites/user/${userId}/type/${targetType}`)
  },
  check(userId, targetId, targetType) {
    return request.get('/favorites/check', { params: { userId, targetId, targetType } })
  }
}

export const aiApi = {
  generateRoute(data) {
    return request.post('/ai/generate-route', data)
  },
  generatePlan(data) {
    return request.post('/ai/plan', data)
  }
}

export default request
