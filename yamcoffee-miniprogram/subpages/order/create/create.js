const api = require('../../../utils/request')
const util = require('../../../utils/util')

Page({
  data: {
    productId: null,
    quantity: 1,
    product: null,
    selectedAddress: null,
    userNote: '',
    totalPrice: 0,
    loading: true,
    submitting: false,
    showPaymentModal: false,
    paymentInfo: {
      orderNo: '',
      amount: 0,
      qrCodeUrl: ''
    },
    paymentTimeout: null
  },

  onLoad(options) {
    if (options.productId) {
      this.setData({ 
        productId: parseInt(options.productId),
        quantity: parseInt(options.quantity) || 1
      })
    }
  },

  onShow() {
    this.loadData()
  },

  async loadData() {
    try {
      const addresses = await api.get('/address/list')
      
      let product = null
      let totalPrice = 0
      
      if (this.data.productId) {
        product = await api.get(`/product/${this.data.productId}`)
        totalPrice = parseFloat(product.price) * this.data.quantity
      }
      
      let defaultAddress = null
      if (addresses && addresses.length > 0) {
        defaultAddress = addresses.find(a => a.isDefault) || addresses[0]
      }
      
      this.setData({
        product,
        totalPrice,
        selectedAddress: defaultAddress,
        loading: false
      })
    } catch (e) {
      this.setData({ loading: false })
      util.showToast('网络连接失败，请稍后重试')
    }
  },

  onSelectAddress() {
    wx.navigateTo({
      url: '/subpages/address/list/list?selectMode=true'
    })
  },

  onNoteInput(e) {
    this.setData({ userNote: e.detail.value })
  },

  async onSubmit() {
    if (this.data.submitting) return
    
    if (!this.data.selectedAddress) {
      util.showToast('请选择收货地址')
      return
    }
    
    if (!this.data.product) {
      util.showToast('商品信息错误')
      return
    }
    
    this.setData({ submitting: true })
    util.showLoading('提交中...')
    
    try {
      const data = await api.post('/order/direct', {
        addressId: this.data.selectedAddress.id,
        productId: this.data.productId,
        quantity: this.data.quantity,
        userNote: this.data.userNote
      })
      
      util.hideLoading()
      
      const paymentData = await this.getPaymentQrCode(data.orderNo, this.data.totalPrice)
      if (paymentData) {
        this.setData({
          showPaymentModal: true,
          paymentInfo: {
            orderNo: data.orderNo,
            amount: this.data.totalPrice,
            qrCodeUrl: paymentData.qrCodeUrl || ''
          }
        })
        this.startPaymentTimeout()
      } else {
        util.showToast('下单成功')
        setTimeout(() => {
          wx.redirectTo({
            url: `/subpages/order/detail/detail?orderNo=${data.orderNo}`
          })
        }, 1000)
      }
    } catch (e) {
      util.hideLoading()
      this.setData({ submitting: false })
      util.showToast('下单失败，请检查网络后重试')
    }
  },

  async getPaymentQrCode(orderNo, amount) {
    try {
      const data = await api.get('/payment/qrcode')
      return data
    } catch (e) {
      return null
    }
  },

  onPaymentModalClose() {
    this.clearPaymentTimeout()
    this.setData({
      showPaymentModal: false,
      submitting: false
    })
    wx.redirectTo({
      url: '/pages/order/order'
    })
  },

  startPaymentTimeout() {
    this.clearPaymentTimeout()
    const timeout = setTimeout(() => {
      util.showToast('支付超时，请重新下单')
      this.setData({
        showPaymentModal: false,
        submitting: false
      })
      setTimeout(() => {
        wx.redirectTo({
          url: '/pages/order/order'
        })
      }, 1500)
    }, 5 * 60 * 1000)
    this.setData({ paymentTimeout: timeout })
  },

  clearPaymentTimeout() {
    if (this.data.paymentTimeout) {
      clearTimeout(this.data.paymentTimeout)
      this.setData({ paymentTimeout: null })
    }
  },

  onUnload() {
    this.clearPaymentTimeout()
  },

  async onConfirmPay() {
    const orderNo = this.data.paymentInfo.orderNo
    try {
      util.showLoading('确认中...')
      await api.put(`/order/${orderNo}/confirm-pay`)
      util.hideLoading()
      util.showToast('已确认支付')
      this.clearPaymentTimeout()
      
      this.setData({
        showPaymentModal: false,
        submitting: false
      })
      
      setTimeout(() => {
        wx.redirectTo({
          url: `/subpages/order/detail/detail?orderNo=${orderNo}`
        })
      }, 1000)
    } catch (e) {
      util.hideLoading()
      util.showToast('确认支付失败，请稍后重试')
    }
  }
})
