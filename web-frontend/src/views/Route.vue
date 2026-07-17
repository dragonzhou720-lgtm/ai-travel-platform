<template>
  <div class="min-h-screen">
    <div class="bg-gradient-to-r from-blue-600 to-indigo-700 text-white py-12">
      <div class="container mx-auto px-4 text-center">
        <h1 class="text-3xl font-bold mb-4">AI智能路线规划</h1>
        <p class="text-blue-100">输入您的出行偏好，AI帮您生成完美的旅行计划</p>
      </div>
    </div>

    <div class="container mx-auto px-4 py-8">
      <div class="max-w-2xl mx-auto">
        <form @submit.prevent="handleGenerate" class="bg-white rounded-2xl shadow-xl p-8">
          <div class="space-y-6">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">目的地城市</label>
              <select
                v-model="form.city"
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                required
              >
                <option value="">请选择城市</option>
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
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">出行天数</label>
              <select
                v-model="form.days"
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                required
              >
                <option value="">请选择天数</option>
                <option value="1">1天</option>
                <option value="2">2天</option>
                <option value="3">3天</option>
                <option value="4">4天</option>
                <option value="5">5天</option>
                <option value="6">6天</option>
                <option value="7">7天</option>
                <option value="8">8天以上</option>
              </select>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">旅行风格</label>
              <select
                v-model="form.style"
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                required
              >
                <option value="">请选择风格</option>
                <option value="休闲">休闲度假</option>
                <option value="文化">文化探索</option>
                <option value="美食">美食之旅</option>
                <option value="自然风光">自然风光</option>
                <option value="亲子">亲子游</option>
                <option value="购物">购物血拼</option>
                <option value="探险">探险冒险</option>
              </select>
            </div>

            <button
              type="submit"
              :disabled="loading"
              class="w-full px-6 py-4 bg-blue-600 text-white font-semibold rounded-lg hover:bg-blue-700 focus:ring-4 focus:ring-blue-300 transition-all disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <span v-if="loading" class="flex items-center justify-center">
                <svg class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
                AI生成中...
              </span>
              <span v-else>生成路线</span>
            </button>
          </div>
        </form>

        <div v-if="error" class="mt-6 p-4 bg-red-50 border border-red-200 rounded-lg">
          <p class="text-red-600 text-sm">{{ error }}</p>
        </div>
      </div>

      <div v-if="aiResult" class="mt-8 max-w-4xl mx-auto">
        <div class="bg-white rounded-2xl shadow-xl overflow-hidden">
          <div class="bg-gradient-to-r from-blue-500 to-indigo-600 text-white p-6">
            <div class="flex items-center justify-between">
              <div>
                <h2 class="text-2xl font-bold">{{ aiResult.route?.trip_name || aiResult.route?.destination || `${form.city}${form.days}日游` }}</h2>
                <p class="text-blue-100 mt-1">{{ form.city }} · {{ form.days }}天 · {{ form.style }}</p>
              </div>
              <span class="px-4 py-2 bg-white/20 text-white rounded-full text-sm backdrop-blur">AI生成</span>
            </div>
          </div>

          <div class="p-6">
            <div v-if="formattedDays.length === 0" class="bg-yellow-50 border border-yellow-200 rounded-lg p-4 mb-6">
              <p class="text-sm text-yellow-800">原始数据：</p>
              <pre class="text-xs text-gray-600 mt-2 p-2 bg-white rounded">{{ aiResult.ai_response || JSON.stringify(aiResult, null, 2) }}</pre>
            </div>
            
            <div class="space-y-6">
              <div
                v-for="(day, dayIndex) in formattedDays"
                :key="dayIndex"
                class="bg-gray-50 rounded-xl overflow-hidden"
              >
                <div class="bg-gradient-to-r from-blue-500 to-blue-600 text-white px-5 py-3">
                  <div class="flex items-center gap-3">
                    <div class="w-8 h-8 bg-white/20 rounded-full flex items-center justify-center font-bold text-sm">
                      {{ day.day }}
                    </div>
                    <h3 class="font-semibold">第{{ day.day }}天行程</h3>
                  </div>
                </div>

                <div class="p-5 space-y-4">
                  <div v-if="day.morning" class="flex gap-4">
                    <div class="flex-shrink-0 w-10 h-10 bg-amber-100 rounded-full flex items-center justify-center">
                      <svg class="w-5 h-5 text-amber-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z" />
                      </svg>
                    </div>
                    <div class="flex-1 bg-white rounded-lg p-4 shadow-sm">
                      <div class="flex items-center justify-between mb-1">
                        <div class="flex items-center gap-2">
                          <span class="text-xs font-medium text-amber-600 bg-amber-50 px-2 py-0.5 rounded">上午</span>
                          <h4 class="font-semibold text-gray-800">{{ day.morning.name }}</h4>
                        </div>
                        <span class="text-sm font-medium text-red-500">{{ formatPrice(day.morning.price) }}</span>
                      </div>
                      <p class="text-sm text-gray-500 mb-2">{{ day.morning.location }}</p>
                      <p class="text-sm text-gray-600 leading-relaxed">{{ day.morning.description }}</p>
                    </div>
                  </div>

                  <div v-if="day.afternoon" class="flex gap-4">
                    <div class="flex-shrink-0 w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center">
                      <svg class="w-5 h-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z" />
                      </svg>
                    </div>
                    <div class="flex-1 bg-white rounded-lg p-4 shadow-sm">
                      <div class="flex items-center justify-between mb-1">
                        <div class="flex items-center gap-2">
                          <span class="text-xs font-medium text-blue-600 bg-blue-50 px-2 py-0.5 rounded">下午</span>
                          <h4 class="font-semibold text-gray-800">{{ day.afternoon.name }}</h4>
                        </div>
                        <span class="text-sm font-medium text-red-500">{{ formatPrice(day.afternoon.price) }}</span>
                      </div>
                      <p class="text-sm text-gray-500 mb-2">{{ day.afternoon.location }}</p>
                      <p class="text-sm text-gray-600 leading-relaxed">{{ day.afternoon.description }}</p>
                    </div>
                  </div>

                  <div v-if="day.evening" class="flex gap-4">
                    <div class="flex-shrink-0 w-10 h-10 bg-indigo-100 rounded-full flex items-center justify-center">
                      <svg class="w-5 h-5 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z" />
                      </svg>
                    </div>
                    <div class="flex-1 bg-white rounded-lg p-4 shadow-sm">
                      <div class="flex items-center justify-between mb-1">
                        <div class="flex items-center gap-2">
                          <span class="text-xs font-medium text-indigo-600 bg-indigo-50 px-2 py-0.5 rounded">晚上</span>
                          <h4 class="font-semibold text-gray-800">{{ day.evening.name }}</h4>
                        </div>
                        <span class="text-sm font-medium text-red-500">{{ formatPrice(day.evening.price) }}</span>
                      </div>
                      <p class="text-sm text-gray-500 mb-2">{{ day.evening.location }}</p>
                      <p class="text-sm text-gray-600 leading-relaxed">{{ day.evening.description }}</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="px-6 pb-6">
            <div class="flex gap-4">
              <button
                @click="handleGenerate"
                class="flex items-center gap-2 px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
              >
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
                </svg>
                重新生成
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { aiApi } from '@/api'

