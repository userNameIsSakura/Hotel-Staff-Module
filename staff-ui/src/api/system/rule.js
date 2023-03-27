import memberRequest from "@/utils/memberRequest";
import request from "@/utils/request";
import axios from "axios";


// 查询优惠规则列表
export async function listRule(query) {
  var chotelId;
  await request({
    url: '/business/chainHotel',
    method: 'get'
  }).then(res => {
    chotelId = res;
  });

  query.chotelId = chotelId;

  return memberRequest({
    url: '/system/rule/list',
    method: 'post',
    data: query
  })
}


// 查询优惠规则列表
export function listSignRule() {
  return memberRequest({
    url: '/system/rule/listSign',
    method: 'post',
  })
}

export async function issue(query) {
  var chotelId;
  await request({
    url: '/business/chainHotel',
    method: 'get'
  }).then(res => {
    chotelId = res;
  });

  query.chotelId = chotelId;

  return memberRequest({
    url: '/system/coupon/issue',
    method: 'post',
    data: query
  })
}

export async function signRule(query) {
  var chotelId;
  await request({
    url: '/business/chainHotel',
    method: 'get'
  }).then(res => {
    chotelId = res;
  });

  query.chotelId = chotelId;

  return memberRequest({
    url: '/system/rule/sign',
    method: 'post',
    data: query
  })
}





// 查询优惠规则详细
export function getRule(ruleId) {
  return memberRequest({
    url: '/system/rule/' + ruleId,
    method: 'get'
  })
}

// 新增优惠规则
export async function addRule(data) {
  var chotelId;
  await request({
    url: '/business/chainHotel',
    method: 'get'
  }).then(res => {
    chotelId = res;
  });
  data.chotelId = chotelId;

  return memberRequest({
    url: '/system/rule',
    method: 'post',
    data: data
  })
}

// 修改优惠规则
export async function updateRule(data) {
  var chotelId;
  await request({
    url: '/business/chainHotel',
    method: 'get'
  }).then(res => {
    chotelId = res;
  });

  data.chotelId = chotelId;

  return memberRequest({
    url: '/system/rule',
    method: 'put',
    data: data
  })
}

// 删除优惠规则
export async function delRule(ruleId) {
  var chotelId;
  await request({
    url: '/business/chainHotel',
    method: 'get'
  }).then(res => {
    chotelId = res;
  });

  return memberRequest({
    url: '/system/rule/' + ruleId + "/" + chotelId,
    method: 'delete'
  })
}
