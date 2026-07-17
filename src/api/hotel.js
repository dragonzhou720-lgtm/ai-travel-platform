import request from '@/utils/request'

export function searchHotels(city, keyword = '') {
  return request({
    url: '/hotel/search',
    method: 'get',
    params: { city, keyword }
  })
}

export function getHotelDetail(id) {
  return request({
    url: '/hotel/detail',
    method: 'get',
    params: { id }
  })
}
