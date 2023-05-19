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
        <div class="header">
          <p>
            <span>转账流⽔</span>
            &nbsp;
            <el-select v-model="timeIntervalValue" placeholder="请选择">
              <el-option
                  v-for="item in timeIntervalOption"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                  @click="setTimeInterval">
              </el-option>
            </el-select>
            <el-select v-model="inAndOutValue" placeholder="请选择">
              <el-option
                  v-for="item in inAndOutOption"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
          </p>
        </div>
        <div class="water-bill">
          <el-table :data="billdata" style="width: 100%" stripe show-summary max-height="1000">
            <el-table-column prop="initiatorName" label="转出⽅"></el-table-column>
            <el-table-column prop="receiverName" label="转⼊⽅"></el-table-column>
            <el-table-column prop="amount" label="⾦额" sortable></el-table-column>
            <el-table-column prop="tradeRecord" label="备注信息"></el-table-column>
            <el-table-column prop="tradeTime" label="日期"></el-table-column>
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
      timeIntervalOption: [{
        value: 0,
        label: '全部（日期）'
      }, {
        value: 7,
        label: '近七天'
      }, {
        value: 30,
        label: '近一个月'
      }],
      timeIntervalValue: 0,
      inAndOutOption: [{
        value: 0,
        label: '全部（支出 / 收入）'
      }, {
        value: -1,
        label: '支出'
      }, {
        value: 1,
        label: '收入'
      }],
      inAndOutValue: 0,
      isUser: false,
      isMerchant: false,
      isAdmin: false,
      bdata: [{
        from: 'ME',
        to: 'HE',
        value: '123',
        description: 'Hallo',
        date: '2023/5/14'
      }, {
        from: 'ME',
        to: 'HE',
        value: '-123',
        description: 'Hallo',
        date: '2023/5/14'
      }, {
        from: 'ME',
        to: 'HE',
        value: '123',
        description: 'Hallo',
        date: '2023/5/14'
      }, {
        from: 'ME',
        to: 'HE',
        value: '123',
        description: 'Hallo',
        date: '2023/5/14'
      }]
    }
  },
  methods: {
    setTimeInterval(){
      const token = localStorage.getItem('token')
      let url="/getAccountRecorder?timeInterval="+this.timeIntervalValue;
      this.$axios
          .get(url,{
            headers: {
              token: `${token}`
            }
          })
          .then((response) => {
            this.billdata=response.data.data;
            console.log(response.data);
          })
          .catch((error) => {
            console.log(error)
          })
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
        .get('/getAccountRecorder?timeInterval=0',{
          headers: {
            token: `${token}`
          }
        })
        .then((response) => {
          this.billdata=response.data.data;
          console.log(response.data);
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
  margin-top: 50px;
}

.water-bill {
  -webkit-box-shadow: -1px 0px 21px 0px rgba(0, 0, 0, 0.25);
  -moz-box-shadow: -1px 0px 21px 0px rgba(0, 0, 0, 0.25);
  box-shadow: -1px 0px 21px 0px rgba(0, 0, 0, 0.25);
}
</style>