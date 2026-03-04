const api = require('../../../utils/request')
const util = require('../../../utils/util')

Page({
  data: {
    orderNo: '',
    order: null,
    loading: true
  },

  onLoad(options) {
    if (options.orderNo) {
      this.setData({ orderNo: options.orderNo })
      this.loadOrder(options.orderNo)
    }
  },

  async loadOrder(orderNo) {
    try {
      const data = await api.get(`/order/${orderNo}`)
      this.setData({ order: data, loading: false })
    } catch (e) {
      this.setData({ loading: false })
      util.showToast('订单不存在')
      setTimeout(() => wx.navigateBack(), 1500)
    }
  },

  async onCancelOrder() {
    const confirmed = await util.showConfirm('确定要取消该订单吗？')
    
    if (confirmed) {
      try {
        util.showLoading('取消中...')
        await api.put(`/order/${this.data.orderNo}/cancel`, { reason: '用户取消' })
        util.hideLoading()
        util.showToast('取消成功')
        this.loadOrder(this.data.orderNo)
      } catch (e) {
        util.hideLoading()
      }
    }
  },

  async onConfirmReceive() {
    const confirmed = await util.showConfirm('确认已收到商品？')
    
    if (confirmed) {
      try {
        util.showLoading('确认中...')
        await api.put(`/order/${this.data.orderNo}/receive`)
        util.hideLoading()
        util.showToast('确认成功')
        this.loadOrder(this.data.orderNo)
      } catch (e) {
        util.hideLoading()
      }
    }
  },

  onPay() {
    util.showToast('支付功能开发中')
  },

  onCopyOrderNo() {
    wx.setClipboardData({
      data: this.data.orderNo,
      success: () => {
        util.showToast('已复制订单号')
      }
    })
  },

  async onConfirmReceiptPayment() {
    const confirmed = await util.showConfirm('确认已收到付款并安排配送？')
    
    if (confirmed) {
      try {
        util.showLoading('确认中...')
        await api.put(`/order/${this.data.orderNo}/confirm-receipt-payment`)
        util.hideLoading()
        util.showToast('确认成功')
        this.loadOrder(this.data.orderNo)
      } catch (e) {
        util.hideLoading()
        util.showToast('确认收款失败，请稍后重试')
      }
    }
  }
})
