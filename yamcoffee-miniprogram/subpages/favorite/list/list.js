const api = require('../../../utils/request')

Page({
  data: {
    favorites: [],
    loading: true
  },

  onShow() {
    this.loadFavorites()
  },

  onPullDownRefresh() {
    this.loadFavorites().then(() => {
      wx.stopPullDownRefresh()
    })
  },

  async loadFavorites() {
    try {
      const data = await api.get('/favorite/list')
      this.setData({ favorites: data || [], loading: false })
    } catch (e) {
      this.setData({ loading: false })
    }
  },

  onProductTap(e) {
    const product = e.currentTarget.dataset.product
    wx.navigateTo({
      url: `/subpages/product/detail/detail?id=${product.id}`
    })
  },

  async onRemoveFavorite(e) {
    const productId = e.currentTarget.dataset.id
    
    try {
      await api.del(`/favorite/${productId}`)
      const favorites = this.data.favorites.filter(f => f.id !== productId)
      this.setData({ favorites })
      wx.showToast({ title: '已取消收藏', icon: 'none' })
    } catch (e) {}
  }
})
