function getImageUrl(name, size = 'landscape_4_3') {
  const map = {
    '北京王府井大饭店': 'Beijing luxury hotel interior downtown',
    '北京国贸大酒店': 'Beijing CBD luxury hotel skyline view',
    '北京四季酒店': 'Four Seasons Hotel Beijing luxury elegant',
    '北京希尔顿酒店': 'Hilton Hotel Beijing modern comfortable',
    '上海外滩茂悦大酒店': 'Shanghai luxury hotel Bund river view',
    '上海浦东丽思卡尔顿': 'Ritz Carlton Shanghai Pudong luxury',
    '上海迪士尼乐园酒店': 'Disneyland Hotel Shanghai castle magical',
    '杭州西湖国宾馆': 'Hangzhou hotel West Lake view garden',
    '杭州西溪悦榕庄': 'Banyan Tree Hangzhou resort nature',
    '成都太古里博舍酒店': 'Chengdu luxury boutique hotel stylish',
    '成都香格里拉大酒店': 'Shangri-La Hotel Chengdu elegant',
    '西安钟楼希尔顿酒店': 'Hilton Hotel Xian bell tower view',
    '西安威斯汀大酒店': 'Westin Hotel Xian cultural modern',
    '三亚亚龙湾丽思卡尔顿': 'Ritz Carlton Sanya Yalong Bay beach',
    '三亚海棠湾免税店酒店': 'Sanya duty free hotel beach resort'
  }
  const prompt = map[name] || 'luxury hotel interior modern'
  return `https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=${encodeURIComponent(prompt)}&image_size=${size}`
}

const hotelsData = {
  '北京': [
    { id: 1, name: '北京王府井大饭店', city: '北京', description: '位于王府井商圈中心，交通便利，步行可达故宫天安门。', price: 580, rating: 4.6, image: getImageUrl('北京王府井大饭店'), address: '东城区王府井大街' },
    { id: 2, name: '北京国贸大酒店', city: '北京', description: 'CBD核心区域，俯瞰城市美景，五星级商务酒店。', price: 880, rating: 4.8, image: getImageUrl('北京国贸大酒店'), address: '朝阳区建国门外大街' },
    { id: 3, name: '北京四季酒店', city: '北京', description: '五星级奢华体验，卓越服务品质，设施一流。', price: 1280, rating: 4.9, image: getImageUrl('北京四季酒店'), address: '朝阳区亮马桥路' },
    { id: 4, name: '北京希尔顿酒店', city: '北京', description: '国际知名品牌，舒适入住体验，商务休闲两相宜。', price: 680, rating: 4.5, image: getImageUrl('北京希尔顿酒店'), address: '朝阳区东三环北路' }
  ],
  '上海': [
    { id: 11, name: '上海外滩茂悦大酒店', city: '上海', description: '坐拥外滩和陆家嘴全景，尽享魔都繁华。', price: 980, rating: 4.8, image: getImageUrl('上海外滩茂悦大酒店'), address: '黄浦路199号' },
    { id: 12, name: '上海浦东丽思卡尔顿', city: '上海', description: '奢华五星级酒店，顶级服务体验。', price: 1680, rating: 4.9, image: getImageUrl('上海浦东丽思卡尔顿'), address: '浦东新区世纪大道' },
    { id: 13, name: '上海迪士尼乐园酒店', city: '上海', description: '梦幻主题酒店，亲子出行首选。', price: 1280, rating: 4.7, image: getImageUrl('上海迪士尼乐园酒店'), address: '浦东新区川沙镇' }
  ],
  '杭州': [
    { id: 21, name: '杭州西湖国宾馆', city: '杭州', description: '坐拥西湖美景，古典园林式酒店。', price: 880, rating: 4.8, image: getImageUrl('杭州西湖国宾馆'), address: '西湖区杨公堤' },
    { id: 22, name: '杭州西溪悦榕庄', city: '杭州', description: '西溪湿地旁的奢华度假酒店，环境优美。', price: 1580, rating: 4.9, image: getImageUrl('杭州西溪悦榕庄'), address: '西湖区紫金港路' }
  ],
  '成都': [
    { id: 31, name: '成都太古里博舍酒店', city: '成都', description: '位于太古里商圈，设计型奢华酒店。', price: 980, rating: 4.8, image: getImageUrl('成都太古里博舍酒店'), address: '锦江区中纱帽街' },
    { id: 32, name: '成都香格里拉大酒店', city: '成都', description: '锦江畔的五星级酒店，服务一流。', price: 680, rating: 4.6, image: getImageUrl('成都香格里拉大酒店'), address: '锦江区滨江东路' }
  ],
  '西安': [
    { id: 41, name: '西安钟楼希尔顿酒店', city: '西安', description: '紧邻钟楼鼓楼，古城中心位置。', price: 580, rating: 4.6, image: getImageUrl('西安钟楼希尔顿酒店'), address: '碑林区南大街' },
    { id: 42, name: '西安威斯汀大酒店', city: '西安', description: '曲江新区，近大雁塔，文化主题酒店。', price: 780, rating: 4.7, image: getImageUrl('西安威斯汀大酒店'), address: '曲江新区慈恩路' }
  ],
  '三亚': [
    { id: 51, name: '三亚亚龙湾丽思卡尔顿', city: '三亚', description: '亚龙湾海滩旁的奢华度假酒店。', price: 1880, rating: 4.9, image: getImageUrl('三亚亚龙湾丽思卡尔顿'), address: '亚龙湾国家旅游度假区' },
    { id: 52, name: '三亚海棠湾免税店酒店', city: '三亚', description: '海棠湾畔，购物免税，度假便利。', price: 980, rating: 4.7, image: getImageUrl('三亚海棠湾免税店酒店'), address: '海棠湾海棠北路' }
  ]
}

const allHotels = Object.values(hotelsData).flat()

export { hotelsData, allHotels }

export default [
  {
    url: '/api/hotel/search',
    method: 'get',
    response: ({ query }) => {
      const { city, keyword } = query
      let list = hotelsData[city] || []
      if (keyword) {
        list = list.filter(h => h.name.includes(keyword) || h.description.includes(keyword))
      }
      return list
    }
  },
  {
    url: '/api/hotel/detail',
    method: 'get',
    response: ({ query }) => {
      const id = Number(query.id)
      return allHotels.find(h => h.id === id) || null
    }
  }
]
