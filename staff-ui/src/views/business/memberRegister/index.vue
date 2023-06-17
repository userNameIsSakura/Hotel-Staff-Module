<template>
  <div class="member-register">
    <h2 v-if="isMember">您所在集团已加入会员系统</h2>
    <h2 v-else>您所在集团未加入会员系统</h2>
    <div class="button-group">
      <el-button v-if="isMember" type="primary" @click="redirectToDiscountActivity">优惠活动</el-button>
      <el-button v-if="isMember" type="primary" @click="redirectToSubscribeActivity">订阅活动</el-button>
      <el-button v-else type="primary" @click="joinHotelModule">立即加入</el-button>
    </div>
  </div>
</template>

<script>
import { Button } from 'element-ui'
import {memberRegister, validation} from "@/api/business/aosMember";

export default {
  name: "MemberRegister",
  components: {
    ElButton: Button
  },
  data() {
    return {
      isMember: false
    }
  },
  mounted() {
    this.checkMembership()
  },
  methods: {
    checkMembership() {
//调用接口判断是否加入会员系统
//如果已加入，设置isMember为true
//如果未加入，保持isMember为false
      validation().then(res => {
        this.isMember = res;
      })
    },
    joinHotelModule() {

      this.$confirm('是否确定加入会员系统?', '确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        memberRegister().then(res => {
          this.$message({
            type: 'success',
            message: '加入成功!'
          });
          this.isMember = true;
        })
      });
    },
    redirectToDiscountActivity() {
      this.$router.push('/member/rule')
    },
    redirectToSubscribeActivity() {
      this.$router.push('/member/sign')
    }
  }
}
</script>

<style scoped>
.member-register {
  margin-top: 20vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.button-group {
  display: flex;
  justify-content: center;
}
</style>
