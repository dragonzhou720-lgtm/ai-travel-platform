<template>
  <div class="min-h-screen">
    <div v-if="loading" class="flex items-center justify-center py-20">
      <svg class="animate-spin h-10 w-10 text-blue-600" fill="none" viewBox="0 0 24 24">
        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
        <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
      </svg>
    </div>

    <div v-else-if="attraction" class="bg-white">
      <div class="h-64 bg-gradient-to-br from-green-200 to-emerald-200 flex items-center justify-center">
        <img v-if="attraction.coverImage" :src="attraction.coverImage" :alt="attraction.name" class="w-full h-full object-cover" />
        <svg v-else class="w-32 h-32 text-green-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
        </svg>
      </div>

      <div class="container mx-auto px-4 py-8">
        <div class="flex items-start justify-between mb-6">
          <div>
            <h1 class="text-3xl font-bold text-gray-800 mb-2">{{ attraction.name }}</h1>
            <div class="flex items-center gap-4">
              <span class="text-yellow-500 font-semibold text-xl">{{ attraction.rating }}</span>
              <span class="text-gray-500">{{ attraction.city }}</span>
              <span class="text-gray-500">{{ attraction.address }}</span>
            </div>
          </div>
          <div class="text-right">
            <p class="text-green-600 font-bold text-2xl">¥{{ attraction.ticketPrice }}</p>
            <p class="text-gray-500 text-sm">门票价格</p>
          </div>
        </div>

        <div class="flex gap-2 mb-6">
          <span
            v-for="tag in (attraction.tags || [])"
            :key="tag"
            class="px-3 py-1 bg-blue-100 text-blue-600 rounded-full text-sm"
          >
            {{ tag }}
          </span>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div class="bg-gray-50 rounded-xl p-4">
            <svg class="w-8 h-8 text-gray-400 mb-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <h3 class="font-semibold text-gray-800 mb-1">开放时间</h3>
            <p class="text-gray-600">{{ attraction.open_time || '暂无信息' }}</p>
          </div>
          <div class="bg-gray-50 rounded-xl p-4">
            <svg class="w-8 h-8 text-gray-400 mb-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
            </svg>
            <h3 class="font-semibold text-gray-800 mb-1">地址</h3>
            <p class="text-gray-600">{{ attraction.address }}</p>
          </div>
          <div class="bg-gray-50 rounded-xl p-4">
            <svg class="w-8 h-8 text-gray-400 mb-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
            </svg>
            <h3 class="font-semibold text-gray-800 mb-1">联系电话</h3>
            <p class="text-gray-600">暂无信息</p>
          </div>
        </div>

        <div class="mb-8">
          <h2 class="text-xl font-bold text-gray-800 mb-4">景点介绍</h2>
          <p class="text-gray-600 leading-relaxed">{{ attraction.description }}</p>
        </div>

        <div class="flex gap-4">
          <button
            @click="handleFavorite"
            :class="[
              'flex items-center gap-2 px-6 py-3 rounded-lg transition-colors',
              isFavorite ? 'bg-red-100 text-red-600' : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
            ]"
          >
            <svg class="w-5 h-5" :fill="isFavorite ? 'currentColor' : 'none'" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
            </svg>
            {{ isFavorite ? '取消收藏' : '收藏景点' }}
          </button>
          <button
            @click="handlePlan"
            class="flex items-center gap-2 px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
          >
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
            </svg>
            加入行程
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { attractionApi, favoriteApi } from '@/api'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const loading = ref(true)
const attraction = ref(null)
const isFavorite = ref(false)

onMounted(async () => {
  const id = route.params.id
  try {
    attraction.value = await attractionApi.getDetail(id)
    if (userStore.isLoggedIn && userStore.userInfo) {
      const check = await favoriteApi.check(userStore.userInfo.id, id, 'attraction')
      isFavorite.value = check.isFavorite
    }
  } catch (e) {
    console.error('获取景点详情失败', e)
  } finally {
    loading.value = false
  }
})

async function handleFavorite() {
  if (!userStore.isLoggedIn) {
    router.push('/login')
    return
  }

  try {
    if (isFavorite.value) {
      await favoriteApi.remove(userStore.userInfo.id, attraction.value.id, 'attraction')
      isFavorite.value = false
    } else {
      await favoriteApi.add({
        userId: userStore.userInfo.id,
        targetId: attraction.value.id,
        targetType: 'attraction',
        targetName: attraction.value.name
      })
      isFavorite.value = true
    }
  } catch (e) {
    console.error('操作收藏失败', e)
  }
}

function handlePlan() {
  if (!userStore.isLoggedIn) {
    router.push('/login')
    return
  }
  router.push('/route')
}
</script>
