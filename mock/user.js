// 用户相关mock数据

// 模拟用户数据库
const users = [
  { id: 1, username: 'admin', password: '123456' },
  { id: 2, username: 'test', password: '123456' }
]

// 简单JWT生成（仅用于mock）
function createMockToken(userId, username) {
  const header = Buffer.from(JSON.stringify({ alg: 'HS256', typ: 'JWT' })).toString('base64')
  const payload = Buffer.from(JSON.stringify({
    userId,
    sub: username,
    exp: Date.now() + 86400000
  })).toString('base64')
  return `${header}.${payload}.mock-signature`
}

export default [
  // 用户注册
  {
    url: '/api/user/register',
    method: 'post',
    response: ({ body }) => {
      const { username, password } = body
      if (!username || !password) {
        return { success: false, message: '用户名或密码不能为空' }
      }
      if (users.find(u => u.username === username)) {
        return { success: false, message: '用户名已存在' }
      }
      const newId = users.length + 1
      users.push({ id: newId, username, password })
      return { success: true, message: '注册成功' }
    }
  },
  // 用户登录
  {
    url: '/api/user/login',
    method: 'post',
    response: ({ body }) => {
      const { username, password } = body
      const user = users.find(u => u.username === username && u.password === password)
      if (!user) {
        return { token: '', tokenType: '', expiresIn: 0 }
      }
      return {
        token: createMockToken(user.id, user.username),
        tokenType: 'Bearer',
        expiresIn: 86400
      }
    }
  },
  // 获取用户信息
  {
    url: '/api/user/info',
    method: 'get',
    response: ({ headers }) => {
      const auth = headers.authorization || ''
      if (!auth.startsWith('Bearer ')) {
        return { username: '', roles: [] }
      }
      try {
        const payload = JSON.parse(Buffer.from(auth.split(' ')[1].split('.')[1], 'base64').toString())
        return {
          username: payload.sub,
          roles: [{ authority: 'ROLE_USER' }]
        }
      } catch {
        return { username: 'guest', roles: [] }
      }
    }
  },
  // 根据用户名获取用户
  {
    url: '/api/user/:username',
    method: 'get',
    response: ({ params }) => {
      return { username: params.username }
    }
  }
]
