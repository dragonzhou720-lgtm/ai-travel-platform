<template>
  <div class="hotel-detail">
    <NavBar />
    <div class="detail-content">
      <div class="detail-header">
        <div class="header-image" :style="{ backgroundImage: `url(${hotel.image})` }">
          <div class="header-overlay"></div>
          <div class="header-info">
            <div class="back-btn" @click="goBack">
              <i class="el-icon-arrow-left"></i> 返回
            </div>
            <h1>{{ hotel.name }}</h1>
            <div class="header-meta">
              <span class="city-tag">{{ hotel.city }}</span>
              <span class="rating">
                <i class="el-icon-star" style="color: #f5a623;"></i>
                {{ hotel.rating }}
              </span>
            </div>
          </div>
        </div>
      </div>
      <div class="detail-body">
        <div class="main-info">
          <div class="info-section">
            <h2 class="section-title">酒店介绍</h2>
            <p class="description">{{ hotel.description }}</p>
          </div>
          <div class="info-section">
            <h2 class="section-title">基本信息</h2>
            <div class="info-grid">
              <div class="info-item">
                <span class="info-label">酒店地址</span>
                <span class="info-value">{{ hotel.address }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">参考价格</span>
                <span class="info-value">¥{{ hotel.price }}/晚</span>
              </div>
              <div class="info-item">
                <span class="info-label">酒店评级</span>
                <span class="info-value">★★★★★</span>
              </div>
              <div class="info-item">
                <span class="info-label">联系电话</span>
                <span class="info-value">400-888-8888</span>
              </div>
            </div>
          </div>
          <div class="info-section">
            <h2 class="section-title">酒店设施</h2>
            <div class="facilities">
              <div class="facility-item" v-for="facility in facilities" :key="facility">
                <i class="el-icon-check"></i>
                <span>{{ facility }}</span>
              </div>
            </div>
          </div>
          <div class="info-section">
            <h2 class="section-title">用户评价</h2>
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
              <span class="price-label">参考价格</span>
              <div class="price-row">
                <span class="price-value">¥{{ hotel.price }}</span>
                <span class="price-unit">/晚起</span>
              </div>
            </div>
            <div class="booking-form">
              <div class="form-item">
                <label>入住日期</label>
                <input type="date" class="date-input" v-model="checkIn" />
              </div>
              <div class="form-item">
                <label>退房日期</label>
                <input type="date" class="date-input" v-model="checkOut" />
              </div>
              <div class="form-item">
                <label>人数</label>
                <select class="select-input" v-model="people">
                  <option value="1">1人</option>
                  <option value="2">2人</option>
                  <option value="3">3人</option>
                  <option value="4">4人</option>
                </select>
              </div>
            </div>
            <button class="btn-book" @click="bookRoom">立即预订</button>
            <button class="btn-favorite" :class="{ active: isFavorite }" @click="toggleFavorite">
              <i class="el-icon-star"></i> {{ isFavorite ? '已收藏' : '收藏' }}
            </button>
          </div>
          <div class="related-section">
            <h3>同城市推荐</h3>
            <div class="related-list">
              <div v-for="item in relatedHotels" :key="item.id" class="related-item" @click="goToHotel(item.id)">
                <div class="related-image" :style="{ backgroundImage: `url(${item.image})` }"></div>
                <div class="related-info">
                  <span class="related-name">{{ item.name }}</span>
                  <span class="related-price">¥{{ item.price }}/晚</span>
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
import { getHotelDetail, searchHotels } from '@/api/hotel'

const route = useRoute()
const router = useRouter()

const hotel = ref({
  id: null,
  name: '',
  city: '',
  description: '',
  price: 0,
  rating: 0,
  image: '',
  address: ''
})

const facilities = ref([
  '免费WiFi', '停车场', '餐厅', '游泳池', '健身房', '商务中心', '客房服务', '24小时前台'
])

const reviews = ref([
  { id: 1, reviewer: '商务旅客', rating: 5, content: '位置很好，交通便利，服务周到。' },
  { id: 2, reviewer: '家庭出游', rating: 4, content: '房间很大，设施齐全，早餐很棒。' },
  { id: 3, reviewer: '情侣旅行', rating: 5, content: '环境优雅，适合度假，推荐！' }
])

const relatedHotels = ref([])
const isFavorite = ref(false)
const checkIn = ref('')
const checkOut = ref('')
const people = ref('2')

const hotelId = computed(() => route.params.id)

onMounted(async () => {
  try {
    const data = await getHotelDetail(hotelId.value)
    hotel.value = {
      ...data,
      image: data.coverImage,
      price: data.pricePerNight
    }
    
    const related = await searchHotels(hotel.value.city)
    relatedHotels.value = related.filter(h => h.id !== hotel.value.id).slice(0, 3).map(item => ({
      ...item,
      image: item.coverImage,
      price: item.pricePerNight
    }))
  } catch (error) {
    console.error('获取酒店详情失败:', error)
  }
})

function goBack() {
  router.back()
}

function goToHotel(id) {
  router.push(`/hotel/${id}`)
}

function bookRoom() {
  if (!checkIn.value || !checkOut.value) {
    alert('请选择入住和退房日期')
    return
  }
  alert(`预订成功！\n酒店：${hotel.value.name}\n入住：${checkIn.value}\n退房：${checkOut.value}\n人数：${people.value}人\n价格：¥${hotel.value.price}/晚`)
}

function toggleFavorite() {
  isFavorite.value = !isFavorite.value
  alert(isFavorite.value ? '已收藏' : '已取消收藏')
}
</script>

<style scoped>
.hotel-detail {
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
  font-size: 15px;
  color: #303133;
  font-weight: 500;
}

.facilities {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
}

.facility-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px;
  background: #fafafa;
  border-radius: 6px;
  font-size: 13px;
  color: #606266;
}

.facility-item i {
  color: #67c23a;
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

.price-row {
  display: flex;
  align-items: baseline;
  justify-content: center;
}

.price-value {
  font-size: 32px;
  color: #f56c6c;
  font-weight: 700;
}

.price-unit {
  font-size: 14px;
  color: #909399;
  margin-left: 4px;
}

.booking-form {
  margin-bottom: 20px;
}

.form-item {
  margin-bottom: 12px;
}

.form-item label {
  display: block;
  font-size: 13px;
  color: #909399;
  margin-bottom: 6px;
}

.date-input, .select-input {
  width: 100%;
  padding: 10px;
  border: 1px solid #dcdfe6;
  border-radius: 6px;
  font-size: 14px;
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

.related-price {
  font-size: 13px;
  color: #f56c6c;
}
</style>
