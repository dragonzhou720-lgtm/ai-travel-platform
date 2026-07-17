import request from '@/utils/request'

export function searchAttractions(city, keyword = '') {
  return request({
    url: '/attraction/search',
    method: 'get',
    params: { city, keyword }
  })
}

export function getAttractionDetail(id) {
  return request({
    url: '/attraction/detail',
    method: 'get',
    params: { id }
  })
}
