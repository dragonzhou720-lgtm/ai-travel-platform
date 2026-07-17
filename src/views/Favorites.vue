<template>
  <div class="favorites-container">
    <NavBar />
    <div class="favorites-content">
      <div class="page-header">
        <h1>我的收藏</h1>
        <p>管理您收藏的路线、景点和酒店</p>
      </div>
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="收藏路线" name="route">
          <div v-if="routeFavorites.length > 0" class="favorites-list">
            <div
              v-for="favorite in routeFavorites"
              :key="favorite.id"
              class="favorite-card"
            >
              <div class="favorite-header">
                <span class="favorite-type">路线</span>
                <el-button
                  type="danger"
                  text
                  @click="handleRemove(favorite)"
                >
                  删除
                </el-button>
              </div>
              <div class="favorite-info" @click="goToRoute(favorite.targetId)">
                <h3>{{ getRouteName(favorite.targetId) }}</h3>
                <p>{{ formatDate(favorite.createdAt) }}收藏</p>
              </div>
            </div>
          </div>
          <div v-else class="empty-state">
            <div class="empty-icon">🗺️</div>
            <p>暂无收藏的路线</p>
            <router-link to="/plan" class="el-button el-button--primary">去规划路线</router-link>
          </div>
        </el-tab-pane>
        <el-tab-pane label="收藏景点" name="attraction">
          <div v-if="attractionFavorites.length > 0" class="favorites-list">
            <div
              v-for="favorite in attractionFavorites"
              :key="favorite.id"
              class="favorite-card"
            >
              <div class="favorite-header">
                <span class="favorite-type">景点</span>
                <el-button
                  type="danger"
                  text
                  @click="handleRemove(favorite)"
                >
                  删除
                </el-button>
              </div>
              <div class="favorite-info">
                <h3>{{ getAttractionName(favorite.targetId) }}</h3>
                <p>{{ formatDate(favorite.createdAt) }}收藏</p>
              </div>
            </div>
          </div>
          <div v-else class="empty-state">
            <div class="empty-icon">🏛️</div>
            <p>暂无收藏的景点</p>
            <router-link to="/" class="el-button el-button--primary">去发现景点</router-link>
          </div>
        </el-tab-pane>
        <el-tab-pane label="收藏酒店" name="hotel">
          <div v-if="hotelFavorites.length > 0" class="favorites-list">
            <div
              v-for="favorite in hotelFavorites"
              :key="favorite.id"
              class="favorite-card"
            >
              <div class="favorite-header">
                <span class="favorite-type">酒店</span>
                <el-button
                  type="danger"
                  text
                  @click="handleRemove(favorite)"
                >
                  删除
                </el-button>
              </div>
              <div class="favorite-info">
                <h3>{{ getHotelName(favorite.targetId) }}</h3>
                <p>{{ formatDate(favorite.createdAt) }}收藏</p>
              </div>
            </div>
          </div>
          <div v-else class="empty-state">
            <div class="empty-icon">🏨</div>
            <p>暂无收藏的酒店</p>
            <router-link to="/" class="el-button el-button--primary">去发现酒店</router-link>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import NavBar from '@/components/NavBar.vue'
import { getFavorites, removeFavorite } from '@/api/favorite'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const activeTab = ref('route')
const favorites = ref([])
const userStore = useUserStore()

const routeFavorites = computed(() => favorites.value.filter(f => f.targetType === 'route'))
const attractionFavorites = computed(() => favorites.value.filter(f => f.targetType === 'attraction'))
const hotelFavorites = computed(() => favorites.value.filter(f => f.targetType === 'hotel'))

const routeNames = {
  1: '北京3日游',
  2: '上海5日游',
  3: '杭州2日游'
}

const attractionNames = {
  1: '故宫博物院',
  2: '八达岭长城',
  3: '颐和园'
}

const hotelNames = {
  1: '北京王府井大饭店',
  2: '北京国贸大酒店',
  3: '北京四季酒店'
}

onMounted(async () => {
  await loadFavorites()
})

async function loadFavorites() {
  try {
    const userId = userStore.userId || 1
    favorites.value = await getFavorites(userId)
  } catch (error) {
    console.error('加载收藏失败', error)
    const userId = userStore.userId || 1
    favorites.value = [
      { id: 1, userId, targetId: 1, targetType: 'route', createdAt: Date.now() - 86400000 },
      { id: 2, userId, targetId: 2, targetType: 'route', createdAt: Date.now() - 172800000 },
      { id: 3, userId, targetId: 1, targetType: 'attraction', createdAt: Date.now() - 259200000 },
      { id: 4, userId, targetId: 2, targetType: 'attraction', createdAt: Date.now() - 345600000 },
      { id: 5, userId, targetId: 1, targetType: 'hotel', createdAt: Date.now() - 432000000 }
    ]
  }
}

async function handleRemove(favorite) {
  try {
    await removeFavorite(favorite.userId, favorite.targetId, favorite.targetType)
    const index = favorites.value.findIndex(f => f.id === favorite.id)
    if (index > -1) {
      favorites.value.splice(index, 1)
    }
    ElMessage.success('删除成功')
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

function goToRoute(id) {
  router.push(`/route/${id}`)
}

function getRouteName(id) {
  return routeNames[id] || `路线${id}`
}

function getAttractionName(id) {
  return attractionNames[id] || `景点${id}`
}

function getHotelName(id) {
  return hotelNames[id] || `酒店${id}`
}

function formatDate(timestamp) {
  if (!timestamp) return ''
  const date = new Date(timestamp)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}
</script>

<style scoped>
.favorites-container {
  min-height: 100vh;
  background: #f5f7fa;
}

.favorites-content {
  padding: 80px 20px;
  max-width: 800px;
  margin: 0 auto;
}

.page-header {
  text-align: center;
  margin-bottom: 40px;
}

.page-header h1 {
  font-size: 32px;
  color: #303133;
  margin-bottom: 10px;
}

.page-header p {
  font-size: 14px;
  color: #909399;
}

.favorites-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.favorite-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.favorite-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.favorite-type {
  font-size: 12px;
  color: #fff;
  background: #667eea;
  padding: 4px 12px;
  border-radius: 12px;
}

.favorite-info {
  cursor: pointer;
}

.favorite-info:hover h3 {
  color: #667eea;
}

.favorite-info h3 {
  font-size: 18px;
  color: #303133;
  margin-bottom: 8px;
  transition: color 0.3s;
}

.favorite-info p {
  font-size: 14px;
  color: #909399;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 20px;
}

.empty-state p {
  font-size: 16px;
  color: #909399;
  margin-bottom: 20px;
}
</style>
