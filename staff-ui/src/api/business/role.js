import request from '@/utils/request'
import {parseStrEmpty} from "@/utils/ruoyi";

// 查询角色信息列表
export function listRole(query) {
  return request({
    url: '/business/role/list',
    method: 'get',
    params: query
  })
}

// 查询角色信息详细
export function getRole(hotelId,roleId) {
  return request({
    url: '/business/role/' + hotelId + "/" + parseStrEmpty(roleId),
    method: 'get'
  })
}

// 新增角色信息
export function addRole(data) {
  return request({
    url: '/business/role',
    method: 'post',
    data: data
  })
}

// 修改角色信息
export function updateRole(data) {
  return request({
    url: '/business/role',
    method: 'put',
    data: data
  })
}

// 删除角色信息
export function delRole(roleId) {
  return request({
    url: '/business/role/' + roleId,
    method: 'delete'
  })
}
