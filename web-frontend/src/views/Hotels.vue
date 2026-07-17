<template>
  <div class="min-h-screen">
    <div class="bg-white shadow-sm mb-6">
      <div class="container mx-auto px-4 py-4">
        <div class="flex flex-col md:flex-row gap-4">
          <div class="flex-1">
            <input
              v-model="keyword"
              type="text"
              placeholder="搜索酒店名称..."
              class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            />
          </div>
          <div class="flex gap-4">
            <select
              v-model="city"
              class="px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            >
              <option value="">全部城市</option>
              <option value="北京">北京</option>
              <option value="上海">上海</option>
              <option value="广州">广州</option>
              <option value="深圳">深圳</option>
              <option value="杭州">杭州</option>
              <option value="成都">成都</option>
              <option value="西安">西安</option>
              <option value="重庆">重庆</option>
              <option value="武汉">武汉</option>
              <option value="南京">南京</option>
            </select>
            <button
              @click="handleSearch"
              class="px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
            >
              搜索
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="container mx-auto px-4">
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div
          v-for="hotel in hotels"
          :key="hotel.id"
          class="bg-white rounded-xl overflow-hidden shadow-md hover:shadow-xl transition-shadow cursor-pointer"
          @click="$router.push(`/hotels/detail/${hotel.id}`)"
        >
          <div class="h-48 bg-gradient-to-br from-blue-200 to-indigo-200 flex items-center justify-center">
            <img v-if="hotel.coverImage" :src="hotel.coverImage" :alt="hotel.name" class="w-full h-full object-cover" />
            <svg v-else class="w-20 h-20 text-blue-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
            </svg>
          </div>
          <div class="p-4">
            <div class="flex items-center justify-between mb-2">
              <h3 class="font-semibold text-gray-800 text-lg">{{ hotel.name }}</h3>
              <div class="flex items-center">
                <span class="text-yellow-500 font-semibold">{{ hotel.rating }}</span>
                <span class="text-gray-400 text-sm ml-1">{{ hotel.star_level }}星</span>
              </div>
            </div>
            <p class="text-sm text-gray-500 mb-2">{{ hotel.city }} · {{ hotel.address }}</p>
            <p class="text-sm text-gray-600 line-clamp-2 mb-3">{{ hotel.description }}</p>
            <div class="flex items-center justify-between">
              <span class="text-blue-600 font-medium text-xl">¥{{ hotel.pricePerNight }}/晚</span>
              <div class="flex gap-1">
                <span
                  v-for="tag in (hotel.tags || [])"
                  :key="tag"
                  class="text-xs px-2 py-1 bg-gray-100 text-gray-600 rounded"
                >
                  {{ tag }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div v-if="hotels.length === 0" class="text-center py-16">
        <svg class="w-16 h-16 text-gray-300 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
        <p class="text-gray-500">暂无酒店数据</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { hotelApi } from '@/api'

const route = useRoute()

const hotels = ref([])
const keyword = ref('')
const city = ref('')

onMounted(() => {
  const queryCity = route.query.city
  if (queryCity) {
    city.value = queryCity
  }
  handleSearch()
})

watch([keyword, city], () => {
  handleSearch()
})

async function handleSearch() {
  try {
    hotels.value = await hotelApi.search(city.value || '北京', keyword.value)
  } catch (e) {
    console.error('搜索酒店失败', e)
  }
}
</script>
