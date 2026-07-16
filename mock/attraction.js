function getImageUrl(name, size = 'landscape_4_3') {
  const map = {
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
    '秦始皇兵马俑': 'Terracotta Army Xian ancient warriors',
    '大雁塔': 'Big Wild Goose Pagoda Xian Buddhist tower',
    '回民街': 'Muslim Quarter Xian street food market',
    '亚龙湾': 'Sanya Yalong Bay beach tropical sea',
    '天涯海角': 'Tianya Haijiao Sanya scenic ocean',
    '南山寺': 'Nanshan Temple Sanya Buddhist temple',
    '北京王府井大饭店': 'Beijing luxury hotel interior',
    '北京国贸大酒店': 'Beijing CBD luxury hotel',
    '北京四季酒店': 'Four Seasons Hotel Beijing luxury',
    '北京希尔顿酒店': 'Hilton Hotel Beijing modern',
    '上海外滩茂悦大酒店': 'Shanghai luxury hotel river view',
    '上海浦东丽思卡尔顿': 'Ritz Carlton Shanghai luxury',
    '上海迪士尼乐园酒店': 'Disneyland Hotel Shanghai castle',
    '杭州西湖国宾馆': 'Hangzhou lake view hotel',
    '杭州西溪悦榕庄': 'Banyan Tree Hangzhou resort',
    '成都太古里博舍酒店': 'Chengdu luxury boutique hotel',
    '成都香格里拉大酒店': 'Shangri-La Hotel Chengdu',
    '西安钟楼希尔顿酒店': 'Hilton Hotel Xian',
    '西安威斯汀大酒店': 'Westin Hotel Xian',
    '三亚亚龙湾丽思卡尔顿': 'Ritz Carlton Sanya beach resort',
    '三亚海棠湾免税店酒店': 'Sanya duty free hotel beach'
  }
  const prompt = map[name] || 'travel destination beautiful scenery'
  return `https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=${encodeURIComponent(prompt)}&image_size=${size}`
}

