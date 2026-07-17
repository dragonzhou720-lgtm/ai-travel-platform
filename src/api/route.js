import request from '@/utils/request'

export function generateRoute(data) {
  return request({
    url: '/route/generate',
    method: 'post',
    data
  })
}

export function getRouteById(id) {
  return request({
    url: `/route/${id}`,
    method: 'get'
  })
}

export function getAllRoutes() {
  return request({
    url: '/route',
    method: 'get'
  })
}

export function getHotRoutes(limit = 10) {
  return request({
    url: '/route/hot',
    method: 'get',
    params: { limit }
  })
}

export function getHotRouteCount(city, days, style) {
  return request({
    url: '/route/hot/count',
    method: 'get',
    params: { city, days, style }
  })
}
