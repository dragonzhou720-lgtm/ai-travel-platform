<template>
  <div class="attraction-detail">
    <NavBar />
    <div class="detail-content">
      <div class="detail-header">
        <div class="header-image" :style="{ backgroundImage: `url(${attraction.image})` }">
          <div class="header-overlay"></div>
          <div class="header-info">
            <div class="back-btn" @click="goBack">
              <i class="el-icon-arrow-left"></i> 返回
            </div>
            <h1>{{ attraction.name }}</h1>
            <div class="header-meta">
              <span class="city-tag">{{ attraction.city }}</span>
              <span class="rating">
                <i class="el-icon-star" style="color: #f5a623;"></i>
                {{ attraction.rating }}
              </span>
            </div>
          </div>
        </div>
      </div>
      <div class="detail-body">
        <div class="main-info">
          <div class="info-section">
            <h2 class="section-title">景点介绍</h2>
            <p class="description">{{ attraction.description }}</p>
          </div>
          <div class="info-section">
            <h2 class="section-title">基本信息</h2>
            <div class="info-grid">
              <div class="info-item">
                <span class="info-label">门票价格</span>
                <span class="info-value">{{ attraction.ticket === 0 ? '免费' : `¥${attraction.ticket}` }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">景点评级</span>
                <span class="info-value">★★★★★</span>
              </div>
            </div>
          </div>
          <div class="info-section">
            <h2 class="section-title">景点标签</h2>
            <div class="tags">
              <span v-for="tag in attraction.tags" :key="tag" class="tag">{{ tag }}</span>
            </div>
          </div>
          <div class="info-section">
            <h2 class="section-title">游客评价</h2>
            <div class="reviews">
              <div class="review-item" v-for="review in reviews" :key="review.id">
                <div class="review-header">
                  <span class="reviewer">{{ review.reviewer }}</span>
                  <span class="review-rating">
                    <i v-for="i in 5" :key="i" class="el-icon-star" :style="{ color: i <= review.rating ? '#f5a623' : '#ddd' }"></i>
                  </span>
                </div>
                <p class="review-content">{{ review.content }}</p>
              </div>
            </div>
          </div>
        </div>
        <div class="side-bar">
          <div class="action-card">
            <div class="price-info">
              <span class="price-label">门票价格</span>
              <span class="price-value">{{ attraction.ticket === 0 ? '免费' : `¥${attraction.ticket}` }}</span>
            </div>
            <button class="btn-book" @click="bookTicket">预订门票</button>
            <button class="btn-favorite" :class="{ active: isFavorite }" @click="toggleFavorite">
              <i class="el-icon-star"></i> {{ isFavorite ? '已收藏' : '收藏' }}
            </button>
          </div>
          <div class="related-section">
            <h3>推荐景点</h3>
            <div class="related-list">
              <div v-for="item in relatedAttractions" :key="item.id" class="related-item" @click="goToAttraction(item.id)">
                <div class="related-image" :style="{ backgroundImage: `url(${item.image})` }"></div>
                <div class="related-info">
                  <span class="related-name">{{ item.name }}</span>
                  <span class="related-rating">{{ item.rating }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import NavBar from '@/components/NavBar.vue'
import { getAttractionDetail, searchAttractions } from '@/api/attraction'

const route = useRoute()
const router = useRouter()

const attraction = ref({
  id: null,
  name: '',
  city: '',
  description: '',
  tags: [],
  image: '',
  rating: 0,
  ticket: 0
})

const reviews = ref([
  { id: 1, reviewer: '游客小明', rating: 5, content: '非常值得一去！景色很美，服务也很好。' },
  { id: 2, reviewer: '旅行达人', rating: 4, content: '景点很大，建议早点去，可以玩一整天。' },
  { id: 3, reviewer: '摄影爱好者', rating: 5, content: '拍照圣地，随便拍都是大片！' }
])

const relatedAttractions = ref([])
const isFavorite = ref(false)

const attractionId = computed(() => route.params.id)

onMounted(async () => {
  try {
    const data = await getAttractionDetail(attractionId.value)
    attraction.value = {
      ...data,
      image: data.coverImage,
      ticket: data.ticketPrice,
      tags: Array.isArray(data.tags) ? data.tags : (typeof data.tags === 'string' && data.tags.trim() ? data.tags.split(',') : [])
    }
    
    const related = await searchAttractions(attraction.value.city)
    relatedAttractions.value = related.filter(a => a.id !== attraction.value.id).slice(0, 3).map(item => ({
      ...item,
      image: item.coverImage
    }))
  } catch (error) {
    console.error('获取景点详情失败:', error)
  }
})

function goBack() {
  router.back()
}

function goToAttraction(id) {
  router.push(`/attraction/${id}`)
}

function bookTicket() {
  alert(`预订 ${attraction.value.name} 门票，价格：${attraction.value.ticket === 0 ? '免费' : `¥${attraction.value.ticket}`}`)
}

function toggleFavorite() {
  isFavorite.value = !isFavorite.value
  alert(isFavorite.value ? '已收藏' : '已取消收藏')
}
</script>

<style scoped>
.attraction-detail {
  min-height: 100vh;
  background: #f5f7fa;
}

.detail-content {
  padding-top: 60px;
}

.detail-header {
  position: relative;
}

.header-image {
  height: 400px;
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

.header-info {
  position: absolute;
  bottom: 30px;
  left: 30px;
  color: #fff;
}

.back-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  background: rgba(255, 255, 255, 0.2);
  padding: 8px 16px;
  border-radius: 20px;
  margin-bottom: 16px;
  cursor: pointer;
  font-size: 14px;
}

.back-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.header-info h1 {
  font-size: 36px;
  margin-bottom: 12px;
}

.header-meta {
  display: flex;
  gap: 16px;
  align-items: center;
}

.city-tag {
  background: #667eea;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
}

.rating {
  font-size: 16px;
  font-weight: 600;
}

.detail-body {
  display: flex;
  gap: 30px;
  padding: 40px;
  max-width: 1200px;
  margin: 0 auto;
}

.main-info {
  flex: 1;
  background: #fff;
  border-radius: 12px;
  padding: 30px;
}

.section-title {
  font-size: 20px;
  color: #303133;
  margin-bottom: 16px;
  padding-bottom: 10px;
  border-bottom: 2px solid #667eea;
}

.description {
  font-size: 15px;
  color: #606266;
  line-height: 1.8;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.info-item {
  background: #fafafa;
  padding: 16px;
  border-radius: 8px;
}

.info-label {
  display: block;
  font-size: 13px;
  color: #909399;
  margin-bottom: 6px;
}

.info-value {
  font-size: 16px;
  color: #303133;
  font-weight: 500;
}

.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.tag {
  background: #667eea;
  color: #fff;
  padding: 6px 14px;
  border-radius: 16px;
  font-size: 13px;
}

.reviews {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.review-item {
  padding: 16px;
  background: #fafafa;
  border-radius: 8px;
}

.review-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.reviewer {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

.review-content {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
}

.side-bar {
  width: 320px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.action-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  position: sticky;
  top: 80px;
}

.price-info {
  text-align: center;
  margin-bottom: 20px;
}

.price-label {
  display: block;
  font-size: 13px;
  color: #909399;
  margin-bottom: 8px;
}

.price-value {
  font-size: 32px;
  color: #f56c6c;
  font-weight: 700;
}

.btn-book {
  width: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  padding: 14px;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  margin-bottom: 10px;
}

.btn-book:hover {
  opacity: 0.9;
}

.btn-favorite {
  width: 100%;
  background: #fafafa;
  color: #909399;
  padding: 12px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.btn-favorite.active {
  background: #fef0f0;
  color: #f56c6c;
  border-color: #f56c6c;
}

.related-section {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
}

.related-section h3 {
  font-size: 16px;
  color: #303133;
  margin-bottom: 16px;
}

.related-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.related-item {
  display: flex;
  gap: 12px;
  cursor: pointer;
  padding: 10px;
  border-radius: 8px;
  transition: background 0.2s;
}

.related-item:hover {
  background: #f5f7fa;
}

.related-image {
  width: 80px;
  height: 60px;
  background-size: cover;
  background-position: center;
  border-radius: 6px;
}

.related-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.related-name {
  font-size: 14px;
  color: #303133;
  margin-bottom: 4px;
}

.related-rating {
  font-size: 12px;
  color: #f5a623;
}
</style>
