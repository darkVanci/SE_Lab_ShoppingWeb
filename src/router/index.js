import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/RegisterView.vue')
    },
    {
      path: '/login',
      name: 'login',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/LoginView.vue')
    },
    {
      path: '/setup',
      name: 'setup',
      component: () => import('../views/SetupView.vue')
    },
    {
      path: '/backstage',
      name: 'backstage',
      component: () => import('../views/BackstageView.vue')
    },
    {
      path: '/shop/:id',
      name: 'shop',
      component: () => import('../views/ShopView.vue')
    },
    {
      path: '/shopmanager',
      name: 'shopmanager',
      component: () => import('../views/ShopManager.vue')
    },
    {
      path: '/recordview',
      name: 'recordview',
      component: () => import('../views/RecordView.vue')
    },
    {
      path: '/fixrecordview',
      name: 'fixrecordview',
      component: () => import('../views/FixRecordView.vue')
    },
    {
      path: '/addgoods',
      name: 'addgoods',
      component: () => import('../views/AddGoods.vue')
    },
    {
      path: '/modifygoods/:id',
      name: 'modifygoods',
      component: () => import('../views/ModifyGoods.vue')
    },
    {
      path: '/newgoodshandler',
      name: 'newgoodshandler',
      component: () => import('../views/NewGoodsHandler.vue')
    },
    {
      path: '/modifygoodshandler',
      name: 'modifygoodshandler',
      component: () => import('../views/ModifyGoodsHandler.vue')
    },
    {
      path: '/shoppingcart',
      name: 'shoppingcart',
      component: () => import('../views/ShoppingCart.vue')
    },
    {
      path: '/accountmodify',
      name: 'accountmodify',
      component: () => import('../views/AccountModify.vue')
    },
    {
      path: '/accounttopup',
      name: 'accounttopup',
      component: () => import('../views/AccountTopUp.vue')
    },
    {
      path: '/accountuser',
      name: 'accountuser',
      component: () => import('../views/AccountUserInfo.vue')
    },
    {
      path: '/accountmerchant',
      name: 'accountmerchant',
      component: () => import('../views/AccountMerchantInfo.vue')
    },
    {
      path: '/accountwaterbill',
      name: 'accountwaterbill',
      component: () => import('../views/AccountWaterBill.vue')
    },
    {
      path: '/accountshopdelete',
      name: 'accountshopdelete',
      component: () => import('../views/AccountShopDeletePremission.vue')
    },
    {
      path: '/orderpage',
      name: 'orderpage',
      component: () => import('../views/OrderPage.vue'),
    },
    // {
    //   path: '/paymentpage',
    //   name: 'paymentpage',
    //   component: () => import('../views/PaymentPage.vue')
    // },
    {
      path: '/goods/:id',
      name: 'goods',
      component: () => import('../views/GoodsView.vue')
    },
    {
      path: '/ordersdisplay',
      name: 'ordersdisplay',
      component: () => import('../views/OrdersDisplay.vue'),
      children:[{
        path: '/ordersdisplay/tobepaid',
        name: 'tobepaid',
        component: () => import('../views/ToBePaid.vue')
      }]
    },
    {
      path: '/ordersdisplay/tobepaid',
      name: 'tobepaid',
      component: () => import('../views/ToBePaid.vue')
    },
    {
      path: '/activitymanager',
      name: 'activitymanager',
      component: () => import('../views/ActivityManager.vue'),
    },
  ]
})

export default router
