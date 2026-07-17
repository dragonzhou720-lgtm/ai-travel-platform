import request from '@/utils/request'

export function addFavorite(data) {
  return request({
    url: '/favorites',
    method: 'post',
    data
  })
}

export function removeFavorite(userId, targetId, targetType) {
  return request({
    url: '/favorites',
    method: 'delete',
    params: { userId, targetId, targetType }
  })
}

export function getFavorites(userId) {
  return request({
    url: `/favorites/user/${userId}`,
    method: 'get'
  })
}

export function getFavoritesByType(userId, targetType) {
  return request({
    url: `/favorites/user/${userId}/type/${targetType}`,
    method: 'get'
  })
}

export function checkFavorite(userId, targetId, targetType) {
  return request({
    url: '/favorites/check',
    method: 'get',
    params: { userId, targetId, targetType }
  })
}
