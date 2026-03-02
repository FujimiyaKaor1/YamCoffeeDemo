const api = require('../../../utils/request')
const util = require('../../../utils/util')

Page({
  data: {
    id: null,
    form: {
      receiverName: '',
      receiverPhone: '',
      province: '',
      city: '',
      district: '',
      detailAddress: '',
      isDefault: 0
    },
    region: []
  },

  onLoad(options) {
    if (options.id) {
      this.setData({ id: options.id })
      this.loadAddress(options.id)
    }
  },

  async loadAddress(id) {
    try {
      const data = await api.get(`/address/${id}`)
      const form = {
        receiverName: data.receiverName,
        receiverPhone: data.receiverPhone,
        province: data.province,
        city: data.city,
        district: data.district,
        detailAddress: data.detailAddress,
        isDefault: data.isDefault
      }
      const region = [data.province, data.city, data.district]
      this.setData({ form, region })
    } catch (e) {}
  },

  onInputChange(e) {
    const field = e.currentTarget.dataset.field
    const value = e.detail.value
    this.setData({
      [`form.${field}`]: value
    })
  },

  onRegionChange(e) {
    const region = e.detail.value
    this.setData({
      region,
      'form.province': region[0],
      'form.city': region[1],
      'form.district': region[2]
    })
  },

  onDefaultChange(e) {
    this.setData({
      'form.isDefault': e.detail.value ? 1 : 0
    })
  },

  async onSubmit() {
    const { form } = this.data
    
    if (!form.receiverName) {
      util.showToast('请输入收货人姓名')
      return
    }
    if (!form.receiverPhone) {
      util.showToast('请输入手机号')
      return
    }
    if (!/^1[3-9]\d{9}$/.test(form.receiverPhone)) {
      util.showToast('手机号格式不正确')
      return
    }
    if (!form.province) {
      util.showToast('请选择地区')
      return
    }
    if (!form.detailAddress) {
      util.showToast('请输入详细地址')
      return
    }
    
    util.showLoading('保存中...')
    
    try {
      if (this.data.id) {
        await api.put(`/address/${this.data.id}`, form)
      } else {
        await api.post('/address', form)
      }
      util.hideLoading()
      util.showToast('保存成功')
      setTimeout(() => wx.navigateBack(), 1000)
    } catch (e) {
      util.hideLoading()
    }
  }
})
