import request from "@/utils/request";

export function getModel(id) {

  return request({
    url:"/hotel/pdf",
    method: "post",
    data: id,
    responseType: 'blob'
  });
}

export function getContract(id) {

  return request({
    url:"/business/contract/historyContract",
    method: "post",
    data: id,
    responseType: 'blob'
  });
}


export function formulate(modelId) {
  return request({
    url:"/hotel/formulate",
    method: "post",
    data: modelId,
  })
}
