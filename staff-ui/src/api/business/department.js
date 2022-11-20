import request from '@/utils/request'

// 查询部门信息列表
export function listDepartment(query) {
  return request({
    url: '/business/department/list',
    method: 'get',
    params: query
  })
}

// 查询部门信息详细
export function getDepartment(departmentId) {
  return request({
    url: '/business/department/' + departmentId,
    method: 'get'
  })
}

// 新增部门信息
export function addDepartment(data) {
  return request({
    url: '/business/department',
    method: 'post',
    data: data
  })
}

// 修改部门信息
export function updateDepartment(data) {
  return request({
    url: '/business/department',
    method: 'put',
    data: data
  })
}

// 删除部门信息
export function delDepartment(departmentId) {
  return request({
    url: '/business/department/' + departmentId,
    method: 'delete'
  })
}
