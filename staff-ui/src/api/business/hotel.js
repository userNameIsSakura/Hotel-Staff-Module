import request from '@/utils/request'

// 查询酒店列表列表
export function listHotel(query) {
  return request({
    url: '/business/hotel/list',
    method: 'get',
    params: query
  })
}
// 查询全部酒店
export function listHotelAll() {
  return request({
    url: '/business/hotel/listAll',
    method: 'get',
  })
}

// 查询酒店列表详细
export function getHotel(hotelId) {
  return request({
    url: '/business/hotel/' + hotelId,
    method: 'get'
  })
}

// 查询酒店列表详细
export function getHotelByCHotelId(chotelId) {
  return request({
    url: '/business/hotel/chotelId/' + chotelId,
    method: 'get',
  })
}

// 新增酒店列表
export function addHotel(data) {
  return request({
    url: '/business/hotel',
    method: 'post',
    data: data
  })
}

// 修改酒店列表
export function updateHotel(data) {
  return request({
    url: '/business/hotel',
    method: 'put',
    data: data
  })
}

// 删除酒店列表
export function delHotel(hotelId) {
  return request({
    url: '/business/hotel/' + hotelId,
    method: 'delete'
  })

}
