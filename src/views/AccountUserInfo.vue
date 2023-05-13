<template>
  <el-container>
    <el-header>
      <el-menu mode="horizontal" :ellipsis="false" router>
        <el-menu-item index="/">首页</el-menu-item>
        <el-menu-item index="/accountuser">个⼈信息</el-menu-item>
        <el-menu-item index="/accountmerchant" v-if="isMerchant || isAdmin">商家信息</el-menu-item>
        <el-menu-item index="/accounttopup">充值</el-menu-item>
        <el-menu-item index="/accountwaterbill">转账流⽔</el-menu-item>
        <el-menu-item index="/accountmodify" v-if="isUser || isMerchant">个⼈信息修改</el-menu-item>
      </el-menu>
    </el-header>
    <el-header style="margin-top: 10px; font-size: 30px">个⼈账户</el-header>
    <el-main>
      <ul style="list-style-type: none">
        <li v-for="(item, index) in info">{{ index }}: {{ item }}</li>
      </ul>
    </el-main>
  </el-container>
</template>

<script>
export default {
  name: "AccountUserInfo",
  data() {
    return {
      info: {
        name: '',
        email: '',
        tel: '',
        id: '',
        balance: '¥',
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
</style>