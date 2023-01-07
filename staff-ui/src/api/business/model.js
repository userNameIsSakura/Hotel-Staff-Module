import request from '@/utils/request'

// 查询范本列表列表
export function listModel(query) {
  return request({
    url: '/business/model/list',
    method: 'get',
    params: query
  })
}

// 查询范本列表详细
export function getModel(modelId) {
  return request({
    url: '/business/model/' + modelId,
    method: 'get'
  })
}

// 新增范本列表
export function addModel(data) {
  return request({
    url: '/business/model',
    method: 'post',
    data: data
  })
}

// 修改范本列表
export function updateModel(data) {
  return request({
    url: '/business/model',
    method: 'put',
    data: data
  })
}

// 删除范本列表
export function delModel(modelId) {
  return request({
    url: '/business/model/' + modelId,
    method: 'delete'
  })
}
