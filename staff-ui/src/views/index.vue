<template>

  <div class="home-container">
    <!--头标题-->
    <div class="the_header">
      <div class="the_header_top">
        <div class="the_header_top_left">
          <img src="../assets/logo.png" />
          <p>AOS酒店管理</p>
        </div>
        <div class="the_header_top_right">
          <p @click="logout">注销</p>
        </div>

      </div>
      <div class="the_header_bottom">
        <span>AOS酒店管理</span>
        <p>AOS酒店管理致力于为酒店业主提供全方位的酒店管理服务。</p>
      </div>
    </div>
    <!--第二部分-->
    <div class="the_mian_one">
      <div class="the_mian_one_left">
        <img src="../assets/bg1.png" />
        <H2>专业性和责任感</H2>
        <p>为客户提供最优质的服务是公司的宗旨</p>

      </div>
      <div class="the_mian_one_center">
        <img src="../assets/bg2.png" />
        <H2>丰富经验与行业背景</H2>
        <p>公司的管理团队拥有丰富的酒店管理经验和深厚的行业背景，能够为客户提供专业的建议和解决方案。</p>


      </div>
      <div class="the_mian_one_right">
        <img src="../assets/bg3.png" />
        <H2>高素质队伍</H2>
        <p>他们有着丰富的酒店管理经验和专业的技能，能够为客户提供高效、优质的服务。</p>

      </div>
    </div>
    <!--第三部分-->
    <div class="the_mian_two">
      <!--         <span>AOS酒店管理是一个专业、可信赖的酒店管理公司，为客户提供全方位的酒店管理服务，帮助客户实现酒店经营的成功和可持续发展！</span>-->

      <div class="the_mian_two_pic">
        <div  class="the_mian_two_details">
          <img src="../assets/logo.png" />
          <h2>酒店信息</h2>
          <p>包含了酒店的基本信息和服务信息，可以帮助客人更好地了解酒店的情况，从而做出更好的决策。</p>
          <button @click="goToHotel">进入</button>
        </div>

        <div  class="the_mian_two_details" >
          <img src="../assets/logo.png" />
          <h2>会员系统</h2>
          <p>帮助酒店吸引更多的忠实客户，提高客户满意度和忠诚度。</p>
          <button @click="goToVIP">进入</button>
        </div>

        <div  class="the_mian_two_details" >
          <img src="../assets/logo.png" />
          <h2>员工管理</h2>
          <p>帮助酒店管理人员对员工进行有效的管理和监控，提高员工工作效率和服务质量。</p>
          <button @click="goToUser">进入</button>
        </div>

        <div  class="the_mian_two_details" >
          <img src="../assets/logo.png" />
          <h2>自助机</h2>
          <p>酒店自助机是一种便捷的自助服务设备，可以为客人提供快速、高效的服务体验。</p>
<!--          <button @click="goToZiZhuJi">进入</button>-->
        </div>

        <div  class="the_mian_two_details">
          <img src="../assets/logo.png" />
          <h2>小程序/App对接</h2>
          <p>将酒店的服务和信息整合到小程序或App平台上，让客人可以通过手机随时随地获取酒店的服务和信息。</p>
<!--          <button @click="goToAPP">进入</button>-->
        </div>

        <div  class="the_mian_two_details">
          <img src="../assets/logo.png" />
          <h2>数据分析</h2>
          <p>帮助酒店管理者更好地了解酒店的运营状况、客户需求和市场趋势，从而制定更加科学的经营策略和决策。</p>
