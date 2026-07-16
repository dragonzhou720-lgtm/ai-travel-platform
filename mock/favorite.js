// 收藏相关mock数据

// 存储收藏数据
let favoriteIdCounter = 1
const favorites = [
  { id: 1, userId: 1, targetId: 1, targetType: 'route', createdAt: Date.now() - 86400000 },
  { id: 2, userId: 1, targetId: 2, targetType: 'route', createdAt: Date.now() - 172800000 },
  { id: 3, userId: 1, targetId: 1, targetType: 'attraction', createdAt: Date.now() - 259200000 },
  { id: 4, userId: 1, targetId: 2, targetType: 'attraction', createdAt: Date.now() - 345600000 },
  { id: 5, userId: 1, targetId: 1, targetType: 'hotel', createdAt: Date.now() - 432000000 }
]

export default [
  // 添加收藏
  {
    url: '/api/favorites',
    method: 'post',
    response: ({ body }) => {
      const { userId, targetId, targetType } = body
      const existing = favorites.find(
        f => f.userId === userId && f.targetId === targetId && f.targetType === targetType
      )
      if (existing) {
        return existing
      }
      const newFavorite = {
        id: ++favoriteIdCounter,
        userId,
        targetId,
        targetType,
        createdAt: Date.now()
      }
      favorites.push(newFavorite)
      return newFavorite
    }
  },
  // 删除收藏
  {
    url: '/api/favorites',
    method: 'delete',
    response: ({ query }) => {
      const userId = Number(query.userId)
      const targetId = Number(query.targetId)
      const targetType = query.targetType
      const index = favorites.findIndex(
        f => f.userId === userId && f.targetId === targetId && f.targetType === targetType
      )
      if (index > -1) {
        favorites.splice(index, 1)
      }
      return null
    }
  },
  // 检查是否已收藏
  {
    url: '/api/favorites/check',
    method: 'get',
    response: ({ query }) => {
      const userId = Number(query.userId)
      const targetId = Number(query.targetId)
      const targetType = query.targetType
      const isFavorite = favorites.some(
        f => f.userId === userId && f.targetId === targetId && f.targetType === targetType
      )
      return { isFavorite }
    }
  },
  // 按类型获取用户收藏（更具体的路径放前面）
  {
    url: '/api/favorites/user/:userId/type/:targetType',
    method: 'get',
    response: ({ params }) => {
      const userId = Number(params.userId)
      const targetType = params.targetType
      return favorites.filter(f => f.userId === userId && f.targetType === targetType)
    }
  },
  // 获取用户所有收藏
  {
    url: '/api/favorites/user/:userId',
    method: 'get',
    response: ({ params }) => {
      const userId = Number(params.userId)
      return favorites.filter(f => f.userId === userId)
    }
  }
]
