const api = require('../../../utils/request')
const util = require('../../../utils/util')

Page({
  data: {
    cartList: [],
    isAllSelected: false,
    totalPrice: 0,
    selectedCount: 0,
    loading: true
  },

  onShow() {
    this.loadCartList()
  },

  async loadCartList() {
    try {
      const data = await api.get('/cart/list')
      this.setData({ cartList: data || [], loading: false })
      this.calculateTotal()
    } catch (e) {
      this.setData({ loading: false })
    }
  },

  calculateTotal() {
    const { cartList } = this.data
    let totalPrice = 0
    let selectedCount = 0
    let isAllSelected = cartList.length > 0
    
    cartList.forEach(item => {
      if (item.selected) {
        totalPrice += parseFloat(item.subtotal)
        selectedCount++
      } else {
        isAllSelected = false
      }
    })
    
    this.setData({ totalPrice, selectedCount, isAllSelected })
  },

  async onQuantityChange(e) {
    const { id, type } = e.currentTarget.dataset
    const item = this.data.cartList.find(c => c.id === id)
    if (!item) return
    
    let quantity = item.quantity
    if (type === 'minus' && quantity > 1) {
      quantity--
    } else if (type === 'plus' && quantity < item.stock) {
      quantity++
    } else {
      return
    }
    
    try {
      await api.put(`/cart/${id}`, { quantity })
      item.quantity = quantity
      item.subtotal = (item.productPrice * quantity).toFixed(2)
      this.setData({ cartList: this.data.cartList })
      this.calculateTotal()
    } catch (e) {}
  },

  async onSelectItem(e) {
    const id = e.currentTarget.dataset.id
    const item = this.data.cartList.find(c => c.id === id)
    if (!item) return
    
    try {
      await api.put(`/cart/${id}/select`, null, { params: { selected: item.selected ? 0 : 1 } })
      item.selected = item.selected ? 0 : 1
      this.setData({ cartList: this.data.cartList })
      this.calculateTotal()
    } catch (e) {}
  },

  async onSelectAll() {
    const newSelected = this.data.isAllSelected ? 0 : 1
    
    try {
      await api.put('/cart/selectAll', null, { params: { selected: newSelected } })
      this.data.cartList.forEach(item => item.selected = newSelected)
      this.setData({ cartList: this.data.cartList })
      this.calculateTotal()
    } catch (e) {}
  },

  async onDeleteItem(e) {
    const id = e.currentTarget.dataset.id
    const confirmed = await util.showConfirm('确定要删除该商品吗？')
    
    if (confirmed) {
      try {
        await api.del(`/cart/${id}`)
        const cartList = this.data.cartList.filter(c => c.id !== id)
        this.setData({ cartList })
        this.calculateTotal()
      } catch (e) {}
    }
  },

  onProductTap(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({
      url: `/subpages/product/detail/detail?id=${id}`
    })
  },

  onCheckout() {
    if (this.data.selectedCount === 0) {
      util.showToast('请选择商品')
      return
    }
    
    const selectedIds = this.data.cartList
      .filter(item => item.selected)
      .map(item => item.id)
    
    wx.navigateTo({
      url: `/subpages/order/create/create?cartItemIds=${selectedIds.join(',')}`
    })
  }
})
