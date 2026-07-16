import request from '@/utils/request'

export function addFavorite(data) {
  return request({
    url: '/favorite',
    method: 'post',
    data
  })
}

export function removeFavorite(userId, targetId, targetType) {
  return request({
    url: '/favorite',
    method: 'delete',
    params: { userId, targetId, targetType }
  })
}

export function getFavorites(userId) {
  return request({
    url: `/favorite/user/${userId}`,
    method: 'get'
  })
}

export function getFavoritesByType(userId, targetType) {
  return request({
    url: `/favorite/user/${userId}/type/${targetType}`,
    method: 'get'
  })
}

export function checkFavorite(userId, targetId, targetType) {
  return request({
    url: '/favorite/check',
    method: 'get',
    params: { userId, targetId, targetType }
  })
}
