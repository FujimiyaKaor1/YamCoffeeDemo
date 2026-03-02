App({
  globalData: {
    userInfo: null,
    token: '',
    baseUrl: 'http://localhost:8080/api/v1'
  },

  onLaunch() {
    this.checkLoginStatus()
  },

  checkLoginStatus() {
    const token = wx.getStorageSync('token')
    if (token) {
      this.globalData.token = token
      this.getUserInfo()
    }
  },

  getUserInfo() {
    const that = this
    wx.request({
      url: that.globalData.baseUrl + '/user/info',
      method: 'GET',
      header: {
        'Authorization': 'Bearer ' + that.globalData.token
      },
      success(res) {
        if (res.data.code === 200) {
          that.globalData.userInfo = res.data.data
        }
      }
    })
  },

  login() {
    return new Promise((resolve, reject) => {
      wx.login({
        success: (res) => {
          if (res.code) {
            wx.request({
              url: this.globalData.baseUrl + '/user/login',
              method: 'POST',
              data: { code: res.code },
              success: (response) => {
                if (response.data.code === 200) {
                  const data = response.data.data
                  this.globalData.token = data.token
                  this.globalData.userInfo = data.userInfo
                  wx.setStorageSync('token', data.token)
                  resolve(data)
                } else {
                  reject(response.data)
                }
              },
              fail: reject
            })
          } else {
            reject(new Error('wx.login failed'))
          }
        },
        fail: reject
      })
    })
  },

  logout() {
    this.globalData.token = ''
    this.globalData.userInfo = null
    wx.removeStorageSync('token')
  },

  isLoggedIn() {
    return !!this.globalData.token
  }
})
