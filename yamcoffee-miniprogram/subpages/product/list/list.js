const api = require('../../../utils/request')

Page({
  data: {
    categoryId: null,
    keyword: '',
    products: [],
    pageNum: 1,
    pageSize: 10,
    hasMore: true,
    loading: false
  },

  onLoad(options) {
    if (options.categoryId) {
      this.setData({ categoryId: parseInt(options.categoryId) })
    }
    if (options.keyword) {
      this.setData({ keyword: options.keyword })
    }
    if (options.title) {
      wx.setNavigationBarTitle({ title: options.title })
    }
    this.loadProducts(true)
  },

  onPullDownRefresh() {
    this.loadProducts(true).then(() => {
      wx.stopPullDownRefresh()
    })
  },

  onReachBottom() {
    if (this.data.hasMore && !this.data.loading) {
      this.loadProducts(false)
    }
  },

  async loadProducts(refresh = false) {
    if (this.data.loading) return
    
    this.setData({ loading: true })
    
    const pageNum = refresh ? 1 : this.data.pageNum
    const params = {
      pageNum,
      pageSize: this.data.pageSize
    }
    
    if (this.data.categoryId) {
      params.categoryId = this.data.categoryId
    }
    if (this.data.keyword) {
      params.keyword = this.data.keyword
    }
    
    try {
      const data = await api.get('/product/list', params)
      const products = data.list || []
      
      this.setData({
        products: refresh ? products : [...this.data.products, ...products],
        pageNum: pageNum + 1,
        hasMore: products.length >= this.data.pageSize,
        loading: false
      })
    } catch (e) {
      this.setData({ loading: false })
    }
  },

  onProductTap(e) {
    const product = e.currentTarget.dataset.product
    wx.navigateTo({
      url: `/subpages/product/detail/detail?id=${product.id}`
    })
  }
})
