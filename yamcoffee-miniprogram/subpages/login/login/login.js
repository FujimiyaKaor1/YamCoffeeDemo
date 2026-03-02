const app = getApp()
const util = require('../../../utils/util')

Page({
  data: {
    loading: false,
    canUseGetUserProfile: false
  },

  onLoad() {
    if (wx.getUserProfile) {
      this.setData({ canUseGetUserProfile: true })
    }
  },

  async onLogin() {
    if (this.data.loading) return
    
    this.setData({ loading: true })
    
    try {
      await app.login()
      util.showToast('登录成功')
      
      setTimeout(() => {
        wx.navigateBack()
      }, 1000)
    } catch (e) {
      util.showToast('登录失败，请重试')
    } finally {
      this.setData({ loading: false })
    }
  },

  onGetUserProfile() {
    if (this.data.loading) return
    
    wx.getUserProfile({
      desc: '用于完善用户资料',
      success: (res) => {
        this.onLogin()
      },
      fail: () => {
        util.showToast('授权失败')
      }
    })
  }
})
