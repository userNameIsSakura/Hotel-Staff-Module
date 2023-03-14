import request from '@/utils/request'

// 查询介绍图类别列表
export function listDiagramType(query) {
  return request({
    url: '/business/diagramType/list',
    method: 'get',
    params: query
  })
}

// 查询介绍图类别详细
export function getDiagramType(diagramType) {
  return request({
    url: '/business/diagramType/' + diagramType,
    method: 'get'
  })
}

// 新增介绍图类别
export function addDiagramType(data) {
  return request({
    url: '/business/diagramType',
    method: 'post',
    data: data
  })
}

// 修改介绍图类别
export function updateDiagramType(data) {
  return request({
    url: '/business/diagramType',
    method: 'put',
    data: data
  })
}

// 删除介绍图类别
export function delDiagramType(diagramType) {
  return request({
    url: '/business/diagramType/' + diagramType,
    method: 'delete'
  })
}
