import request from '@/utils/request'
import {parseStrEmpty} from "@/utils/ruoyi";

// 查询员工信息列表
export function listStaff(query) {




  return request({
    url: '/business/staff/list',
    method: 'get',
    params: query
  })
}



// 查询员工信息详细
export function getStaff(hotelId,staffId) {
  return request({
    url: '/business/staff/' + hotelId + "/" + parseStrEmpty(staffId),
    method: 'get'
  })
}

// 新增员工信息
export function addStaff(data) {
  return request({
    url: '/business/staff',
    method: 'post',
    data: data
  })
}

// 修改员工信息
export function updateStaff(data) {
  return request({
    url: '/business/staff',
    method: 'put',
    data: data
  })
}

// 修改员工密码
export function updateStaffPassword(data) {
  return request({
    url: '/business/staff/reset',
    method: 'put',
    data: data
  })
}

// 删除员工信息
export function delStaff(staffId) {
  return request({
    url: '/business/staff/' + staffId,
    method: 'delete'
  })
}
