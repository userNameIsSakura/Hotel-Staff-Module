import request from '@/utils/request'

// 查询权限信息列表
export function validation() {
  return request({
    url: '/business/aosMember/validation',
    method: 'get',
  })


}



// 加入会员系统
export function memberRegister() {
  return request({
    url: '/business/aosMember/memberRegister',
    method: 'get',
  })
}
