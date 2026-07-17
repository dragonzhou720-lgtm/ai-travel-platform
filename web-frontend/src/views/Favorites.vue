<template>
  <div class="min-h-screen">
    <div class="bg-white shadow-sm mb-6">
      <div class="container mx-auto px-4 py-4">
        <h1 class="text-2xl font-bold text-gray-800">我的收藏</h1>
        <div class="flex gap-4 mt-4">
          <button
            @click="activeTab = 'all'"
            :class="[
              'px-4 py-2 rounded-lg transition-colors',
              activeTab === 'all' ? 'bg-blue-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
            ]"
          >
            全部
          </button>
          <button
            @click="activeTab = 'attraction'"
            :class="[
              'px-4 py-2 rounded-lg transition-colors',
              activeTab === 'attraction' ? 'bg-blue-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
            ]"
          >
            景点
          </button>
          <button
            @click="activeTab = 'hotel'"
            :class="[
              'px-4 py-2 rounded-lg transition-colors',
              activeTab === 'hotel' ? 'bg-blue-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
            ]"
          >
            酒店
          </button>
          <button
            @click="activeTab = 'route'"
            :class="[
              'px-4 py-2 rounded-lg transition-colors',
              activeTab === 'route' ? 'bg-blue-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
            ]"
          >
            路线
          </button>
        </div>
      </div>
    </div>

    <div class="container mx-auto px-4">
      <div v-if="loading" class="flex items-center justify-center py-20">
        <svg class="animate-spin h-10 w-10 text-blue-600" fill="none" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
        </svg>
      </div>

      <div v-else-if="filteredFavorites.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div
          v-for="favorite in filteredFavorites"
          :key="favorite.id"
          class="bg-white rounded-xl overflow-hidden shadow-md hover:shadow-xl transition-shadow"
        >
          <div class="h-40">
            <img
              v-if="favorite.coverImage"
              :src="favorite.coverImage"
              :alt="favorite.targetName"
              class="w-full h-full object-cover"
            />
            <div v-else class="h-full flex items-center justify-center" :class="getBgColor(favorite.targetType)">
              <svg v-if="favorite.targetType === 'attraction'" class="w-16 h-16 text-green-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
              <svg v-else-if="favorite.targetType === 'hotel'" class="w-16 h-16 text-blue-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
              </svg>
              <svg v-else class="w-16 h-16 text-purple-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3.055 11H5a2 2 0 012 2v1a2 2 0 002 2 2 2 0 012 2v2.945M8 3.935V5.5A2.5 2.5 0 0010.5 8h.5a2 2 0 012 2 2 2 0 104 0 2 2 0 012-2h1.064M15 20.488V18a2 2 0 012-2h3.064M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
          </div>
          <div class="p-4">
            <div class="flex items-center justify-between">
              <h3 class="font-semibold text-gray-800">{{ favorite.targetName }}</h3>
              <span class="text-xs px-2 py-1 rounded" :class="getTagColor(favorite.targetType)">
                {{ getTypeName(favorite.targetType) }}
              </span>
            </div>
            <p class="text-sm text-gray-500 mt-1">收藏于 {{ formatDate(favorite.createdAt) }}</p>
            <div class="flex gap-2 mt-4">
              <button
                @click="handleView(favorite)"
                class="flex-1 px-4 py-2 bg-gray-100 text-gray-600 rounded-lg hover:bg-gray-200 transition-colors text-sm"
              >
                查看详情
              </button>
              <button
                @click="handleRemove(favorite)"
                class="px-4 py-2 bg-red-100 text-red-600 rounded-lg hover:bg-red-200 transition-colors"
              >
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>
          </div>
        </div>
      </div>

      <div v-else class="text-center py-16">
        <svg class="w-16 h-16 text-gray-300 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
        </svg>
        <p class="text-gray-500">暂无收藏数据</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { favoriteApi, attractionApi, hotelApi } from '@/api'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(true)
const favorites = ref([])
const activeTab = ref('all')

const filteredFavorites = computed(() => {
  if (activeTab.value === 'all') return favorites.value
  return favorites.value.filter(f => f.targetType === activeTab.value)
})

onMounted(async () => {
  if (!userStore.isLoggedIn || !userStore.userInfo) {
    router.push('/login')
    return
  }

  try {
    favorites.value = await favoriteApi.getFavorites(userStore.userInfo.id)
    
    for (const favorite of favorites.value) {
      try {
        if (favorite.targetType === 'attraction') {
          const detail = await attractionApi.getDetail(favorite.targetId)
          favorite.coverImage = detail.coverImage
        } else if (favorite.targetType === 'hotel') {
          const detail = await hotelApi.getDetail(favorite.targetId)
          favorite.coverImage = detail.coverImage
        }
      } catch (e) {
        console.error('获取收藏详情失败', e)
      }
    }
  } catch (e) {
    console.error('获取收藏失败', e)
  } finally {
    loading.value = false
  }
})

function getBgColor(type) {
  const colors = {
    attraction: 'bg-gradient-to-br from-green-200 to-emerald-200',
    hotel: 'bg-gradient-to-br from-blue-200 to-indigo-200',
    route: 'bg-gradient-to-br from-purple-200 to-pink-200'
  }
  return colors[type] || 'bg-gray-200'
}

function getTagColor(type) {
  const colors = {
    attraction: 'bg-green-100 text-green-600',
    hotel: 'bg-blue-100 text-blue-600',
    route: 'bg-purple-100 text-purple-600'
  }
  return colors[type] || 'bg-gray-100 text-gray-600'
}

function getTypeName(type) {
  const names = {
    attraction: '景点',
    hotel: '酒店',
    route: '路线'
  }
  return names[type] || type
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

function handleView(favorite) {
  const paths = {
    attraction: `/attractions/detail/${favorite.targetId}`,
    hotel: `/hotels/detail/${favorite.targetId}`,
    route: `/route/detail/${favorite.targetId}`
  }
  router.push(paths[favorite.targetType] || '/')
}

async function handleRemove(favorite) {
  try {
    await favoriteApi.remove(favorite.userId, favorite.targetId, favorite.targetType)
    favorites.value = favorites.value.filter(f => f.id !== favorite.id)
  } catch (e) {
    console.error('删除收藏失败', e)
  }
}
</script>
