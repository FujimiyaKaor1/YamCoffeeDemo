const api = require('../../utils/request')
const util = require('../../utils/util')
const auth = require('../../utils/auth')

Page({
  data: {
    tabs: [
      { key: -1, name: '全部' },
      { key: 0, name: '待付款' },
      { key: 1, name: '待发货' },
      { key: 2, name: '待收货' }
    ],
    currentTab: -1,
    orders: [],
    pageNum: 1,
    pageSize: 10,
    hasMore: true,
    loading: false
  },

  onLoad(options) {
    if (options.status) {
      this.setData({ currentTab: parseInt(options.status) })
    }
  },

  onShow() {
    auth.checkLogin().then(() => {
      this.loadOrders(true)
    }).catch(() => {})
  },

  onPullDownRefresh() {
    this.loadOrders(true).then(() => {
      wx.stopPullDownRefresh()
    })
  },

  onReachBottom() {
    if (this.data.hasMore && !this.data.loading) {
      this.loadOrders(false)
    }
  },

  async loadOrders(refresh = false) {
    if (this.data.loading) return
    
    this.setData({ loading: true })
    
    const pageNum = refresh ? 1 : this.data.pageNum
    
    try {
      const data = await api.get('/order/list', {
        status: this.data.currentTab,
        pageNum,
        pageSize: this.data.pageSize
      })
      
      const orders = data.list || []
      
      this.setData({
        orders: refresh ? orders : [...this.data.orders, ...orders],
        pageNum: pageNum + 1,
        hasMore: orders.length >= this.data.pageSize,
        loading: false
      })
    } catch (e) {
      this.setData({ loading: false })
    }
  },

  onTabChange(e) {
    const index = e.currentTarget.dataset.index
    const tab = this.data.tabs[index]
    this.setData({ currentTab: tab.key, orders: [], pageNum: 1, hasMore: true })
    this.loadOrders(true)
  },

  onOrderTap(e) {
    const order = e.currentTarget.dataset.order
    wx.navigateTo({
      url: `/subpages/order/detail/detail?orderNo=${order.orderNo}`
    })
  },

  async onCancelOrder(e) {
    const order = e.currentTarget.dataset.order
    const confirmed = await util.showConfirm('确定要取消该订单吗？')
    
    if (confirmed) {
      try {
        util.showLoading('取消中...')
        await api.put(`/order/${order.orderNo}/cancel`, { reason: '用户取消' })
        util.showToast('取消成功')
        this.loadOrders(true)
      } catch (e) {
        console.error(e)
      } finally {
        util.hideLoading()
      }
    }
  },

  async onConfirmReceive(e) {
    const order = e.currentTarget.dataset.order
    const confirmed = await util.showConfirm('确认已收到商品？')
    
    if (confirmed) {
      try {
        util.showLoading('确认中...')
        await api.put(`/order/${order.orderNo}/receive`)
        util.showToast('确认成功')
        this.loadOrders(true)
      } catch (e) {
        console.error(e)
      } finally {
        util.hideLoading()
      }
    }
  },

  onPay(e) {
    const order = e.currentTarget.dataset.order
    util.showToast('支付功能开发中')
  }
})
