<template>
  <div class="plan-container">
    <NavBar />
    <div class="plan-content">
      <div class="plan-header">
        <h1>AI智能旅游规划</h1>
        <p>输入您的旅行需求，让AI为您生成专属旅行方案</p>
      </div>
      <div class="plan-form-wrapper">
        <el-form ref="formRef" :model="form" :rules="rules" class="plan-form">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="目的地城市" prop="city">
                <el-select v-model="form.city" placeholder="请选择目的地城市" size="large">
                  <el-option v-for="city in cities" :key="city" :label="city" :value="city" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="出行天数" prop="days">
                <el-select v-model="form.days" placeholder="请选择出行天数" size="large">
                  <el-option v-for="day in daysOptions" :key="day" :label="`${day}天`" :value="day" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="预算范围" prop="budget">
                <el-select v-model="form.budget" placeholder="请选择预算范围" size="large">
                  <el-option v-for="budget in budgetOptions" :key="budget.value" :label="budget.label" :value="budget.value" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="旅游偏好" prop="style">
                <el-select v-model="form.style" placeholder="请选择旅游偏好" size="large">
                  <el-option v-for="style in styleOptions" :key="style.value" :label="style.label" :value="style.value" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="可选景点（选填）">
            <div class="attractions-select">
              <el-button type="primary" text @click="showAttractionsDialog = true">添加景点</el-button>
              <div v-if="selectedAttractions.length > 0" class="selected-tags">
                <el-tag
                  v-for="attraction in selectedAttractions"
                  :key="attraction.id"
                  closable
                  @close="removeAttraction(attraction.id)"
                >
                  {{ attraction.name }}
                </el-tag>
              </div>
              <p v-else class="empty-hint">暂无选择的景点</p>
            </div>
          </el-form-item>
          <el-form-item label="可选酒店（选填）">
            <div class="hotels-select">
              <el-button type="primary" text @click="showHotelsDialog = true">添加酒店</el-button>
              <div v-if="selectedHotels.length > 0" class="selected-tags">
                <el-tag
                  v-for="hotel in selectedHotels"
                  :key="hotel.id"
                  closable
                  @close="removeHotel(hotel.id)"
                >
                  {{ hotel.name }}
                </el-tag>
              </div>
              <p v-else class="empty-hint">暂无选择的酒店</p>
            </div>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              size="large"
              class="generate-btn"
              :loading="loading"
              @click="handleGenerate"
            >
              <span v-if="!loading">生成旅行路线</span>
              <span v-else>AI正在规划中...</span>
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <div v-if="recommendRoutes.length > 0" class="recommend-section">
        <h2>推荐路线</h2>
        <div class="recommend-list">
          <div
            v-for="route in recommendRoutes"
            :key="route.id"
            class="recommend-card"
            @click="goToRoute(route.id)"
          >
            <div class="route-header">
              <span class="route-city">{{ route.city }}</span>
              <span class="route-days">{{ route.days }}天</span>
              <span class="route-style">{{ getStyleLabel(route.style) }}</span>
            </div>
            <div class="route-preview">
              <div v-for="(day, index) in route.itinerary?.slice(0, 3)" :key="index" class="day-item">
                <span class="day-num">第{{ index + 1 }}天</span>
                <span class="day-desc">{{ day.activity }}</span>
              </div>
            </div>
            <div class="route-action">查看详情 →</div>
          </div>
        </div>
      </div>
    </div>
    <el-dialog title="选择景点" v-model="showAttractionsDialog" width="800px">
      <el-input v-model="attractionKeyword" placeholder="搜索景点" class="search-input" @input="searchAttractionsList" />
      <div class="dialog-list">
        <div
          v-for="attraction in attractionsList"
          :key="attraction.id"
          class="dialog-item"
          :class="{ selected: isAttractionSelected(attraction.id) }"
          @click="toggleAttraction(attraction)"
        >
          <div class="item-image" :style="{ backgroundImage: `url(${attraction.image})` }"></div>
          <div class="item-info">
            <h4>{{ attraction.name }}</h4>
            <p>{{ attraction.description }}</p>
          </div>
          <el-checkbox :checked="isAttractionSelected(attraction.id)" />
        </div>
      </div>
    </el-dialog>
    <el-dialog title="选择酒店" v-model="showHotelsDialog" width="800px">
      <el-input v-model="hotelKeyword" placeholder="搜索酒店" class="search-input" @input="searchHotelsList" />
      <div class="dialog-list">
        <div
          v-for="hotel in hotelsList"
          :key="hotel.id"
          class="dialog-item"
          :class="{ selected: isHotelSelected(hotel.id) }"
          @click="toggleHotel(hotel)"
        >
          <div class="item-image" :style="{ backgroundImage: `url(${hotel.image})` }"></div>
          <div class="item-info">
            <h4>{{ hotel.name }}</h4>
            <p>{{ hotel.description }}</p>
            <span class="price">¥{{ hotel.price }}/晚起</span>
          </div>
          <el-checkbox :checked="isHotelSelected(hotel.id)" />
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import NavBar from '@/components/NavBar.vue'
import { generateRoute, getHotRoutes } from '@/api/route'
import { searchAttractions } from '@/api/attraction'
import { searchHotels } from '@/api/hotel'

const router = useRouter()
const route = useRoute()

const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  city: '',
  days: '',
  budget: '',
  style: ''
})

const rules = {
  city: [{ required: true, message: '请选择目的地城市', trigger: 'change' }],
  days: [{ required: true, message: '请选择出行天数', trigger: 'change' }],
  budget: [{ required: false }],
  style: [{ required: true, message: '请选择旅游偏好', trigger: 'change' }]
}

