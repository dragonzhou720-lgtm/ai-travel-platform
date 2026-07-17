import request from '@/utils/request'

export function login(username, password) {
  return request({
    url: '/user/login',
    method: 'post',
    data: { username, password }
  })
}

export function register(username, password) {
  return request({
    url: '/user/register',
    method: 'post',
    data: { username, password }
  })
}

export function getUserInfo() {
  return request({
    url: '/user/info',
    method: 'get'
  })
}

export function getUserByUsername(username) {
  return request({
    url: `/user/${username}`,
    method: 'get'
  })
}
