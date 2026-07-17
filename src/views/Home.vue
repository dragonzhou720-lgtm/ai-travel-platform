<template>
  <div class="home-container">
    <NavBar />
    <div class="home-content">
      <section class="hero-section">
        <div class="hero-content">
          <h1>AI智能旅游规划</h1>
          <p>让AI为您定制专属旅行方案，探索世界的美好</p>
          <div class="hero-buttons">
            <router-link to="/plan" class="btn-primary">开始规划</router-link>
            <router-link to="/" class="btn-secondary">了解更多</router-link>
          </div>
        </div>
      </section>
      <section class="features-section">
        <div class="section-header">
          <h2>平台功能</h2>
          <p>一站式智能旅游服务</p>
        </div>
        <div class="features-grid">
          <div class="feature-card">
            <div class="feature-icon">🧭</div>
            <h3>AI路线规划</h3>
            <p>智能分析目的地、预算、天数，生成最优旅行路线</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">🏛️</div>
            <h3>景点推荐</h3>
            <p>基于用户偏好，推荐热门景点和隐藏宝藏</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">🏨</div>
            <h3>酒店预订</h3>
            <p>智能匹配性价比高的住宿选择</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">❤️</div>
            <h3>收藏管理</h3>
            <p>收藏喜欢的路线、景点和酒店</p>
          </div>
        </div>
      </section>
      <section class="cities-section">
        <div class="section-header">
          <h2>热门旅游城市</h2>
          <p>精选热门目的地</p>
        </div>
        <div class="cities-grid">
          <div
            v-for="city in hotCities"
            :key="city.name"
            class="city-card"
            @click="handleCityClick(city.name)"
          >
            <div class="city-image" :style="{ backgroundImage: `url(${city.image})` }">
              <div class="city-overlay">
                <span class="city-name">{{ city.name }}</span>
                <span class="city-count">{{ city.count }}条路线</span>
              </div>
            </div>
          </div>
        </div>
      </section>
      <section class="attractions-section">
        <div class="section-header">
          <h2>热门景点推荐</h2>
          <p>精选必游景点</p>
          <router-link to="/attractions" class="view-more">查看更多</router-link>
        </div>
        <div class="attractions-list">
          <div
            v-for="attraction in attractions"
            :key="attraction.id"
            class="attraction-card"
            @click="goToAttraction(attraction.id)"
          >
            <div class="attraction-image" :style="{ backgroundImage: `url(${attraction.image})` }"></div>
            <div class="attraction-info">
              <h3>{{ attraction.name }}</h3>
              <p class="attraction-city">{{ attraction.city }}</p>
              <p class="attraction-desc">{{ attraction.description }}</p>
              <div class="attraction-tags">
                <span v-for="tag in attraction.tags" :key="tag" class="tag">{{ tag }}</span>
              </div>
            </div>
          </div>
        </div>
      </section>
      <section class="hotels-section">
        <div class="section-header">
          <h2>热门酒店推荐</h2>
          <p>舒适住宿选择</p>
          <router-link to="/hotels" class="view-more">查看更多</router-link>
        </div>
        <div class="hotels-list">
          <div
            v-for="hotel in hotels"
            :key="hotel.id"
            class="hotel-card"
            @click="goToHotel(hotel.id)"
          >
            <div class="hotel-image" :style="{ backgroundImage: `url(${hotel.image})` }"></div>
            <div class="hotel-info">
              <h3>{{ hotel.name }}</h3>
              <p class="hotel-city">{{ hotel.city }}</p>
              <p class="hotel-desc">{{ hotel.description }}</p>
              <div class="hotel-price">
                <span class="price">¥{{ hotel.price }}</span>
                <span class="price-unit">/晚起</span>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
    <footer class="footer">
      <p>&copy; 2024 AI旅游规划平台 - 让旅行更智能</p>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import NavBar from '@/components/NavBar.vue'
import { searchAttractions } from '@/api/attraction'
import { searchHotels } from '@/api/hotel'

const router = useRouter()

const hotCities = ref([
  { name: '北京', count: 156, image: 'https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=400&h=300&fit=crop' },
  { name: '上海', count: 142, image: 'https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=400&h=300&fit=crop' },
  { name: '杭州', count: 128, image: 'https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=400&h=300&fit=crop' },
  { name: '成都', count: 98, image: 'https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=400&h=300&fit=crop' },
  { name: '西安', count: 89, image: 'https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=400&h=300&fit=crop' },
  { name: '三亚', count: 76, image: 'https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=400&h=300&fit=crop' }
])

const attractions = ref([])
const hotels = ref([])

