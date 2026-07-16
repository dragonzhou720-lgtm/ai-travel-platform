import dotenv from 'dotenv'
import https from 'https'

dotenv.config()

const API_KEY = process.env.DEEPSEEK_API_KEY
const API_BASE = process.env.DEEPSEEK_API_BASE || 'https://api.deepseek.com'

function getImageUrl(name) {
  const imagePrompts = {
    '故宫博物院': 'Beijing Forbidden City traditional Chinese architecture',
    '八达岭长城': 'Great Wall of China mountain landscape',
    '颐和园': 'Summer Palace Beijing traditional garden',
    '天坛公园': 'Temple of Heaven Beijing ancient architecture',
    '鸟巢': 'Beijing National Stadium bird nest modern architecture',
    '外滩': 'Shanghai Bund skyline night view',
    '东方明珠塔': 'Shanghai Oriental Pearl Tower landmark',
    '迪士尼乐园': 'Disneyland amusement park castle',
    '豫园': 'Shanghai Yu Garden classical Chinese garden',
    '西湖': 'Hangzhou West Lake scenic landscape',
    '灵隐寺': 'Lingyin Temple Buddhist temple',
    '千岛湖': 'Qiandao Lake thousand islands scenic',
    '大熊猫繁育研究基地': 'Giant panda bear cute animal',
    '武侯祠': 'Wuhou Temple Chengdu ancient architecture',
    '锦里古街': 'Jinli Ancient Street Chengdu traditional market',
    '秦始皇兵马俑': 'Terracotta Army Xi\'an ancient warriors',
    '大雁塔': 'Big Wild Goose Pagoda Xi\'an Buddhist tower',
    '回民街': 'Muslim Quarter Xi\'an street food market',
    '亚龙湾': 'Sanya Yalong Bay beach tropical sea',
    '天涯海角': 'Tianya Haijiao Sanya scenic ocean',
    '南山寺': 'Nanshan Temple Sanya Buddhist temple',
    '东方明珠': 'Shanghai Oriental Pearl',
    '陆家嘴': 'Shanghai Lujiazui skyline'
  }
  const prompt = imagePrompts[name] || `travel destination ${name} scenic view`
  return `https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=${encodeURIComponent(prompt)}&image_size=landscape_4_3`
}