const cities = ['北京', '上海', '杭州', '成都', '西安', '三亚', '广州', '深圳', '重庆', '南京']
const daysOptions = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
const budgetOptions = [
  { value: 'low', label: '经济型（1000元以下/天）' },
  { value: 'medium', label: '舒适型（1000-3000元/天）' },
  { value: 'high', label: '豪华型（3000元以上/天）' }
]
const styleOptions = [
  { value: 'sightseeing', label: '观光游览' },
  { value: 'leisure', label: '休闲度假' },
  { value: 'food', label: '美食探索' },
  { value: 'adventure', label: '探险体验' },
  { value: 'culture', label: '文化历史' }
]

const selectedAttractions = ref([])
const selectedHotels = ref([])
const showAttractionsDialog = ref(false)
const showHotelsDialog = ref(false)
const attractionsList = ref([])
const hotelsList = ref([])
const attractionKeyword = ref('')
const hotelKeyword = ref('')
const recommendRoutes = ref([])

onMounted(async () => {
  if (route.query.city) {
    form.city = route.query.city
  }
  try {
    const routes = await getHotRoutes(6)
    recommendRoutes.value = routes.map(r => ({
      ...r,
      city: r.destination,
      style: r.preference
    }))
  } catch (error) {
    console.error('获取推荐路线失败', error)
  }
})

async function handleGenerate() {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    try {
      const data = {
        city: form.city,
        days: form.days,
        style: form.style,
        attractions: selectedAttractions.value.map(a => a.name),
        hotels: selectedHotels.value.map(h => h.name)
      }
      const response = await generateRoute(data)
      router.push(`/route/${response.id}`)
    } catch (error) {
      ElMessage.error('生成路线失败，请重试')
    } finally {
      loading.value = false
    }
  })
}

async function searchAttractionsList() {
  if (!form.city) {
    ElMessage.warning('请先选择目的地城市')
    return
  }
  try {
    attractionsList.value = await searchAttractions(form.city, attractionKeyword.value)
  } catch (error) {
    attractionsList.value = []
  }
}

async function searchHotelsList() {
  if (!form.city) {
    ElMessage.warning('请先选择目的地城市')
    return
  }
  try {
    hotelsList.value = await searchHotels(form.city, hotelKeyword.value)
  } catch (error) {
    hotelsList.value = []
  }
}

function toggleAttraction(attraction) {
  const index = selectedAttractions.value.findIndex(a => a.id === attraction.id)
  if (index > -1) {
    selectedAttractions.value.splice(index, 1)
  } else {
    selectedAttractions.value.push(attraction)
  }
}

function toggleHotel(hotel) {
  const index = selectedHotels.value.findIndex(h => h.id === hotel.id)
  if (index > -1) {
    selectedHotels.value.splice(index, 1)
  } else {
    selectedHotels.value.push(hotel)
  }
}

function removeAttraction(id) {
  const index = selectedAttractions.value.findIndex(a => a.id === id)
  if (index > -1) {
    selectedAttractions.value.splice(index, 1)
  }
}

function removeHotel(id) {
  const index = selectedHotels.value.findIndex(h => h.id === id)
  if (index > -1) {
    selectedHotels.value.splice(index, 1)
  }
}

function isAttractionSelected(id) {
  return selectedAttractions.value.some(a => a.id === id)
}

function isHotelSelected(id) {
  return selectedHotels.value.some(h => h.id === id)
}

function goToRoute(id) {
  router.push(`/route/${id}`)
}

function getStyleLabel(style) {
  const option = styleOptions.find(s => s.value === style)
  return option ? option.label : style
}
</script>

<style scoped>
.plan-container {
  min-height: 100vh;
  background: #f5f7fa;
}

.plan-content {
  padding: 80px 20px;
  max-width: 800px;
  margin: 0 auto;
}

.plan-header {
  text-align: center;
  margin-bottom: 40px;
}

.plan-header h1 {
  font-size: 36px;
  color: #303133;
  margin-bottom: 10px;
}

.plan-header p {
  font-size: 14px;
  color: #909399;
}

.plan-form-wrapper {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  padding: 40px;
}

.generate-btn {
  width: 100%;
  height: 48px;
  font-size: 18px;
}

.selected-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 10px;
}

.empty-hint {
  color: #909399;
  font-size: 14px;
  margin-top: 10px;
}

.recommend-section {
  margin-top: 40px;
}

.recommend-section h2 {
  font-size: 24px;
  color: #303133;
  margin-bottom: 20px;
}

.recommend-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.recommend-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  cursor: pointer;
  transition: box-shadow 0.3s;
}

.recommend-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.route-header {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
}

.route-city {
  background: #667eea;
  color: #fff;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
}

.route-days {
  background: #e6a23c;
  color: #fff;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
}

.route-style {
  background: #409eff;
  color: #fff;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
}

.route-preview {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 15px;
}

.day-item {
  display: flex;
  gap: 10px;
  font-size: 14px;
}

.day-num {
  color: #667eea;
  font-weight: 600;
  width: 50px;
}

.day-desc {
  color: #606266;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.route-action {
  color: #667eea;
  font-size: 14px;
  text-align: right;
}

.search-input {
  margin-bottom: 20px;
}

.dialog-list {
  max-height: 400px;
  overflow-y: auto;
}

.dialog-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.3s;
}

.dialog-item:hover, .dialog-item.selected {
  background: #f5f7fa;
}

.item-image {
  width: 80px;
  height: 60px;
  background-size: cover;
  background-position: center;
  border-radius: 8px;
}

.item-info {
  flex: 1;
}

.item-info h4 {
  font-size: 14px;
  color: #303133;
  margin-bottom: 4px;
}

.item-info p {
  font-size: 12px;
  color: #909399;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.price {
  color: #f56c6c;
  font-size: 12px;
}
</style>
