const app = getApp()

const checkLogin = () => {
  return new Promise((resolve, reject) => {
    if (app.isLoggedIn()) {
      resolve(true)
    } else {
      wx.navigateTo({
        url: '/subpages/login/login'
      })
      reject(new Error('未登录'))
    }
  })
}

const requireLogin = (page) => {
  const originalOnLoad = page.onLoad
  const originalOnShow = page.onShow
  
  page.onLoad = function(options) {
    checkLogin().then(() => {
      if (originalOnLoad) {
        originalOnLoad.call(this, options)
      }
    }).catch(() => {})
  }
  
  page.onShow = function() {
    checkLogin().then(() => {
      if (originalOnShow) {
        originalOnShow.call(this)
      }
    }).catch(() => {})
  }
  
  return page
}

const loginRequired = (fn) => {
  return function(...args) {
    if (app.isLoggedIn()) {
      return fn.apply(this, args)
    } else {
      wx.navigateTo({
        url: '/subpages/login/login'
      })
    }
  }
}

module.exports = {
  checkLogin,
  requireLogin,
  loginRequired
}