const loading = ref(false)
const error = ref('')
const aiResult = ref(null)

const form = reactive({
  city: '',
  days: '',
  style: ''
})

const formattedDays = computed(() => {
  if (!aiResult.value?.route) return []
  
  const route = aiResult.value.route
  
  if (route.day1 || route.day2 || route.day3) {
    const days = []
    
    for (let i = 1; i <= parseInt(form.days); i++) {
      const dayKey = `day${i}`
      const dayData = route[dayKey]
      
      if (dayData) {
        days.push({
          day: i,
          morning: parseActivity(dayData.morning),
          afternoon: parseActivity(dayData.afternoon),
          evening: parseActivity(dayData.evening)
        })
      }
    }
    
    return days
  }
  
  if (route.itinerary && typeof route.itinerary === 'object' && !Array.isArray(route.itinerary)) {
    const itinerary = route.itinerary
    const days = []
    
    for (let i = 1; i <= parseInt(form.days); i++) {
      const dayKey = `day${i}`
      const dayData = itinerary[dayKey]
      
      if (dayData) {
        days.push({
          day: i,
          morning: parseActivity(dayData.morning),
          afternoon: parseActivity(dayData.afternoon),
          evening: parseActivity(dayData.evening)
        })
      }
    }
    
    return days
  }
  
  if (route.schedule) {
    const schedule = route.schedule
    const days = []
    
    for (let i = 1; i <= parseInt(form.days); i++) {
      const dayKey = `day${i}`
      const dayData = schedule[dayKey]
      
      if (dayData) {
        days.push({
          day: i,
          morning: parseActivity(dayData.morning),
          afternoon: parseActivity(dayData.afternoon),
          evening: parseActivity(dayData.evening)
        })
      }
    }
    
    return days
  }
  
  if (route.itinerary && Array.isArray(route.itinerary)) {
    return route.itinerary.map(dayPlan => ({
      day: dayPlan.day || dayPlan.dayNo || dayPlan.id || 1,
      morning: parseActivity(dayPlan.morning),
      afternoon: parseActivity(dayPlan.afternoon),
      evening: parseActivity(dayPlan.evening)
    }))
  }
  
  return []
})

