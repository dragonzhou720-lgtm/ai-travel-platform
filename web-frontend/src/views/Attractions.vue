<template>
  <div class="min-h-screen">
    <div class="bg-white shadow-sm mb-6">
      <div class="container mx-auto px-4 py-4">
        <div class="flex flex-col md:flex-row gap-4">
          <div class="flex-1">
            <input
              v-model="keyword"
              type="text"
              placeholder="搜索景点名称..."
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
          v-for="attraction in attractions"
          :key="attraction.id"
          class="bg-white rounded-xl overflow-hidden shadow-md hover:shadow-xl transition-shadow cursor-pointer"
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
              <h3 class="font-semibold text-gray-800 text-lg">{{ attraction.name }}</h3>
              <span class="text-yellow-500 font-semibold">{{ attraction.rating }}</span>
            </div>
            <p class="text-sm text-gray-500 mb-2">{{ attraction.city }} · {{ attraction.address }}</p>
            <p class="text-sm text-gray-600 line-clamp-2 mb-3">{{ attraction.description }}</p>
            <div class="flex items-center justify-between">
              <span class="text-green-600 font-medium text-xl">¥{{ attraction.ticketPrice }}</span>
              <div class="flex gap-1">
                <span
                  v-for="tag in (attraction.tags || [])"
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

      <div v-if="attractions.length === 0" class="text-center py-16">
        <svg class="w-16 h-16 text-gray-300 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
        <p class="text-gray-500">暂无景点数据</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { attractionApi } from '@/api'

const route = useRoute()
const router = useRouter()

const attractions = ref([])
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
    attractions.value = await attractionApi.search(city.value || '北京', keyword.value)
  } catch (e) {
    console.error('搜索景点失败', e)
  }
}
</script>
