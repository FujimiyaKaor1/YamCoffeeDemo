const api = require('../../utils/request')
const util = require('../../utils/util')

Page({
  data: {
    banners: [
      { id: 1, image: '/images/banner/banner1.png', url: '' },
      { id: 2, image: '/images/banner/banner2.png', url: '' }
    ],
    categories: [],
    hotProducts: [],
    recommendProducts: [],
    loading: true
  },

  onLoad() {
    this.loadData()
  },

  onPullDownRefresh() {
    this.loadData().then(() => {
      wx.stopPullDownRefresh()
    })
  },

  async loadData() {
    this.setData({ loading: true })
    
    try {
      await Promise.all([
        this.loadCategories(),
        this.loadHotProducts(),
        this.loadRecommendProducts()
      ])
    } catch (e) {
      console.error(e)
    } finally {
      this.setData({ loading: false })
    }
  },

  async loadCategories() {
    try {
      const data = await api.get('/category/list')
      this.setData({ categories: data || [] })
    } catch (e) {
      console.error('加载分类失败', e)
    }
  },

  async loadHotProducts() {
    try {
      const data = await api.get('/product/hot')
      this.setData({ hotProducts: data || [] })
    } catch (e) {
      console.error('加载热销商品失败', e)
    }
  },

  async loadRecommendProducts() {
    try {
      const data = await api.get('/product/recommend')
      this.setData({ recommendProducts: data || [] })
    } catch (e) {
      console.error('加载推荐商品失败', e)
    }
  },

  onBannerTap(e) {
    const banner = e.currentTarget.dataset.banner
    if (banner && banner.url) {
      wx.navigateTo({ url: banner.url })
    }
  },

  onCategoryTap(e) {
    const category = e.currentTarget.dataset.category
    wx.navigateTo({
      url: `/subpages/product/list/list?categoryId=${category.id}&title=${category.name}`
    })
  },

  onProductTap(e) {
    const product = e.currentTarget.dataset.product
    wx.navigateTo({
      url: `/subpages/product/detail/detail?id=${product.id}`
    })
  },

  onSearchTap() {
    wx.navigateTo({
      url: '/subpages/product/list/list'
    })
  }
})
