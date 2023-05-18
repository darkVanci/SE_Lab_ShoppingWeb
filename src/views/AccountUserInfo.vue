<template>
  <div class="body">
    <div class="container1">
      <div class="wrap">
        <div class="nav-bar">
          <el-menu mode="horizontal" :ellipsis="false" router>
            <el-menu-item index="/">首页</el-menu-item>
            <el-menu-item index="/accountuser">个⼈信息</el-menu-item>
            <el-menu-item index="/accountmerchant" v-if="isMerchant || isAdmin">商家信息</el-menu-item>
            <el-menu-item index="/accounttopup">充值</el-menu-item>
            <el-menu-item index="/accountwaterbill">转账流⽔</el-menu-item>
            <el-menu-item index="/accountmodify" v-if="isUser || isMerchant">个⼈信息修改</el-menu-item>
          </el-menu>
        </div>
      </div>
    </div>
    <div class="container2">
      <div class="wrap">
        <div class="personal-info">
          <div class="icon"></div>
          <ul style="list-style-type: none">
            <li class="name"><h1>{{ info.name }}</h1></li>
            <li class="email"><h4>{{ info.email }}</h4></li>
            <li class="details">电话：{{ info.tel }}</li>
            <li class="details">身份证：{{ info.id }}</li>
            <li class="details">余额：{{ info.balance }}</li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "AccountUserInfo",
  data() {
    return {
      info: {
        name: 'Lecky',
        email: 'leckycheong135@gmail.com',
        tel: '15920791357',
        id: '123456789123',
        balance: '100¥',
      },
      isUser: false,
      isMerchant: false,
      isAdmin: false
    }
  },
  mounted() {
    const token = localStorage.getItem('token')
    this.$axios
        .get('/getData', {
          headers: {
            token: `${token}`
          }
        })
        .then((response) => {
          console.log(response.data)
          this.info.name = response.data.data.name;
          this.info.email = response.data.data.email;
          this.info.tel = response.data.data.phone;
          this.info.id = response.data.data.idCard;
          this.id = response.data.data.id;
          if (response.data.message == 1) {
            this.isUser = true;
          } else if (response.data.message == 2) {
            this.isMerchant = true;
          } else {
            this.isAdmin = true;
          }
          console.log('this.id = ' + this.id)
        })
        .catch((error) => {
          console.log(error)
        })
    this.$axios
        .get('/getAccount', {
          headers: {
            token: `${token}`
          }
        })
        .then((response) => {
          console.log(response.data)
          this.info.balance = response.data.data.amount + this.info.balance;
          console.log('this.id = ' + this.id)
        })
        .catch((error) => {
          console.log(error)
        })
  }
}
</script>

<style scoped>
.body {
  font-family: 'Microsoft YaHei';
  box-sizing: border-box;
}

.wrap {
  width: 80vw;
  margin: auto;
  padding-left: 25px;
  padding-right: 25px;
}

.personal-info {
  width: 100%;
  text-align: center;
}

.container2 {
  margin-top: 50px;
}

.personal-info {
  padding: 50px 0px;
  border-radius: 10px;
  -webkit-box-shadow: -1px 0px 21px 0px rgba(0,0,0,0.25);
  -moz-box-shadow: -1px 0px 21px 0px rgba(0,0,0,0.25);
  box-shadow: -1px 0px 21px 0px rgba(0,0,0,0.25);
}

.personal-info ul{
  padding:0;
}

.icon {
  width: 200px;
  height: 200px;
  background-color: #3c00a0;
  border-radius: 50%;
  position: relative;
  left: 50%;
  transform: translateX(-50%);
}

.details {
  padding: 10px;
}

</style>