onMounted(async () => {
  try {
    const [attractionsData, hotelsData] = await Promise.all([
      searchAttractions('北京'),
      searchHotels('北京')
    ])
    attractions.value = attractionsData.slice(0, 4).map(item => ({
        ...item,
        image: item.coverImage,
        tags: Array.isArray(item.tags) ? item.tags : (typeof item.tags === 'string' && item.tags.trim() ? item.tags.split(',') : [])
      }))
    hotels.value = hotelsData.slice(0, 4).map(item => ({
      ...item,
      image: item.coverImage,
      price: item.pricePerNight
    }))
  } catch (error) {
    attractions.value = [
      { id: 1, name: '故宫博物院', city: '北京', description: '中国明清两代的皇家宫殿，世界文化遗产', tags: ['历史', '文化'], image: 'https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=300&h=200&fit=crop' },
      { id: 2, name: '八达岭长城', city: '北京', description: '万里长城的重要组成部分，世界文化遗产', tags: ['历史', '自然'], image: 'https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=300&h=200&fit=crop' },
      { id: 3, name: '颐和园', city: '北京', description: '中国现存规模最大、保存最完整的皇家园林', tags: ['园林', '文化'], image: 'https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=300&h=200&fit=crop' },
      { id: 4, name: '天坛公园', city: '北京', description: '明清两代皇帝祭天祈谷的场所', tags: ['历史', '文化'], image: 'https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=300&h=200&fit=crop' }
    ]
    hotels.value = [
      { id: 1, name: '北京王府井大饭店', city: '北京', description: '位于王府井商圈中心，交通便利', price: 580, image: 'https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=300&h=200&fit=crop' },
      { id: 2, name: '北京国贸大酒店', city: '北京', description: 'CBD核心区域，俯瞰城市美景', price: 880, image: 'https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=300&h=200&fit=crop' },
      { id: 3, name: '北京四季酒店', city: '北京', description: '五星级奢华体验，卓越服务品质', price: 1280, image: 'https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=300&h=200&fit=crop' },
      { id: 4, name: '北京希尔顿酒店', city: '北京', description: '国际知名品牌，舒适入住体验', price: 680, image: 'https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=300&h=200&fit=crop' }
    ]
  }
})

function handleCityClick(city) {
  router.push({ path: '/plan', query: { city } })
}

function goToAttraction(id) {
  router.push(`/attraction/${id}`)
}

function goToHotel(id) {
  router.push(`/hotel/${id}`)
}
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background: #f5f7fa;
}

.home-content {
  padding-top: 60px;
}

.hero-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 100px 20px;
  text-align: center;
}

.hero-content h1 {
  font-size: 48px;
  color: #fff;
  margin-bottom: 20px;
  font-weight: 700;
}

.hero-content p {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 40px;
}

.hero-buttons {
  display: flex;
  gap: 20px;
  justify-content: center;
}

.btn-primary {
  background: #fff;
  color: #667eea;
  padding: 14px 32px;
  border-radius: 30px;
  text-decoration: none;
  font-size: 16px;
  font-weight: 600;
  transition: transform 0.3s;
}

.btn-primary:hover {
  transform: translateY(-2px);
}

.btn-secondary {
  background: transparent;
  color: #fff;
  padding: 14px 32px;
  border-radius: 30px;
  text-decoration: none;
  font-size: 16px;
  font-weight: 600;
  border: 2px solid #fff;
  transition: transform 0.3s;
}

.btn-secondary:hover {
  transform: translateY(-2px);
}

.section-header {
  text-align: center;
  padding: 40px 20px 20px;
}

.section-header h2 {
  font-size: 32px;
  color: #303133;
  margin-bottom: 10px;
}

.section-header p {
  font-size: 14px;
  color: #909399;
}

.view-more {
  display: inline-block;
  margin-top: 10px;
  font-size: 14px;
  color: #667eea;
  text-decoration: none;
}

.view-more:hover {
  text-decoration: underline;
}

.features-section {
  background: #fff;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  padding: 20px 40px 60px;
  max-width: 1200px;
  margin: 0 auto;
}

.feature-card {
  text-align: center;
  padding: 30px;
  border-radius: 12px;
  background: #fafafa;
}

.feature-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.feature-card h3 {
  font-size: 18px;
  color: #303133;
  margin-bottom: 10px;
}

.feature-card p {
  font-size: 14px;
  color: #909399;
  line-height: 1.6;
}

.cities-section {
  background: #f5f7fa;
}

.cities-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  padding: 20px 40px 60px;
  max-width: 1200px;
  margin: 0 auto;
}

.city-card {
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.3s;
}

.city-card:hover {
  transform: translateY(-5px);
}

.city-image {
  height: 150px;
  background-size: cover;
  background-position: center;
  position: relative;
}

.city-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.6));
  padding: 20px;
  color: #fff;
}

.city-name {
  display: block;
  font-size: 18px;
  font-weight: 600;
}

.city-count {
  font-size: 12px;
  opacity: 0.8;
}

.attractions-section, .hotels-section {
  background: #fff;
}

.attractions-list, .hotels-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  padding: 20px 40px 60px;
  max-width: 1200px;
  margin: 0 auto;
}

.attraction-card, .hotel-card {
  display: flex;
  flex-direction: column;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  cursor: pointer;
  transition: transform 0.3s;
}

.attraction-card:hover, .hotel-card:hover {
  transform: translateY(-3px);
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
  margin-bottom: 8px;
}

.attraction-city, .hotel-city {
  font-size: 12px;
  color: #667eea;
  margin-bottom: 10px;
}

.attraction-desc, .hotel-desc {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.attraction-tags {
  display: flex;
  gap: 8px;
}

.tag {
  font-size: 12px;
  color: #fff;
  background: #667eea;
  padding: 4px 12px;
  border-radius: 12px;
}

.hotel-price {
  display: flex;
  align-items: baseline;
}

.price {
  font-size: 20px;
  color: #f56c6c;
  font-weight: 600;
}

.price-unit {
  font-size: 12px;
  color: #909399;
  margin-left: 4px;
}

.footer {
  background: #303133;
  color: #fff;
  text-align: center;
  padding: 30px;
  font-size: 14px;
}
</style>
