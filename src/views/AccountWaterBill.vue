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
        <div class="water-bill">
          <el-table :data="bdata" style="width: 100%" stripe show-summary>
            <el-table-column prop="from" label="转出⽅"></el-table-column>
            <el-table-column prop="to" label="转⼊⽅"></el-table-column>
            <el-table-column prop="value" label="⾦额"></el-table-column>
            <el-table-column prop="description" label="备注信息"></el-table-column>
            <el-table-column prop="date"
                             label="日期"
                             :filters="[{ text: '全部', value: '家' }, { text: '公司', value: '公司' }]"
                             :filter-method="filterTag"
                             filter-placement="bottom-end"></el-table-column>
          </el-table>
<!--          <el-table :data="billdata" style="width: 100%">-->
<!--            <el-table-column prop="initiatorName" label="from"></el-table-column>-->
<!--            <el-table-column prop="receiverName" label="to"></el-table-column>-->
<!--            <el-table-column prop="amount" label="value"></el-table-column>-->
<!--            <el-table-column prop="tradeRecord" label="traderecord"></el-table-column>-->
<!--          </el-table>-->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "AccountWaterBill",
  data() {
    return {
      billdata: [],
      isUser: false,
      isMerchant: false,
      isAdmin: false,
      bdata:[{
        from: 'ME',
        to: 'HE',
        value: '123',
        description: 'Hallo',
        date: '2023/5/14'
      },{
        from: 'ME',
        to: 'HE',
        value: '123',
        description: 'Hallo',
        date: '2023/5/14'
      },{
        from: 'ME',
        to: 'HE',
        value: '123',
        description: 'Hallo',
        date: '2023/5/14'
      },{
        from: 'ME',
        to: 'HE',
        value: '123',
        description: 'Hallo',
        date: '2023/5/14'
      }]
    }
  },
  methods: {},
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
          this.billdata = response.data.data;
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

.container2{
  margin-top: 50px;
}

.water-bill{
  -webkit-box-shadow: -1px 0px 21px 0px rgba(0,0,0,0.25);
  -moz-box-shadow: -1px 0px 21px 0px rgba(0,0,0,0.25);
  box-shadow: -1px 0px 21px 0px rgba(0,0,0,0.25);
}
</style>