const attractionsData = {
  '北京': [
    { id: 1, name: '故宫博物院', city: '北京', description: '中国明清两代的皇家宫殿，世界文化遗产，馆藏珍品超过180万件。', tags: ['历史', '文化', '5A景区'], image: getImageUrl('故宫博物院'), rating: 4.8, ticket: 60 },
    { id: 2, name: '八达岭长城', city: '北京', description: '万里长城的重要组成部分，世界文化遗产，雄伟壮观。', tags: ['历史', '自然', '5A景区'], image: getImageUrl('八达岭长城'), rating: 4.7, ticket: 40 },
    { id: 3, name: '颐和园', city: '北京', description: '中国现存规模最大、保存最完整的皇家园林，世界文化遗产。', tags: ['园林', '文化', '5A景区'], image: getImageUrl('颐和园'), rating: 4.6, ticket: 30 },
    { id: 4, name: '天坛公园', city: '北京', description: '明清两代皇帝祭天祈谷的场所，世界文化遗产。', tags: ['历史', '文化'], image: getImageUrl('天坛公园'), rating: 4.5, ticket: 15 },
    { id: 5, name: '鸟巢', city: '北京', description: '2008年北京奥运会主体育场，现代建筑奇迹。', tags: ['现代', '地标'], image: getImageUrl('鸟巢'), rating: 4.4, ticket: 50 }
  ],
  '上海': [
    { id: 11, name: '外滩', city: '上海', description: '上海标志性景点，黄浦江畔的历史建筑群，夜景绝美。', tags: ['地标', '夜景', '免费'], image: getImageUrl('外滩'), rating: 4.7, ticket: 0 },
    { id: 12, name: '东方明珠塔', city: '上海', description: '上海地标性建筑，俯瞰城市全景的绝佳地点。', tags: ['地标', '观光'], image: getImageUrl('东方明珠塔'), rating: 4.6, ticket: 120 },
    { id: 13, name: '迪士尼乐园', city: '上海', description: '中国大陆首座迪士尼主题乐园，梦幻游乐体验。', tags: ['主题乐园', '亲子'], image: getImageUrl('迪士尼乐园'), rating: 4.8, ticket: 399 },
    { id: 14, name: '豫园', city: '上海', description: '上海著名古典园林，江南园林艺术的杰出代表。', tags: ['园林', '文化'], image: getImageUrl('豫园'), rating: 4.4, ticket: 40 }
  ],
  '杭州': [
    { id: 21, name: '西湖', city: '杭州', description: '杭州最著名的景点，世界文化遗产，淡妆浓抹总相宜。', tags: ['自然', '文化', '免费'], image: getImageUrl('西湖'), rating: 4.8, ticket: 0 },
    { id: 22, name: '灵隐寺', city: '杭州', description: '千年古刹，杭州最早的名刹，香火鼎盛。', tags: ['寺庙', '文化'], image: getImageUrl('灵隐寺'), rating: 4.6, ticket: 30 },
    { id: 23, name: '千岛湖', city: '杭州', description: '人工湖泊中的经典，山水秀丽，空气清新。', tags: ['自然', '湖泊'], image: getImageUrl('千岛湖'), rating: 4.5, ticket: 130 }
  ],
  '成都': [
    { id: 31, name: '大熊猫繁育研究基地', city: '成都', description: '近距离观赏国宝大熊猫，了解熊猫保护工作。', tags: ['动物', '亲子'], image: getImageUrl('大熊猫繁育研究基地'), rating: 4.7, ticket: 55 },
    { id: 32, name: '武侯祠', city: '成都', description: '纪念诸葛亮的祠堂，三国文化圣地。', tags: ['历史', '文化'], image: getImageUrl('武侯祠'), rating: 4.5, ticket: 50 },
    { id: 33, name: '锦里古街', city: '成都', description: '成都著名历史文化街区，品尝地道成都小吃。', tags: ['美食', '文化', '免费'], image: getImageUrl('锦里古街'), rating: 4.4, ticket: 0 }
  ],
  '西安': [
    { id: 41, name: '秦始皇兵马俑', city: '西安', description: '世界第八大奇迹，秦始皇陵的陪葬坑。', tags: ['历史', '文化', '5A景区'], image: getImageUrl('秦始皇兵马俑'), rating: 4.8, ticket: 120 },
    { id: 42, name: '大雁塔', city: '西安', description: '唐代著名的佛教建筑，西安标志性建筑之一。', tags: ['历史', '文化'], image: getImageUrl('大雁塔'), rating: 4.5, ticket: 25 },
    { id: 43, name: '回民街', city: '西安', description: '西安著名的美食文化街区，汇集各种西北美食。', tags: ['美食', '文化', '免费'], image: getImageUrl('回民街'), rating: 4.3, ticket: 0 }
  ],
  '三亚': [
    { id: 51, name: '亚龙湾', city: '三亚', description: '中国最美的海湾之一，碧海蓝天，沙滩洁白。', tags: ['海滩', '自然', '免费'], image: getImageUrl('亚龙湾'), rating: 4.7, ticket: 0 },
    { id: 52, name: '天涯海角', city: '三亚', description: '三亚经典景点，天涯石、海角石闻名遐迩。', tags: ['自然', '地标'], image: getImageUrl('天涯海角'), rating: 4.3, ticket: 81 },
    { id: 53, name: '南山寺', city: '三亚', description: '中国最大的佛教文化主题园区，108米海上观音像。', tags: ['寺庙', '文化'], image: getImageUrl('南山寺'), rating: 4.6, ticket: 122 }
  ]
}

const allAttractions = Object.values(attractionsData).flat()

export { attractionsData, allAttractions }

export default [
  {
    url: '/api/attraction/search',
    method: 'get',
    response: ({ query }) => {
      const { city, keyword } = query
      let list = attractionsData[city] || []
      if (keyword) {
        list = list.filter(a => a.name.includes(keyword) || a.description.includes(keyword))
      }
      return list
    }
  },
  {
    url: '/api/attraction/detail',
    method: 'get',
    response: ({ query }) => {
      const id = Number(query.id)
      return allAttractions.find(a => a.id === id) || null
    }
  }
]
