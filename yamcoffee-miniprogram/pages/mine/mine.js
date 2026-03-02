const api = require('../../utils/request')
const util = require('../../utils/util')
const auth = require('../../utils/auth')

Page({
  data: {
    userInfo: null,
    orderCount: {
      pendingPayment: 0,
      pendingShipment: 0,
      pendingReceipt: 0
    },
    isLoggedIn: false
  },

  onLoad() {
    this.checkLogin()
  },

  onShow() {
    this.checkLogin()
    if (this.data.isLoggedIn) {
      this.loadOrderCount()
    }
  },

  checkLogin() {
    const app = getApp()
    const isLoggedIn = app.isLoggedIn()
    this.setData({ 
      isLoggedIn,
      userInfo: isLoggedIn ? app.globalData.userInfo : null
    })
  },

  async loadOrderCount() {
    try {
      const data = await api.get('/order/count')
      this.setData({ orderCount: data })
    } catch (e) {
      console.error(e)
    }
  },

  onLoginTap() {
    wx.navigateTo({
      url: '/subpages/login/login'
    })
  },

  onOrderTap(e) {
    const status = e.currentTarget.dataset.status
    if (!this.data.isLoggedIn) {
      this.onLoginTap()
      return
    }
    wx.switchTab({
      url: '/pages/order/order'
    })
  },

  onAddressTap() {
    if (!this.data.isLoggedIn) {
      this.onLoginTap()
      return
    }
    wx.navigateTo({
      url: '/subpages/address/list/list'
    })
  },

  onFavoriteTap() {
    if (!this.data.isLoggedIn) {
      this.onLoginTap()
      return
    }
    wx.navigateTo({
      url: '/subpages/favorite/list/list'
    })
  },

  onLogoutTap() {
    wx.showModal({
      title: '提示',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          const app = getApp()
          app.logout()
          this.setData({
            isLoggedIn: false,
            userInfo: null,
            orderCount: {
              pendingPayment: 0,
              pendingShipment: 0,
              pendingReceipt: 0
            }
          })
          util.showToast('已退出登录')
        }
      }
    })
  }
})
