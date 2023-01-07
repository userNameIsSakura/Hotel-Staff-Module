import request from '@/utils/request'

// 查询权限信息列表
export function listAuth(query) {
  return request({
    url: '/business/auth/list',
    method: 'get',
    params: query
  })
}

// 查询权限信息详细
export function getAuth(authId) {
  return request({
    url: '/business/auth/' + authId,
    method: 'get'
  })
}

// 新增权限信息
export function addAuth(data) {
  return request({
    url: '/business/auth',
    method: 'post',
    data: data
  })
}

// 修改权限信息
export function updateAuth(data) {
  return request({
    url: '/business/auth',
    method: 'put',
    data: data
  })
}

// 删除权限信息
export function delAuth(authId) {
  return request({
    url: '/business/auth/' + authId,
    method: 'delete'
  })
}
