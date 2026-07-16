<template>
  <div class="history-container">
    <NavBar />
    <div class="history-content">
      <div class="page-header">
        <h1>历史记录</h1>
        <p>查看您浏览过的旅游路线</p>
      </div>
      <div v-if="historyRoutes.length > 0" class="history-list">
        <div
          v-for="route in historyRoutes"
          :key="route.id"
          class="history-card"
        >
          <div class="card-header">
            <div class="route-info" @click="goToRoute(route.id)">
              <h3>{{ route.city }} {{ route.days }}日游</h3>
              <div class="route-tags">
                <span class="tag">{{ getStyleLabel(route.style) }}</span>
                <span class="tag date">{{ formatDate(route.createdAt) }}</span>
              </div>
            </div>
            <el-button
              type="danger"
              text
              @click="handleRemove(route)"
            >
              删除
            </el-button>
          </div>
          <div class="card-body">
            <div class="itinerary-preview">
              <div v-for="(day, index) in route.itinerary?.slice(0, 2)" :key="index" class="day-preview">
                <span class="day-num">第{{ index + 1 }}天</span>
                <span class="day-desc">{{ day.activity || day.title }}</span>
              </div>
              <div v-if="route.itinerary?.length > 2" class="more-days">
                还有{{ route.itinerary.length - 2 }}天行程...
              </div>
            </div>
          </div>
        </div>
      </div>
      <div v-else class="empty-state">
        <div class="empty-icon">📋</div>
        <p>暂无历史记录</p>
        <router-link to="/plan" class="el-button el-button--primary">去规划路线</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import NavBar from '@/components/NavBar.vue'
import { getAllRoutes } from '@/api/route'

const router = useRouter()
const historyRoutes = ref([])

const styleOptions = [
  { value: 'sightseeing', label: '观光游览' },
  { value: 'leisure', label: '休闲度假' },
  { value: 'food', label: '美食探索' },
  { value: 'adventure', label: '探险体验' },
  { value: 'culture', label: '文化历史' }
]

onMounted(async () => {
  await loadHistory()
})

async function loadHistory() {
  try {
    const routes = await getAllRoutes()
    historyRoutes.value = routes.slice(0, 10)
  } catch (error) {
    console.error('加载历史记录失败', error)
    historyRoutes.value = [
      {
        id: 1,
        city: '北京',
        days: 3,
        style: 'sightseeing',
        createdAt: Date.now() - 86400000,
        itinerary: [
          { activity: '上午：故宫博物院游览', description: '参观太和殿、乾清宫等著名景点' },
          { activity: '下午：天安门广场', description: '参观人民英雄纪念碑、毛主席纪念堂' },
          { activity: '晚上：王府井步行街', description: '购物、品尝美食' }
        ],
        attractions: [],
        hotels: []
      },
      {
        id: 2,
        city: '上海',
        days: 5,
        style: 'leisure',
        createdAt: Date.now() - 172800000,
        itinerary: [
          { activity: '上午：外滩观光', description: '欣赏上海标志性建筑' },
          { activity: '下午：陆家嘴金融中心', description: '参观东方明珠、金茂大厦' }
        ],
        attractions: [],
        hotels: []
      },
      {
        id: 3,
        city: '杭州',
        days: 2,
        style: 'culture',
        createdAt: Date.now() - 259200000,
        itinerary: [
          { activity: '上午：西湖游览', description: '断桥残雪、苏堤春晓' },
          { activity: '下午：灵隐寺', description: '千年古刹，祈福许愿' }
        ],
        attractions: [],
        hotels: []
      }
    ]
  }
}

function handleRemove(route) {
  const index = historyRoutes.value.findIndex(r => r.id === route.id)
  if (index > -1) {
    historyRoutes.value.splice(index, 1)
    ElMessage.success('删除成功')
  }
}

function goToRoute(id) {
  router.push(`/route/${id}`)
}

function getStyleLabel(style) {
  const option = styleOptions.find(s => s.value === style)
  return option ? option.label : style
}

function formatDate(timestamp) {
  if (!timestamp) return ''
  const date = new Date(timestamp)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}
</script>

<style scoped>
.history-container {
  min-height: 100vh;
  background: #f5f7fa;
}

.history-content {
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

.history-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.history-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
}

.route-info {
  flex: 1;
  cursor: pointer;
}

.route-info:hover h3 {
  color: #667eea;
}

.route-info h3 {
  font-size: 20px;
  color: #303133;
  margin-bottom: 10px;
  transition: color 0.3s;
}

.route-tags {
  display: flex;
  gap: 10px;
}

.tag {
  font-size: 12px;
  color: #fff;
  background: #667eea;
  padding: 4px 12px;
  border-radius: 12px;
}

.tag.date {
  background: #f0f0f0;
  color: #909399;
}

.card-body {
  border-top: 1px solid #f0f0f0;
  padding-top: 15px;
}

.itinerary-preview {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.day-preview {
  display: flex;
  gap: 10px;
  font-size: 14px;
}

.day-num {
  color: #667eea;
  font-weight: 600;
  min-width: 60px;
}

.day-desc {
  color: #606266;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.more-days {
  font-size: 12px;
  color: #909399;
  padding-left: 70px;
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