async function fetchDeepSeek(prompt) {
  return new Promise((resolve, reject) => {
    if (!API_KEY || API_KEY === 'your_deepseek_api_key_here') {
      console.warn('DeepSeek API key not configured, using fallback')
      reject(new Error('DeepSeek API key not configured'))
      return
    }

    const data = JSON.stringify({
      model: 'deepseek-chat',
      messages: [
        {
          role: 'system',
          content: '你是一个专业的旅游规划师，擅长根据用户需求生成详细的旅行路线。请用中文回复，保持专业友好的语气。'
        },
        {
          role: 'user',
          content: prompt
        }
      ],
      temperature: 0.7,
      max_tokens: 4000
    })

    const url = new URL(API_BASE)
    const options = {
      hostname: url.hostname,
      path: '/v1/chat/completions',
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${API_KEY}`,
        'Content-Length': data.length
      },
      timeout: 60000,
      agent: new https.Agent({ rejectUnauthorized: false })
    }

    console.debug('DeepSeek API request:', { hostname: options.hostname, path: options.path, dataLength: data.length })

    const req = https.request(options, (res) => {
      let responseData = ''
      res.on('data', (chunk) => {
        responseData += chunk
      })
      res.on('end', () => {
        console.debug('DeepSeek API response status:', res.statusCode)
        console.debug('DeepSeek API response headers:', JSON.stringify(res.headers))
        console.debug('DeepSeek API response raw (first 1000 chars):', responseData.substring(0, 1000))

        if (res.statusCode !== 200) {
          reject(new Error(`DeepSeek API returned status ${res.statusCode}: ${responseData.substring(0, 200)}`))
          return
        }

        if (!responseData || responseData.trim() === '') {
          reject(new Error('DeepSeek API returned empty response'))
          return
        }

        try {
          const json = JSON.parse(responseData)
          if (json.error) {
            reject(new Error(`DeepSeek API error: ${json.error.message || JSON.stringify(json.error)}`))
          } else {
            const content = json.choices?.[0]?.message?.content
            if (!content) {
              reject(new Error('DeepSeek API returned empty content'))
            } else {
              resolve(content)
            }
          }
        } catch (err) {
          console.error('DeepSeek API raw response:', responseData)
          reject(new Error(`Failed to parse DeepSeek response: ${err.message}. Response: ${responseData.substring(0, 300)}`))
        }
      })
    })

    req.on('error', (error) => {
      console.error('DeepSeek request error:', error)
      reject(new Error(`DeepSeek request failed: ${error.message}`))
    })

    req.on('timeout', () => {
      req.destroy()
      reject(new Error('DeepSeek request timed out'))
    })

    req.write(data)
    req.end()
  })
}

function parseRouteFromResponse(content, city, days, style) {
  try {
    const jsonMatch = content.match(/\{[\s\S]*\}/)
    if (jsonMatch) {
      const route = JSON.parse(jsonMatch[0])
      return route
    }
  } catch (e) {
    console.warn('Failed to parse JSON from DeepSeek response:', e.message)
  }

  return generateFallbackRoute(city, days, style)
}

function generateFallbackRoute(city, days, style) {
  const styleLabels = {
    sightseeing: '观光游览',
    leisure: '休闲度假',
    food: '美食探索',
    adventure: '探险体验',
    culture: '文化历史'
  }

  const cityAttractions = {
    '北京': ['故宫博物院', '八达岭长城', '颐和园', '天坛公园', '鸟巢'],
    '上海': ['外滩', '东方明珠塔', '迪士尼乐园', '豫园', '南京路'],
    '杭州': ['西湖', '灵隐寺', '千岛湖', '雷峰塔', '西溪湿地'],
    '成都': ['大熊猫繁育研究基地', '武侯祠', '锦里古街', '宽窄巷子', '都江堰'],
    '西安': ['秦始皇兵马俑', '大雁塔', '回民街', '古城墙', '碑林'],
    '三亚': ['亚龙湾', '天涯海角', '南山寺', '蜈支洲岛', '海棠湾'],
    '广州': ['广州塔', '长隆乐园', '陈家祠', '沙面', '白云山'],
    '深圳': ['世界之窗', '欢乐谷', '东部华侨城', '大鹏湾', '莲花山'],
    '重庆': ['洪崖洞', '解放碑', '长江索道', '磁器口', '武隆'],
    '南京': ['中山陵', '夫子庙', '明孝陵', '玄武湖', '总统府']
  }

  const attractions = cityAttractions[city] || ['当地著名景点']
  const itinerary = []

  const activityTypes = {
    sightseeing: ['上午游览', '下午参观', '晚上漫步'],
    leisure: ['上午休闲', '下午放松', '晚上享受'],
    food: ['上午品尝', '下午探店', '晚上美食'],
    adventure: ['上午探险', '下午挑战', '晚上露营'],
    culture: ['上午参观', '下午体验', '晚上赏析']
  }

  const activities = activityTypes[style] || activityTypes.sightseeing

  for (let i = 0; i < days; i++) {
    const dayAttractions = [
      attractions[(i * 2) % attractions.length],
      attractions[(i * 2 + 1) % attractions.length]
    ]

    itinerary.push({
      activities: [
        {
          time: activities[0],
          activity: dayAttractions[0],
          description: `游览${dayAttractions[0]}，感受当地独特魅力`
        },
        {
          time: activities[1],
          activity: dayAttractions[1],
          description: `参观${dayAttractions[1]}，体验当地文化风情`
        },
        {
          time: activities[2],
          activity: '当地美食体验',
          description: `品尝${city}地道美食，感受当地夜生活`
        }
      ]
    })
  }

  return {
    city,
    days,
    style,
    styleLabel: styleLabels[style] || style,
    itinerary,
    attractions: itinerary.flatMap(day => 
      day.activities.filter(a => a.activity !== '当地美食体验').map((a, idx) => ({
        id: Date.now() + idx,
        name: a.activity,
        city,
        description: a.description,
        tags: ['AI推荐'],
        image: getImageUrl(a.activity),
        rating: 4.5 + Math.random() * 0.5,
        ticket: Math.floor(Math.random() * 100) + 20
      }))
    ),
    hotels: [
      {
        id: Date.now(),
        name: `${city}精选酒店`,
        city,
        description: `${city}市中心舒适酒店，交通便利`,
        price: Math.floor(Math.random() * 500) + 300,
        rating: 4.5 + Math.random() * 0.4,
        image: `https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=${encodeURIComponent(`${city} luxury hotel`)}&image_size=landscape_4_3`,
        address: `${city}市中心`
      }
    ],
    aiSuggestions: `根据您的需求，为您规划了${days}天的${styleLabels[style]}之旅。建议提前预订热门景点门票，注意天气变化。`
  }
}

export async function generateRouteWithAI(city, days, style, selectedAttractions = [], selectedHotels = []) {
  const styleLabels = {
    sightseeing: '观光游览',
    leisure: '休闲度假',
    food: '美食探索',
    adventure: '探险体验',
    culture: '文化历史'
  }

  const prompt = `请为我规划一个${city}${days}天的${styleLabels[style]}旅行路线。

用户偏好信息：
- 目的地：${city}
- 天数：${days}天
- 旅行风格：${styleLabels[style]}
- 必去景点：${selectedAttractions.length > 0 ? selectedAttractions.join('、') : '无'}
- 偏好酒店：${selectedHotels.length > 0 ? selectedHotels.join('、') : '无'}

请输出JSON格式的旅行路线，包含以下字段：
{
  "city": "${city}",
  "days": ${days},
  "style": "${style}",
  "styleLabel": "${styleLabels[style]}",
  "itinerary": [
    {
      "activities": [
        {"time": "时间段", "activity": "活动名称", "description": "活动描述"},
        {"time": "时间段", "activity": "活动名称", "description": "活动描述"},
        {"time": "时间段", "activity": "活动名称", "description": "活动描述"}
      ]
    }
  ],
  "attractions": [
    {"id": 数字, "name": "景点名称", "city": "${city}", "description": "描述", "tags": ["标签1", "标签2"], "image": "图片URL", "rating": 4.5, "ticket": 门票价格}
  ],
  "hotels": [
    {"id": 数字, "name": "酒店名称", "city": "${city}", "description": "描述", "price": 价格, "rating": 4.5, "image": "图片URL", "address": "地址"}
  ],
  "aiSuggestions": "AI给出的旅行建议"
}

注意：
1. itinerary数组要有${days}个元素，每个元素代表一天的行程
2. 每天要有3个活动（上午、下午、晚上）
3. attractions和hotels数组要有真实的景点和酒店信息
4. 图片URL使用https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=景点名称的英文描述&image_size=landscape_4_3
5. 只输出JSON格式，不要包含其他文字`

  try {
    const response = await fetchDeepSeek(prompt)
    const route = parseRouteFromResponse(response, city, days, style)
    
    if (!route.itinerary || !route.attractions) {
      return generateFallbackRoute(city, days, style)
    }
    
    return route
  } catch (error) {
    console.warn('DeepSeek API call failed, using fallback:', error.message)
    return generateFallbackRoute(city, days, style)
  }
}

export default generateRouteWithAI
