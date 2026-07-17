<template>
  <div class="min-h-screen bg-gray-50">
    <div v-if="loading" class="flex items-center justify-center py-20">
      <svg class="animate-spin h-10 w-10 text-blue-600" fill="none" viewBox="0 0 24 24">
        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
        <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
      </svg>
    </div>

    <div v-else-if="route" class="bg-white">
      <div class="bg-gradient-to-r from-blue-600 via-indigo-600 to-purple-700 text-white py-20">
        <div class="container mx-auto px-4">
          <div class="flex flex-col md:flex-row items-start md:items-center justify-between gap-6">
            <div>
              <div class="flex items-center gap-2 mb-3">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3.055 11H5a2 2 0 012 2v1a2 2 0 002 2 2 2 0 012 2v2.945M8 3.935V5.5A2.5 2.5 0 0010.5 8h.5a2 2 0 012 2 2 2 0 104 0 2 2 0 012-2h1.064M15 20.488V18a2 2 0 012-2h3.064M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <span class="text-blue-200 text-sm">{{ route.city }}</span>
              </div>
              <h1 class="text-4xl md:text-5xl font-bold mb-4">{{ route.name }}</h1>
              <div class="flex flex-wrap items-center gap-3 mt-4">
                <span class="bg-white/20 backdrop-blur px-4 py-2 rounded-full text-sm font-medium">
                  <svg class="w-4 h-4 inline mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                  </svg>
                  {{ route.destination }}
                </span>
                <span class="bg-white/20 backdrop-blur px-4 py-2 rounded-full text-sm font-medium">
                  <svg class="w-4 h-4 inline mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                  </svg>
                  {{ route.days }}天
                </span>
                <span class="bg-white/20 backdrop-blur px-4 py-2 rounded-full text-sm font-medium">
                  <svg class="w-4 h-4 inline mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                  </svg>
                  {{ route.preference }}
                </span>
              </div>
            </div>
            <div class="text-right bg-white/10 backdrop-blur-lg rounded-2xl p-6">
              <p class="text-blue-200 text-sm mb-1">预计预算</p>
              <p class="text-4xl font-bold">¥{{ route.budget || '0' }}</p>
              <p class="text-blue-200 text-xs mt-2">人均消费</p>
            </div>
          </div>
        </div>
      </div>

      <div class="container mx-auto px-4 py-8">
        <div v-if="route.aiSummary" class="mb-8 bg-gradient-to-br from-indigo-50 to-purple-50 rounded-2xl p-8 border border-indigo-100">
          <div class="flex items-center gap-3 mb-4">
            <div class="w-10 h-10 bg-gradient-to-br from-indigo-500 to-purple-600 rounded-full flex items-center justify-center">
              <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
              </svg>
            </div>
            <h3 class="text-xl font-bold text-gray-800">AI智能规划建议</h3>
          </div>
          <div class="prose prose-indigo max-w-none">
            <p class="text-gray-600 leading-relaxed text-lg">{{ route.aiSummary }}</p>
          </div>
          <div class="mt-4 flex items-center gap-2 text-indigo-600">
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <span class="text-sm">基于您的偏好智能生成</span>
          </div>
        </div>

        <div class="mb-6">
          <h2 class="text-2xl font-bold text-gray-800 mb-2">行程安排</h2>
          <p class="text-gray-500">共{{ route.routeDays?.length || 0 }}天行程</p>
        </div>

        <div v-if="route.routeDays && route.routeDays.length > 0" class="space-y-6">
          <div
            v-for="(day, index) in route.routeDays"
            :key="day.day || index"
            class="bg-white rounded-2xl overflow-hidden shadow-lg hover:shadow-xl transition-shadow border border-gray-100"
          >
            <div class="bg-gradient-to-r from-blue-500 to-indigo-600 px-6 py-5">
              <div class="flex items-center gap-4">
                <div class="w-14 h-14 bg-white/20 backdrop-blur rounded-full flex items-center justify-center">
                  <span class="text-white text-2xl font-bold">D{{ day.day || index + 1 }}</span>
                </div>
                <div>
                  <h3 class="text-xl font-bold text-white">{{ day.title || `第${day.day || index + 1}天行程` }}</h3>
                  <p class="text-blue-100 text-sm mt-1">{{ formatDate(index) }}</p>
                </div>
              </div>
            </div>
            <div class="p-6">
              <div class="prose prose-sm max-w-none">
                <p class="text-gray-600 leading-relaxed">{{ day.scheduleDetail }}</p>
              </div>
              <div v-if="day.attractionIds" class="mt-6 flex flex-wrap gap-3">
                <span
                  v-for="attrId in day.attractionIds"
                  :key="attrId"
                  class="inline-flex items-center gap-2 px-4 py-2 bg-green-50 text-green-700 rounded-lg text-sm font-medium border border-green-200"
                >
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                  </svg>
                  景点 {{ attrId }}
                </span>
              </div>
            </div>
          </div>
        </div>

        <div v-else class="text-center py-16 bg-gray-50 rounded-2xl">
          <svg class="w-16 h-16 text-gray-300 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
          </svg>
          <p class="text-gray-500">暂无具体行程安排</p>
        </div>

        <div class="mt-8 flex flex-wrap gap-4 justify-center">
          <button
            @click="handleFavorite"
            :class="[
              'flex items-center gap-2 px-8 py-3 rounded-xl transition-all transform hover:scale-105',
              isFavorite ? 'bg-red-500 text-white shadow-lg shadow-red-200' : 'bg-white text-gray-700 border-2 border-gray-200 hover:border-red-300 hover:text-red-500'
            ]"
          >
            <svg class="w-5 h-5" :fill="isFavorite ? 'currentColor' : 'none'" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
            </svg>
            {{ isFavorite ? '取消收藏' : '收藏路线' }}
          </button>
          <router-link
            to="/route"
            class="flex items-center gap-2 px-8 py-3 bg-gradient-to-r from-blue-600 to-indigo-600 text-white rounded-xl hover:from-blue-700 hover:to-indigo-700 transition-all transform hover:scale-105 shadow-lg shadow-blue-200"
          >
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
            </svg>
            AI生成新路线
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { routeApi, favoriteApi } from '@/api'

const routerRoute = useRoute()
const router = useRouter()
const userStore = useUserStore()

const loading = ref(true)
const routeData = ref(null)
const isFavorite = ref(false)

const route = computed(() => routeData.value)

onMounted(async () => {
  const id = routerRoute.params.id
  try {
    routeData.value = await routeApi.getRoute(id)
    if (userStore.isLoggedIn && userStore.userInfo) {
      const check = await favoriteApi.check(userStore.userInfo.id, id, 'route')
      isFavorite.value = check.isFavorite
    }
  } catch (e) {
    console.error('获取路线详情失败', e)
  } finally {
    loading.value = false
  }
})

function formatDate(index) {
  const days = ['第一天', '第二天', '第三天', '第四天', '第五天', '第六天', '第七天']
  return days[index] || `第${index + 1}天`
}

async function handleFavorite() {
  if (!userStore.isLoggedIn) {
    router.push('/login')
    return
  }

  if (!route.value?.id) return

  try {
    if (isFavorite.value) {
      await favoriteApi.remove(userStore.userInfo.id, route.value.id, 'route')
      isFavorite.value = false
    } else {
      await favoriteApi.add({
        userId: userStore.userInfo.id,
        targetId: route.value.id,
        targetType: 'route',
        targetName: route.value.name
      })
      isFavorite.value = true
    }
  } catch (e) {
    console.error('操作收藏失败', e)
  }
}
</script>
