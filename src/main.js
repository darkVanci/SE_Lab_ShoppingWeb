import axios from 'axios'
// import VueAxios from 'vue-axios'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

const app = createApp(App)
// app.use(VueAxios, axios)
app.use(createPinia())
app.use(router)
app.use(ElementPlus)

app.mount('#app')

// 将axios挂载到原型对象上
app.config.globalProperties.$axios = axios
// 设置axios请求的地址默认是'/api'，后续会通过代理转移
axios.defaults.baseURL = '/api'
// 请求时带上cookie
axios.defaults.withCredentials = true
