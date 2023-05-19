<template>
  <div class="body">
    <div class="container1">
      <div class="wrap">
        <div class="nav-bar">
          <el-menu mode="horizontal" :ellipsis="false" router>
            <el-menu-item index="/">首页</el-menu-item>
            <el-menu-item index="/accountuser">个⼈信息</el-menu-item>
            <el-menu-item index="/accountmerchant">商家信息</el-menu-item>
            <el-menu-item index="/accounttopup">充值</el-menu-item>
            <el-menu-item index="/accountwaterbill">转账流⽔</el-menu-item>
            <el-menu-item index="/accountmodify">个⼈信息修改</el-menu-item>
          </el-menu>
        </div>
      </div>
    </div>
    <div class="container2">
      <div class="wrap">
        <div class="side">
          <div class="menu">
            <el-menu
                class="el-menu-vertical"
                default-active="1">
              <el-menu-item index="0">全部</el-menu-item>
              <el-menu-item index="1">待⽀付</el-menu-item>
              <el-menu-item index="2">待发货</el-menu-item>
              <el-menu-item index="3">待收货</el-menu-item>
              <el-menu-item index="4">已完成</el-menu-item>
              <el-menu-item index="5">已撤销</el-menu-item>
              <el-menu-item index="6">已退款</el-menu-item>
            </el-menu>
          </div>
        </div>
        <div class="main">
          <router-view/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "OrdersDisplay"
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

.header {
  width: 100%;
  height: 50px;
  margin-bottom: 8px;
  background-color: #f2f6fc;
  -webkit-box-shadow: 0px 0px 10px 5px rgba(0, 0, 0, 0.15);
  -moz-box-shadow: 0px 0px 10px 5px rgba(0, 0, 0, 0.15);
  box-shadow: 0px 0px 10px 5px rgba(0, 0, 0, 0.15);
}

.header p {
  position: relative;
  transform: translateY(-50%);
  left: 2%;
  top: 50%;
}

.container2 {
  width: 100%;
  height: 90vh;
}

.side {
  width: 20%;
  height: 90vh;
  float: left;
}

.side .menu {
  position: relative;
  transform: translateY(-50%);
  top: 50%;
  /* background-color: */
}

.main {
  width: 80%;
  height: 90vh;
  float: left;
}
</style>