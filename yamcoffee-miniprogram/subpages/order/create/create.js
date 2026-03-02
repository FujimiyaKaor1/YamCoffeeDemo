const api = require('../../../utils/request')
const util = require('../../../utils/util')

Page({
  data: {
    cartItemIds: [],
    cartItems: [],
    selectedAddress: null,
    userNote: '',
    totalPrice: 0,
    loading: true,
    submitting: false
  },

  onLoad(options) {
    if (options.cartItemIds) {
      const cartItemIds = options.cartItemIds.split(',').map(id => parseInt(id))
      this.setData({ cartItemIds })
    }
  },

  onShow() {
    this.loadData()
  },

  async loadData() {
    try {
      const [cartItems, addresses] = await Promise.all([
        api.get('/cart/list'),
        api.get('/address/list')
      ])
      
      const selectedItems = cartItems.filter(item => 
        this.data.cartItemIds.includes(item.id) || this.data.cartItemIds.length === 0
      )
      
      let totalPrice = 0
      selectedItems.forEach(item => {
        if (item.selected) {
          totalPrice += parseFloat(item.subtotal)
        }
      })
      
      let defaultAddress = null
      if (addresses && addresses.length > 0) {
        defaultAddress = addresses.find(a => a.isDefault) || addresses[0]
      }
      
      this.setData({
        cartItems: selectedItems.filter(item => item.selected),
        selectedAddress: defaultAddress,
        totalPrice,
        loading: false
      })
    } catch (e) {
      this.setData({ loading: false })
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
    
    if (this.data.cartItems.length === 0) {
      util.showToast('请选择商品')
      return
    }
    
    this.setData({ submitting: true })
    util.showLoading('提交中...')
    
    try {
      const cartItemIds = this.data.cartItems.map(item => item.id)
      const data = await api.post('/order', {
        addressId: this.data.selectedAddress.id,
        cartItemIds,
        userNote: this.data.userNote
      })
      
      util.hideLoading()
      util.showToast('下单成功')
      
      setTimeout(() => {
        wx.redirectTo({
          url: `/subpages/order/detail/detail?orderNo=${data.orderNo}`
        })
      }, 1000)
    } catch (e) {
      util.hideLoading()
      this.setData({ submitting: false })
    }
  }
})