<!--          <button @click="goToData">进入</button>-->
        </div>

      </div>

    </div>
    <!--第四部分-->
    <div class="the_mian_three">
      <div class="the_mian_three_top">
        <div class="the_mian_three_top_left">

          <ul>
            <li class="the_mian_three_top_left_title">
              <img src="../assets/logo.png" />  <H2>AOS酒店管理</H2>
            </li>

          </ul>
          <ul>
            <li>
              公司由一群经验丰富的酒店管理专家和行业精英组成，拥有丰富的酒店管理经验和深厚的行业背景。
            </li>
          </ul>
        </div>
        <div class="the_mian_three_top_right">
          <ul class="the_mian_three_top_right_box">
            <li>
              <p>关于</p>
            </li>
            <li>
              关于我们
            </li>
            <li>
              广告合作
            </li>
            <li>
              友情链接
            </li>
            <li>
              加入我们
            </li>
          </ul>
          <ul class="the_mian_three_top_right_box">
            <li>
              <p>酒店推荐</p>
            </li>
            <li>
              格林豪泰
            </li>
            <li>
              全季酒店
            </li>
            <li>
              如家酒店
            </li>
            <li>
              锦江之星
            </li>
          </ul>
          <ul class="the_mian_three_top_right_box">
            <li>
              <p>联系我们</p>
            </li>
            <li>
              微博：XXXX
            </li>
            <li>
              邮箱：XXX@XXX
            </li>
            <li>
              电话:XXX
            </li>
          </ul>
        </div>
      </div>
      <div class="the_mian_three_bottom">
        <p>@2012-2023 AOS酒店管理系统</p>
        <p>鲁ICP备XXXXXX | 鲁公安网安备XXXXXXXXXXX</p>
      </div>
    </div>
  </div>

</template>

<script>
export default {
  data() {
    return {

    };
  },
  computed: {

  },
  created() {

  },
  mounted() {

  },
  methods: {
    goToData(){
      alert("数据分析")
    },
    goToHotel(){
      this.$router.push({ path: "/hotel/chainHotel" });
    },
    goToVIP(){
      this.$router.push({ path: "/member/rule" });
    },
    goToUser(){
      this.$router.push({ path: "/staff/staff" });
    },
    goToZiZhuJi(){
      alert("自助机")
    },
    goToAPP(){
      alert("小程序/App对接")
    },
    async logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.href = '/index';
        })
      }).catch(() => {});
    }
  },
};
</script>

