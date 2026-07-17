<template>
  <div class="min-h-screen">
    <section class="relative bg-gradient-to-r from-blue-600 to-indigo-700 text-white py-20">
      <div class="absolute inset-0 bg-black opacity-10"></div>
      <div class="container mx-auto px-4 relative z-10">
        <div class="text-center max-w-3xl mx-auto">
          <h1 class="text-4xl md:text-5xl font-bold mb-6">智能AI旅游规划平台</h1>
          <p class="text-xl text-blue-100 mb-8">输入您的目的地和偏好，AI帮您生成完美的旅行计划</p>
          <div class="flex flex-col sm:flex-row gap-4 justify-center">
            <router-link to="/route" class="px-8 py-4 bg-white text-blue-600 font-semibold rounded-lg hover:bg-blue-50 transition-colors">
              开始规划
            </router-link>
            <router-link to="/attractions" class="px-8 py-4 border-2 border-white text-white font-semibold rounded-lg hover:bg-white hover:text-blue-600 transition-colors">
              浏览景点
            </router-link>
          </div>
        </div>
      </div>
    </section>

    <section class="py-16 bg-white">
      <div class="container mx-auto px-4">
        <h2 class="text-2xl font-bold text-center text-gray-800 mb-12">热门路线</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <div
            v-for="route in hotRoutes"
            :key="route.city + route.days + route.style"
            :class="[
              'rounded-xl overflow-hidden shadow-md hover:shadow-xl transition-shadow cursor-pointer p-6',
              getRouteBgColor(route.style)
            ]"
            @click="goToRouteList(route)"
          >
            <div class="flex flex-col items-center text-center">
              <svg :class="['w-16 h-16 mb-4', getRouteIconColor(route.style)]" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path v-if="route.style === '美食'" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
                <path v-else-if="route.style === '历史文化'" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                <path v-else-if="route.style === '城市观光'" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                <path v-else-if="route.style === '亲子'" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                <path v-else stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3.055 11H5a2 2 0 012 2v1a2 2 0 002 2 2 2 0 012 2v2.945M8 3.935V5.5A2.5 2.5 0 0010.5 8h.5a2 2 0 012 2 2 2 0 104 0 2 2 0 012-2h1.064M15 20.488V18a2 2 0 012-2h3.064M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              <span :class="['text-sm font-medium mb-2', getRouteTextColor(route.style)]">{{ getRouteIconName(route.style) }}</span>
              <h3 class="font-bold text-lg text-gray-800 mb-1">{{ route.city }}</h3>
              <div class="flex items-center gap-2">
                <span class="text-sm text-gray-600">{{ route.days }}天</span>
                <span class="text-gray-400">·</span>
                <span class="text-sm text-gray-600">{{ route.count }}条路线</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="py-16 bg-gray-50">
      <div class="container mx-auto px-4">
        <h2 class="text-2xl font-bold text-center text-gray-800 mb-12">热门城市</h2>
        <div class="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4">
          <div
            v-for="city in hotCities"
            :key="city.city"
            class="bg-white rounded-xl p-4 text-center shadow-md hover:shadow-lg transition-shadow cursor-pointer"
            @click="$router.push(`/attractions?city=${city.city}`)"
          >
            <div class="w-16 h-16 bg-gradient-to-br from-blue-100 to-indigo-100 rounded-full flex items-center justify-center mx-auto mb-3">
              <svg class="w-8 h-8 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
              </svg>
            </div>
            <h3 class="font-medium text-gray-800">{{ city.city }}</h3>
            <p class="text-sm text-gray-500">{{ city.count }}条路线</p>
          </div>
        </div>
      </div>
    </section>

    <section class="py-16 bg-white">
      <div class="container mx-auto px-4">
        <h2 class="text-2xl font-bold text-center text-gray-800 mb-12">推荐景点</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div
            v-for="attraction in attractions"
            :key="attraction.id"
            class="bg-gray-50 rounded-xl overflow-hidden shadow-md hover:shadow-xl transition-shadow cursor-pointer"
            @click="$router.push(`/attractions/detail/${attraction.id}`)"
          >
            <div class="h-48 bg-gradient-to-br from-green-200 to-emerald-200 flex items-center justify-center">
              <img v-if="attraction.coverImage" :src="attraction.coverImage" :alt="attraction.name" class="w-full h-full object-cover" />
              <svg v-else class="w-20 h-20 text-green-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
            </div>
            <div class="p-4">
              <div class="flex items-center justify-between mb-2">
                <h3 class="font-semibold text-gray-800">{{ attraction.name }}</h3>
                <span class="text-yellow-500 font-semibold">{{ attraction.rating }}</span>
              </div>
              <p class="text-sm text-gray-500 mb-2">{{ attraction.city }} · {{ attraction.address }}</p>
              <p class="text-sm text-green-600 font-medium">¥{{ attraction.ticketPrice }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { routeApi, attractionApi } from '@/api'

const router = useRouter()
const hotRoutes = ref([])
const hotCities = ref([])
const attractions = ref([])

onMounted(async () => {
  try {
    hotRoutes.value = await routeApi.getHotRoutes(8)
  } catch (e) {
    console.error('获取热门路线失败', e)
  }

  try {
    hotCities.value = await routeApi.getHotRoutesByCity(6)
  } catch (e) {
    console.error('获取热门城市失败', e)
  }

  try {
    attractions.value = (await attractionApi.getAll()).slice(0, 6)
  } catch (e) {
    console.error('获取景点失败', e)
  }
})

function getRouteBgColor(style) {
  const colors = {
    '美食': 'bg-gradient-to-br from-orange-400 to-red-500',
    '历史文化': 'bg-gradient-to-br from-amber-400 to-yellow-500',
    '城市观光': 'bg-gradient-to-br from-blue-400 to-cyan-500',
    '亲子': 'bg-gradient-to-br from-pink-400 to-rose-500',
    '自然': 'bg-gradient-to-br from-green-400 to-emerald-500',
    '购物': 'bg-gradient-to-br from-purple-400 to-violet-500',
    '海滨度假': 'bg-gradient-to-br from-cyan-400 to-blue-500',
    '夜景': 'bg-gradient-to-br from-indigo-500 to-purple-600'
  }
  return colors[style] || 'bg-gradient-to-br from-gray-400 to-gray-500'
}

function getRouteIconColor(style) {
  const colors = {
    '美食': 'text-white',
    '历史文化': 'text-white',
    '城市观光': 'text-white',
    '亲子': 'text-white',
    '自然': 'text-white',
    '购物': 'text-white',
    '海滨度假': 'text-white',
    '夜景': 'text-white'
  }
  return colors[style] || 'text-white'
}

function getRouteTextColor(style) {
  const colors = {
    '美食': 'text-white/80',
    '历史文化': 'text-white/80',
    '城市观光': 'text-white/80',
    '亲子': 'text-white/80',
    '自然': 'text-white/80',
    '购物': 'text-white/80',
    '海滨度假': 'text-white/80',
    '夜景': 'text-white/80'
  }
  return colors[style] || 'text-white/80'
}

function getRouteIconName(style) {
  const names = {
    '美食': '美食之旅',
    '历史文化': '文化探索',
    '城市观光': '城市漫游',
    '亲子': '亲子出游',
    '自然': '自然风光',
    '购物': '购物狂欢',
    '海滨度假': '海滨度假',
    '夜景': '夜景欣赏'
  }
  return names[style] || style
}

function goToRouteList(route) {
  router.push(`/route/list/${route.city}`)
}
</script>
