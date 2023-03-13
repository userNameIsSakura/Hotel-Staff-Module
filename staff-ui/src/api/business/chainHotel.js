import request from '@/utils/request'

// 查询连锁酒店列表
export function listChainHotel(query) {
  return request({
    url: '/business/chainHotel/list',
    method: 'get',
    params: query
  })
}

// 查询连锁酒店详细
export function getChainHotel(chotelId) {
  return request({
    url: '/business/chainHotel/' + chotelId,
    method: 'get'
  })
}

// 新增连锁酒店
export function addChainHotel(data) {
  return request({
    url: '/business/chainHotel',
    method: 'post',
    data: data
  })
}

// 修改连锁酒店
export function updateChainHotel(data) {
  return request({
    url: '/business/chainHotel',
    method: 'put',
    data: data
  })
}

// 删除连锁酒店
export function delChainHotel(chotelId) {
  return request({
    url: '/business/chainHotel/' + chotelId,
    method: 'delete'
  })
}
