import userMock from './user.js'
import attractionMock from './attraction.js'
import hotelMock from './hotel.js'
import routeMock from './route.js'
import favoriteMock from './favorite.js'

export function setupMock() {
  return [
    ...userMock,
    ...attractionMock,
    ...hotelMock,
    ...routeMock,
    ...favoriteMock
  ]
}

export default setupMock()
