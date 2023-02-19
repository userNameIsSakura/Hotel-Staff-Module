import request from '@/utils/request'

// 查询订阅信息列表
export function listSubscribe(query) {
  return request({
    url: '/business/subscribe/list',
    method: 'get',
    params: query
  })
}

// 查询订阅信息详细
export function getSubscribe(subscribeId) {
  return request({
    url: '/business/subscribe/' + subscribeId,
    method: 'get'
  })
}

// 新增订阅信息
export function addSubscribe(data) {
  return request({
    url: '/business/subscribe',
    method: 'post',
    data: data
  })
}

// 修改订阅信息
export function updateSubscribe(data) {
  return request({
    url: '/business/subscribe',
    method: 'put',
    data: data
  })
}

// 删除订阅信息
export function delSubscribe(subscribeId) {
  return request({
    url: '/business/subscribe/' + subscribeId,
    method: 'delete'
  })
}
