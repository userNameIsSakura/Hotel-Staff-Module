import request from '@/utils/request'

// 查询合同记录列表
export function listContract(query) {
  return request({
    url: '/business/contract/list',
    method: 'get',
    params: query
  })
}

export function lookContract(contractId) {
  return request({
    url: '/business/contract/historyContract',
    method: 'post',
    data: contractId
  });
}


