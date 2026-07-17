<template>
  <div class="route-detail-container">
    <NavBar />
    <div class="route-detail-content" v-if="route">
      <div class="route-header">
        <div class="route-title">
          <h1>{{ route.city }} {{ route.days }}日游</h1>
          <div class="route-tags">
            <span class="tag style">{{ getStyleLabel(route.style) }}</span>
            <span class="tag date">{{ formatDate(route.createdAt) }}</span>
          </div>
        </div>
        <div class="route-actions">
          <el-button
            :icon="isFavorite ? StarFilled : Star"
            :type="isFavorite ? 'primary' : 'default'"
            @click="toggleFavorite"
          >
            {{ isFavorite ? '已收藏' : '收藏' }}
          </el-button>
          <el-button icon="Share" @click="handleShare">分享</el-button>
          <router-link to="/plan" class="el-button">重新规划</router-link>
        </div>
      </div>
      <div class="route-info">
        <div class="info-card">
          <div class="info-icon">📍</div>
          <div class="info-content">
            <span class="info-label">目的地</span>
            <span class="info-value">{{ route.city }}</span>
          </div>
        </div>
        <div class="info-card">
          <div class="info-icon">📅</div>
          <div class="info-content">
            <span class="info-label">行程天数</span>
            <span class="info-value">{{ route.days }}天</span>
          </div>
        </div>
        <div class="info-card">
          <div class="info-icon">🎯</div>
          <div class="info-content">
            <span class="info-label">旅游风格</span>
            <span class="info-value">{{ getStyleLabel(route.style) }}</span>
          </div>
        </div>
      </div>
      <section class="itinerary-section">
        <h2>每日行程安排</h2>
        <div class="itinerary-list">
          <div
            v-for="(day, index) in route.itinerary"
            :key="index"
            class="day-card"
          >
            <div class="day-header">
              <div class="day-num">第{{ index + 1 }}天</div>
              <div class="day-date">{{ getDayDate(index) }}</div>
            </div>
            <div class="day-content">
              <div v-for="(activity, actIndex) in day.activities || [day]" :key="actIndex" class="activity-item">
                <div class="activity-time">{{ activity.time || '全天' }}</div>
                <div class="activity-info">
                  <h4>{{ activity.activity || activity.title }}</h4>
                  <p>{{ activity.description || activity.desc }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
      <section class="attractions-section">
        <h2>推荐景点</h2>
        <div class="attractions-list">
          <div
            v-for="attraction in route.attractions"
            :key="attraction.id || attraction.name"
            class="attraction-card"
          >
            <div class="attraction-image" :style="{ backgroundImage: `url(${attraction.image})` }"></div>
            <div class="attraction-info">
              <h3>{{ attraction.name }}</h3>
              <p>{{ attraction.description }}</p>
              <div class="attraction-tags">
                <span v-for="tag in (attraction.tags || [])" :key="tag" class="tag">{{ tag }}</span>
              </div>
            </div>
          </div>
        </div>
      </section>
      <section class="hotels-section">
        <h2>推荐酒店</h2>
        <div class="hotels-list">
          <div
            v-for="hotel in route.hotels"
            :key="hotel.id || hotel.name"
            class="hotel-card"
          >
            <div class="hotel-image" :style="{ backgroundImage: `url(${hotel.image})` }"></div>
            <div class="hotel-info">
              <h3>{{ hotel.name }}</h3>
              <p>{{ hotel.description }}</p>
              <div class="hotel-price">
                <span class="price">¥{{ hotel.price }}</span>
                <span class="price-unit">/晚起</span>
              </div>
            </div>
          </div>
        </div>
      </section>
      <section class="ai-suggestions-section">
        <h2>AI规划建议</h2>
        <div class="suggestions-card">
          <div class="suggestion-icon">💡</div>
          <div class="suggestion-content">
            <h3>行程小贴士</h3>
            <ul>
              <li v-for="(tip, index) in suggestions" :key="index">{{ tip }}</li>
            </ul>
          </div>
        </div>
      </section>
    </div>
    <div v-else class="loading-container">
      <div class="loading-spinner"></div>
      <p>加载中...</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Star, StarFilled } from '@element-plus/icons-vue'
import NavBar from '@/components/NavBar.vue'
import { getRouteById } from '@/api/route'
import { addFavorite, removeFavorite, checkFavorite } from '@/api/favorite'
import { useUserStore } from '@/stores/user'

const router = useRoute()
const route = ref(null)
const isFavorite = ref(false)
const userStore = useUserStore()

const styleOptions = [
  { value: 'sightseeing', label: '观光游览' },
  { value: 'leisure', label: '休闲度假' },
  { value: 'food', label: '美食探索' },
  { value: 'adventure', label: '探险体验' },
  { value: 'culture', label: '文化历史' }
]

const suggestions = [
  '建议提前预订热门景点门票，避免排队等待',
  '出行前查看目的地天气，做好准备',
  '合理安排行程时间，避免过于紧凑',
  '注意保管好个人财物，确保旅行安全',
  '尊重当地文化习俗，文明旅游'
]

