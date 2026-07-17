<template>
  <div class="min-h-screen bg-gray-50">
    <div class="bg-gradient-to-r from-blue-600 to-indigo-700 text-white py-12">
      <div class="container mx-auto px-4">
        <div class="flex items-center justify-between">
          <div>
            <h1 class="text-3xl font-bold">{{ city }}路线列表</h1>
            <p class="text-blue-100 mt-2">共{{ routes.length }}条路线</p>
          </div>
          <router-link
            to="/route"
            class="flex items-center gap-2 px-6 py-3 bg-white text-blue-600 rounded-lg hover:bg-blue-50 transition-colors"
          >
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
            </svg>
            AI生成路线
          </router-link>
        </div>
      </div>
    </div>

    <div class="container mx-auto px-4 py-8">
      <div v-if="loading" class="flex items-center justify-center py-20">
        <svg class="animate-spin h-10 w-10 text-blue-600" fill="none" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
        </svg>
      </div>

      <div v-else-if="routes.length === 0" class="text-center py-20">
        <svg class="w-16 h-16 text-gray-300 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
        </svg>
        <p class="text-gray-500">暂无路线数据</p>
        <router-link to="/route" class="mt-4 inline-block px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors">
          生成新路线
        </router-link>
      </div>

      <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div
          v-for="route in routes"
          :key="route.id"
          class="bg-white rounded-xl overflow-hidden shadow-md hover:shadow-xl transition-shadow cursor-pointer"
          @click="$router.push(`/route/detail/${route.id}`)"
        >
          <div :class="['p-6', getRouteBgColor(route.style)]">
            <div class="flex items-center justify-between mb-4">
              <span :class="['text-xs font-medium px-3 py-1 rounded-full', getRouteTagColor(route.style)]">{{ route.style }}</span>
              <span class="text-white/80">{{ route.days }}天</span>
            </div>
            <h3 class="text-xl font-bold text-white mb-1">{{ route.city }}</h3>
            <p class="text-white/70 text-sm">{{ route.aiSummary ? route.aiSummary.substring(0, 50) + '...' : '暂无描述' }}</p>
          </div>
          <div class="p-4">
            <div class="flex items-center justify-between">
              <div class="flex items-center gap-2">
                <svg class="w-4 h-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                </svg>
                <span class="text-sm text-gray-600">{{ route.city }}</span>
              </div>
              <button
                @click.stop="handleViewDetail(route.id)"
                class="px-4 py-2 bg-blue-600 text-white text-sm rounded-lg hover:bg-blue-700 transition-colors"
              >
                查看详情
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { routeApi } from '@/api'

const routerRoute = useRoute()
const router = useRouter()

const city = ref('')
const routes = ref([])
const loading = ref(true)

onMounted(async () => {
  city.value = routerRoute.params.city || routerRoute.query.city || ''
  if (!city.value) {
    router.push('/')
    return
  }
  
  try {
    routes.value = await routeApi.getRoutesByCity(city.value)
  } catch (e) {
    console.error('获取路线列表失败', e)
  } finally {
    loading.value = false
  }
})

function handleViewDetail(id) {
  router.push(`/route/detail/${id}`)
}

function getRouteBgColor(style) {
  const colors = {
    '美食': 'bg-gradient-to-br from-orange-400 to-red-500',
    '历史文化': 'bg-gradient-to-br from-amber-400 to-yellow-500',
    '城市观光': 'bg-gradient-to-br from-blue-400 to-cyan-500',
    '亲子': 'bg-gradient-to-br from-pink-400 to-rose-500',
    '自然': 'bg-gradient-to-br from-green-400 to-emerald-500',
    '购物': 'bg-gradient-to-br from-purple-400 to-violet-500',
    '海滨度假': 'bg-gradient-to-br from-cyan-400 to-blue-500',
    '夜景': 'bg-gradient-to-br from-indigo-500 to-purple-600',
    '休闲': 'bg-gradient-to-br from-teal-400 to-cyan-500',
    '文化': 'bg-gradient-to-br from-amber-400 to-orange-500',
    '自然风光': 'bg-gradient-to-br from-green-400 to-teal-500'
  }
  return colors[style] || 'bg-gradient-to-br from-gray-400 to-gray-500'
}

function getRouteTagColor(style) {
  const colors = {
    '美食': 'bg-orange-100 text-orange-600',
    '历史文化': 'bg-amber-100 text-amber-600',
    '城市观光': 'bg-blue-100 text-blue-600',
    '亲子': 'bg-pink-100 text-pink-600',
    '自然': 'bg-green-100 text-green-600',
    '购物': 'bg-purple-100 text-purple-600',
    '海滨度假': 'bg-cyan-100 text-cyan-600',
    '夜景': 'bg-indigo-100 text-indigo-600',
    '休闲': 'bg-teal-100 text-teal-600',
    '文化': 'bg-amber-100 text-amber-600',
    '自然风光': 'bg-green-100 text-green-600'
  }
  return colors[style] || 'bg-gray-100 text-gray-600'
}
</script>
