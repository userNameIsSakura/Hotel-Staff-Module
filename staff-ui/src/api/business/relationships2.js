import request from '@/utils/request'

// 查询订阅黑名单列表
export function listRelationships2(query) {
  return request({
    url: '/business/relationships2/list',
    method: 'get',
    params: query
  })
}


// 查询全部订阅
export function listSubscribeAll() {
  return request({
    url: '/business/subscribe/listAll',
    method: 'get',
  })
}

// 查询订阅黑名单详细
export function getRelationships2(id) {
  return request({
    url: '/business/relationships2/' + id,
    method: 'get'
  })
}

// 新增订阅黑名单
export function addRelationships2(data) {
  return request({
    url: '/business/relationships2',
    method: 'post',
    data: data
  })
}

// 修改订阅黑名单
export function updateRelationships2(data) {
  return request({
    url: '/business/relationships2',
    method: 'put',
    data: data
  })
}

// 删除订阅黑名单
export function delRelationships2(id) {
  return request({
    url: '/business/relationships2/' + id,
    method: 'delete'
  })
}
