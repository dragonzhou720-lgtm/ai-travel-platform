import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login, register, getUserInfo } from '@/api/user'
import { getUserIdFromToken } from '@/utils/jwt'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const username = ref('')
  const roles = ref([])
  const userId = ref(null)

  const isLoggedIn = computed(() => !!token.value)

  async function handleLogin(username, password) {
    const response = await login(username, password)
    token.value = response.token
    localStorage.setItem('token', response.token)
    userId.value = getUserIdFromToken(response.token) || 1
    await fetchUserInfo()
    return response
  }

  async function handleRegister(username, password) {
    return await register(username, password)
  }

  async function fetchUserInfo() {
    try {
      const response = await getUserInfo()
      username.value = response.username
      roles.value = response.roles || []
    } catch (error) {
      console.error('获取用户信息失败', error)
    }
  }

  function logout() {
    token.value = ''
    username.value = ''
    roles.value = []
    userId.value = null
    localStorage.removeItem('token')
  }

  return {
    token,
    username,
    roles,
    userId,
    isLoggedIn,
    handleLogin,
    handleRegister,
    fetchUserInfo,
    logout
  }
})
