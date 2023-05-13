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
      <el-header style="margin-top: 10px; font-size: 30px">转账流⽔</el-header>
      <el-main>
        <el-table :data="billdata" style="width: 100%">
          <el-table-column prop="initiatorName" label="from"></el-table-column>
          <el-table-column prop="receiverName" label="to"></el-table-column>
          <el-table-column prop="amount" label="value"></el-table-column>
          <el-table-column prop="tradeRecord" label="traderecord"></el-table-column>
        </el-table>
      </el-main>
  </el-container>
</template>

<script>
export default {
  name: "AccountWaterBill",
  data(){
    return{
      billdata:[],
      isUser: false,
      isMerchant: false,
      isAdmin: false
    }
  },
  methods: {

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
          console.log(response.data);
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
        .get('/getAccountRecorder', {
          headers: {
            token: `${token}`
          }
        })
        .then((response) => {
          console.log(response.data);
          this.billdata=response.data.data;
        })
        .catch((error) => {
          console.log(error)
        })
  }
}
</script>

<style scoped>
/*.wrapper {*/
/*  width: 1350px;*/
/*  margin: 0 auto;*/
/*}*/
</style>