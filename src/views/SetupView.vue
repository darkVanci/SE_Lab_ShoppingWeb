<template>
  <div class="setup">
    <div class="background">
      <div class="form-container">
        <div class="go-back">
          <router-link to="/"><img src="../assets/image/img_83238.png" /></router-link>
        </div>
        <div class="input-container">
          <h1>商户开店</h1>
          <el-form size="large" label-width="auto">
            <el-form-item label="店名">
              <el-input v-model="shopname" @blur="shopnameBlur()" required>店名</el-input>
              <div>{{ shopname.length }} &le; 12 &nbsp;&nbsp;</div>
              <div id="shopname_err" class="err_msg" v-show="!shopnameFlag">店名格式错误</div>
            </el-form-item>
            <el-form-item label="活动性质">
              <el-checkbox-group v-model="types">
                <el-checkbox label="百货" name="type"></el-checkbox>
                <el-checkbox label="图书" name="type"></el-checkbox>
                <el-checkbox label="服装" name="type"></el-checkbox>
                <el-checkbox label="食品" name="type"></el-checkbox>
                <el-checkbox label="家电" name="type"></el-checkbox>
                <el-checkbox label="电脑" name="type"></el-checkbox>
                <el-checkbox label="手机" name="type"></el-checkbox>
                <el-checkbox label="数码" name="type"></el-checkbox>
                <el-checkbox label="二手" name="type"></el-checkbox>
                <el-checkbox label="奢侈" name="type"></el-checkbox>
                <el-checkbox label="其他" name="type"></el-checkbox>
              </el-checkbox-group>
              <div id="types_err" class="err_msg" v-show="this.types.length == 0">
                &nbsp;&nbsp;&nbsp;至少选1项
              </div>
            </el-form-item>

            <el-form-item label="身份证号">
              <el-input v-model="identity" @blur="identityBlur()" required>身份证号</el-input>
              <div id="identity_err" class="err_msg" v-show="!identityFlag">身份证号格式错误</div>
            </el-form-item>
            <el-form-item label="商店简介">
              <el-input
                type="textarea"
                v-model="description"
                @blur="descriptionBlur()"
                required
              ></el-input>
              <div>{{ description.length }} &le; 128 &nbsp;&nbsp;</div>
              <div id="description_err" class="err_msg" v-show="!descriptionFlag">
                商店简介格式错误
              </div>
            </el-form-item>
            <el-form-item label="备案地址">
              <el-input type="textarea" v-model="address" @blur="addressBlur()" required></el-input>
              <div>{{ address.length }} &le; 32 &nbsp;&nbsp;</div>
              <div id="address_err" class="err_msg" v-show="!addressFlag">备案地址格式错误</div>
            </el-form-item>
            <el-form-item label="注册资金">
              <el-input type="textarea" v-model="double" @blur="doubleBlur()" required></el-input>
              <div>&gt; 1000 &nbsp;&nbsp;</div>
              <div id="double_err" class="err_msg" v-show="!doubleFlag">注册资金金额错误</div>
            </el-form-item>
            <el-form-item label="注册时间">
              <el-col :span="11">
                <el-date-picker
                  v-model="date"
                  type="date"
                  placeholder="请选一天"
                  value-format="YYYY-MM-DD"
                  style="width: 100%"
                />
              </el-col>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="setup">申请开店</el-button>
              <el-button @click="cancel">取消</el-button>
            </el-form-item>
            <div class="err_msg">{{ message }}</div>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// import moment from 'moment'
export default {
  data() {
    return {
      shopname: '',
      shopnameFlag: true,
      types: [],
      identity: '',
      identityFlag: true,
      description: '',
      descriptionFlag: true,
      address: '',
      addressFlag: true,
      double: '',
      doubleFlag: true,
      // year: '',
      // month: '',
      // day: '',
      date: '',
      users: [],
      message: ''
    }
  },
  mounted() {
    // alert('mounted start')
    const token = localStorage.getItem('token')
    this.$axios
      .get('/getData', {
        headers: {
          token: `${token}`
        }
      })
      .then((response) => {
        console.log(response.data)
        if (response.data.state == 200) {
          this.character = response.data.message
          if (this.character != 2) {
            this.$router.push({ name: 'home' })
          }
        } else {
          this.$router.push({ name: 'home' })
        }
      })
      .catch((error) => {
        console.log(error)
      })
  },
  methods: {
    cancel() {
      this.shopname = ''
      this.shopnameFlag = true
      this.types = []
      this.identity = ''
      this.identityFlag = true
      this.description = ''
      this.descriptionFlag = true
      this.address = ''
      this.addressFlag = true
      this.double = ''
      this.doubleFlag = true
      // this.year = ''
      // this.month = ''
      // this.day = ''
      this.date = ''
      this.users = []
      this.message = ''
    },
    shopnameBlur() {
      if (this.shopname.length > 12) {
        this.shopnameFlag = false
      } else {
        this.shopnameFlag = true
      }
    },
    identityBlur() {
      let reg = /^\d{17}[\dX]$/
      this.identityFlag = reg.test(this.identity) || this.identity.length == 0
    },
    descriptionBlur() {
      if (this.description.length > 128) {
        this.descriptionFlag = false
      } else {
        this.descriptionFlag = true
      }
    },
    addressBlur() {
      if (this.address.length > 32) {
        this.addressFlag = false
      } else {
        this.addressFlag = true
      }
    },
    doubleBlur() {
      if (this.double > 1000 || this.double.length == 0) {
        this.doubleFlag = true
      } else {
        this.doubleFlag = false
      }
    },
    setup() {
      const token = localStorage.getItem('token')
      console.log(this.types)
      if (
        !(
          this.shopnameFlag &&
          this.identityFlag &&
          this.descriptionFlag &&
          this.addressFlag &&
          this.doubleFlag &&
          this.types.length != 0
        )
      ) {
        this.message = '格式错误'
        return
      }
      // const dateStr = `${this.year}-${this.month}-${this.day}`
      // const dateObj = moment(dateStr).format('YYYY-MM-DD')
      this.$axios
        .post(
          'merchant/openShopApply',
          {
            shopName: this.shopname,
            category: this.types.join(','),
            idCard: this.identity,
            introduction: this.description,
            address: this.address,
            funds: this.double,
            // registerdate: dateObj
            registerdate: this.date
          },
          {
            headers: { token: `${token}`, 'Content-Type': 'application/json' }
          }
        )
        .then((response) => {
          console.log(response.data)
          this.users = response.data
          if (this.users.state == 200) {
            // alert('开店成功')
            this.message = '请求发送成功'
            this.$router.push({ name: 'home' })
          } else {
            // alert(this.users.message)
            this.message = this.users.message
          }
        })
        .catch((error) => {
          console.log(error)
          // alert('开店请求失败')
          this.message = '请求失败'
        })
    }
  }
}
</script>

<style scoped>
.err_msg {
  color: red;
}

.background {
  width: 100%;
  height: 100%;
  background-image: url('../assets/image/gradient-1.jpg');
  position: fixed;
  background-size: cover;
}

.form-container {
  width: 90%;
  height: 90%;
  background-color: white;
  border-radius: 30px;
  position: relative;
  top: 2%;
  left: 5%;
}

.go-back img {
  float: left;
  padding: 20px 20px;
  width: 58px;
  height: 50px;
}

.input-container {
  position: absolute;
  width: 80%;
  height: 90%;
  top: 2%;
  left: 10%;
}
</style>