<style lang="less" scoped>
.home-container {
  height: 100%;
  min-width: 1200px;
  text-align: center;

}
.the_header{
  height: 500px;
  background-color: #8268d7;

}
.the_header_top{
  width: 90%;
  margin-left: 5%;

}
.the_header_top_left{
  >p{
    float: left;
    margin-top: 25px;
    color: #FFFFFF;
    font-size: 22px;
    font-weight: 500;
    padding-left: 10px;
  }
  >img{
    height: 40px;
    width: 40px;
    margin-top: 20px;
    float: left;
    border-radius: 20px;

  }
}
.the_header_top_right{
  >p{
    float: right;
    margin-top: 25px;
    color: #ececec;
    font-size: 22px;
    font-weight: 500;
    padding-left: 40px;
    cursor: pointer;

  }

}
.the_header_bottom{
  padding-top: 180px;
  >span{
    font-size: 55px;
    color: #FFFFFF;
    font-weight: 500;
    font-family:'黑体';
  }
  >p{
    font-size: 24px;
    color: #ececec;
    font-weight: 400;
    font-family:Arial, Helvetica, sans-serif;
  }
}
.the_mian_one{
  margin-top: -130px;
  width: 90%;
  margin-left: 5%;
  height: 400px;
  background-color: #FFFFFF;
  border:2px solid #FFFFFF;
  box-shadow: 2px 4px 5px #d4d4d4;
}
.the_mian_one_left{
  width: 33.3%;
  float: left;
  height: 400px;
  >img{
    padding-top: 50px;
    height: 130px;
    width: 140px;
  }
  >p{
    font-size: 18px;
    font-weight: 400;

  }


}
.the_mian_one_center{
  width: 33.3%;
  float: left;
  height: 400px;
  border-left: 1px solid #d0d0d0;
  border-right: 1px solid #d0d0d0;
  >img{
    padding-top: 50px;
    height: 130px;
    width: 140px;
  }
  >p{
    font-size: 18px;
    font-weight: 400;

  }
  >button{
    width: 160px;
    height: 40px;
    border-radius: 20px;
    background-color: #FFFFFF;
    border: 2px solid  #712cf9;
    color: #712cf9;
    margin-top: 50px;
    cursor: pointer;
  }
}
.the_mian_one_right{

  >img{
    padding-top: 50px;
    height: 130px;
    width: 140px;
  }
  >p{
    font-size: 18px;
    font-weight: 400;


  }
  >button{
    width: 160px;
    height: 40px;
    border-radius: 20px;
    background-color: #FFFFFF;
    border: 2px solid  #712cf9;
    color: #712cf9;
    margin-top: 50px;
    cursor: pointer;
  }
}
.the_mian_one_right:hover{
  >button{
    width: 160px;
    height: 40px;
    border-radius: 20px;
    background-color: #473584;
    border: 2px solid #712cf9;
    color: #FFFFFF;
    margin-top: 50px;
    cursor: pointer;
  }

}
.the_mian_one_left:hover{
  >button{
    width: 160px;
    height: 40px;
    border-radius: 20px;
    background-color: #473584;
    border: 2px solid #712cf9;
    color: #FFFFFF;
    margin-top: 50px;
    cursor: pointer;
  }
}
.the_mian_two_details:hover{
  >button{
    width: 160px;
    height: 40px;
    border-radius: 20px;
    background-color: #473584;
    border: 2px solid #712cf9;
    color: #FFFFFF;
    margin-top: 50px;
    cursor: pointer;
  }
}
.the_mian_two{
  margin-top: 60px;
  width: 90%;
  margin-left: 5%;
  >span{
    font-size: 26px;
    font-weight: 500;
  }
}
.the_mian_two_input{
  margin-top: 40px;

  >input{
    height: 45px;
    width: calc(95% - 100px) ;
    margin-left: 2.5%;
    float: left;
    border: 1px solid #d0d0d0;
    font-size: 18px;

  }
  >button{
    width: 100px;
    height: 45px;
    border-radius: 5px;
    background-color: #712cf9;
    cursor: pointer;
    color: #FFFFFF;
    font-size: 18px;
    margin-top: 2px;

  }
}
.the_mian_two_pic{
  height: 1000px;
  margin-top: 60px;
}
.the_mian_two_details{
  width: 28.8%;
  float: left;
  cursor: pointer;
  border:2px solid #FFFFFF;
  box-shadow: 2px 4px 5px #d4d4d4;
  margin-left: 3.5%;
  margin-top: 20px;
  height: 450px;
  >img{
    height: 170px;
    width: 100%;
  }
  >p{
    padding-top: 20px;
    height: 60px;
  }
  >button{
    width: 160px;
    height: 40px;
    border-radius: 20px;
    background-color: #FFFFFF;
    border: 2px solid  #712cf9;
    color: #712cf9;
    margin-top: 50px;
    cursor: pointer;
  }
}
.the_mian_three{
  margin-top: 40px;
  height: 500px;
  width: 100%;
  min-width: 1200px;
  background-color: #252525;
}
.the_mian_three_top{
  width: 88%;
  position: absolute;
  margin-left: 6%;
  float: left;
}
.the_mian_three_top_left{
  width: 40%;
  height: 500px;
  float: left;
  color: #FFFFFF;
  >ul{
    list-style-type: none;
    padding-top: 120px;
    text-align: left;

  }

}
.the_mian_three_top_left_title{

  >img{
    float: left;
    width: 50px;
    height: 50px;
    border-radius: 30px;
  }
  >H2{
    float: left;
    margin-left: 10px;

  }

}
.the_mian_three_top_right{
  width: 60%;
  float: left;
  color: #FFFFFF;
  height: 400px;
}
.the_mian_three_top_right_box{
  width: 25%;
  list-style-type: none;
  float: left;
  padding-top: 90px;
  >li{
    padding-top: 10px;
    color: #d0d0d0;
    font-size: 18px;
    >p{
      font-size: 20px;
      font-weight: 500;
      color: #FFFFFF;
    }
  }

}
.the_mian_three_bottom{
  height: 80px;
  position: absolute;
  color: #FFFFFF;
  width: 100%;
  margin-top: 400px;
  >p{
    float: left;
    width: 50%;

  }
}

</style>
