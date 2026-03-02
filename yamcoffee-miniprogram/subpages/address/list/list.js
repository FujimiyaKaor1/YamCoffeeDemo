const api = require('../../../utils/request')
const util = require('../../../utils/util')

Page({
  data: {
    addressList: [],
    loading: true,
    selectMode: false
  },

  onLoad(options) {
    if (options.selectMode) {
      this.setData({ selectMode: true })
    }
  },

  onShow() {
    this.loadAddressList()
  },

  async loadAddressList() {
    try {
      const data = await api.get('/address/list')
      this.setData({ addressList: data || [], loading: false })
    } catch (e) {
      this.setData({ loading: false })
    }
  },

  onAddAddress() {
    wx.navigateTo({
      url: '/subpages/address/edit/edit'
    })
  },

  onEditAddress(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({
      url: `/subpages/address/edit/edit?id=${id}`
    })
  },

  async onSetDefault(e) {
    const id = e.currentTarget.dataset.id
    
    try {
      await api.put(`/address/${id}/default`)
      util.showToast('设置成功')
      this.loadAddressList()
    } catch (e) {}
  },

  async onDeleteAddress(e) {
    const id = e.currentTarget.dataset.id
    const confirmed = await util.showConfirm('确定要删除该地址吗？')
    
    if (confirmed) {
      try {
        await api.del(`/address/${id}`)
        util.showToast('删除成功')
        this.loadAddressList()
      } catch (e) {}
    }
  },

  onSelectAddress(e) {
    if (!this.data.selectMode) return
    
    const address = e.currentTarget.dataset.address
    const pages = getCurrentPages()
    const prevPage = pages[pages.length - 2]
    
    if (prevPage) {
      prevPage.setData({ selectedAddress: address })
    }
    
    wx.navigateBack()
  }
})
