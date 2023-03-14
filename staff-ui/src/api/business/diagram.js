import request from '@/utils/request'

// 查询酒店介绍图列表
export function listDiagram(query) {
  return request({
    url: '/business/diagram/list',
    method: 'get',
    params: query
  })
}

// 查询酒店介绍图详细
export function getDiagram(id) {
  return request({
    url: '/business/diagram/' + id,
    method: 'get'
  })
}

// 新增酒店介绍图
export function addDiagram(data) {
  return request({
    url: '/business/diagram',
    method: 'post',
    data: data
  })
}

// 修改酒店介绍图
export function updateDiagram(data) {
  return request({
    url: '/business/diagram',
    method: 'put',
    data: data
  })
}

// 删除酒店介绍图
export function delDiagram(id) {
  return request({
    url: '/business/diagram/' + id,
    method: 'delete'
  })
}
