<template>
  <div class="login">
    <div class="background">
      <div class="form-container">
        <div class="go-back">
          <router-link to="/"><img src="../assets/image/img_83238.png" /></router-link>
        </div>
        <div class="input-container">
          <h1>登录</h1>
          <el-form :label-width="auto" size="large">
            <el-form-item label="角色">
              <el-radio-group v-model="character">
                <el-radio label="1">普通用户</el-radio>
                <el-radio label="2">商户</el-radio>
                <el-radio label="3">系统管理员</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="用户名">
              <el-input v-model="username" type="text" placeholder="用户名" clearable></el-input>
            </el-form-item>
            <el-form-item label="密码">
              <el-input
                v-model="password"
                type="password"
                placeholder="密码"
                clearable
                show-password
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="login">登入</el-button>
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
export default {
  name: 'Login',
  data() {
    return {
      character: '',
      username: '',
      password: '',
      users: [],
      api: '',
      message: ''
    }
  },
  methods: {
    cancel() {
      this.character = ''
      this.username = ''
      this.password = ''
      this.users = []
      this.api = ''
      this.message = ''
    },
    login() {
      if (this.character == '') {
        // alert('未选择角色')
        this.message = '未选择角色'
        return
      } else if (this.character == 1) {
        this.api = '/user/login'
      } else if (this.character == 2) {
        this.api = '/merchant/login'
      } else if (this.character == 3) {
        this.api = '/admin/login' //临时测试用,之后要改回'/admin/login'
      }
      this.$axios
        .post(
          this.api,
          {
            name: this.username,
            password: this.password
          },
          {
            headers: { 'Content-Type': 'application/json' }
          }
        )
        .then((response) => {
          console.log(response.data)
          this.users = response.data
          console.log(this.users)
          if (this.users.state == 200) {
            // alert('登录成功')
            localStorage.setItem('token', this.users.data)
            this.message = '登录成功'
            if (this.character != 3) {
              this.$router.push({ name: 'home' })
            } else {
              //管理员登录跳转到后台
              this.$router.push({ name: 'backstage' })
            }
          } else {
            // alert(this.users.message)
            this.message = this.users.message
          }
        })
        .catch((error) => {
          console.log(error)
          // alert('登录请求失败')
          this.message = '登录请求失败'
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
  background-image: url('../assets/image/Y7V4l0.webp');
  position: fixed;
  background-size: cover;
}

.form-container {
  width: 50%;
  height: 50%;
  background-color: white;
  border-radius: 30px;
  position: relative;
  top: 25%;
  left: 25%;
}

.go-back img {
  float: left;
  padding: 20px 20px;
  width: 58px;
  height: 50px;
}

.input-container {
  position: absolute;
  width: 70%;
  height: 70%;
  top: 15%;
  left: 16%;
}
</style>