onMounted(async () => {
  await loadRoute()
})

async function loadRoute() {
  try {
    route.value = await getRouteById(router.params.id)
    await checkRouteFavorite()
  } catch (error) {
    ElMessage.error('加载路线失败')
  }
}

async function checkRouteFavorite() {
  try {
    const userId = userStore.userId || 1
    const response = await checkFavorite(userId, router.params.id, 'route')
    isFavorite.value = response.isFavorite
  } catch (error) {
    console.error('检查收藏失败', error)
  }
}

async function toggleFavorite() {
  try {
    const userId = userStore.userId || 1
    if (isFavorite.value) {
      await removeFavorite(userId, router.params.id, 'route')
      isFavorite.value = false
      ElMessage.success('已取消收藏')
    } else {
      await addFavorite({ userId, targetId: Number(router.params.id), targetType: 'route' })
      isFavorite.value = true
      ElMessage.success('收藏成功')
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

function handleShare() {
  ElMessage.info('分享功能开发中')
}

function getStyleLabel(style) {
  const option = styleOptions.find(s => s.value === style)
  return option ? option.label : style
}

function formatDate(timestamp) {
  if (!timestamp) return ''
  const date = new Date(timestamp)
  return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日`
}

function getDayDate(index) {
  const date = new Date()
  date.setDate(date.getDate() + index)
  const weekDays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
  return `${date.getMonth() + 1}月${date.getDate()}日 ${weekDays[date.getDay()]}`
}
</script>

<style scoped>
.route-detail-container {
  min-height: 100vh;
  background: #f5f7fa;
}

.route-detail-content {
  padding: 80px 20px;
  max-width: 1000px;
  margin: 0 auto;
}

.route-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 30px;
}

.route-title h1 {
  font-size: 32px;
  color: #303133;
  margin-bottom: 10px;
}

.route-tags {
  display: flex;
  gap: 10px;
}

.tag {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
}

.tag.style {
  background: #667eea;
  color: #fff;
}

.tag.date {
  background: #f0f0f0;
  color: #909399;
}

.route-actions {
  display: flex;
  gap: 10px;
}

.route-info {
  display: flex;
  gap: 20px;
  margin-bottom: 40px;
}

.info-card {
  flex: 1;
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.info-icon {
  font-size: 28px;
}

.info-content {
  display: flex;
  flex-direction: column;
}

.info-label {
  font-size: 12px;
  color: #909399;
}

.info-value {
  font-size: 18px;
  color: #303133;
  font-weight: 600;
}

section {
  background: #fff;
  border-radius: 12px;
  padding: 30px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

section h2 {
  font-size: 24px;
  color: #303133;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #667eea;
}

.itinerary-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.day-card {
  border: 1px solid #f0f0f0;
  border-radius: 12px;
  overflow: hidden;
}

.day-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 15px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.day-num {
  font-size: 20px;
  color: #fff;
  font-weight: 600;
}

.day-date {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
}

.day-content {
  padding: 20px;
}

.activity-item {
  display: flex;
  gap: 15px;
  padding: 15px 0;
  border-bottom: 1px dashed #f0f0f0;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-time {
  background: #f5f7fa;
  color: #667eea;
  padding: 6px 12px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  min-width: 80px;
  text-align: center;
}

.activity-info h4 {
  font-size: 16px;
  color: #303133;
  margin-bottom: 8px;
}

.activity-info p {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
}

.attractions-list, .hotels-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
}

.attraction-card, .hotel-card {
  display: flex;
  flex-direction: column;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.attraction-image, .hotel-image {
  height: 180px;
  background-size: cover;
  background-position: center;
}

.attraction-info, .hotel-info {
  padding: 20px;
}

.attraction-info h3, .hotel-info h3 {
  font-size: 18px;
  color: #303133;
  margin-bottom: 10px;
}

.attraction-info p, .hotel-info p {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
  margin-bottom: 12px;
}

.attraction-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.hotel-price {
  display: flex;
  align-items: baseline;
}

.price {
  font-size: 22px;
  color: #f56c6c;
  font-weight: 600;
}

.price-unit {
  font-size: 12px;
  color: #909399;
  margin-left: 4px;
}

.suggestions-card {
  display: flex;
  gap: 20px;
}

.suggestion-icon {
  font-size: 48px;
}

.suggestion-content h3 {
  font-size: 18px;
  color: #303133;
  margin-bottom: 15px;
}

.suggestion-content ul {
  list-style: none;
  padding: 0;
}

.suggestion-content li {
  font-size: 14px;
  color: #606266;
  padding: 8px 0;
  padding-left: 20px;
  position: relative;
}

.suggestion-content li::before {
  content: '✓';
  position: absolute;
  left: 0;
  color: #667eea;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 50vh;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading-container p {
  margin-top: 20px;
  color: #909399;
}
</style>