function parseActivity(activity) {
  if (!activity) return null
  
  if (Array.isArray(activity)) {
    if (activity.length === 0) return null
    const firstItem = activity[0]
    if (typeof firstItem === 'object' && firstItem !== null) {
      return {
        name: firstItem.name || firstItem.activity || '',
        location: firstItem.location || '',
        description: firstItem.description || '',
        price: firstItem.price || ''
      }
    }
    const str = String(firstItem)
    return {
      name: str.replace(/^Day \d+: /, '') || str,
      location: '',
      description: str,
      price: ''
    }
  }
  
  if (typeof activity === 'object') {
    return {
      name: activity.name || activity.activity || '',
      location: activity.location || '',
      description: activity.description || '',
      price: activity.price || ''
    }
  }
  
  const str = String(activity)
  const nameMatch = str.match(/name=(.+?)(?:,|})/)
  const locationMatch = str.match(/location=(.+?)(?:,|})/)
  const descriptionMatch = str.match(/description=(.+?)(?:,|})/)
  const priceMatch = str.match(/price=(.+?)(?:,|})/)
  
  return {
    name: nameMatch ? nameMatch[1].trim() : str.replace(/^Day \d+: /, '') || str,
    location: locationMatch ? locationMatch[1].trim() : '',
    description: descriptionMatch ? descriptionMatch[1].trim() : str,
    price: priceMatch ? priceMatch[1].trim() : ''
  }
}

function formatPrice(price) {
  if (!price) return ''
  const str = String(price).trim()
  if (str === '0' || str === '免费' || str === 'Free' || str.toLowerCase() === 'free') {
    return '免费'
  }
  if (str.includes('¥') || str.includes('元') || str.includes('￥')) {
    return str
  }
  if (!isNaN(str)) {
    return `¥${str}`
  }
  return str
}

async function handleGenerate() {
  loading.value = true
  error.value = ''

  try {
    aiResult.value = await aiApi.generateRoute({
      city: form.city,
      days: parseInt(form.days),
      style: form.style
    })
    console.log('AI Result:', JSON.stringify(aiResult.value, null, 2))
    console.log('Route:', JSON.stringify(aiResult.value?.route, null, 2))
    console.log('Formatted Days:', JSON.stringify(formattedDays.value, null, 2))
  } catch (e) {
    console.error('Generate route error:', e)
    error.value = e.response?.data?.message || '生成路线失败，请稍后重试'
  } finally {
    loading.value = false
  }
}
</script>
