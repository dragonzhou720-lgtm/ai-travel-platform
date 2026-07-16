import { attractionsData, allAttractions } from './attraction'
import { hotelsData, allHotels } from './hotel'
import { generateRouteWithAI } from './deepseek'

let routeIdCounter = 100
const routes = []

const hotRouteTemplates = [
  { id: 1, city: '北京', days: 3, style: 'sightseeing', createdAt: Date.now() - 86400000 },
  { id: 2, city: '上海', days: 5, style: 'leisure', createdAt: Date.now() - 172800000 },
  { id: 3, city: '杭州', days: 2, style: 'culture', createdAt: Date.now() - 259200000 },
  { id: 4, city: '成都', days: 4, style: 'food', createdAt: Date.now() - 345600000 },
  { id: 5, city: '西安', days: 3, style: 'culture', createdAt: Date.now() - 432000000 },
  { id: 6, city: '三亚', days: 5, style: 'leisure', createdAt: Date.now() - 518400000 }
]

function generateItinerary(city, days, style) {
  const cityAttractions = attractionsData[city] || allAttractions.slice(0, 3)
  const itinerary = []

  const styleActivities = {
    sightseeing: ['上午游览', '下午参观', '晚上漫步'],
    leisure: ['上午休闲', '下午放松', '晚上享受'],
    food: ['上午品尝', '下午探店', '晚上美食'],
    adventure: ['上午探险', '下午挑战', '晚上露营'],
    culture: ['上午参观', '下午体验', '晚上赏析']
  }

  const activities = styleActivities[style] || styleActivities.sightseeing

  for (let i = 0; i < days; i++) {
    const dayAttractions = cityAttractions.slice(
      (i * 2) % cityAttractions.length,
      (i * 2) % cityAttractions.length + 2
    )
    if (dayAttractions.length < 2) {
      dayAttractions.push(...cityAttractions.slice(0, 2 - dayAttractions.length))
    }

    itinerary.push({
      activities: [
        {
          time: activities[0] || '上午',
          activity: dayAttractions[0]?.name || `${city}景点${i + 1}`,
          description: dayAttractions[0]?.description || '游览当地著名景点'
        },
        {
          time: activities[1] || '下午',
          activity: dayAttractions[1]?.name || `${city}景点${i + 2}`,
          description: dayAttractions[1]?.description || '继续探索城市魅力'
        },
        {
          time: activities[2] || '晚上',
          activity: '当地美食体验',
          description: `品尝${city}地道美食，感受当地夜生活`
        }
      ]
    })
  }

  return itinerary
}

function getAttractionsForCity(city) {
  return attractionsData[city] || allAttractions.slice(0, 4)
}

function getHotelsForCity(city) {
  return hotelsData[city] || allHotels.slice(0, 3)
}

hotRouteTemplates.forEach(route => {
  route.itinerary = generateItinerary(route.city, route.days, route.style)
  route.attractions = getAttractionsForCity(route.city)
  route.hotels = getHotelsForCity(route.city)
})
routes.push(...hotRouteTemplates)

export default [
  {
    url: '/api/route/generate',
    method: 'post',
    async response({ body }) {
      const { city, days, style, attractions = [], hotels = [] } = body
      const newId = ++routeIdCounter
      
      let routeData
      try {
        routeData = await generateRouteWithAI(city, days, style, attractions, hotels)
      } catch (error) {
        console.warn('DeepSeek AI generation failed, using fallback:', error.message)
        routeData = {
          itinerary: generateItinerary(city, days, style),
          attractions: getAttractionsForCity(city),
          hotels: getHotelsForCity(city),
          aiSuggestions: '已为您生成旅行路线'
        }
      }

      const route = {
        id: newId,
        city: city || '北京',
        days: days || 3,
        style: style || 'sightseeing',
        itinerary: routeData.itinerary,
        attractions: routeData.attractions,
        hotels: routeData.hotels,
        createdAt: Date.now(),
        aiSuggestions: routeData.aiSuggestions || '已为您生成旅行路线'
      }
      routes.push(route)
      return route
    }
  },
  {
    url: '/api/route/hot/count',
    method: 'get',
    response: ({ query }) => {
      const count = hotRouteTemplates.filter(
        r => r.city === query.city && r.days === Number(query.days) && r.style === query.style
      ).length
      return { city: query.city, days: Number(query.days), style: query.style, count }
    }
  },
  {
    url: '/api/route/hot',
    method: 'get',
    response: ({ query }) => {
      const limit = Number(query.limit) || 10
      return hotRouteTemplates.slice(0, limit).map(r => ({
        id: r.id,
        city: r.city,
        days: r.days,
        style: r.style,
        itinerary: r.itinerary,
        createdAt: r.createdAt
      }))
    }
  },
  {
    url: '/api/route',
    method: 'get',
    response: () => {
      return routes
    }
  },
  {
    url: '/api/route/:id',
    method: 'get',
    response: ({ params }) => {
      const id = Number(params.id)
      return routes.find(r => r.id === id) || null
    }
  },
]
