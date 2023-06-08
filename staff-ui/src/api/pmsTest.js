import axios from "axios";

export function test() {

  var axiosInstance = axios.create({});

  return axiosInstance({
    url: 'http://127.0.0.1:8090/business/subscribe/pmsRequest',
    header: {
      isToken : false
    },
    method: 'post',
    data: {
      "command":"Room_Building_PageList"
    }
  })

}
