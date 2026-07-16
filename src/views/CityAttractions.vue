<template>
  <div class="city-attractions">
    <NavBar />
    <div class="page-content">
      <div class="page-header">
        <div class="header-bg" :style="{ backgroundImage: `url(${cityImage})` }">
          <div class="header-overlay"></div>
          <div class="header-content">
            <div class="back-btn" @click="goBack">
              <i class="el-icon-arrow-left"></i>
            </div>
            <h1>{{ currentCity }}热门景点</h1>
            <p>探索{{ currentCity }}的精彩景点</p>
          </div>
        </div>
      </div>
      <div class="filter-section">
        <div class="city-tabs">
          <button
            v-for="city in cities"
            :key="city"
            :class="['city-tab', { active: currentCity === city }]"
            @click="switchCity(city)"
          >
            {{ city }}
          </button>
        </div>
        <div class="search-box">
          <input
            type="text"
            v-model="keyword"
            placeholder="搜索景点..."
            class="search-input"
            @keyup.enter="search"
          />
          <button class="search-btn" @click="search">
            <i class="el-icon-search"></i>
          </button>
        </div>
      </div>
      <div class="attractions-grid">
        <div
          v-for="attraction in attractions"
          :key="attraction.id"
          class="attraction-card"
          @click="goToDetail(attraction.id)"
        >
          <div class="card-image" :style="{ backgroundImage: `url(${attraction.image})` }">
            <div class="rating-badge">
              <i class="el-icon-star"></i> {{ attraction.rating }}
            </div>
          </div>
          <div class="card-content">
            <h3 class="card-title">{{ attraction.name }}</h3>
            <p class="card-desc">{{ attraction.description }}</p>
            <div class="card-tags">
              <span v-for="tag in attraction.tags" :key="tag" class="tag">{{ tag }}</span>
            </div>
            <div class="card-footer">
              <span class="ticket-price">{{ attraction.ticket === 0 ? '免费' : `¥${attraction.ticket}` }}</span>
              <span class="view-count">查看详情</span>
            </div>
          </div>
        </div>
      </div>
      <div v-if="attractions.length === 0" class="empty-state">
        <i class="el-icon-location-outline"></i>
        <p>暂无景点数据</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import NavBar from '@/components/NavBar.vue'
import { searchAttractions } from '@/api/attraction'

const route = useRoute()
const router = useRouter()

const cities = ['北京', '上海', '杭州', '成都', '西安', '三亚']

const currentCity = ref('北京')
const keyword = ref('')
const attractions = ref([])

const cityImages = {
  '北京': 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Beijing%20city%20skyline%20Forbidden%20City%20panorama&image_size=landscape_16_9',
  '上海': 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Shanghai%20skyline%20Bund%20night%20view&image_size=landscape_16_9',
  '杭州': 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Hangzhou%20West%20Lake%20scenic%20landscape&image_size=landscape_16_9',
  '成都': 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Chengdu%20city%20panda%20traditional%20architecture&image_size=landscape_16_9',
  '西安': 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Xian%20ancient%20city%20wall%20Terracotta%20Army&image_size=landscape_16_9',
  '三亚': 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=Sanya%20beach%20tropical%20sea%20palm%20trees&image_size=landscape_16_9'
}

const cityImage = computed(() => cityImages[currentCity.value] || cityImages['北京'])

onMounted(async () => {
  if (route.query.city) {
    currentCity.value = route.query.city
  }
  await loadAttractions()
})

async function loadAttractions() {
  try {
    const data = await searchAttractions(currentCity.value, keyword.value)
    attractions.value = data.map(item => ({
      ...item,
      image: item.coverImage,
      ticket: item.ticketPrice,
      tags: item.tags ? item.tags.split(',') : []
    }))
  } catch (error) {
    console.error('获取景点列表失败:', error)
    attractions.value = []
  }
}

function switchCity(city) {
  currentCity.value = city
  keyword.value = ''
  loadAttractions()
}

function search() {
  loadAttractions()
}

function goToDetail(id) {
  router.push(`/attraction/${id}`)
}

function goBack() {
  router.back()
}
</script>

<style scoped>
.city-attractions {
  min-height: 100vh;
  background: #f5f7fa;
}

.page-content {
  padding-top: 60px;
}

.page-header {
  position: relative;
}

.header-bg {
  height: 250px;
  background-size: cover;
  background-position: center;
  position: relative;
}

.header-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.6));
}

.header-content {
  position: absolute;
  bottom: 30px;
  left: 30px;
  color: #fff;
}

.back-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  margin-bottom: 16px;
  cursor: pointer;
  font-size: 18px;
}

.back-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.header-content h1 {
  font-size: 32px;
  margin-bottom: 8px;
}

.header-content p {
  font-size: 14px;
  opacity: 0.8;
}

.filter-section {
  background: #fff;
  padding: 20px 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.city-tabs {
  display: flex;
  gap: 10px;
}

.city-tab {
  padding: 8px 20px;
  border: 1px solid #dcdfe6;
  border-radius: 20px;
  background: #fff;
  cursor: pointer;
  font-size: 14px;
  color: #606266;
  transition: all 0.3s;
}

.city-tab.active {
  background: #667eea;
  color: #fff;
  border-color: #667eea;
}

.search-box {
  display: flex;
  gap: 0;
}

.search-input {
  padding: 10px 16px;
  border: 1px solid #dcdfe6;
  border-radius: 20px 0 0 20px;
  font-size: 14px;
  width: 200px;
}

.search-btn {
  padding: 10px 20px;
  background: #667eea;
  color: #fff;
  border: none;
  border-radius: 0 20px 20px 0;
  cursor: pointer;
}

.attractions-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 20px;
  padding: 30px;
  max-width: 1200px;
  margin: 0 auto;
}

.attraction-card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  cursor: pointer;
  transition: transform 0.3s;
}

.attraction-card:hover {
  transform: translateY(-5px);
}

.card-image {
  height: 200px;
  background-size: cover;
  background-position: center;
  position: relative;
}

.rating-badge {
  position: absolute;
  top: 12px;
  right: 12px;
  background: rgba(0, 0, 0, 0.6);
  color: #fff;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.rating-badge i {
  color: #f5a623;
}

.card-content {
  padding: 20px;
}

.card-title {
  font-size: 18px;
  color: #303133;
  margin-bottom: 8px;
}

.card-desc {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 12px;
}

.tag {
  font-size: 12px;
  color: #667eea;
  background: #f0f5ff;
  padding: 4px 10px;
  border-radius: 8px;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.ticket-price {
  font-size: 16px;
  color: #f56c6c;
  font-weight: 600;
}

.view-count {
  font-size: 13px;
  color: #909399;
}

.empty-state {
  text-align: center;
  padding: 60px;
  color: #909399;
}

.empty-state i {
  font-size: 48px;
  margin-bottom: 16px;
}

.empty-state p {
  font-size: 14px;
}
</style>
