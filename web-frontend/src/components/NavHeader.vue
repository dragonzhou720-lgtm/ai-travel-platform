<template>
  <header class="bg-white shadow-md sticky top-0 z-50">
    <div class="container mx-auto px-4">
      <div class="flex items-center justify-between h-16">
        <router-link to="/" class="flex items-center space-x-2">
          <svg class="w-8 h-8 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3.055 11H5a2 2 0 012 2v1a2 2 0 002 2 2 2 0 012 2v2.945M8 3.935V5.5A2.5 2.5 0 0010.5 8h.5a2 2 0 012 2 2 2 0 104 0 2 2 0 012-2h1.064M15 20.488V18a2 2 0 012-2h3.064M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          <span class="text-xl font-bold text-gray-800">AI旅游规划</span>
        </router-link>

        <nav class="hidden md:flex items-center space-x-8">
          <router-link to="/" class="text-gray-600 hover:text-blue-600 transition-colors">首页</router-link>
          <router-link to="/attractions" class="text-gray-600 hover:text-blue-600 transition-colors">景点</router-link>
          <router-link to="/hotels" class="text-gray-600 hover:text-blue-600 transition-colors">酒店</router-link>
          <router-link to="/route" class="text-gray-600 hover:text-blue-600 transition-colors">AI规划</router-link>
          <router-link to="/favorites" class="text-gray-600 hover:text-blue-600 transition-colors">我的收藏</router-link>
        </nav>

        <div class="flex items-center space-x-4">
          <template v-if="userStore.isLoggedIn">
            <router-link to="/profile" class="flex items-center space-x-2 text-gray-600 hover:text-blue-600">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
              </svg>
              <span class="hidden sm:inline">{{ userStore.userInfo?.username || '用户' }}</span>
            </router-link>
            <button @click="handleLogout" class="px-4 py-2 text-red-600 hover:bg-red-50 rounded-lg transition-colors">
              退出登录
            </button>
          </template>
          <template v-else>
            <router-link to="/login" class="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded-lg transition-colors">登录</router-link>
            <router-link to="/register" class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors">注册</router-link>
          </template>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { userApi } from '@/api'

const userStore = useUserStore()

async function handleLogout() {
  userStore.logout()
  window.location.href = '/login'
}

onMounted(async () => {
  if (userStore.isLoggedIn && !userStore.userInfo) {
    try {
      const info = await userApi.getUserInfo()
      userStore.setUserInfo(info)
    } catch (e) {
      userStore.logout()
    }
  }
})
</script>
