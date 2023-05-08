import request from "@/utils/request";


// 查询优惠规则列表
export function listRule(query) {

  return request({
    url: '/business/rule/list',
    method: 'post',
    data: query
  })


}


// 查询优惠规则列表
export function listSignRule() {
  return request({
    url: '/business/rule/listSign',
    method: 'post',
  })
}

export  function issue(query) {

  return request({
    url: '/business/rule/issue',
    method: 'post',
    data: query
  })
}

export  function signRule(query) {

  return request({
    url: '/business/rule/sign',
    method: 'post',
    data: query
  })
}

// 查询优惠规则详细
export function getRule(ruleId) {
  return request({
    url: '/business/rule/' + ruleId,
    method: 'get'
  })
}

// 新增优惠规则
export  function addRule(data) {
  return request({
    url: '/business/rule/addRule',
    method: 'post',
    data: data
  })
}

// 修改优惠规则
export  function updateRule(data) {

  return request({
    url: '/business/rule/updateRule',
    method: 'post',
    data: data
  })
}

// 删除优惠规则
export  function delRule(ruleId) {
  return request({
    url: '/business/rule/delete/' + ruleId,
    method: 'get'
  })
}
