import userMock from './user.js'
import attractionMock from './attraction.js'
import hotelMock from './hotel.js'
import routeMock from './route.js'
import favoriteMock from './favorite.js'

const mockList = [
  ...userMock,
  ...attractionMock,
  ...hotelMock,
  ...routeMock,
  ...favoriteMock
]

export function createMockPlugin() {
  return {
    name: 'vite-mock-plugin',
    configureServer(server) {
      server.middlewares.use(async (req, res, next) => {
        const url = new URL(req.url, `http://localhost:5173`)
        const path = url.pathname
        const method = req.method.toLowerCase()
        
        for (const item of mockList) {
          const mockPath = item.url
          const mockMethod = item.method.toLowerCase()
          
          if (mockPath.includes(':') && method === mockMethod) {
            const pathParts = mockPath.split('/').filter(p => p)
            const reqParts = path.split('/').filter(p => p)
            
            if (pathParts.length === reqParts.length) {
              const params = {}
              let match = true
              
              for (let i = 0; i < pathParts.length; i++) {
                if (pathParts[i].startsWith(':')) {
                  params[pathParts[i].slice(1)] = reqParts[i]
                } else if (pathParts[i] !== reqParts[i]) {
                  match = false
                  break
                }
              }
              
              if (match) {
                await handleMockResponse(req, res, item, url, params)
                return
              }
            }
          } else if (mockPath === path && method === mockMethod) {
            await handleMockResponse(req, res, item, url, {})
            return
          }
        }
        
        next()
      })
    }
  }
}

async function handleMockResponse(req, res, item, url, params) {
  res.setHeader('Content-Type', 'application/json')
  
  const query = {}
  url.searchParams.forEach((value, key) => {
    query[key] = value
  })
  
  const headers = {}
  for (const [key, value] of Object.entries(req.headers)) {
    headers[key] = value
  }
  
  if (req.method === 'POST' || req.method === 'PUT' || req.method === 'DELETE') {
    await new Promise((resolve) => {
      let data = ''
      req.on('data', chunk => data += chunk)
      req.on('end', async () => {
        try {
          const body = JSON.parse(data)
          const result = await item.response({ query, params, body, headers })
          res.end(JSON.stringify(result))
        } catch {
          const result = await item.response({ query, params, body: {}, headers })
          res.end(JSON.stringify(result))
        }
        resolve()
      })
    })
  } else {
    const result = await item.response({ query, params, body: {}, headers })
    res.end(JSON.stringify(result))
  }
}
