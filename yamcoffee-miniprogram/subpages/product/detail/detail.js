const api = require('../../../utils/request')
const util = require('../../../utils/util')

Page({
  data: {
    product: null,
    isFavorite: false,
    quantity: 1,
    loading: true
  },

  onLoad(options) {
    if (options.id) {
      this.loadProduct(options.id)
    }
  },

  async loadProduct(id) {
    try {
      const product = await api.get(`/product/${id}`)
      this.setData({ product, loading: false })
      
      this.checkFavorite(id)
    } catch (e) {
      this.setData({ loading: false })
      util.showToast('商品不存在')
      setTimeout(() => wx.navigateBack(), 1500)
    }
  },

  async checkFavorite(productId) {
    const app = getApp()
    if (!app.isLoggedIn()) return
    
    try {
      const data = await api.get(`/favorite/check/${productId}`)
      this.setData({ isFavorite: data.isFavorite })
    } catch (e) {}
  },

  onQuantityChange(e) {
    const type = e.currentTarget.dataset.type
    let quantity = this.data.quantity
    
    if (type === 'minus' && quantity > 1) {
      quantity--
    } else if (type === 'plus' && quantity < this.data.product.stock) {
      quantity++
    }
    
    this.setData({ quantity })
  },

  onInputQuantity(e) {
    let quantity = parseInt(e.detail.value) || 1
    quantity = Math.max(1, Math.min(quantity, this.data.product.stock))
    this.setData({ quantity })
  },

  async onToggleFavorite() {
    const app = getApp()
    if (!app.isLoggedIn()) {
      wx.navigateTo({ url: '/subpages/login/login' })
      return
    }
    
    try {
      if (this.data.isFavorite) {
        await api.del(`/favorite/${this.data.product.id}`)
        util.showToast('已取消收藏')
      } else {
        await api.post('/favorite', { productId: this.data.product.id })
        util.showToast('收藏成功')
      }
      this.setData({ isFavorite: !this.data.isFavorite })
    } catch (e) {}
  },

  async onBuyNow() {
    const app = getApp()
    if (!app.isLoggedIn()) {
      wx.navigateTo({ url: '/subpages/login/login' })
      return
    }
    
    wx.navigateTo({
      url: `/subpages/order/create/create?productId=${this.data.product.id}&quantity=${this.data.quantity}`
    })
  }
})
