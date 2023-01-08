<template>
  <div class="pdf_wrap">

    <template>

      <el-form ref="form" label-width="80px">
        <div style='text-align: center;margin: 30px;' v-if="loading">
          数据加载中...
        </div>

        <div class="operation" v-if="loading==false" style="display: flex;align-items: center;">
          <div style="flex: 1;"></div>
          <el-button size="mini" @click="changePdfPage(0)" type="primary">上一页</el-button>
          <div style="position: relative; margin: 0px 10px; top: -10px;">
            {{currentPage}} / {{pageCount}}  共 {{numberPage}} 页
          </div>
          <el-button size="mini" @click="changePdfPage(1)" type="primary">下一页</el-button>
          <el-button size="mini" @click='print' type="primary">打印</el-button>
          <el-button class="signed" v-if="signed" size="mini" @click='confirm()' type="primary">签订</el-button>
        </div>

      </el-form>
      <div class="pdf">
        <pdf v-if="showPdf" ref="pdf" :src="pdfUrl" :page="currentPage" @num-pages="pageCount=$event"
             @page-loaded="currentPage=$event" @loaded="loadPdfHandler">
        </pdf>
      </div>

    </template>


  </div>
</template>

<script>

import pdf from 'vue-pdf-signature'
import {formulate, getContract, getModel, getPDF} from "@/api/business/hotelService";
import CMapReaderFactory from 'vue-pdf/src/CMapReaderFactory.js'
export default {
  components: {
    pdf
  },
  data () {
    return {
      loading: true,
      showPdf: false,
      currentPage: 1, // pdf文件页码
      pageCount: 1, // pdf文件总页数
      fileType: 'pdf', // 文件类型
      pdfUrl: '',
      numberPage:null,
      open: true,
      pdfId: null,
      signed: false
    }
  },
  mounted () {
    this.showPdf = true;

    if(this.$route.query.modelId !== undefined) {
      this.signed = true;
      this.pdfId = this.$route.query.modelId;
      getModel(this.pdfId).then(res=>{
        console.log(res)
        this.pdfUrl = this.getObjectURL(res)
        console.log(this.pdfUrl)
        const loadingTask = pdf.createLoadingTask({url:this.pdfUrl,CMapReaderFactory})
        // // 注意这里一定要这样写
        loadingTask.promise.then(load => {
          this.numberPage = load.numPages
        }).catch(err => {
          console.log(err)
        });
        this.$refs.pdf.$el.style.width = "120%";
        this.loading = false;
      })
    }else if(this.$route.query.contractId !== undefined) {
      this.pdfId = this.$route.query.contractId;
      getContract(this.pdfId).then(res=>{
        this.pdfUrl = pdf.createLoadingTask({url:this.getObjectURL(res),CMapReaderFactory})
        // // 注意这里一定要这样写
        loadingTask.promise.then(load => {
          this.numberPage = load.numPages
        }).catch(err => {
          console.log(err)
        });
        this.$refs.pdf.$el.style.width = "120%";
        this.loading = false;
      })
    }else {
      this.$message.error("路由参数获取失败！")
      this.$router.push("");
    }


  },
  methods: {
    print(){
      this.$refs.pdf.print(600)
    },
    getObjectURL(file) {
      let url = null
      if (window.createObjectURL !== undefined) { // basic
        url = window.createObjectURL(file)
      } else if (window.webkitURL !== undefined) { // webkit or chrome
        // try {
        let blob = new Blob([file], {
          type: "application/pdf"
        });
        url = window.URL.createObjectURL(blob)
        console.log(url)
      } else if (window.URL !== undefined) { // mozilla(firefox)
        try {
          url = window.URL.createObjectURL(file)
        } catch (error) {
          console.log(error)
        }
      }
      return url
    },
    changePdfPage(val) {
      console.log(val)
      if (val === 0 && this.currentPage > 1) {
        this.currentPage--
        // console.log(this.currentPage)
      }
      if (val === 1 && this.currentPage < this.pageCount) {
        this.currentPage++
        // console.log(this.currentPage)
      }
    },
    // pdf加载时
    loadPdfHandler() {
      console.log('jiazai')
      this.currentPage = 1 // 加载的时候先加载第一页
      this.loading = false;
    },
    confirm() {


      this.$confirm('您是否确定签订此合同?', '确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        formulate(this.pdfId).then(res => {
          this.$message({
            type: 'success',
            message: '签订成功!'
          });
        })
      });
    }
  }
}
</script>
<style scoped>

.pdf {
  width: 900px;
  position: relative;
  right: 200px;
}

.operation {
  position: fixed;
  right: 0;
  bottom: 0;
  z-index: 999;
}

.pdf_wrap {
  background: #fff;
  height: 1400px;
  width: 80vh;
  margin: 0 auto;
}
.pdf_list {
  height: 80vh;
  overflow: scroll;
}

.signed {
  background-color: orange;
}

span {
  position: absolute;
  bottom: 0;
}

button {
  margin-bottom: 20px;
}


</style>
