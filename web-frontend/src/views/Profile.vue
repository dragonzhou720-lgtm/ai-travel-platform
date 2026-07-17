<template>
  <div class="min-h-screen">
    <div class="bg-gradient-to-r from-blue-600 to-indigo-700 text-white py-16">
      <div class="container mx-auto px-4">
        <div class="flex items-center gap-6">
          <div class="w-24 h-24 bg-white/20 rounded-full flex items-center justify-center">
            <svg class="w-12 h-12" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>
          </div>
          <div>
            <h1 class="text-2xl font-bold">{{ userInfo?.nickname || userInfo?.username }}</h1>
            <p class="text-blue-200 mt-1">{{ userInfo?.email || userInfo?.phone }}</p>
          </div>
        </div>
      </div>
    </div>

    <div class="container mx-auto px-4 py-8">
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div class="bg-white rounded-xl shadow-md p-6">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-gray-500 text-sm">收藏景点</p>
              <p class="text-2xl font-bold text-green-600">{{ attractionCount }}</p>
            </div>
            <div class="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center">
              <svg class="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-xl shadow-md p-6">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-gray-500 text-sm">收藏酒店</p>
              <p class="text-2xl font-bold text-blue-600">{{ hotelCount }}</p>
            </div>
            <div class="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center">
              <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
              </svg>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-xl shadow-md p-6">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-gray-500 text-sm">收藏路线</p>
              <p class="text-2xl font-bold text-purple-600">{{ routeCount }}</p>
            </div>
            <div class="w-12 h-12 bg-purple-100 rounded-full flex items-center justify-center">
              <svg class="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3.055 11H5a2 2 0 012 2v1a2 2 0 002 2 2 2 0 012 2v2.945M8 3.935V5.5A2.5 2.5 0 0010.5 8h.5a2 2 0 012 2 2 2 0 104 0 2 2 0 012-2h1.064M15 20.488V18a2 2 0 012-2h3.064M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
          </div>
        </div>
      </div>

      <div class="mt-6 bg-white rounded-xl shadow-md p-6">
        <h2 class="text-lg font-bold text-gray-800 mb-4">个人信息</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div class="p-4 bg-gray-50 rounded-lg">
            <p class="text-gray-500 text-sm">用户名</p>
            <p class="text-gray-800 font-medium">{{ userInfo?.username }}</p>
          </div>
          <div class="p-4 bg-gray-50 rounded-lg">
            <p class="text-gray-500 text-sm">昵称</p>
            <p class="text-gray-800 font-medium">{{ userInfo?.nickname || '-' }}</p>
          </div>
          <div class="p-4 bg-gray-50 rounded-lg">
            <p class="text-gray-500 text-sm">邮箱</p>
            <p class="text-gray-800 font-medium">{{ userInfo?.email || '-' }}</p>
          </div>
          <div class="p-4 bg-gray-50 rounded-lg">
            <p class="text-gray-500 text-sm">手机号</p>
            <p class="text-gray-800 font-medium">{{ userInfo?.phone || '-' }}</p>
          </div>
          <div class="p-4 bg-gray-50 rounded-lg">
            <p class="text-gray-500 text-sm">性别</p>
            <p class="text-gray-800 font-medium">{{ getGenderText(userInfo?.gender) }}</p>
          </div>
          <div class="p-4 bg-gray-50 rounded-lg">
            <p class="text-gray-500 text-sm">注册时间</p>
            <p class="text-gray-800 font-medium">{{ formatDate(userInfo?.createdAt) }}</p>
          </div>
        </div>
      </div>

      <div class="mt-6">
        <button
          @click="handleLogout"
          class="w-full px-6 py-4 bg-red-100 text-red-600 font-semibold rounded-xl hover:bg-red-200 transition-colors"
        >
          退出登录
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { userApi, favoriteApi } from '@/api'

const router = useRouter()
const userStore = useUserStore()

const userInfo = ref(null)
const attractionCount = ref(0)
const hotelCount = ref(0)
const routeCount = ref(0)

onMounted(async () => {
  if (!userStore.isLoggedIn) {
    router.push('/login')
    return
  }

  try {
    userInfo.value = await userApi.getUserInfo()
    userStore.setUserInfo(userInfo.value)

    const [attractions, hotels, routes] = await Promise.all([
      favoriteApi.getFavoritesByType(userInfo.value.id, 'attraction'),
      favoriteApi.getFavoritesByType(userInfo.value.id, 'hotel'),
      favoriteApi.getFavoritesByType(userInfo.value.id, 'route')
    ])

    attractionCount.value = attractions.length
    hotelCount.value = hotels.length
    routeCount.value = routes.length
  } catch (e) {
    console.error('获取用户信息失败', e)
    userStore.logout()
    router.push('/login')
  }
})

function getGenderText(gender) {
  const map = { 0: '未知', 1: '男', 2: '女' }
  return map[gender] || '未知'
}

function formatDate(dateStr) {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}

function handleLogout() {
  userStore.logout()
  router.push('/login')
}
</script>
